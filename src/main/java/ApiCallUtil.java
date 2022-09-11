import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.*;

import db.DbHandler;
import http.HttpHandler;
import okhttp3.Response;
import db.SqlCondition;


public class ApiCallUtil {

	static String getApiUrlFromDB() {
		String result_url = null;
		DbHandler db = new DbHandler();
		Connection conn = null;
		ResultSet rs = null;
		String sql = " SELECT API_ID, REQ_URL, CERT_KEY, FILE_TYPE, SERVICE_NAME " + 
					 " FROM T_API_INFO ";
		try {
			conn = db.sqliteDbConn();
			SqlCondition cond = new SqlCondition(" WHERE API_NAME = ? ", "SeoulFreeWifi");
			ArrayList<SqlCondition> condList = new ArrayList();
			
			condList.add(cond);
			
			rs = db.dbGetSelect(conn, sql, condList);
			
			if(rs==null) {
				System.out.println("NO DATA FOUND");
			}else {
				int api_id = rs.getInt(1);
				String req_url = rs.getString(2);
				String cert_key = "/".concat(rs.getString(3));
				String file_type = "/".concat(rs.getString(4));
				String service_name = "/".concat(rs.getString(5));
				//http://openapi.seoul.go.kr:8088/(인증키)/xml/TbPublicWifiInfo/1/5/
				result_url = req_url.concat(cert_key).concat(file_type).concat(service_name).concat("|"+String.valueOf(api_id));
			}
			
			
		}catch(Exception E) {
			E.getStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result_url;
		
	}
	
	static int getReqIdFromDB() {
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		String sql = "SELECT COUNT()+1 REQ_ID FROM T_WIFI_REQ_INFO ";
		int req_id =0;
		try {
			ResultSet rs = db.dbGetSelect(conn, sql);
		    req_id = rs.getInt(1);
			
		}catch(Exception E) {
			E.getStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return req_id;
	}
	

	static boolean setListDbInsert(List<FreeWifiInfo> list) {
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		PreparedStatement ps = null;
		try {
			//시작하기전 데이터 삭제
			String sql = "DELETE FROM T_WIFI_INFO";
			db.dbSetInsert(conn, sql);

		    sql = " INSERT INTO T_WIFI_INFO(REQ_ID, RGM_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, "
					+ " INSTL_FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, "
					+ " CNSTC_YEAR, INOUT_DOOR, REMARS3, "
					+ "	LAT, LNT, WORK_DTTM)"
					+ " VALUES(?, ?, ?, ?, ?, ?, "
				    + " ?, ?, ?, ?, ?, "
				    + " ?, ?, ?,  "
					+ " ?, ?, ?) ";
			for(int i=0; i< list.size(); i++) {
				try {
					ps = conn.prepareStatement(sql);	
				}catch(Exception E) {
					System.out.println(E.getMessage());
				}
				ps.setInt(1, list.get(i).getReq_id());
				ps.setString(2, list.get(i).getMgr_no());
				ps.setString(3, list.get(i).getWrdofc());
				ps.setString(4, list.get(i).getMain_nm());
				ps.setString(5, list.get(i).getAdres1());
				ps.setString(6, list.get(i).getAdres2());
				ps.setString(7, list.get(i).getInstl_floor());
				ps.setString(8, list.get(i).getInstl_ty());
				ps.setString(9, list.get(i).getInstl_mby());
				ps.setString(10, list.get(i).getSvc_se());
				ps.setString(11, list.get(i).getCmcwr());
				ps.setString(12, list.get(i).getCnstc_year());
				ps.setString(13, list.get(i).getInout_door());
				ps.setString(14, list.get(i).getRemars3());
				ps.setDouble(15, list.get(i).getLat());
				ps.setDouble(16, list.get(i).getLnt());
				ps.setString(17, list.get(i).getWork_dttm());
				if(db.dbSetInsert(conn, ps)<1) {
					throw new Exception();
				}
			}
		}catch(Exception E) {
			System.out.println(E.getMessage());
			E.getStackTrace();
			return false;
		}finally {
			try {
				if(conn!=null) {
					conn.close();
				}
				if(ps!=null) {
					ps.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	static int setDbInsert(String sql) {
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		try {
			return db.dbSetInsert(conn,sql);
			
		}catch(Exception E) {
			E.getStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return -1;
	}
	
	static String getNowDateTime(String format) {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(format));
	}
	
	static List jsonToList(String api_url) {
		List<FreeWifiInfo> list = new ArrayList();
		int loopCnt =0;
		int totalCnt = 0;
		int dbExeCnt = -1;
		String res_code;
		String res_msg;
		//한번 호출 시 최대데이터 1000
		String tail_url;
		int start_url_call_idx = 1;
		int end_url_call_idx = 1000;
		//결과를 가져오기 위한 response
		Response res = null;
		HttpHandler http = new HttpHandler();
		
		JsonElement element = null;
		JsonObject object = null;
		JsonObject object_res = null;
		JsonObject object_row = null;
		JsonObject object_rowDetail = null;
		JsonArray jsonArray = null;
				
		FreeWifiInfo freeWifiInfo = null;
		
		//API 호출 전 사전작업
		//1.DB에 호출 이력 쌓기
		int req_id = getReqIdFromDB();
	    String api_id = api_url.substring(api_url.indexOf('|')+1, api_url.length());
	    api_url = api_url.replace("|".concat(api_id),"");
		
		do {
			try {
				tail_url = "/" + start_url_call_idx + "/" + end_url_call_idx+ "/";
				String total_url = api_url.concat(tail_url);
				res = http.getHttpResponse(total_url);
				if(res==null) {
					//response가 null이면 오류처
					throw new Exception();
				}
				String response_body = res.body().string();
			    element = JsonParser.parseString(response_body);
			    //JsonObject형으로 변환
			    object = element.getAsJsonObject();
			    //첫 호출 시 데이터 총 건수 가져오기
			    object = object.get("TbPublicWifiInfo").getAsJsonObject();
		    	totalCnt = object.get("list_total_count").getAsInt();
			    object_res = object.get("RESULT").getAsJsonObject();
			    res_code = object_res.get("CODE").getAsString();
			    res_msg = object_res.get("MESSAGE").getAsString();
			    if(loopCnt==0) {
			    	//list_total_count":17804,"RESULT":{"CODE":"INFO-000","MESSAGE":"정상 처리되었습니다"\
					String insert_sql = " INSERT INTO T_WIFI_REQ_INFO(REQ_ID, API_ID, DATA_TOTAL_CNT, RES_CODE, RES_MSG, REQ_URL, REQ_DT) "
					          + " VALUES( "
							  + req_id + ", "
					          + api_id + ", "
					          + totalCnt + ", "
					          + "'" + res_code + "', "
					          + "'" + res_msg + "', "
					          + "'" + total_url + "', "
					          + "'" + getNowDateTime("YYYY-MM-dd HH:mm:ss") + "')";
					dbExeCnt = setDbInsert(insert_sql);
					if(dbExeCnt <= 0 ) {
						throw new Exception();
					}
			    }
			    if(loopCnt> 0 &&!res_code.equals("INFO-000")) {
			    	String updateSql = "UPDATE T_WIFI_REQ_INFO SET "
			    						+ " RES_CODE = '" + res_code + "'"
			    						+ ", RES_MSG = " + res_msg + "'"
			    						+ " WHERE REQ_ID = " 
			    						+ req_id;
			    	dbExeCnt = setDbInsert(updateSql);
			    	if(dbExeCnt <= 0 ) {
						throw new Exception();
					}
			    }
			    jsonArray = object.get("row").getAsJsonArray();
			    for(int i =0; i< jsonArray.size(); i++) {
			    	freeWifiInfo = new FreeWifiInfo();
				    object_rowDetail = jsonArray.get(i).getAsJsonObject();
				    freeWifiInfo.setReq_id(req_id);
				    freeWifiInfo.setMgr_no(object_rowDetail.get("X_SWIFI_MGR_NO").getAsString());
				    freeWifiInfo.setWrdofc(object_rowDetail.get("X_SWIFI_WRDOFC").getAsString());
				    freeWifiInfo.setMain_nm(object_rowDetail.get("X_SWIFI_MAIN_NM").getAsString());
				    freeWifiInfo.setAdres1(object_rowDetail.get("X_SWIFI_ADRES1").getAsString());
				    freeWifiInfo.setAdres2(object_rowDetail.get("X_SWIFI_ADRES2").getAsString());
				    freeWifiInfo.setInstl_floor(object_rowDetail.get("X_SWIFI_INSTL_FLOOR").getAsString());
				    freeWifiInfo.setInstl_ty(object_rowDetail.get("X_SWIFI_INSTL_TY").getAsString());
				    freeWifiInfo.setInstl_mby(object_rowDetail.get("X_SWIFI_INSTL_MBY").getAsString());
				    freeWifiInfo.setSvc_se(object_rowDetail.get("X_SWIFI_SVC_SE").getAsString());
				    freeWifiInfo.setCmcwr(object_rowDetail.get("X_SWIFI_CMCWR").getAsString());
				    freeWifiInfo.setCnstc_year(object_rowDetail.get("X_SWIFI_CNSTC_YEAR").getAsString());
				    freeWifiInfo.setInout_door(object_rowDetail.get("X_SWIFI_INOUT_DOOR").getAsString());
				    freeWifiInfo.setRemars3(object_rowDetail.get("X_SWIFI_REMARS3").getAsString());
				    freeWifiInfo.setLat(object_rowDetail.get("LAT").getAsDouble());
				    freeWifiInfo.setLnt(object_rowDetail.get("LNT").getAsDouble());
				    freeWifiInfo.setWork_dttm(object_rowDetail.get("WORK_DTTM").getAsString());
				    list.add(freeWifiInfo);
			    }
				loopCnt++;
				
				start_url_call_idx = loopCnt * 1000 + 1;
				end_url_call_idx = (start_url_call_idx + 999) > totalCnt ? totalCnt : (start_url_call_idx + 999);
				
			}catch(Exception E) {
				System.out.println("HTTP RESPONSE NULL");
				System.out.println(E.getLocalizedMessage());
				E.getStackTrace();
				list = null;
				break;
			}finally {
				res.close();
			}
			
		}while(list.size() < totalCnt);
		
		if(list.size()==totalCnt) {
		//완료업데이트
	    	String updateSql = "UPDATE T_WIFI_REQ_INFO SET "
					+ " RES_DT = '" + getNowDateTime("YYYY-MM-dd HH:mm:ss") + "'"
					+ " WHERE REQ_ID = " 
					+ req_id;
	    	setDbInsert(updateSql);
		}
		
		return list;
	}
	
	static boolean setSearchLogIns(double lat, double lnt){
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		PreparedStatement ps = null;
		
		try {
			String sql = "SELECT COUNT(1) + 1 FROM T_SEARCH_LOG";

			ResultSet rs = db.dbGetSelect(conn, sql);
			int log_id = rs.getInt(1);
		    sql = "INSERT INTO T_SEARCH_LOG(LOG_ID, LAT, LNT, WORK_DT) VALUES(?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(0, log_id);
			ps.setDouble(1, lat);
			ps.setDouble(2, lnt);
			ps.setString(3, getNowDateTime("YYYY-MM-dd HH:mm:ss"));
			db.dbSetInsert(conn, ps);
			
			if(conn!=null) {
				conn.close();
			}
			if(ps!=null) {
				ps.close();
			}
			if(rs!=null) {
				rs.close();
			}
		}catch(Exception E) {
			System.out.println(E.getMessage());
			return false;
		}
		return true;
	}
	
	static int setSearchLogDel(int logId) {
		DbHandler db = new DbHandler();
		
		String sql = "UPDATE T_SEARCH_LOG SET DEL_CHK = " + "'" + "Y" + "'" + " WHERE LOG_ID = " + logId;
		int dbExeCnt = setDbInsert(sql);
		
		return dbExeCnt;
	}
	
	static ResultSet getSearchLogSel() {
		ResultSet rs = null;
		String sql = "SELECT LOG_ID, LAT, LNT, WORK_DT WHERE DEL_CHK IS NULL ORDER BY LOG_ID";
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		
		try {
			rs = db.dbGetSelect(conn, sql);
			
			if(conn!=null) {
				conn.close();
				
			}
			if(rs!=null) {
				rs.close();
			}
		}catch(Exception E) {
			rs = null;
			System.out.println(E.getMessage());
		}
		
		return rs;
	}
	
	static ResultSet getWifiInfoSel(double lat, double lnt){
		ResultSet rs = null;
		Connection conn = null;
		DbHandler db = new DbHandler();
		String sql = "";
		
		
		
		return rs;
	}
	
	
	public static void main(String[] args) {
/*
 *        ˚ DB에 저장되어 있는 URL Select
       ˚ HTTP 호출하여 정보를 JSON형태로 가져온다.
       ˚ JSON형태의 데이터를 DB에 Insert		
 */
	   String apiUrl = getApiUrlFromDB();
	   List<FreeWifiInfo> list = jsonToList(apiUrl);
	   
	   System.out.println(setListDbInsert(list));
	}
}

package util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.*;

import db.DbHandler;
import http.HttpHandler;
import okhttp3.Response;
import db.SqlCondition;


public class ApiCallUtil {
	

	public String getApiUrlFromDB() {
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
			if(rs!= null) {
				try {
					rs.close();
				}catch(Exception E) {
					E.printStackTrace();
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
	
//	 public boolean setListDbInsert(List<FreeWifiInfo> list) {
//		DbHandler db = new DbHandler();
//		Connection conn = db.sqliteDbConn();
//		try {
//			//시작하기전 데이터 삭제
//			String sql = "DELETE FROM T_WIFI_INFO";
//			db.dbSetInsert(conn, sql);
//
////		    sql = " INSERT INTO T_WIFI_INFO(REQ_ID, RGM_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, "
////					+ " INSTL_FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, "
////					+ " CNSTC_YEAR, INOUT_DOOR, REMARS3, "
////					+ "	LAT, LNT, WORK_DTTM)"
////					+ " VALUES(?, ?, ?, ?, ?, ?, "
////				    + " ?, ?, ?, ?, ?, "
////				    + " ?, ?, ?,  "
////					+ " ?, ?, ?) ";
//			String head_sql = " INSERT INTO T_WIFI_INFO(REQ_ID, RGM_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, INSTL_FLOOR,"
//					+ " INSTL_TY, INSTL_MBY, SVC_SE, CMCWR,"
//					+ " CNSTC_YEAR, INOUT_DOOR, REMARS3,"
//					+ " LAT, LNT, WORK_DTTM)"
//					+ " VALUES( "; 
//		    
//		    StringBuffer sbSql = new StringBuffer();
//		    
//			for(int i=0; i< list.size(); i++) {
//				sbSql.append(head_sql);
//				sbSql.append(list.get(i).getReq_id() + ", ");
//				sbSql.append("'" + list.get(i).getMgr_no() + "', ");
//				sbSql.append("'" + list.get(i).getWrdofc() + "', ");
//				sbSql.append("'" + list.get(i).getMain_nm() + "', ");
//				sbSql.append("'" + list.get(i).getAdres1() + "', ");
//				sbSql.append("'" + list.get(i).getAdres2() + "', ");
//				sbSql.append("'" + list.get(i).getInstl_floor() + "', ");
//				sbSql.append("'" + list.get(i).getInstl_ty() + "', ");
//				sbSql.append("'" + list.get(i).getInstl_mby() + "', ");
//				sbSql.append("'" + list.get(i).getSvc_se() + "', ");
//				sbSql.append("'" + list.get(i).getCmcwr() + "', ");
//				sbSql.append("'" + list.get(i).getCnstc_year() + "', ");
//				sbSql.append("'" + list.get(i).getInout_door() + "', ");
//				sbSql.append("'" + list.get(i).getRemars3() + "', ");
//				
//				if(list.get(i).getMgr_no().equals("NW090011")) {
//					sbSql.append(list.get(i).getLnt() + ", ");
//					sbSql.append(list.get(i).getLat() + ", ");
//				}else {
//					sbSql.append(list.get(i).getLat() + ", ");
//					sbSql.append(list.get(i).getLnt() + ", ");
//				}
//				sbSql.append("'" + list.get(i).getWork_dttm() + "'");
//				sbSql.append("); ");
//				if(i!=0 && (i%200==0 || i==list.size()-1)) {
//					File file = new File("D:\\file\\text"+i+".txt");
//					
//					try {
//						file.createNewFile();
//						BufferedWriter wr = new BufferedWriter(new FileWriter(file, true));
//						wr.write(sbSql.toString());
//						wr.flush();
//						wr.close();
//					}catch(Exception E) {
//						System.out.println(E.getMessage());
//					}
//					if(db.dbSetInsert(conn, sbSql.toString())<1) {
//						throw new Exception();
//					}
//					sbSql = new StringBuffer();
//				}
//			}
//		}catch(Exception E) {
//			System.out.println(E.getMessage());
//			E.getStackTrace();
//			return false;
//		}finally {
//			try {
//				if(conn!=null) {
//					conn.close();
//				}
//				
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return true;
//	}
	
	 public boolean setListDbInsert(List<FreeWifiInfo> list) {
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
			try {
				ps = conn.prepareStatement(sql);	
				
			}catch(Exception E) {
				System.out.println(E.getMessage());
			}
			conn.setAutoCommit(false);
//			System.out.println(getNowDateTime("YYYY-MM-dd HH:mm:ss"));
			for(int i=0; i< list.size(); i++) {
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
				if(list.get(i).getMgr_no().equals("NW090011")) {
					ps.setDouble(15, list.get(i).getLnt());
					ps.setDouble(16, list.get(i).getLat());
				}else {
					ps.setDouble(15, list.get(i).getLat());
					ps.setDouble(16, list.get(i).getLnt());
				}
				ps.setString(17, list.get(i).getWork_dttm());
				if(db.dbSetInsert(conn, ps)<1) {
					conn.rollback();
					throw new Exception();
				}
			}
//			System.out.println(getNowDateTime("YYYY-MM-dd HH:mm:ss"));
			conn.commit();
			conn.setAutoCommit(true);
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
	
	 public List jsonToList(String api_url) {
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
				    freeWifiInfo.setMgr_no(object_rowDetail.get("X_SWIFI_MGR_NO").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setWrdofc(object_rowDetail.get("X_SWIFI_WRDOFC").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setMain_nm(object_rowDetail.get("X_SWIFI_MAIN_NM").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setAdres1(object_rowDetail.get("X_SWIFI_ADRES1").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setAdres2(object_rowDetail.get("X_SWIFI_ADRES2").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setInstl_floor(object_rowDetail.get("X_SWIFI_INSTL_FLOOR").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setInstl_ty(object_rowDetail.get("X_SWIFI_INSTL_TY").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setInstl_mby(object_rowDetail.get("X_SWIFI_INSTL_MBY").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setSvc_se(object_rowDetail.get("X_SWIFI_SVC_SE").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setCmcwr(object_rowDetail.get("X_SWIFI_CMCWR").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setCnstc_year(object_rowDetail.get("X_SWIFI_CNSTC_YEAR").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setInout_door(object_rowDetail.get("X_SWIFI_INOUT_DOOR").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setRemars3(object_rowDetail.get("X_SWIFI_REMARS3").getAsString().trim().replace("\n"," "));
				    freeWifiInfo.setLat(object_rowDetail.get("LAT").getAsDouble());
				    freeWifiInfo.setLnt(object_rowDetail.get("LNT").getAsDouble());
				    freeWifiInfo.setWork_dttm(object_rowDetail.get("WORK_DTTM").getAsString().trim().replace("\n"," "));
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
				if(res!=null) {
					res.body().close();
				}
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
	
	 public boolean setSearchLogIns(double lat, double lnt){
		DbHandler db = new DbHandler();
		Connection conn = db.sqliteDbConn();
		
		try {
			String sql = "SELECT COUNT(1) + 1 FROM T_SEARCH_LOG";

			ResultSet rs = db.dbGetSelect(conn, sql);
			int log_id = rs.getInt(1);
			if(rs!=null) {
				rs.close();
			}
			System.out.println( " log_id  " + log_id);
		    sql = " INSERT INTO T_SEARCH_LOG (LOG_ID, LAT, LNT, WORK_DT) VALUES (" + log_id +"," + lat + "," + lnt + ",\"" + getNowDateTime("YYYY-MM-dd HH:mm:ss") + "\");";
		    
			System.out.println(sql);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.println(ps.executeUpdate());
			
			System.out.println(3);
			if(conn!=null) {
				conn.close();
			}
			if(rs!=null) {
				rs.close();
			}
		}catch(Exception E) {
			System.out.println(E.getMessage());
			E.printStackTrace();
			return false;
		}
		return true;
	}
	
	 int setSearchLogDel(int logId) {
		DbHandler db = new DbHandler();
		
		String sql = "UPDATE T_SEARCH_LOG SET DEL_CHK = " + "'" + "Y" + "'" + " WHERE LOG_ID = " + logId;
		int dbExeCnt = setDbInsert(sql);
		
		return dbExeCnt;
	}
	
	 public ResultSet getSearchLogSel() {
		ResultSet rs = null;
		String sql = "SELECT LOG_ID, COUNT(1) CNT, LAT, LNT, WORK_DT WHERE DEL_CHK IS NULL ORDER BY WORK_DT";
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
	
	 public List getWifiInfoSel(double x, double y){
		List<FreeWifiInfo> listAll = new ArrayList<>();
		List<FreeWifiInfo> list = new ArrayList<>();
		ResultSet rs = null;
		Connection conn = null;
		DbHandler db = new DbHandler();
		String sql = "SELECT RGM_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, "
				+ " INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, "
				+ " REMARS3, LAT, LNT, WORK_DTTM, INSTL_FLOOR, INSTL_TY "
				+ " FROM T_WIFI_INFO";
		
		try {
			conn = db.sqliteDbConn();
			rs = db.dbGetSelect(conn, sql);
			while(rs.next()) {
				FreeWifiInfo freeWifiInfo = new FreeWifiInfo();
				freeWifiInfo.setMgr_no(rs.getString("RGM_NO"));
				freeWifiInfo.setWrdofc(rs.getString("WRDOFC"));
				freeWifiInfo.setMain_nm(rs.getString("MAIN_NM"));
				freeWifiInfo.setAdres1(rs.getString("ADRES1"));
				freeWifiInfo.setAdres2(rs.getString("ADRES2"));
				freeWifiInfo.setInstl_mby(rs.getString("INSTL_MBY"));
				freeWifiInfo.setSvc_se(rs.getString("SVC_SE"));
				freeWifiInfo.setCmcwr(rs.getString("CMCWR"));
				freeWifiInfo.setCnstc_year(rs.getString("CNSTC_YEAR"));
				freeWifiInfo.setInout_door(rs.getString("INOUT_DOOR"));
				freeWifiInfo.setRemars3(rs.getString("REMARS3"));
				freeWifiInfo.setLat(rs.getDouble("LAT"));
				freeWifiInfo.setLnt(rs.getDouble("LNT"));
				freeWifiInfo.setWork_dttm(rs.getString("WORK_DTTM"));
				freeWifiInfo.setInstl_floor(rs.getString("INSTL_FLOOR"));
				freeWifiInfo.setInstl_ty(rs.getString("INSTL_TY"));
				freeWifiInfo.setDistance(distance(x, y, freeWifiInfo.getLat(), freeWifiInfo.getLnt()));
				listAll.add(freeWifiInfo);
			}
			//거리순 정렬 후 20개만 리스트에 담아 리턴
			Collections.sort(listAll, (a, b) -> Double.compare(a.getDistance(), b.getDistance()));

			for(int i =0; i<20; i++) {
				list.add(listAll.get(i));
			}

			
		}catch(Exception E) {
			System.out.println(E.getMessage());
			list = null;
		}finally {
			try {
				if(conn!= null) {
					conn.close();
				}
				if(rs!=null) {
					rs.close();
				}
			}catch(Exception E) {
				System.out.println(E.getMessage());
			}
		}
		return list;
	}
	
	static double distance(double myX, double myY, double x, double y) {
		double theta = myX - x;
		double dis  =Math.sin((myY * Math.PI / 180.0)) * Math.sin((y * Math.PI / 180.0)) + 
				     Math.cos((myY * Math.PI / 180.0)) * Math.cos((y * Math.PI / 180.0)) * Math.cos((theta * Math.PI / 180.0));
	
		
		dis = Math.acos(dis);
		dis = (dis * 180 / Math.PI);
		
		dis = dis * 60 * 1.1515;
		
		dis = dis * 1.609344;

		return dis;
	}

}

import java.sql.*;
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
	
	
	static List jsonToList(String api_url) {
		List list = null;
		int loopCnt =0;
		int totalCnt = 0;
		String res_code;
		String res_msg;
		//한번 호출 시 최대데이터 1000
		String tail_url = "/1/1000/";
		//결과를 가져오기 위한 response
		Response res = null;
		HttpHandler http = new HttpHandler();
		
		JsonElement element = null;
		JsonObject object = null;
		JsonArray jsonArray = null;
		
		//API 호출 전 사전작업
		//1.DB에 호출 이력 쌓기
		int req_id = getReqIdFromDB();
	    String api_id = api_url.substring(api_url.indexOf('|')+1, api_url.length());
	    api_url = api_url.replace("|".concat(api_id),"");
		
		String insert_sql = "";
		
		
		do {
			try {
				res = http.getHttpResponse(api_url.concat(tail_url));
				if(res==null) {
					//response가 null이면 오류처
					throw new Exception();
				}
				String response_body = res.body().string();
			    element = JsonParser.parseString(response_body);
			    //JsonObject형으로 변환
			    object = element.getAsJsonObject();//error
			    //첫 호출 시 데이터 총 건수 가져오기
			    if(loopCnt==0) {
			    	//list_total_count":17804,"RESULT":{"CODE":"INFO-000","MESSAGE":"정상 처리되었습니다"
			    	totalCnt = object.get("list_total_count").getAsInt();
			    }
			    
			    res_code = object.get("CODE").getAsString();
			    res_msg = object.get("MESSAGE").getAsString();
			    
			    System.out.println(totalCnt);
			    System.out.println(res_code);
			    System.out.println(res_msg);
			    
			    if(!res_code.equals("INFO-000")) {
			    	list = null;
			    	
			    }
				
				
				loopCnt++;
			}catch(Exception E) {
				System.out.println("HTTP RESPONSE NULL");
				E.getStackTrace();
			}finally {
				res.close();
			}
			
		}while(totalCnt > 0);
		
		
		return list;
	}
	
	public static void main(String[] args) {
/*
 *        ˚ DB에 저장되어 있는 URL Select
       ˚ HTTP 호출하여 정보를 JSON형태로 가져온다.
       ˚ JSON형태의 데이터를 DB에 Insert		
 */
	   String apiUrl = getApiUrlFromDB();
	   List list = jsonToList(apiUrl);
	   
	}

}

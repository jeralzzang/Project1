package http;

import okhttp3.*;

public class HttpHandler {
	
	/*
	 *http를 호출 후 response반환
	 */
	public Response getHttpResponse(String url) {
		Response res = null;
		try{
			//httpclient 객체 생성
			OkHttpClient client = new OkHttpClient();
			
			Request.Builder getReq = new Request.Builder().url(url).get();
			
			Request req = getReq.build();
			res = client.newCall(req).execute();
			
			if(!res.isSuccessful()) {
				res = null;
			}
		}catch(Exception E) {
			E.getStackTrace();
		}
		
		return res;
	}
	
//	public static void main(String[] args) throws Exception {
//		HttpHandler http = new HttpHandler();
//		String url ="http://openapi.seoul.go.kr:8088/504a4e6e716a65723433506e6b634b/json/TbPublicWifiInfo/1/1000/";
//		Response res = http.getHttpResponse(url);
//		System.out.print(res.body().string());
//		res.close();
//	}
	
}

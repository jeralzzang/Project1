package http;

import okhttp3.*;

public class HttpHandler {
	
	/*
	 *http를 호출 후 response반환
	 */
	public Response getHttpResponse(String url) {
		Response res = null;
		//httpclient 객체 생성
		OkHttpClient client = new OkHttpClient();
		try{
			
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
	
	
}

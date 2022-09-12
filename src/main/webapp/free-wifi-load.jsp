<%@page import="util.ApiCallUtil"%>
<%@page import="util.FreeWifiInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<% 
	   ApiCallUtil ap = new ApiCallUtil();
	   String apiUrl = ap.getApiUrlFromDB(); 
	   List<FreeWifiInfo> list = ap.jsonToList(apiUrl);
	   int listSize = list.size();
	   boolean isGetData = ap.setListDbInsert(list);
	   String result;
	   if(isGetData){
		   result = listSize + "개의 서울시 공공와이파이 정보를 정상적으로 가져왔습니다.";
	   }else{
		   result = "서울시 공공와이파이 정보를 가져오는데 실패 했습니다.";
	   }
	   out.write(result); 
	%>
	
	<a href="index.jsp">홈으로</a>
	
	
</body>
</html>
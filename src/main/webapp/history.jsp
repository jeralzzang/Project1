<%@page import="java.sql.ResultSet"%>
<%@page import="util.ApiCallUtil"%>
<%@page import="util.FreeWifiInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>위치 히스토리 목록</title>
	<style>
		table{
		width:100%;
        border-collapse: collapse;
        border:solid 1px gray;
		}
		th{
		  background-color: #04AA6D;
		  color: white;
		  border:solid 1px white;
		}
		th, td{
			text-align: center;
		}
	</style>
</head>
<body>
	<h1> 위치 히스토리 목록</h1>
	<a href="index.jsp"> 홈으로</a>|
	<a href="free-wifi-load.jsp">서울시 공공wifi가져오기</a>|
	<a href="history.jsp">공공wifi조회이력</a>
	<br>
<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>도로명주소</th>
				<th>비고</th>
			</tr>
		</thead>
		<% 
		ApiCallUtil ap = new ApiCallUtil();
		ResultSet rs = ap.getSearchLogSel();
		if(rs!=null){
			rs.last();
			while(rs.previous()){
				out.write("<tr>");
				out.write("<td>" + rs.getString("CNT")+"</td>");
				out.write("<td>" + rs.getString("LNT")+"</td>");
				out.write("<td>" + rs.getString("LAT")+"</td>");
				out.write("<td>" + rs.getString("WORK_DT")+"</td>");
				out.write("<td> <button type = \"button\" value = "+ rs.getString("LOG_ID") + " onclick = \"delLog(" + rs.getString("LOG_ID") +")\">삭제</td>");
				out.write("</tr>");
			}	
		}
		%>
</table>
</body>
<script>
	function delLog(log_id){
		
	}
</script>
</html>
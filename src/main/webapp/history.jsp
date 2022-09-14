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
	<% 
		ApiCallUtil ap = new ApiCallUtil();
		String log_id = request.getParameter("log_id");
		if(log_id!=null){
			ap.setSearchLogDel(Integer.parseInt(log_id));
		}
	%>
<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
		<% 
		List<List> list = ap.getSearchLogSel();
		
		for(int i=0; i<list.size(); i++){
			out.write("<tr>");
			out.write("<td>" + (i+1) +"</td>");
			out.write("<td>" + list.get(i).get(2) +"</td>");
			out.write("<td>" + list.get(i).get(1) +"</td>");
			out.write("<td>" + list.get(i).get(3) +"</td>");
			out.write("<td> <button type = \"button\" onclick = \"delLog(" + list.get(i).get(0) +")\">삭제</td>");
			out.write("</tr>");
		}
		
		%>
		</tbody>
</table>
</body>
<script>
function delLog(log_id){
		document.location.href="history.jsp?log_id="+log_id;
}
</script>
</html>
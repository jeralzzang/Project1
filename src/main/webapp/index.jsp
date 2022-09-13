<%@page import="util.ApiCallUtil"%>
<%@page import="util.FreeWifiInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>서울시 공공와이파이</title>
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
	<h1> 서울시 공공 와이파이 조회 페이지</h1>
	<a href="index.jsp"> 홈으로</a>|
	<a href="free-wifi-load.jsp">서울시 공공wifi가져오기</a>|
	<a href="history.jsp">공공wifi조회이력</a>
	<br>

<%
	String lat = request.getParameter("lat");
	String lnt = request.getParameter("lnt");
	if(lat==null){
		out.write("LAT : <input type = " + "text" + " value = " + "0.0" + " name = " + "lat" + " id =" + "lat>");
	}else{
		out.write("LAT : <input type = " + "text" + " value = " + lat + " name = " + "lat" + " id =" + "lat>");
	}
	if(lnt==null){
		out.write("LNT : <input type = " + "text" + " value = " + "0.0" + " name = " + "lnt" + " id =" + "lnt>");
	}else{
		out.write("LNT : <input type = " + "text" + " value = " + lnt + " name = " + "lnt" + " id =" + "lnt>");
	}	
	
%>	
	<input type = "button" value="내위치가져오기" onclick = "getMyLocation()">
	<input type = "button" value="근처와이파이정보보기" onclick = "getWifiList(lat.value,lnt.value)">
	<br>
	
	<table>
		<thead>
			<tr>
				<th>거리(km)</th>
				<th>관리번호</th>
				<th>자치구</th>
				<th>와이파이명</th>
				<th>도로명주소</th>
				<th>상세주소</th>
				<th>설치위치(층)</th>
				<th>설치유형</th>
				<th>설치기관</th>
				<th>서비스구분</th>
				<th>망종류</th>
				<th>설치년도</th>
				<th>실내외구분</th>
				<th>WIFI접속환경</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>작업일자</th>
			</tr>
		</thead>
		<tbody>
		<% 
			if(lat==null||lnt==null){
				out.write("<tr>");
				out.write("<td colspan=" + "17" + ">위치 정보를 입력한 후에 조회해 주세요.</td>");
				out.write("</tr>");
			}else{
				ApiCallUtil ap = new ApiCallUtil();
				List<FreeWifiInfo> list = ap.getWifiInfoSel(Double.parseDouble(lnt), Double.parseDouble(lat));
				ap.setSearchLogIns(Double.parseDouble(lnt), Double.parseDouble(lat));
				for(int i=0; i<list.size(); i++){
					out.write("<tr>");
					out.write("<td>" + String.format("%.4f", list.get(i).getDistance()) + "</td>"); 
					out.write("<td>" + list.get(i).getMgr_no() + "</td>"); 
					out.write("<td>" + list.get(i).getWrdofc() + "</td>"); 
					out.write("<td>" + list.get(i).getMain_nm() + "</td>"); 
					out.write("<td>" + list.get(i).getAdres1() + "</td>"); 
					out.write("<td>" + list.get(i).getAdres2() + "</td>");
					out.write("<td>" + list.get(i).getInstl_floor() + "</td>");
					out.write("<td>" + list.get(i).getInstl_ty() + "</td>");
					out.write("<td>" + list.get(i).getInstl_mby() + "</td>");
					out.write("<td>" + list.get(i).getSvc_se() + "</td>");
					out.write("<td>" + list.get(i).getCmcwr() + "</td>");
					out.write("<td>" + list.get(i).getCnstc_year() + "</td>");
					out.write("<td>" + list.get(i).getInout_door() + "</td>");
					out.write("<td>" + list.get(i).getRemars3() + "</td>");
					out.write("<td>" + list.get(i).getLnt() + "</td>");
					out.write("<td>" + list.get(i).getLat() + "</td>");
					out.write("<td>" + list.get(i).getWork_dttm() + "</td>");
					out.write("</tr>");
				}
			}
		%>
		</tbody>
	</table>
	
	<script>
	function getMyLocation() {
	  if (navigator.geolocation) { // GPS를 지원하면
		    navigator.geolocation.getCurrentPosition(function(position) {
		      document.getElementById("lat").value = position.coords.latitude;
		      document.getElementById("lnt").value = position.coords.longitude;
		      
		    }, function(error) {
		      console.error(error);
		    }, {
		      enableHighAccuracy: false,
		      maximumAge: 0,
		      timeout: Infinity
		    });
		  } else {
		    alert('GPS를 지원하지 않습니다');
		  }
		}
	function getWifiList(lat, lnt){
		if(lat==0.0 | lnt ==0.0){
			alert('위치를 입력해주세요.');
		}else{
			document.location.href="index.jsp?lat="+lat+"&lnt="+lnt;
		}
	}
</script>
</body>
</html>
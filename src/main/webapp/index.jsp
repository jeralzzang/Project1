<%@page import="http.*"%>
<%@page import="java.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>서울시 공공와이파이</title>
</head>
<script>
	 function getMyPosition(){
			if (navigator.geolocation) {
			    
			    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
			    navigator.geolocation.getCurrentPosition(function(position) {
			        
			        var lat = position.coords.latitude, // 위도
			            lon = position.coords.longitude; // 경도
			           alert(lat + "   " + lnt);
			            
			      });
			    
			} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
			    alert("ERROR");
			        message = 'geolocation을 사용할수 없습니다.';
			        
			}
	 }
</script>
<body>
    
	<h1> 서울시 공공 와이파이 조회 페이지</h1>
	<a href="index.jsp"> 홈으로</a>|
	<a href="free-wifi-load.jsp">서울시 공공wifi가져오기</a>|
	<a href="#">공공wifi조회이력</a>
	<br>
	LAT : <input type = "text" value = "0.0" name = "lat">
	LNT : <input type = "text" value = "0.0" name = "lnt">
	<input type = "submit" value="내위치가져오기" onclick = "getMyPosition()">
	<input type = "submit" value="근처와이파이정보보기" onclick = "getMyPosition()">
	<br>
	
	<table>
		<thead>
			<tr>
				<th>거리(km)</th>
				<th>관리번호</th>
				
			</tr>
		</thead>
	</table>
</body>
</html>
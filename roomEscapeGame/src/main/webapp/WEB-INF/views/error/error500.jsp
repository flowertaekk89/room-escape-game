<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="refresh" content="5;${pageContext.request.contextPath}">
	<title>Insert title here</title>
	<style type="text/css">
		body{
			background-color: black;
		}
		#wrapper{
			width : 700;
			height: auto;
			text-align : center;
			margin : 0 auto;
			color: white;
			margin-top: 16%;
		}
		#img{
			width: 200px;
			height: 200px;
		}
	</style>
</head>
<body>
	<div id="wrapper">
		<img src="resources/error.png" id="img">
		<p><b>5초 뒤에 첫 화면으로 이동합니다. 잠시 후 다시 이용해주세요</b></p>
		<p><b>오류 메세지 : 500</b></p>
	</div>
</body>
</html>
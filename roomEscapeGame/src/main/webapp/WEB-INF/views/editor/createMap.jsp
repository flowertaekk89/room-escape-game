<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>맵 만들기</title>
<!-- CSS reset -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/slick-theme.css">
<link rel="stylesheet" href="resources/css/slick.css">
<link rel="stylesheet" href="resources/css/animate.css">
<link rel="stylesheet" href="resources/css/loading.min.css"> 
<link rel="stylesheet" href="resources/css/main.css">

<style>
#imageArea{
	width : 200px;
	height : 200px;
	border : 1px solid #ccc;
}
body{
	margin : 0;
	overflod : auto;
	background : url("resources/images/background02.jpg");
	background-size : cover;
	text-align : center;
	}
body::-webkit-scrollbar{
	display:none;
}
	
.cd-user-modal {
  visibility: hidden;
  opacity: 0;
  transition: opacity 0.3s, visibility 0.3s;
} 
.cd-user-modal.is-visible {
  visibility: visible;
  opacity: 1;
}
footer {
	margin-top: 30%;
}
#btn_logout {
	text-align: center;
}

.da-btn da-booking-btn hvr-sweep-to-right-inverse {
	margin: 0 auto;
	margin-left: 50%;
}
#playListDiv {
	padding-bottom: 300px;
}
.col-sm-3{
	width: 50%;
}
.col-md-5{
	width: 30%;
}
#menu{
	margin-top: 15%;
	margin-left: 15%;
}
.da-bg-container{
	height: 100%;
}
.row{
	background : black;
	border-bottom : 0.5px solid #333;
}
.da-price-plan-block{
	margin-left : 10%;
}
.da-price-container{
	height : 50px;
	margin-top : 100px;
}
.da-text-container{
	height : 550px;
	margin-bottom : 30px;
}
.da-categories-list td{
	border-bottom : 1px solid #ccc;
	padding : 10px;
}

.deleteMap{
	border : none;
	background : none;
	color : #760000;
	font-weight : bolder;
	font-size : 1.5em;
}
#MapInfoWrapper{
	width : 100%;
	height : 120%;
	background-color : rgba(0,0,0,0.5);
}
.radio{
	margin-left : 15%;
	line-height: 40px;
}
.da-form-booking-item{
	margin-bottom : 15px;
}
.da-price{
	font-weight: bolder;
	margin-top : 10px;
	margin-bottom : 15px;
}
.savedMap{
	cursor: pointer;
}
</style>
<!-- JQuery -->
<script src = "resources/js/jquery-1.12.0.min.js"></script>
<script src = "resources/js/jquery-ui.min.js"></script>
<script src ="resources/js/jquery.min.js"></script>

<!-- bootStrap -->
<script src="resources/js/modernizr.js"></script>
<script src="resources/js/map.js"></script>
<script src="resources/js/slick.min.js"></script>
<script src="resources/js/script.js"></script>
<script src="resources/js/counter.js"></script>
<script src="resources/js/jquery.lightbox_me.js"></script>
<script src="resources/js/jquery.loading.min.js"></script>

<!-- animsition.css -->
<link rel="stylesheet" href="resources/css/animsition.min.css">
<!-- animsition.js -->
<script src="resources/js/animsition.min.js"></script>
<!-- lightBox -->
<script src="resources/js/jquery.lightbox_me.js"></script>
<script src="resources/js/jquery.loading.min.js"></script>

<script>
	$(function(){
		$("input[value=CreateMap]").on("click", function(){
			title = $("input[name=title]").val();
			user = $("input[name=user_id]").val();
			theme = $("input[name=theme]").val();
			console.log(title, user, theme);
			
			$(this).parent().submit();
		});
		
		$(".savedMap").on("click", function(){
			map_no = $(this).children(":first").text();
			location.href = "setMapno?map_no=" + map_no;
		});
		
		$(".deleteMap").on("click", function(e){
			e.stopPropagation();
			map_no = $(this).parent().siblings(":first").text();
			console.log(map_no);
			var tr =$(this).parent().parent();
			$.ajax({
				url : "deleteMap"
				, data : "map_no=" + map_no
				, dataType : "text"
				, success : function(resp){
					if(resp == 1){
						alert("정상적으로 삭제되었습니다.");
					}else alert("삭제에 실패하였습니다.");
					tr.remove();
				}
				, error : function(resp){
					console.log(resp);
				}
			});
		});
		
	});
	function logout() {
		location.href = "logout";
		}

	function updateForm() {
	location.href = "updateForm";	
	}
</script>
</head>
<body>
	<header>
		<div class="container-fluid">
			<div class="row">
				<div class="col-lg-1 col-md-2 col-sm-2 col-xs-5">
					<div class="da-header-logo-container">
						<a href="mainForm">
							<img src="resources/images/logo.png" alt="Escape Room" class="img-responsive">
						</a>
					</div>
				</div>
				<div class="col-lg-9 col-md-7 col-sm-7 col-xs-2 da-text-align-right">
					<div class="da-menu-button">
						<img src="resources/images/menu-icon.png" alt="Menu">
					</div>
					<nav>
						<ul class="da-menu">
							<li class="da-active-menu-link"><span class="da-hover-menu-line"></span>
								<a href="#">Home</a>
							</li>
							<li><span class="da-hover-menu-line"></span>
								<a href="#" onclick="updateForm()">Member Info</a>
							</li>
							<li><span class="da-hover-menu-line"></span><a href="faq.html">FAQ</a></li>
							<li class="da-active-menu-link"><span class="da-hover-menu-line"></span>
								<a href="#" onclick="logout()">Logout</a>
							</li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>

<div id = "MapInfoWrapper">
	<div class="col-sm-4 da-text-align-center da-price-plan-block">
		<div class="da-price-container">
			<div>
				<p class="da-price-title">You can create new Map</p>
				<p class="da-margin-top-15">We are make Maps for you, when u want to</p>
			</div>
		</div>
		<div class="da-text-container">
		<p class="da-price">Create Map</p><br>
		<form action="createMap" method = "POST" class="da-form-booking">
			<label><input type = "text" class="da-form-booking-item" name = "title" placeholder="Map title"></label>
			<label><input type = "text" class="da-form-booking-item" name = "user_id" value = "${loginUser.id}" readonly="readonly"></label>
			
			<div class="da-form-booking-item" style = "text-align : left;">Theme | 
				<label class = "radio"><input type = "radio" name = "theme" value = "Normal" checked="checked">Normal</label>
				<label class = "radio"ds><input type = "radio" name = "theme" value = "Hard">Hard</label><br>
			</div> 
			<textarea class="da-form-booking-item da-form-comment" rows="5" cols="30" name = "content"></textarea>
			<input type = "button" class="da-btn da-form-booking-button hvr-sweep-to-right" value = "CreateMap">
			<input type = "button" class="da-btn da-form-booking-button hvr-sweep-to-right" value = "Cancel">
		</form>
		</div>
	</div>
	
	
	<div class="col-sm-4 da-text-align-center da-price-plan-block">
		<div class="da-price-container">
			<div>
				<p class="da-price-title">Aleady maked Map List</p>
				<p class="da-margin-top-15">이전에 만들어둔 맵 목록을 출력합니다</p>
			</div>
		</div>
		<div class="da-text-container">
		<p class="da-price">이전 맵 목록 </p><br>
			<table class="da-sidebar-container col-md-12 col-sm-6 col-xs-12">
			<c:forEach var = "map" items = "${maps}">
				<tr class = "savedMap da-categories-list da-padding-0">
					<td>${map.no}</td>
					<td>${map.title}</td>
					<td>${map.theme }</td>
					<td>${map.inputdate }</td>
					<td>
						<input type = "button" value= "X" class = "deleteMap da-float-right" >
					</td>
				</tr>
			</c:forEach>
			</table>
		</div>
	</div>
</div>

</body>
</html>
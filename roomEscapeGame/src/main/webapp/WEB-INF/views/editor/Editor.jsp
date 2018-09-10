 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html>
	<meta charset="UTF-8">
<head>
<link rel = "stylesheet" href = "resources/css/jquery-ui.min.css">
<link rel = "stylesheet" href="resources/css/literallycanvas.css">

<!-- CSS reset -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/slick-theme.css">
<link rel="stylesheet" href="resources/css/slick.css">
<link rel="stylesheet" href="resources/css/animate.css">
<link rel="stylesheet" href="resources/css/main.css">
<link rel="stylesheet" href="resources/css/loading.min.css"> 

<style>
	body{
		margin : 0;
		overflod : auto;
		background-color: black;
	}
	body::-webkit-scrollbar{
		display:none;
	}
	#canvas, #Objects{
		float : left;
	}
	#objects{
		overflow: auto;
		height : 450px;
	}
	#objects::-webkit-scrollbar{
		display : none;
	}
	img{
		width : 170px;
		float : left;
		margin : 0;
		margin-top : -40px;
	}
	.callFloor>img{
		width : 100%;
		float : left;
		margin : 0;
	}
	button{
		position : relative;
		width : 400px;
		height : 55px;
		margin : 0 10px;
 		overflow: hidden;
		border-radius: 10px 0 0 0;
		background-color : white;
		padding : 0;
	}
	.triangle{
		position : absolute;
		left :110px;
		width : 0px;
		height : 0px;
		border-left : 60px solid transparent;
		border-bottom : 60px solid #000;
		opacity : 0.8;
		display: inline-block;
		z-index : 5;
	}
	.nameTag{
		opacity : 0.8;
		position : absolute;
		left :170px;
		display: inline-block;
		background : #000;
		width : 300px;
		height : 60px;
		z-index : 5;
		font-size: 15px;
		font-weight : bold;
		line-height : 50px;
		text-align : center;
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
	#wrapper{
		margin-top : 6%;
	}
	#mapinfo{
		width : 400px;
		background-color : rgba(200,200,200,0.2);
		margin : 0 10px;
	}
	.da-booking-date-number{
		font-weight: bolder;
		letter-spacing: 10px;
	}
	.da-availabe-date{
		font-size : medium;
		font-size : medium;
	}
	.title{
		width : 100%;
		border-bottom : 1px solid #fff;
		line-height: 0.75em;
		margin-left : 15px;
		margin-bottom : 10px;
	}
	#trick{
		color: black;
		background: rgba(31, 34, 37, 0.6);
		border: 1px solid #536376;
		-webkit-border-radius: 6px;
		-webkit-box-shadow: rgba(0,0,0,.6) 0px 2px 12px;
		-moz-box-shadow: rgba(0,0,0,.6) 0px 2px 12px;
		padding: 14px 22px;
		width: 450px;
		height : 450px;
		position: relative;
		font-size : 2em;
		font-weight : bolder;
		
/* 		display: none; */
		}
	.btn-3d {
		    position: relative;
		    display: inline-block;
		    font-size: 22px;
		    padding: 20px 40px;
		    color: #F16B6F;
		    margin: 20px 10px 10px;
		    border-radius: 6px;
		    text-align: center;
		    transition: top .01s linear;
		    text-shadow: 0 1px 0 rgba(0,0,0,0.15);
	}
	.btn-3d.green {
	   background-color: #3C3530;
	   box-shadow: 0 0 0 1px #3C3530 inset,
	               0 0 0 2px rgba(255,255,255,0.15) inset,
	               0 8px 0 0 rgba(60, 53, 48, .7),
	               0 8px 0 1px rgba(0,0,0,.4),
	               0 8px 8px 1px rgba(0,0,0,0.5);
	}
	.btn-3d.green:active {
	   box-shadow: 0 0 0 1px #3C3530 inset,
	               0 0 0 2px rgba(255,255,255,0.15) inset,
	               0 0 0 1px rgba(0,0,0,0.4);
	}
	.btn-3d:active {
	   top: 9px;
	}
	.btn-3d.green:hover  {
	    color: #F16B6F;
		background-color: #C5C6B6;
	}
	.close_x{
		float : left;
	}
	.editorbtn{
		background : #000;
		border : 2px solid #666;
		margin-left : 6px;
		padding : 4px 6px;
		border-radius : 3px;
		margin-bottom : 5px;
	}
</style>

<!-- JQuery -->
<script src = "resources/js/jquery-1.12.0.min.js"></script>
<script src = "resources/js/jquery-ui.min.js"></script>

<!-- bootStrap -->
<script src="resources/js/modernizr.js"></script>
<script src="resources/js/map.js"></script>
<script src="resources/js/slick.min.js"></script>
<script src="resources/js/script.js"></script>
<script src="resources/js/counter.js"></script>
<script src="resources/js/jquery.lightbox_me.js"></script>
<script src="resources/js/jquery.loading.min.js"></script>

<!-- Three.js -->
<script src = "resources/js/threejs/three.js"></script>
<script src = "resources/js/threejs/SceneExporter.js"></script>
<script src = "resources/js/threejs/SceneLoader.js"></script>
<script src = "resources/js/threejs/stats.js"></script>
<script src = "resources/js/threejs/Projector.js"></script>
<script src = "resources/js/threejs/OrbitControls.js"></script>
<!-- editor -->
<script src = "resources/js/editor/mapEditor.js"></script>
<script src = "resources/js/editor/minimap.js"></script>
<script src = "resources/js/editor/editor01.js"></script>
<script src = "resources/js/editor/editor02.js"></script>
<!-- literallyCanvas -->
 <script src="//cdnjs.cloudflare.com/ajax/libs/react/0.14.7/react-with-addons.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/react/0.14.7/react-dom.js"></script>
<script src ="resources/js/literallyCanvas/literallycanvas.js" ></script>

<!-- animsition.css -->
<link rel="stylesheet" href="resources/css/animsition.min.css">
<!-- animsition.js -->
<script src="resources/js/animsition.min.js"></script>
<!-- lightBox -->
<script src="resources/js/jquery.lightbox_me.js"></script>
<script src="resources/js/jquery.loading.min.js"></script>


<script>
$(".animsition").animsition({
    inClass: 'fade-in-down-sm',
    outClass: 'fade-out-down-sm',
    inDuration: 1500,
    outDuration: 800,
    linkElement: '.animsition-link',
    // e.g. linkElement: 'a:not([target="_blank"]):not([href^="#"])'
    loading: true,
    loadingParentElement: 'body', //animsition wrapper element
    loadingClass: 'animsition-loading',
    loadingInner: '', // e.g '<img src="loading.svg" />'
    timeout: false,
    timeoutCountdown: 5000,
    onLoadEvent: true,
    browser: [ 'animation-duration', '-webkit-animation-duration'],
    // "browser" option allows you to disable the "animsition" in case the css property in the array is not supported by your browser.
    // The default setting is to disable the "animsition" in a browser that does not support "animation-duration".
    overlay : false,
    overlayClass : 'animsition-overlay-slide',
    overlayParentElement : 'body',
    transition: function(url){ window.location.href = url; }
});


function logout() {
location.href = "logout";
}

function updateForm() {
location.href = "updateForm";	
}
</script>


<title>Editor</title>
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

<br>
<div id = "wrapper">
	<div id = "stats"></div>
	<div id = "canvas"></div>
<!-- 	<div id = "minimap" class="container"></div> -->
	
	<!-- 	Mapinfo -->
	<div class="da-booking-date da-last-day" id = "mapinfo">
		<p class="da-booking-date-number" id = "title">${mapinfo.title}</p>
		<div class="da-availabe-date" id = "theme">${mapinfo.theme}</div>
		<div class="da-availabe-date" id = "map_no">${mapinfo.no}</div>
	</div>
	
	
	<!-- 	Editor Buttons -->
	<div id = "editorbuttons">
		<br>
		<input type = "button" value = "Deco/Tools" id = "secondEdit" class = "editorbtn">
		<input type = "button" value = "임시저장" id = "saveScene" class = "editorbtn">
		<input type = "button" value = "cancel" id = "cancel" class = "editorbtn">
		<input type = "button" value = "clear" id = "clear" class = "editorbtn">
		<input type = "button" value = "맵 만들기 취소" id = "deleteMap" class = "editorbtn">
		<div id = "objects">
			<div id = "objects1">
				<!-- itemList 안에 있는 아이템의 개수만큼 foreach -->
				<c:forEach var ="items" items = "${items}">
				<c:set var="key" value = "${items.key}"/>
				<c:set var="list" value = "${items.value}"/>
				<div>
					<p class="da-booking-date-number title">${key}</p>
					<c:forEach var = "item" items = "${list}" varStatus="stat">
						<button data-itemno = "${list[stat.index].no}"
								 data-itemscale = "${list[stat.index].scale}" class =${list[stat.index].type eq "floor"? "callFloor" : "callObjBtn"}>
							<img src = "down_Icon_img?icon=${list[stat.index].icon_file}" alt = "${list[stat.index].icon_file}">
							<div class = "triangle"></div>
							<div class = "nameTag">${list[stat.index].json_file}</div>
						</button>
					</c:forEach>
				</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
</body>
</html>
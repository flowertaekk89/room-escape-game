<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/slick-theme.css">
<link rel="stylesheet" href="resources/css/slick.css">
<link rel="stylesheet" href="resources/css/animate.css">
<link rel="stylesheet" href="resources/css/main.css">
<link rel="stylesheet" href="resources/css/loading.min.css"> <!-- CSS reset -->

<script src="resources/js/modernizr.js"></script>
<script src="resources/js/jquery-1.12.0.min.js"></script>
<script src="resources/js/map.js"></script>
<script src="resources/js/slick.min.js"></script>
<script src="resources/js/script.js"></script>
<script src="resources/js/jquery.lightbox_me.js"></script>
<script src="resources/js/jquery.loading.min.js"></script>
<style type="text/css">
	#wrapper{
		width: 800px;
		height: 800px;
	}
	table,tr,td{
		border-spacing: 10px 10px;
	}
	#userTable{
		width: 100%;
		height: 50%;
		text-align: center;
		margin: 0 auto;
		padding: 10px;
		border-collapse: separate;
	}
	.table_data{
		border: 5px solid #323232;
		border-style:ridge;
		width: 25%;
		height: 50%;
		padding: 10px;
		color: olive;
		font: bold;
	}
	#mapTable{
		width: 50%;
		height: 50%;
		text-align: center;
		margin: 0 auto;
		border-collapse: separate;
	}
	/* #title{
		text-align: center;
	} */
	#text{
		width: 230px;
		
	}
	/* #mapExplanation{
		width: 200px;
	}
	#mapSelection{
		width: 200px;
		height: 200px;
	}
	#startBtn{
		width: 200px;
		height: 150px;
	} */
	
	#chatting{
		width: 75%;
		height: 3%;
	}
	.btn-gradient {
    text-decoration: none;
    color: white;
    padding: 10px 30px;
    display: inline-block;
    position: relative;
    border: 1px solid rgba(0,0,0,0.21);
    border-bottom: 4px solid rgba(0,0,0,0.21);
    border-radius: 4px;
    text-shadow: 0 1px 0 rgba(0,0,0,0.15);
	}
	.btn-gradient.green {
    background: rgba(130,200,160,1);
    background: -moz-linear-gradient(top, rgba(130,200,160,1) 0%, rgba(130,199,158,1) 100%);
    background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(130,200,160,1)), color-stop(100%, rgba(130,199,158,1)));
    background: -webkit-linear-gradient(top, rgba(130,200,160,1) 0%, rgba(130,199,158,1) 100%);
    background: -o-linear-gradient(top, rgba(130,200,160,1) 0%, rgba(130,199,158,1) 100%);
    background: -ms-linear-gradient(top, rgba(130,200,160,1) 0%, rgba(130,199,158,1) 100%);
    background: linear-gradient(to bottom, rgba(130,200,160,1) 0%, rgba(124, 185, 149, 1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#82c8a0', endColorstr='#82c79e', GradientType=0 );
	}
	.btn-gradient {
    margin: 5px;
	}
	.color .green  {background: #82c8a0;}
	.btn.mini, 
	.btn-two.mini, 
	.btn-gradient.mini, 
	.btn-effect.mini {
	  padding: 4px 12px;  
	  font-size: 12px;
	}
	.scrollbar-inner > .scroll-element .scroll-bar { background-color: #c2c2c2; }
	
	
	/* 0920 변경##### 아래 추가 */
	footer{height: 100%; padding-bottom: 1%;}
	#goBack{
		float: right;
	}
	/* btn - 3d */
	.btn-3d {
    position: relative;
    display: inline-block;
    font-size: 22px;
    padding: 20px 60px;
    color: white;
    margin: 20px 10px 10px;
    border-radius: 6px;
    text-align: center;
    transition: top .01s linear;
    text-shadow: 0 1px 0 rgba(0,0,0,0.15);
}
	.btn-3d.green {
    background-color: #82c8a0;
    box-shadow: 0 0 0 1px #82c8a0 inset,
                0 0 0 2px rgba(255,255,255,0.15) inset,
                0 8px 0 0 rgba(126, 194, 155, .7),
                0 8px 0 1px rgba(0,0,0,.4),
                0 8px 8px 1px rgba(0,0,0,0.5);
	}
	.btn-3d.green:active {
    box-shadow: 0 0 0 1px #82c8a0 inset,
                0 0 0 2px rgba(255,255,255,0.15) inset,
                0 0 0 1px rgba(0,0,0,0.4);
	}
	.btn-3d:active {
    top: 9px;
	}
	.btn-3d.green:hover  {background-color: #80C49D;}
</style>


<!-- Scroll 관련 css js 파일 제거 -->
<script type="text/javascript" src="resources/js/jquery-1.12.0.min.js"></script>
<script type="text/javascript">

	//뒤로가기 막기
	history.pushState(null, null, location.href);
	window.onpopstate = function (event)
    {
        history.go(1);
    };//window.onpopstate
    
    // 새로고침 막기
    window.onkeydown = function() {
    	var kcode = event.keyCode;
    	if(kcode == 116) event.returnValue = false;
    };// window.onkeydown

	$(function(){
 		$(document).ready(function() {
			$("#textSend").click(function() {
				$("#chattingScreen").scrollTop($("#chattingScreen")[0].scrollHeight);
				sendMessage();
			});//textSend
		});//ready
	
		var roomNum = '${room.no}';
		var nickname = '${user.nickname}';
		var userId = '${user.id}';
		var userPw = '${user.pw}';
		
		var master_id = '${room.master_id}';
		
		function enterUser (resp){
			//초기화
			$('#user0').html('Empty');
			$('#user1').html('Empty');
			$('#user2').html('Empty');
			$('#user3').html('Empty');
			
			$.each(resp, function(i, elt) {
				if(i == 0){
					$('#user0').html('<img src="resources/images/'+elt.profile+'.png"><br />'+elt.nickname);
				}else if(i == 1){
					$('#user1').html('<img src="resources/images/'+elt.profile+'.png"><br />'+elt.nickname);
				}else if(i == 2){
					$('#user2').html('<img src="resources/images/'+elt.profile+'.png"><br />'+elt.nickname);
				}else if(i == 3){
					$('#user3').html('<img src="resources/images/'+elt.profile+'.png"><br />'+elt.nickname);
				}//inner else if
			});//each
			
		}//enterUser
		
		var ws = new WebSocket("ws://" + window.location.host + "/escape/echo");
		ws.onmessage = onMessage;
		ws.onopen = function() {
			ws.send(roomNum + "|roomNum|" + userId + "|userId|" + userPw + "|userPw|");
			$.ajax({
				url : "renew"
				, method : "GET"
				, data : "id=" + userId + "&pw=" + userPw + "&roomNum=" + roomNum
				, dataType : "json"
				, success : enterUser
				, error : function(){
					alert('ERROR');
				}//error
			});//ajax
			
		}//onopen
		ws.onclose = onClose;
	
		// 근택 ### 변경 0924
		function sendMessage() {
			if($('#text').val() == ''){
				return;
			};//if
			
			ws.send(roomNum + "*" + $("#text").val());
			$('#text').val('');
		}//sendMessage

		function onMessage(evt) {
			var data = evt.data.toString();
			
			// USER 입장 시
			if(data === '|Enter|'){
				$(function(){
					$.ajax({
						url : "renew"
						, method : "GET"
						, data : "id=" + userId + "&pw=" + userPw + "&roomNum=" + roomNum
						, dataType : "json"
						, success : enterUser
						, error : function(){
							alert('ERROR');
						}//error
					});//ajax
				});//main
				return;
			}//if
			
			// GameRoom으로 이동 시
			if(data.includes('|goToGameRoom|')){
				location.href="/escape/testRoom?nickname="+nickname+"&roomNum="+roomNum;
			}//if
			
			// ROOM 삭제 시
			if(data === '|room_deleted|'){
				alert('방장이 퇴장했습니다.');
				history.back();
			}//if
			$("#chattingScreen").append(data + "\r\n");
		}//onMessage
	
		function onClose(evt) {
			$("#chattingScreen").append("연결 끊김");
		}//onClose
		
		$('#text').keyup(function(e){
			if(e.keyCode == 13){
				$('#textSend').trigger('click');
				$(this).val('');
				return;
			}//if
		});//chattingMessage
		
		// 엔터키 눌렀을 때 채팅방에 커서 생김
		$('body').keyup(function(e) {
			if(e.keyCode == 13){
				$('#text').focus();
				return;
			}//if
		});//body
		
		$('#startBtn').on('click', function(){
			
			if(master_id != userId) return;
			
			ws.send('|goToGameRoom|'+ roomNum);
			location.href="/escape/testRoom?nickname="+nickname+"&roomNum="+roomNum+"&userId="+userId;
		});//startBtn
		
		var wid = 600, hei = 650, sw = screen.availWidth, sh=screen.availHeight, px = (sw-wid)/2, py = (sh-hei)/2;
		$('#map_img').on('click', function(){
			window.open('mapList', 'Map', 'left='+px+',top='+py+',width='+wid+',height='+hei+',resizable=0,toolbar=no,menubar=no,status=no,scrollbars');
		});//map_img
	});//main
	
	function goBack(){
		window.onpopstate=function(){};
		history.go(-2);
	}//goBack
	
</script>
</head>
<body>

	<header>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-1 col-md-2 col-sm-2 col-xs-5">
				<div class="da-header-logo-container">
					<a>
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
						<li class="da-active-menu-link"><span class="da-hover-menu-line">천년의 탈출 </span>
							<a > (완규 )</a>
						</li>
					</ul>
				</nav>
			</div>
		</div>
	</div>
	</header>
	
	<footer>
	<div class="container" style="height: 90%">
		<div class="row da-footer-section" style="height: 90%">
			<pre>
			
			
			
			</pre>
				<div class="col-sm-5" style="padding-left: 5%; width: 60%; height: 90%">
					<p class="da-font-montserrat da-white-text" style="font-size: 20px;"><a class="btn-gradient green mini">${room.no}</a>
					${user.room_title}</p>
					<div class="row da-margin-top-30">
						<div class="col-xs-4 da-footer-photo" style=" width: 80%; ">
							<table id="userTable" style="width: 100%">
								<tr class="table_data">
									<td id="user0" style="background: white;" class="table_data"></td>
									<td id="user1" style="background: white;" class="table_data"></td>
									<td id="user2" style="background: white;" class="table_data"></td>
									<td id="user3" style="background: white;" class="table_data"></td>
								</tr>
							</table>
						</div>
						<div id="chat_place" style="height: 100%;width: 80%;">
						<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
							<div id="chatting" style=" height: 100%; width: 80%;">
								<textarea rows="12" cols="70" readonly="readonly" style="resize: none; background: #323232; font: white; border: 5px solid #323232;" id="chattingScreen" ></textarea>
								<!-- <p id="chattingScreen" style="margin-left: 0; overflow:scroll;"><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /></p>
								<br /> -->
							</div>
							<div style="float: right;">
							<input type="text" id="text" class="chatting">
							<input type="button" id="textSend" value="send" class="chatting" style="margin-top: 2%;">
							</div>
						</div>
					</div>
				</div>
				<div id="show_map" style="float: right; width: 40%">
					<table id="mapTable" style="width: 100%;">
						<tr><td id="map_img" class="table_data" style="width: 368.8; height:255.6; overflow-x:hidden;overflow-y:hidden"><img src="resources/images/background01.jpg"></td></tr>
						<tr><td id="map_explain" class="table_data" style="width: 368.8; height:77.2; overflow-x:hidden;overflow-y:hidden">
						<textarea rows="3" cols="60" readonly="readonly" style="resize: none; background: #323232; font: white; border: 5px solid #323232;">이방은 완규가 설계한 만든 방으로서, 시간내에 탈출하지 못할시 완규에게 살해당할 것이다</textarea> 
						</td></tr>
						<tr><td >
							<a class="btn-3d green" id="startBtn" style="font-weight: 300;">入る</a>
							<a class="btn-3d green" style="font-weight: 300;" onclick="goBack()">出る</a>
						</td></tr>
						
					</table>
				</div>
			</div>
			<pre>
			
			<!-- 점프용 -->
			</pre>
	</div>
  </footer>
</body>
</html>
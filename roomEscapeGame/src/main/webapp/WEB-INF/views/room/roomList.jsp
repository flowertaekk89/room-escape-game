<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>RoomList</title>
<style>
	#findID, #findPWD {
		float: left;
		border: 1px solid #666;
		border-radius: 10px;
		width: 49%;
		height: 100%;
		margin: 0 auto;
	}
	
	#findIDpop, #findPWDpop {
		position: absolute;
		top: 20%;
		left: 25%;
		display: none;
		width: 300px;
		height: 100px;
		background: white;
		border: 1px solid #ccc;
	}
	#search{
		margin-right: 50px;
		margin-top: 50px;
		text-align: right;
	}
	
	#searchWord, #selectedWord{
		color: black;
	}
	
	#searchBtn{
		color: black;
	}
	
	/* 0920 변경##### 아래 추가 */
	#goBack{
		float: right;
	}
	.da-booking-date da-last-day{
		color: white;
		font-weight: bold;
		
	}
</style>

<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/slick-theme.css">
<link rel="stylesheet" href="resources/css/slick.css">
<link rel="stylesheet" href="resources/css/animate.css">
<link rel="stylesheet" href="resources/css/main.css">
<script src="resources/js/jquery-1.12.0.min.js"></script>
<script src="resources/js/map.js"></script>
<script src="resources/js/slick.min.js"></script>
<script src="resources/js/script.js"></script>
<script type="text/javascript">

	// 0920 변경###### 아래 내용 추가
	//뒤로가기 막기
	history.pushState(null, null, location.href);
	window.onpopstate = function (event)
    {
        history.go(1);
    };

	$(function(){
		
		// 특정 방 검색
		var selectedWord ="";
		var searchWord="";
		$('#searchBtn').on('click', function(){
			selectedWord = $('#selectedWord option:selected').val();
			searchWord = $('#searchWord').val();
		});//searchBtn
		
		// roomList 목록 실시간 갱신
		setInterval(function() {
			$.ajax({
				url : "roomListRenew"
				, method : "GET"
				, data : 'selectedWord='+ selectedWord + '&searchWord=' + searchWord
				, dataType : 'json'
				, success : function(resp){
					$('.enter_room').html('');
					$.each(resp, function(i, elt) {
						
						//코드변경#### var room 전체 복붙하시면 돼요! ((처음부터 끝까지))
						var available_count;
						if(elt.numberOfUsers == 1){
							available_count = 3;
						}else if(elt.numberOfUsers == 2){
							available_count = 2;
						}else if(elt.numberOfUsers == 3){
							available_count = 1;
						}else if(elt.numberOfUsers == 4){
							available_count = 0;
						}//else if
						
						var room = "";
						if(elt.status == 'waiting'){
							room += '<div class="da-booking-date da-last-day" style="border-style:none; margin-left: 20%; background-color: rgba(0,0,0,0.5); padding:10px 5px; border-radius:5px;">';
							room += '<img src="resources/images/'+ elt.profile +'.png" style="width: 50%; height: 50%;">';
							room += '<div class="da-availabe-date" style=" color: white; font: bold;">title: '+ elt.title + '<br /> room master : ' + elt.master_id + '<br />'
							room += '<div class="da-availabe-date" style=" color: white; font: bold;">Availabe: '+ available_count +'</div>';
							room += '<button class="da-btn da-margin-top-30 hvr-sweep-to-right" style="width: 80%; margin-top: 10%;" data-roomNum="'+elt.no+'" data-roomPw="'+ elt.room_pw +'" data-roomTitle="'+ elt.title +'""><a href="#">Enter Room</a></button>'
							room += '</div>';
						}//if
						if(elt.status == 'Playing'){
							room += '<div class="da-booking-date da-last-day" style="border-style:none; margin-left: 20%;">';
							room += '<img src="resources/images/'+ elt.profile +'.png" style="width: 50%; height: 50%;">';
							room += '<div class="da-availabe-date"></div>'
							room += '<div class="da-availabe-date" style=" color: white; font: bold;"> status : playing<br />room master : ' + elt.master_id + '<br />'
							room += 'Availabe: 0 </div>';
							room += '<button class="da-btn da-margin-top-30 hvr-sweep-to-right" style="width: 80%; margin-top: 10%;"><a href="#" class="da-btn da-booking-btn hvr-sweep-to-right-inverse">Playing</a></button>';
							room += '</div>';
						}//if
						
						$('.enter_room').append(room);
						
						// 코드변경###  $('.da-booking-date-container') => 아래와 같이
						$('.da-btn').on('click', function(){
							
							// 방에 비밀번호 여부 확인 후 비밀번호 요청
							var room_pw = $(this).attr('data-roomPw');
							if(room_pw != 'null'){
								alert(room_pw);
								var password = prompt('Password here!');
								if(room_pw != password){
									alert('Wrong password!!');
									return;
								}//inner if
							}//outer if
							
							// 방 인원 4명초과 시 입장 제한
							if(elt.numberOfUsers == 4){
								alert('방 인원 제한 초과!');
								return;
							}//if
							
							// 방에 입장
							var room_no = $(this).attr('data-roomNum');
							var room_title = $(this).attr('data-roomTitle');
							location.href = "/escape/waitingRoom?room_no="+room_no+"&nickname=${nickname}&roomTitle="+room_title;
							
						});//da-booking-date
					});//each
				}//success
			});//ajax
		}, 500);//setInterval
		
	});//main
	
	// 0920 변경 #### 아래코드 추가
	function goBack(){
		window.onpopstate=function(){};
		history.go(-2);
	}//goBack
	
	function logout() {
		location.href = "logout";
	}
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
							<li class="da-active-menu-link"><span class="da-hover-menu-line"></span>
								<a href="#" onclick="goBack()" style="font: bold;">뒤로가기</a>
							</li>
							<li class="da-active-menu-link"><span class="da-hover-menu-line"></span>
								<a href="#" onclick="logout()" style="font: bold;">Logout</a>
							</li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>
	
	<div class="da-main-banner" style="background-image: url(resources/images/background04.jpg); width:110%; height: 110%; opacity: 0.9; position: absolute; ">
   </div>   <div class="da-main-title" style="margin-top: 3%;">
            
            <form class="da-search-form" style="width: 20%; color: black; float: right; margin-right: 3%; margin-top: 3%;">
               <input type="text" id="searchWord" style="border-radius: 10px; width: 60%;"> 
               <a class="btn-gradient red mini" id="searchBtn">검색</a>
             </form>
             <select id="selectedWord" style="float: right; margin-top: 3.5%;">
               <option value="roomName" selected="selected">방이름</option>
               <option value="roomMaster">방장</option>
            </select>
             <pre>
            
            </pre>
            
         <div class="da-calendar da-margin-top-75 da-font-montserrat da-padding-bottom-200" style="">
            <div class="da-month-name">
               <a href="#" class="da-btn-month da-btn-month-prev" style="border-color: black;font: bold;"><i class="fa fa-angle-left" aria-hidden="true" style="font: bold;color: black"></i></a>
               <p style="font-weight: bold; color: black;">Escape Room</p>
               <a href="#" class="da-btn-month da-btn-month-next" style="border-color: black; font: bold;"><i class="fa fa-angle-right" aria-hidden="true" style="font: bold;color: black"></i></a>
            </div>
            <pre>
             
             
             
            </pre>
            <!-- <div class="da-calendar-block da-margin-top-30" style="margin-left: 25%; top: 35%; border-style:none; border-color:transparent ; padding: 10%;"> -->
               <!-- <div class="da-booking-day-container da-margin-top-50">
                  <div class="da-booking-day">Monday</div>
                  <div class="da-booking-day">Tuesday</div>
                  <div class="da-booking-day">Wednesday</div>
                  <div class="da-booking-day">Thursday</div>
                  <div class="da-booking-day">Friday</div>
                  <div class="da-booking-day">Saturday</div>
                  <div class="da-booking-day">Sunday</div>
               </div> -->
               <div class="enter_room">
                  <!-- <div class="da-booking-date da-last-day" style="border-style:none; margin-left: 20%;">
                     <img src="resources/images/profile_01.png" style="width: 50%; height: 50%;">
                     <p class="da-booking-date-number" style="font-weight: bold; color: white;">01</p>
                        <div class="da-availabe-date" style=" color: white;">Availabe: 9</div>
                     <button class="da-btn da-margin-top-30 hvr-sweep-to-right" style="width: 80%; margin-top: 10%;">enter</button>
                  </div> -->
               </div>
            <!-- </div> -->
           </div>
      </div>
	
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Map List</title>
<link rel = "stylesheet" href = "resources/css/bootstrap.min.css">
<link rel = "stylesheet" href = "resources/css/main.css">
<link rel = "stylesheet" href = "resources/css/animate.css">
<link rel = "stylesheet" href = "resources/css/font-awesome.min.css">
<link rel = "stylesheet" href = "resources/css/pe-icon-7-stroke.css">
<link rel = "stylesheet" href = "resources/css/responsive.css">
<link rel = "stylesheet" href = "resources/css/slick-theme.css">
<link rel = "stylesheet" href = "resources/css/slick.css">
<link rel = "stylesheet" href = "resources/css/style.css">
	
	<style type="text/css">
		#screen{
			border: 5px solid #323232;
			border-style:ridge;
		}
		table{
			width: 100%;
			text-align: center;
		}
		table,td,tr,th{
			border: 5px solid #323232;
			border-style:ridge;
			font: bold;
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
	</style>
	
	<script src="resources/js/jquery-1.12.0.min.js"></script>
	<script type="text/javascript">
	
		$(function(){
			$.ajax({
				url : "getMapList"
				, method : "GET"
				, dataType : "json"
				, success : function(resp){
					
						var table = '';
						table += '<table>';
						
						table += '<tr>';
						table += '<th>NO</th>';
						table += '<th>Title</th>';
						table += '<th>Created date</th>';
						table += '<th>Select</th>';
						table += '</tr>';
						
					$.each(resp, function(i, elt) {
						table += '<tr>';
						table += '<td><span>'+ elt.no +'</span></td>';
						table += '<td>'+ elt.title +'</td>';
						table += '<td>'+ elt.inputdate +'</td>';
						table += '<td><a href="#" class="btn-gradient green mini" data-mapNo="'+ elt.no +'" onclick="selectMap()">선택</a></td>';
						table += '</tr>';
					});//each
					
						table += '</table>';
						
						$('#list').append(table);
						
						$(".btn-gradient").on("click", function(){
							var map_no = $(this).attr('data-mapNo');
							$.ajax({
								url : "mapNo_update"
								, method : "GET"
								, data : "map_no="+map_no
								, success : function() {
									window.close();
								}//success
								, error : function(){
									alert("map_no UPDATE ERROR");
								}//error
							});
						});//map_info
				}//success
			});//ajax
		});//main
	</script>
</head>
<body>
	<div class="container-fluid da-margin-top-50" >
		<div class="col-md-3 col-md-offset-3 col-sm-4 col-sm-offset-0 da-price-plan-block da-margin-bottom-30" id="screen">
			<div id = "roomSetting" class="da-price-container da-price-container-room">
				<p class="da-price-title da-price-title-room">MAP</p>
			</div>
			<div class="da-text-container">
				<div id="list"></div>
			</div>
		</div>
	</div>
	
</body>
</html>

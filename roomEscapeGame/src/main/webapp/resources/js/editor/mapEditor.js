/**
 * mapEditor_Main
 * author LSH 2017.9.3
 */

$(function() {
	init();
//	initMinimap();
});

var scene, orbit;
function init() {
	var width = window.innerWidth * 0.7;
	var height = window.innerHeight*0.8;

//	var stats = initStats();
	scene = MapEdit01.setBasicOption(scene);
	callMapInfo(scene);
	
	// 랜더러 설정
	var renderer = new THREE.WebGLRenderer();
	renderer.setClearColor(new THREE.Color(0x000000, 1));
	renderer.setSize(width, height);
	renderer.shadowMapEnabled = true;

	$('#canvas').append(renderer.domElement);
	
	//Editor button Event
	$("#saveScene").on("click", function() {
		MapEdit01.saveScene(scene);
	});
	
	//맵 삭제하기
	$("#deleteMap").on("click", function(){
		var map_no = $("#map_no").text();
		$.ajax({
			url : "deleteMap"
			, data : "map_no="+map_no
			, dataType : "text"
			, success : function(resp){
				alert("메인 페이지로 돌아갑니다.");
				location.href = "mainForm";
			}
			, error : function(resp){
				console.log(resp);
			}
		});
	});
	//첫번째 에디터 이벤트설정
	firstEditorSetting();
	
	$(document).on("click", "#secondEdit", function(){
		val = $(this).val();
		if(val == "Deco/Tools") {
			console.log("Deco/Tools");
			obj2 = $("#objects2").html();
			console.log(obj2);
			if(!obj2) callSecondItems();
			else {
				$("#objects1").remove();
				$("#objects2").css({
					"display" : "block"
				});
			}
		}
		else if(val == "Furniture"){
			console.log("Furniture");
			$(this).val("Deco/Tools");
			$("#objects2").remove();
			$("#objects1").css({
				"display" : "block"
			});
		}
	});
	
	camera = scene.children.find(function(item) {
		return item instanceof THREE.Camera ? item : null;
	});
	
	orbit = new THREE.OrbitControls(camera, renderer.domElement);
	
	render();
	
	
	function callSecondItems(){
//		scene = MapEdit01.cancel(scene);
		$("#objects1").css({"display" : "none"});
		addDiv = '<div id = "hintcanvas"></div>';
		$("#objects").append(addDiv);
		
		//scene에 놓인 오브젝트 그룹화
		MapEdit02.setScene(scene);
		
		//이벤트 해제
		$("#firstEditor").on("click");
		$("#secondEdit").off("click");
		$("#saveScene").off("click");
		$("#canvas").off("click");
		$("#canvas").off("mousedown");
		$("#canvas").off("mousemove");
		$("#saveScene").on("click", function(){
			MapEdit02.saveScene(scene);
		});
		$("#canvas").on("mousemove", function(e){
			scene = MapEdit02.moveMousePointer(e, scene);
		});
		$("#canvas").on("mousedown", function(e){
			switch(e.which){
			case 1 : 
				scene = MapEdit02.clickGroupFunction(e, scene);
				break;
			case 3 : 
				//오른쪽버튼 원래 contextmenu 비활성화
				e.preventDefault();
				scene = MapEdit02.mouseRightClick(e, scene);
				break;
			}
		});
		$("#canvas").on("contextmenu",function(e){
			e.preventDefault();
		});

		$("#cancel").off("click");
        $("#cancel").on("click", function(){
            scene = MapEdit02.cancel(scene);
        });
        
		var theme = $("#theme").text();
		console.log(theme);
		
		$.ajax({
			url : "call_items_sec"
			, data : "theme=" + theme
			, dataType : "json"
			, success : function(resp){
				//받아온 데이터가 {key : [{},{},{}...], key : [{},{},{},...]} 이렇게 생긴애들이라 풀어줘야함
				//item button setting
				console.log(resp);
				$("#objects").append('<div id="objects2"></div>');
				
				$.each(resp, function(index, item){
					var buttons = '';
					buttons += '<div><p class="da-booking-date-number title">'+index+'</p>';
					for(var i in item){
						console.log(item[i].icon_file);
						buttons +='<button data-itemno=' + item[i].no;
						buttons +=' data-itemscale="' + item[i].scale + '"';
						buttons +=' data-type="' + index + '"';
						buttons +=' class = "callObjBtn_sec">';
						buttons +='<img src = "down_Icon_img?icon='+ item[i].icon_file +'" alt = "'+item[i].item_name+'">';
						buttons +='<div class = "triangle"></div>';
						buttons +='<div class = "nameTag">'+ item[i].json_file +'</div>';
						buttons +='</button>';
					};
					buttons +='</div>';
					$("#objects2").append(buttons);
				});
				
				//이벤트 걸어주기
				$(document).on("click",".callObjBtn_sec", function(){
					itemno = $(this).data("itemno");
					itemType = $(this).data("type");
					icon = $(this).children("img").attr("src");
					console.log(icon);
					itemScale = $(this).data("itemscale") ? $(this).data("itemscale") : 10;
					console.log(itemno + " 번 아이템 클릭 : 물건위에 올릴 수 있는 오브젝트 더하기");
					scene = MapEdit02.addObject(scene, itemno, itemScale, itemType, icon);
				});
			}
			, error : function(){
				
			}
		});
		$("#secondEdit").val("Furniture");
	}
	
	
	function firstEditorSetting(){
		// canvas내부 마우스 모션에 대한 이벤트
		$("#canvas").on("contextmenu",function(e){
			e.preventDefault();
		});
		$("#canvas").on("mousedown", function(e) {
			switch (e.which) {
			case 1:
				console.log("왼쪽");
				console.log(scene);
				scene = MapEdit01.mouseClickFunction(e, scene);
				break;
			case 3:
				e.preventDefault();
				console.log("오른쪽");
				scene = MapEdit01.downMouseButtonRight(e, scene);
				break;
			}
		});
		$("#canvas").on("mousemove", function(e) {
			scene = MapEdit01.groupMove(e, scene);
		});

		// 버튼에 대한 이벤트
		$("#cancel").on("click", function() {
			scene = MapEdit01.cancel(scene);
		});
		$("#clear").on("click", function() {
			scene = MapEdit01.clearScene(scene);
		});

		//objectButtonEvent
		$(document).on("click",".callObjBtn", function(){
			itemno = $(this).data("itemno");
			itemScale = $(this).data("itemscale");
			console.log(itemScale);
			console.log(itemno + " 번 아이템 클릭");
			scene = MapEdit01.addObject(scene, itemno, itemScale);
		});
		
		$(document).on("click", ".callFloor", function(){
			itemno = $(this).data("itemno");
			console.log(itemno + " 번 바닥 클릭");
			scene = MapEdit01.setFloor(scene, itemno);
			
		});
	}
	
	// 랜더러
	function render() {
//		stats.update();
		orbit.update();
		// scene에 들어있는 카메라 찾기
		camera = scene.children.find(function(item) {
			return item instanceof THREE.Camera ? item : null;
		});
		renderer.render(scene, camera);
		requestAnimationFrame(render);
	}

	// Status
	function initStats() {

		var stats = new Stats();
		stats.setMode(0);
		stats.domElement.style.position = "absolute";
		stats.domElement.style.bottom = "0px";
		stats.domElement.style.left = "0px";

		$("#stats").append(stats.domElement);
		return stats;
	}
	
	// 자동 리사이즈 이벤트
    var onResize = function() {
    	width = window.innerWidth*0.7;
    	height = window.innerHeight * 0.8;
        camera.aspect = width / height;
        renderer.setSize( width, height );
        camera.updateProjectionMatrix();
    };
    window.addEventListener( 'resize', onResize, false );
    
	
	function callMapInfo(scene){
		mapno = $("#map_no").text();
		$.ajax({
			url : "getSavedItems"
			, type : "POST"
			, data : "map_no=" + mapno
			, dataType : "json"
			, success : function(resp){
				modelPath = "resources/json/model/";
				texturePath = "resources/json/texture/";
				var arr = [];
				$.each(resp, function(index, item){
					console.log(item);
					if(item.setting){
						console.log("setting is true");
						console.log(index, item);
						if(item.item_name == "plane"){
							//position 이지만 사실은 texture 경로
							textureimg = item.position;
							
							find = scene.children.find(function(item){
								return item.name == 'plane';
							});
							var texture = THREE.ImageUtils.loadTexture(textureimg);
							mat = new THREE.MeshLambertMaterial({
								map : texture
							});
							find.material = mat;
						}else{
							//item.name != plane일 때
							modelPath = "resources/json/model/";
							texturePath = "resources/json/texture/";
							$.ajax({
								url : "getJSON"
								, data : "no=" + item.item_no
								, dataType : "json"
								, async : false
								, success : function(resp){
									var mesh;
									var loader = new THREE.JSONLoader();
									json = resp.json_file;
									loader.load(modelPath + json+".json", function(geom, mat){
										p = JSON.parse(item.position);
										r = JSON.parse(item.rotation);
										s = resp.scale;
										console.log(p, r, s, json);
										mesh = new THREE.Mesh(geom, mat[0]);
										mesh.position.set(p.x, p.y, p.z);
										mesh.rotation.set(r._x, r._y, r._z);
										mesh.scale.set(s, s, s);
										mesh.name = item.item_name;
										mesh["item_no"] = resp.no;
										mesh["item_type"] = resp.type;
										mesh["canvas_availability"] = resp.canvas_availability;
										
										console.log(mesh);
										arr.push(mesh);
										count = arr.length;
										scene.add(mesh);
										MapEdit01.targetList.push(mesh);
										MapEdit01.targetGroup.push(mesh);
									}, texturePath);
								}
							});
						}
					}else console.log("item.setting == false");
				});
				console.log(MapEdit01.targetList);
				console.log(MapEdit01.targetGroup);
				console.log(arr);
			}
			, error : function(resp){
				console.log(resp);
			}
		});
	}
}




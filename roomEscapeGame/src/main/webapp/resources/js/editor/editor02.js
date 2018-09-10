/**
 * editor02.js
 * author LSH
 * 2017.9.5
 */

var MapEdit02 = {
	//선택 플래그
	isGroupSelected: false,
	isObjSelected: false
		//움직임에 대한 플래그
		,
	isGroupMove: false,
	isObjMove: false
		//설정할 캔버스의 가로, 세로
		,
	width: window.innerWidth * 0.7,
	height: window.innerHeight
		//마우스 포인터의 위치를 받아옴
		,
	mouse: {
		x: 0,
		y: 0
	}
	//선택된 그룹
	,
	selectedGroup: null
		//움직일 타겟 그룹
		,
	movingGroup: null
		//선택된 그룹 / 오브젝트의 부모그룹
		,
	parentGroup: null
		//intersects 의 타겟이 될 List : targetList 안에는 판까지 들어가고 Group 안에는 그룹들만 들어감
		,
	targetList: []
		//targetGroup 안에 들어가는 모든 그룹과 메쉬는 name 속성을 가지고 있어야 한다.
		,
	targetGroup: []
		//더하는 오브젝트에 붙일 고유번호
		,
	objnum: 0


		//scene.children 의 요소를 전부 그룹으로 바꾸기 : 다른 이름 줌	
		,
	setScene: function (scene) {
			console.log("스캔 내부에 존재하는 모든 오브젝트를 그룹화 합니다.");
			//scene 안에 존재하는 모든 오브젝트 그룹화 : 바닥, 조명, 카메라 제외
			objectArr = [];
			children = scene.children;
			$.each(children, function (index, item) {
				console.log(index);
				console.log(item);
				if (item instanceof THREE.Mesh) {
					//매쉬가 Plane이 아닐때와 Plane일 때
					objG = new THREE.Group();
					objG.add(item.clone());
					objG.name = item.name;
					
					//분기처리를 위해 설정
					objG["item_no"] = item.item_no;
					objG["item_type"] = item.item_type;
					objG["canvas_availability"] = item.canvas_availability;
					
					objectArr.push(objG);
					MapEdit02.targetList.push(item);
					MapEdit02.targetGroup.push(objG);
					//나머지 광원, 카메라들도 scene.children 에 넣어야 하므로 더해주기
				} else objectArr.push(item);
			});
			console.log(objectArr);
			console.log(MapEdit02.targetList);
			console.log(MapEdit02.targetGroup);
			scene.children = objectArr;
			console.log(scene);
		}

	//scene 저장하기
	, saveScene : function(scene){
		console.log("장면 저장하기");
		children = scene.children;
		console.log(children);
		map_no = $("#map_no").text();
		var count = 0;
		find(children[count]);
		
		//Scene.children 안에 있는 import된 매쉬를 저장한다
		function find(obj){
			console.log(count);
			console.log(children.length);
			if(obj instanceof THREE.Group){
				console.log("group일 때");
				for(var i in obj.children){
					console.log(obj.children[i].name);
					if(obj.children[i] instanceof THREE.Mesh) find(obj.children[i]);
				}
				if(count < children.length) {
					count++;
				}else {
					return;
				}
				find(children[count]);
				
			}else if(obj instanceof THREE.Mesh){
				console.log("mesh일 때");
				var data;
				if(!(obj.geometry instanceof THREE.PlaneGeometry)){
					//plane Geom이 아닐 때 Mesh의 정보 저장
					console.log("저장하기 : " + obj.name);
					console.log(obj.matching_item ? obj.matching_item : null);
					data = {
						"item_no" : obj.item_no
						, "map_no" : map_no
						, "item_name" : obj.name
						, "position" : JSON.stringify(obj.position)
						, "rotation" : JSON.stringify(obj.rotation)
						, "matching_item" : obj.matching_item ? obj.matching_item : null
						, "setting" : "true"
						, "retractable" : "false"
					};
				}else{
					//plane geom 일 때
					if(obj.material.map) textureFile = obj.material.map.sourceFile;
					data = {
							"item_no" : 8
							, "map_no" : map_no
							, "item_name" : obj.name
							, "rotation" : textureFile ? textureFile : ''
							, "position" : textureFile ? textureFile : ''
							, "setting" : "true"
							, "retractable" : "false"
						};
				}
				
				console.log(data);
				var trick_type = obj.trick_type;
				$.ajax({
					url : "saveScene"
					, type : "POST"
					, data : data
					, dataType : "text"
					, success : function(resp){
						console.log(resp);
						if(trick_type && trick_type == "password"){
							console.log("패스워드 트릭에 저장");
							map_no = $("#map_no").text();
							si_no = obj.item_no;
							content = obj.content;
							data = {
								"map_no" : map_no
								, "SI_no" : si_no
								, "content" : content
								, "solved" : "false"
							}
							
							$.ajax({
								url : "insertTrick"
								, type : "POST"
								, data : data
								, dataType : "text"
								, success : function(resp){
									if(resp == 1) alert("trick 저장성공");
									else console.log("저장실패", resp);
								}
							});
							
						}
					}
					, error : function(resp){
						console.log(resp);
					}
				});
			}else{
				//Mesh나 그룹이 아닐 경우에는 넘어가서 다음 객체를 넣은 재귀함수를 호출한다
				if(count < children.length) {
					count++;
				}else {
					return;
				}
				find(children[count]);
			}
		}//function : find
	}
	
	
	
		//오브젝트 더하기
		,
	addObject : function (scene, itemno, itemScale, itemType, icon) {
			console.log("object만들기");
			scene.remove(this.movingGroup);
			this.movingGroup = null;
			//17.9.27 추가
			//먼저 있는 div가 있으면 삭제
			settingDiv = $("#trickSet");
			if(settingDiv){
				settingDiv.remove();
			}
			console.log(itemType);
			if(itemType == "trick"){
				//trick의 정보를 받아옴
				var trickType;
				$.ajax({
					url : "getJSON"
					, data : "no="+itemno
					, dataType : "json"
					, success : function(resp){
						console.log(resp);
						console.log(resp.trick_type);
						
						//trick_type가 password일 경우
						if(resp.trick_type == "password"){
							//trick 설정하는 div 추가하기
							var setPw = '<div id="trick">';
							setPw += '<div id="puzzle_temp"> ';
							setPw += '<table><tr>';
							setPw += '<div class="da-footer-social" id="closeDiv">';
							setPw += '<a id="close_x" class="close sprited" href="#"><i class="fa fa-facebook"></i></a>';
							setPw += '</div>'
							setPw += '</tr><tr>';
							setPw += '<td><a class="btn-3d green" data-num="1">1</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="2">2</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="3">3</a></td>';
							setPw += '<td colspan="3">';
							setPw += '</td></tr><tr>';
							setPw += '<td><a class="btn-3d green" data-num="4">4</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="5">5</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="6">6</a></td>';
							setPw += '</tr><tr>';
							setPw += '<td><a class="btn-3d green" data-num="7">7</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="8">8</a></td>';
							setPw += '<td><a class="btn-3d green" data-num="9">9</a></td>';
							setPw += '</tr></table></div>';
							setPw += '<input type= "button" value = "SetPassword" id = "setpwd">';
							setPw += '</div>';
							$("#canvas").append(setPw);
							
							//lightBoxEvent
							$("#trick").lightbox_me({
								centered: true
								, preventScroll: true
								, onLoad: function() {
									$("#trick").find("input:first").focus();
								}
							});
							
							var answer = [];
							$(".btn-3d").on('click', function() {
								answer.push($(this).data("num"));
							});
							
							$("#setpwd").on('click', function(){
								//내용이 차면 일단 저장하는 것으로
								var content = '';
								for(var i in answer){
									if(i < answer.length-1)	content += answer[i] + '/';
									else if(i == answer.length-1) content += answer[i];
								}
								data = {
									"content" : content
									,"trick_type" : resp.trick_type
									, "item_no" : resp.no
								}
								sessionStorage.setItem("trickData_password", JSON.stringify(data));
								console.log(content);
								$("#trick").remove();
								setTrickdata();
							});
							
						}//if type == password?
						else{
							setTrickdata();
						}
					}//success
				});//ajax
				
			}else{
				console.log("trick이 아닐때");
				callObj();
			}
			return scene;
			
			function setTrickdata(){
				theme = $("#theme").text();
				var trickSet = '';
				trickSet += '<div id = "trickSet">';
				trickSet += '<img src = "' + icon + '" alt = "' + itemno + '" width = "200" height = "200">';
				trickSet += '<input type = "text" name = "itemName" placeholder = "아이템 이름">';
				trickSet += '<br>';
				trickSet += '<fieldset id = "radios"><legend>Matching Items</legend>';
				trickSet += '</fieldset>';
				trickSet += '<br>';
				trickSet += '<input type = "button" value = "설정하기" id = "setTrickInfo">';
				trickSet += '</div>';
				$("#canvas").append(trickSet).css({"position" : "relative"});
				$("#trickSet")
				.on("click", function(e){e.stopPropagation();})
				.css({
					"width" : "200px", "height" : "300px" , "border-radius" : "20px"
						, "background-color" : "rgba(100,100,100,0.7)", "z-index" : "10"
							, "color" : "white", "padding" : "10px", "textalign" : "center", "margin" : "0 auto"
				})
				.lightbox_me({
					centered: true
					, preventScroll: true
					, onLoad: function() {
						$("#trick").find("input:first").focus();
					}
				});
				$("input[name=itemName]").css({
					"color" : "white", "border" : "none", "border-bottom" : "2px solid white"
						, "background" : "#666", "width" : "100%"
				});
				//선택지를 불러오는 ajax
				$.ajax({
					url : "getTypeJSON"
						, data : "type=tool&theme=" + theme
						, dataType : "json"
							, success : function(resp){
								var radios = '';
								$.each(resp, function(index, item){
									radios += '<label>';
									radios += '<input type = "radio" name = "matching_item" value = "'+item.no+'">';
									radios += item.json_file + '</label><br>';
								});
								$("#radios").append(radios);
							}
				, error : function(resp){
					console.log(resp);
				}
				});
				
				
				//팝업 div 버튼 이벤트 걸기
				$("#setTrickInfo").on("click", function(){
					checkedTool = $("input[name=matching_item]:checked").val();
					trickName = $("input[name=itemName]").val();
					trickData = {
							"item_name" : trickName, "matching_item" : checkedTool
					}
					sessionStorage.setItem("trickData", JSON.stringify(trickData));
					console.log(trickData);
					alert("자물쇠 설정이 완료되었습니다.");
					$("#trickSet").remove();
					
					callObj();
					return scene;
				});
				
			}
			
			function callObj(){
				console.log("object 부르기");
				var group = new THREE.Group();
				var mesh; 
				$.ajax({
					url : "getJSON"
					, data : "no=" + itemno
					, dataType : "json"
					, success : function(resp){
						//JSON파일 이름을 받아옴
						var loader = new THREE.JSONLoader();
						modelPath = "resources/json/model/";
						texturePath = "resources/json/texture/";
						console.log(resp);
						console.log("받아온 스케일 : " + itemScale);
						loader.load(modelPath + resp.json_file + ".json", function(geomerty, mat) {
							mesh = new THREE.Mesh(geomerty, mat[0]);
							mesh.position.set(0, 0, 0);
							console.log("받아온 스케일 : " + itemScale);
							mesh.scale.set(itemScale,itemScale,itemScale);
							
							trickData = JSON.parse(sessionStorage.getItem("trickData"));
							trickData_pass = JSON.parse(sessionStorage.getItem("trickData_password"));
							console.log(trickData, trickData_pass);
							if((trickData || trickData_pass)&& itemType == "trick"){
								mesh.name = trickData.item_name;
								mesh["matching_item"] = trickData.matching_item;
								mesh["trick_type"] = trickData_pass.trick_type;
								mesh["content"] = trickData_pass.content;
							}else{
								mesh.name = resp.json_file;
							}
							mesh["item_no"] = resp.no;
							mesh["item_type"] = resp.type;
							mesh["canvas_availability"] = resp.canvas_availability;
							
							console.log(resp);
							//그룹에다 더해주기
							group.add(mesh);
							console.log(mesh);
							console.log("load함 : " + resp.json_file);
						}, texturePath);
						scene.add(group);
					}
				, error : function(resp){
					console.log(JSON.stringify(resp));
				}
				});
				group.name = "addObject";
				MapEdit02.movingGroup = group;
				scene.add(MapEdit02.movingGroup);
			}//callObj
		}

		,
	cancel: function (scene) {
			console.log("cnacle : 2차 : 선택 및 이동, 더하기 취소");
			if (this.selectedGroup) {
				console.log("오브젝트 선택취소");
				//            group = findGroup(scene.children, this.selectedGroup.children[0]);
				var group;
				for (var i in scene.children) {
					if (scene.children[i].name == this.selectedGroup.children[0].name) group = scene.children[i];
					else console.log("일치하는 그룹을 찾을 수 없습니다.");
				}
				if (group) {
					//선택을 해제한다
					tempMesh =this.findTempMesh(group.children);
					group.remove(tempMesh);
					
					if (this.isGroupSelected) {
						//그룹을 재설정 해줄 때
						console.log("그룹을 재설정해주기");
						for (var i in this.targetGroup.children) {
							if (group.name == this.targetGroup.children[i].name) {
								this.targetGroup.children[i] = group;
								break;
							}
						}
					} else if (this.isObjSelected) {
						//오브젝트를 재설정 할 때
						console.log("오브젝트를 재설정");
						scene.remove(group);
						this.parentGroup.add(group.children[0]);
						console.log(this.parentGroup);

					}
					console.log("선택 취소플래그");
					this.isGroupSelected = false;
					this.isObjSelected = false;
					this.selectedGroup = null;

				}

			} else if (this.movingGroup) {
				if (!(this.isGroupMove || this.isObjMove)) {
					console.log("오브젝트 추가 취소");
					console.log(this.movingGroup.children[0]);
					group = findGroup(scene.children, this.movingGroup);
					scene.remove(group);
					this.movingGroup = null;

				} else {
					console.log("오브젝트 이동 취소");
					group = findGroup(scene.children, this.movingGroup);
					scene.remove(group);

					console.log("--미리 저장해둔 이동 전 위치로 그룹 이동시키기 작업 추가해야함--");

					console.log("movingGroup 삭제 및 플래그 초기화");
					this.movingGroup = null;
					this.isGroupMove = false;
					this.isObjMove = false;

				}
			} else console.log("취소할 작업이 존재하지 않음");

			function findGroup(sceneGroupList, thisGroup) {
				for (var i in sceneGroupList) {
					console.log(sceneGroupList[i].name);
					console.log(thisGroup.name);

					if (sceneGroupList[i].name == thisGroup.name) return sceneGroupList[i];
					else console.log("일치하는 그룹을 찾을 수 없습니다.");
				}
			}
			console.log(scene);
			console.log(this.targetGroup);
			return scene;
		}

		//그룹 위에 올려진 Object 선택
		,
	selectObj: function (scene, intersects) {
			console.log("오브젝트 선택하기");
			if (this.selectedGroup) {
				console.log("선택된 그룹이 존재");
				if (this.isObjMove) {
					console.log("현재 이동중인 오브젝트 위치설정하기");
				} else {
					console.log("선택이동하기");
					if (this.isObjSelected) {
						console.log("오브젝트가 선택되어있을 때");
						scene.remove(this.selectedGroup);

						//선택되어있는 그룹에서 통합 매쉬 빼기
						tempMesh = this.findTempMesh(this.selectedGroup.children);
						this.selectedGroup.remove(tempMesh);

						if (tempMesh.parentGroupName) {
							console.log("부모그룹이 존재할경우 : " + tempMesh.parentGroupName);
							//부모 그룹 안에 매쉬 다시 넣어주기
							parent = scene.children.find(function (item) {
								return item.name == tempMesh.parentGroupName;
							});
							parent.add(this.selectedGroup.children[0]);
							console.log(parent);
						}
						//새로 선택한 오브젝트를 그룹으로 만들어주기
						console.log(parentGroup);
						mesh = this.addMesh(intersects.object, intersects.object.position);
						objGroup = new THREE.Group();
						objGroup.add(intersects.object);
						objGroup.add(mesh);
						this.selectedGroup = objGroup;
						//scene 에 더하기
						scene.add(this.selectedGroup);
					} else {
						console.log("그룹이 선택되어있을 때");
						scene.remove(this.selectedGroup);
						//선택용으로 만들어둔 임시 통합 매쉬를 제거한다
						tempMesh = this.findTempMesh(this.selectedGroup.children);
						this.selectedGroup.remove(tempMesh);
						
						//선택이 해제된 오브젝트 그룹을 돌려준다 : 플래그 내리기
						scene.add(this.selectedGroup);
						this.isGroupSelected = false;

						//새로 선택된 오브젝트 그룹으로 만들어주기
						console.log("새로 선택한 오브젝트 그룹으로 만들기");
						console.log(parentGroup);
						mesh = this.addMesh(intersects.object, intersects.object.position);
						objGroup = new THREE.Group();
						objGroup.add(intersects.object);
						objGroup.add(mesh);
						this.selectedGroup = objGroup;
						scene.add(this.selectedGroup);
						this.isObjSelected = true;
						console.log(scene);
					}
				}
			} else {
				console.log("선택된 그룹이 존재하지 않음 : 오브젝트 새로 선택하기");

				//부모그룹에서 해줄 작업 : 부모 그룹에서 오브젝트 분리후 다시 스캔에 더해주기
				//그룹에서 뺄 객체를 찾아온다
				find = this.parentGroup.children.find(function (item) {
					return item.name == intersects.object.name;
				});
				index = this.parentGroup.children.indexOf(find);
				this.parentGroup.children.splice(index, 1);
				console.log(this.parentGroup);
				
				
				mesh = this.addMesh(find);
				tempGroup = new THREE.Group();
				tempGroup.add(find);
				tempGroup.add(mesh);
				tempGroup.name = find.name;
//				console.log(tempGroup);
				scene.add(tempGroup);

				this.selectedGroup = tempGroup;
				this.isObjSelected = true;

				console.log(scene);
			}
			return scene;
		}

		//그룹 전체 선택하기
		,
	selectGroup: function (scene, intersects, parentGroup) {
			console.log(this.selectedGroup);
			if (this.selectedGroup) {
				console.log("선택된 그룹 존재");
				if (this.isGroupMove) {
					console.log("그룹 전체의 위치 지정하기");
				} else {
					//this.isGroupMove = false -> 선택 이동
					if (!(intersects.object.geometry instanceof THREE.PlaneGeometry)) {
						console.log("선택 이동하기");
						if (this.isGroupSelected) {
							scene.remove(this.selectedGroup);
							console.log("group이 선택되어있을 때");
							//그룹이 선택되어있는 경우 : 통합 매쉬 빼기
							tempMesh = this.findTempMesh(this.selectedGroup.children);
							this.selectedGroup.remove(tempMesh);

							//이전 그룹을 scene.add
							console.log(this.selectedGroup);
							scene.add(this.selectedGroup);

							//통합 매쉬 추가하기
							mesh = this.addMesh(parentGroup);
							parentGroup.children.push(mesh);
							console.log(parentGroup);

							//selectGroup에 통합 선택 매쉬가 들어간 parentGroup를 넣어주기
							this.selectedGroup = parentGroup;

							//scene.add(this.selectedGroup);
						} else if (this.isObjSelected) {
							console.log("object가 선택되어있을 때 : parentGroup에 지금 선택되어있는 오브젝트를 더해주고 선택취소 -> 그룹선택");
							scene.remove(this.selectedGroup);
							childMesh = this.selectedGroup.children[0];

							//통합된 매쉬
							tempMesh = this.findTempMesh(this.selectedGroup.children);
							if (tempMesh.parentGroupName) {
								//이 객체가 어딘가에 속해있는 자식객체인 경우, 부모 객체의 이름을 받아온다.
								parentName = tempMesh.parentGroupName;

								pGroup = scene.children.find(function (item) {
									return item.name == parentName;
								});
								pGroup.add(childMesh);
								console.log(pGroup);
							}

							//새로 선택해서 더해주기
							console.log(intersects);
							console.log(parentGroup);

							mesh = this.addMesh(parentGroup);
							parentGroup.add(mesh);

							this.selectedGroup = parentGroup;
						}
						this.isObjSelected = false;
						this.isGroupSelected = true;
					}
				}

			} else {
				if (!(intersects.object.geometry instanceof THREE.PlaneGeometry)) {
					scene.remove(parentGroup);
					console.log("선택된 그룹 없음 : 전체 그룹 새로 선택하기");
					mesh = this.addMesh(parentGroup, parentGroup.position);
					parentGroup.add(mesh);
					console.log(parentGroup);
					scene.add(parentGroup);
					this.isGroupSelected = true;
					this.selectedGroup = parentGroup;
				}
			}
			return scene;
		}

		//그룹 이동
		,
	moveGroup: function (scene) {
			//targetGroup에서 삭제작업 : 그룹일 경우와 오브젝트일 경우
			tempMesh = MapEdit02.selectedGroup.children.find(function (item) {
				return item.name == "temp";
			});
			if (tempMesh.parentGroupName) {
				console.log("오브젝트일 때");
				scene.remove(MapEdit02.selectedGroup);
				parentName = tempMesh.parentGroupName;
				//선택되어있는 오브젝트 객체를 움직일 그룹 안으로 넣어줌
				MapEdit02.movingGroup = MapEdit02.selectedGroup;
				
				//넣어줄 오브젝트의 포지션을 0,0,0 으로 해준다
				for(var i in this.movingGroup.children){
					this.movingGroup.children[i].position.set(0,0,0);
				}
				console.log(MapEdit02.movingGroup);
				//움직임 플래그 설정
				MapEdit02.isObjMove = true;

				//움직여야하니까 해당 객체를 타겟에서 제외시킨다
				find = this.targetList.find(function (item) {
					return item.name == MapEdit02.movingGroup.name;
				});
				index = this.targetList.indexOf(find);
				this.targetList.splice(index, 1);
				
				console.log(this.targetList);
				
				//scene 안에 더해줌
				scene.add(MapEdit02.movingGroup);
				MapEdit02.deleteContextmenu();
			} else {
				console.log("그룹일 때");
				scene.remove(MapEdit02.selectedGroup);
				MapEdit02.movingGroup = MapEdit02.selectedGroup;
				MapEdit02.isGroupMove = true;
				for (var i in this.movingGroup.children) {
				//움직여야하니까 해당 객체를 타겟에서 제외시킨다
					find = this.targetList.find(function (item) {
						return item.name == MapEdit02.movingGroup.children[i].name;
					});
					if (find) {
						var index = this.targetList.indexOf(find);
						console.log(index);
						this.targetList.splice(index, 1);
						console.log(this.targetList);
					}
				}
				objFirstPosition = this.movingGroup.children[0].position.clone();
				for(var i in this.movingGroup.children){
					this.movingGroup.children[i].position.x -= objFirstPosition.x;
					this.movingGroup.children[i].position.z -= objFirstPosition.z;
				}
				
				scene.add(MapEdit02.movingGroup);
				MapEdit02.deleteContextmenu();
			}
			return scene;
		}

		//선택그룹 삭제하기
		,
	delSelectedGroup: function (scene) {
		//삭제 명령을 내렸을 경우 선택되어있는 그룹을 지운다.
		console.log(this.targetList);
		scene.remove(MapEdit02.selectedGroup);
		index = MapEdit02.targetList.indexOf(this.selectedGroup.children[0]);
		console.log(index);	
		
		MapEdit02.targetList.splice(index,1);
		console.log(this.targetList);


			//targetGroup에서 삭제작업 : 그룹일 경우와 오브젝트일 경우
			tempMesh = MapEdit02.selectedGroup.children.find(function (item) {
				return item.name == "temp";
			});
			if (tempMesh.parentGroupName) {
				//부모 그룹의 정보가 존재할 경우 : 오브젝트
				console.log("오브젝트일 때");
				groupname = tempMesh.parentGroupName;

				//오브젝트의 부모 그룹을 찾음
				parent = MapEdit02.targetGroup.find(function (item) {
					return item.name == groupname;
				});
				parent.remove(MapEdit02.selectedGroup.children[0]);
				console.log(parent);
				
				
			} else {
				//부모 그룹의 정보가 존재하지 않을 경우 : 그룹
				console.log("그룹일 때");
				MapEdit02.targetGroup.splice(function () {
					find = MapEdit02.targetGroup.find(function (item) {
						return item.name == this.selectedGroup.name;
					});
					return MapEdit02.targetGroup.indexOf(find);
				}, 1);

				MapEdit02.targetList.splice(function () {
					MapEdit02.targetList.find(function (item) {
						return item.name == this.selectedGroup.name;
					});
					return MapEdit02.targetList.indexOf(find);
				}, 1);
			}
			MapEdit02.selectedGroup = null;
			MapEdit02.isObjSelected = false;
			MapEdit02.isObjMove = false;
			MapEdit02.isGroupSelected = false;
			MapEdit02.isGroupMove = false;
			MapEdit02.deleteContextmenu();
			console.log(scene);
			return scene;
		}

		//마우스 클릭
		,
	clickGroupFunction: function (e, scene) {
			console.log(scene);
			console.log(this.targetGroup);

			//혹시 메뉴가 아직 떠 있으면 메뉴 삭제해주기
			this.deleteContextmenu();

			//마우스 포인터의 위치에 따라 Object 인식하기
			var projector = new THREE.Projector();
			var vector = new THREE.Vector3(this.mouse.x, this.mouse.y, 1);
			projector.unprojectVector(vector, camera);
			var raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
			var intersects = raycaster.intersectObjects(MapEdit02.targetList);
			if (intersects.length > 0) {
				console.log(intersects);
				console.log(raycaster);
				if (this.movingGroup) {
					console.log("움직이는 그룹이 있을 때");
					if (this.isGroupMove || this.isObjMove) {
						console.log("오브젝트 이동하기");
						console.log(intersects[0].object.geometry);
						if (intersects[0].object.geometry instanceof THREE.PlaneGeometry) {
							//intersect[0].object.geom이 plane인 경우
							console.log("PlaneGeom일 때");
							position = this.movingGroup.position;
							//현재 움직이던 그룹 장면에서 빼주기
							scene.remove(this.movingGroup);
							//현재 움직이고 있는 this.movingGroup이 오브젝트를 가져온건지 그룹을 가져온건지 구분하기
							tempMesh = this.findTempMesh(this.movingGroup.children);
							//찾아온 선택 매쉬를 현재 움직이는 그룹에서 빼준다
							this.movingGroup.remove(tempMesh);
							console.log(tempMesh);
							if (tempMesh.parentGroupName) {
								//선택한 그룹의 선택 매쉬안에 부모 그룹의 이름이 존재하면 오브잭트를 가져온것
								console.log("object일 때");
								
								//movingGroup안에 있는 오브젝트 복제 후  포지션 설정
								obj = this.movingGroup.children[0].clone();
								obj["item_no"] = this.movingGroup.children[0].item_no;
								obj["item_type"] = this.movingGroup.children[0].item_type;
								obj["canvas_availability"] = this.movingGroup.children[0].canvas_availability;
								//trick일 때 - 17.9.27 수정
								if(this.movingGroup.children[0].matching_item){
									obj["matching_item"] = this.movingGroup.children[0].matching_item;
								}
								if(this.movingGroup.children[0].trick_type){
									mesh["trick_type"] = this.movingGroup.children[0].trick_type;
								}
								if(this.movingGroup.children[0].content){
									mesh["content"] = this.movingGroup.children[0].content;
								}
								
								//trick일 때 - 17.9.27 수정
								if(this.movingGroup.children[0].matching_item){
									obj["matching_item"] = this.movingGroup.children[0].matching_item;
								}
								
								obj.position.set(position.x, position.y, position.z);
								//plane의 부모 그룹 찾기 : scene
								parent = scene.children.find(function (item) {
									return item.name == intersects[0].object.name;
								});
								console.log(parent);
								//설정한 Obj를 각각 장면과 타겟 리스트 안에 넣어주기
								parent.add(obj);
								
								//select, move 플래그 재설정
								this.selectedGroup = null;
								this.movingGroup = null;
								this.isObjMove = false;
								this.isObjSelected = false;
								this.targetList.push(obj);

							} else {
								//없을 경우 그룹을 가져온 것
								console.log("Group일 때");	
								//targetGroup 업데이트
								for(var i in this.targetGroup){
									if(this.targetGroup[i].name == this.movingGroup.name) this.targetGroup[i] = this.movingGroup;
								}
								scene.add(this.movingGroup);
								
								//그룹에 세팅된 포지션을 받아오고
								position = this.movingGroup.position;
								//각자 오브젝트의 위치에 더해준다
								for(var i in this.movingGroup.children){
									p = this.movingGroup.children[i].position;
									p.x += position.x;
//									p.y += position.y;
									p.z += position.z;
									console.log(this.movingGroup.children[i].name);
									console.log(p);
								}
								//마지막으로 그룹의 포지션을 0으로 해줌
								this.movingGroup.position.set(0,0,0);
								
								//타겟 포인트에 집어넣기
								for (var i in MapEdit02.movingGroup.children) {
									MapEdit02.targetList.push(MapEdit02.movingGroup.children[i]);
									console.log(MapEdit02.targetList);
								}

								this.selectedGroup = null;
								this.movingGroup = null;
								this.isGroupSelected = false;
								this.isGroupMove = false;
								console.log(scene);
								console.log(this.targetGroup);
							}
						} else {
							//intersect[0].object.geom이 Object3d나 Mesh 인 경우
							//부모그룹 찾기
							parent = scene.children.find(function (item) {
								return item.name == intersects[0].object.name;
							});
							console.log(parent);

							console.log(parent.children[0].name);
							console.log(intersects[0].object.name);

							if (parent && intersects[0].object.name == parent.children[0].name) {
								//포인터에 가장 가까운 위치의 오브젝트가 그룹 안의 첫번째 인덱스에 위치할 때
								//현재 움직이고 있는 this.movingGroup이 오브젝트를 가져온건지 그룹을 가져온건지 구분하기
								tempMesh = this.movingGroup.children.find(function (item) {
									return item.name == "temp";
								});

								if (tempMesh.parentGroupName) {
									//선택한 그룹의 선택 매쉬안에 부모 그룹의 이름이 존재하면 오브잭트를 가져온것
									console.log("object일 때");
									
									scene.remove(this.movingGroup);
									
									//movingGroup안에 있는 오브젝트 복제 후  포지션 설정
									position = this.movingGroup.position;
									obj = this.movingGroup.children[0].clone();
									obj.position.set(position.x, position.y, position.z);
									
									//설정한 Obj넣어주기
									parent.add(obj);
									this.selectedGroup = null;
									this.movingGroup = null;
									this.isObjMove = false;
									this.isObjSelected = false;
									this.targetList.push(obj);
									
								}else console.log("가구와 벽은 같은 가구와 벽 오브젝트 위에 올릴 수 없습니다.");
							}
						}

					} else {
						console.log("오브젝트를 올리려고 하는 곳이 그룹의 첫번째 인덱스에 들어있는 객체일 때 : in this.targetList");
						console.log("오브젝트 더하기");
						parentGroup = this.findtargetGroup(intersects[0].object);
						if (parentGroup) {
							obj = this.movingGroup.children[0].clone();
							obj.name += "_" + this.objnum;
							obj["item_no"] = this.movingGroup.children[0].item_no;
							obj["item_type"] = this.movingGroup.children[0].item_type;
							obj["canvas_availability"] = this.movingGroup.children[0].canvas_availability;
							//trick일 때 - 17.9.27 수정
							if(this.movingGroup.children[0].matching_item){
								obj["matching_item"] = this.movingGroup.children[0].matching_item;
							}
							if(this.movingGroup.children[0].trick_type){
								obj["trick_type"] = this.movingGroup.children[0].trick_type;
							}
							if(this.movingGroup.children[0].content){
								obj["content"] = this.movingGroup.children[0].content;
							}
							
							position = this.movingGroup.position;
							
							console.log(position);
							obj.position = position;
							parentGroup.add(obj);
							obj.position.set(position.x, position.y, position.z);
							console.log(obj);

							this.objnum++;
							this.targetList.push(obj);
							console.log(this.targetList);

							console.log("scene");
							console.log(scene);

						} else {
							console.log("오브젝트를 놓을 공간이 부족합니다.");
						}
					}
				} else {
					console.log("움직이는 그룹이 없을 때");
					for (var i in this.targetGroup) {
						console.log(this.targetGroup);
						console.log(intersects[0].object.name);
						console.log(this.targetGroup[i].children[0].name);
						console.log(this.targetGroup[i].children[0].name == intersects[0].object.name);

						if (this.targetGroup[i].children[0].name == intersects[0].object.name) {
							if (!(intersects[0].object.geometry instanceof THREE.PlaneGeometry)) {
								console.log(intersects[0]);
								console.log("그룹의 첫번째 오브젝트를 클릭했을 경우");
								//부모그룹을 저장
								this.parentGroup = this.targetGroup[i];
								console.log(this.targetGroup[i]);
								//그룹 선택하기 : 통합 매쉬가 더해진 그룹을 반환
								scene = this.selectGroup(scene, intersects[0], this.parentGroup);
								return scene;
							}
						} else {
							console.log(this.targetGroup);

							findMesh = this.targetGroup[i].children.find(function (item) {
								return intersects[0].object.name == item.name;
							});
							console.log(intersects[0].object);
							console.log(findMesh);
							//						console.log(findMesh.parent);
							console.log(this.targetGroup);
							if (!(intersects[0].object.geometry instanceof THREE.PlaneGeometry)) {
								if (findMesh) {
									console.log("그룹의 나머지 오브젝트를 선택했을 경우");
									//							//부모그룹을 저장
									this.parentGroup = findMesh.parent;
									console.log(this.parentGroup);
									//							//오브젝트 선택하기
									this.selectObj(scene, intersects[0]);
									return scene;
								}
							} else {
								console.log("선택한 곳이 PlaneMesh일 경우");
								return this.cancel(scene);
							}
						}
					} //for
				}
			}
			return scene;
		}

		//마우스 이동 : 그룹 이동
		,
	moveMousePointer: function (e, scene) {
			//마우스 위치 받아오기
			MapEdit02.mouse.x = (e.clientX / MapEdit02.width) * 2 - 1;
			MapEdit02.mouse.y = -(e.clientY / MapEdit02.height) * 2 + 1;
			//scene에 들어있는 카메라 찾기
			camera = scene.children.find(function (item) {
				return item instanceof THREE.Camera ? item : null;
			});

			//마우스 포인터의 위치에 따라 Object 인식하기
			var projector = new THREE.Projector();
			var vector = new THREE.Vector3(this.mouse.x, this.mouse.y, 1);
			projector.unprojectVector(vector, camera);
			var raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
			var intersects = raycaster.intersectObjects(this.targetList);

			//움직일 단위가 될 Plane의 Segment찾기
			//움직일 수 있는 범위 설정의 바탕이 되는 PlaneGeom의 width, height 찾기
			plane = scene.children.find(function (item) {
				return item.name == "plane";
			}).children[0];
			if (plane) {
				planeG = plane.geometry;
				segmentW = planeG.parameters.widthSegments / 16;
//				console.log(segmentW);

				//범위 설정
				planeW = planeG.parameters.width;
				planeH = planeG.parameters.height;
			}
			if (intersects.length > 0) {
				points = intersects[0].point;
				if (MapEdit02.movingGroup) {
					x = Math.round((points.x / segmentW) - 1) * segmentW + segmentW;

					if (intersects[0].object.geometry instanceof THREE.PlaneGeometry) y = 0;
					else if (this.findtargetGroup(intersects[0].object)) {
						console.log(this.findtargetGroup(intersects[0].object));
						if(intersects[0].object.geometry.parameters){
							//intersects의 가장 가까운 오브젝트의 지오메트리의 높이가 존재하면
							y = intersects[0].object.geometry.parameters.height;
							console.log("object의 파라미터 중 height 가 있으면 : " + y);
						}else{
							//height가 없을 경우
							console.log("Object의 파라미터 중 height가 없을 경우");
							faceHeight = intersects[0].distance;
							index = intersects.length-1;
							raycasterH = intersects[index].distance;
							console.log(raycasterH + "-" + faceHeight);
							y = raycasterH - faceHeight;
						}
						
					} else y = Math.round((points.y / segmentW) - 1) * segmentW + segmentW;

					z = Math.round((points.z / segmentW) - 1) * segmentW + segmentW;

					if (x < -(planeW * 0.45)) x = -(planeW * 0.45);
					if (x > planeW * 0.45) x = planeW * 0.45;
					if (z < -(planeH * 0.45)) z = -(planeW * 0.45);
					if (z > planeH * 0.45) z = planeW * 0.45;

//					console.log(x + " : " + y + " : " + z);
					MapEdit02.movingGroup.position.set(x, y, z);
				}
			}
			return scene;
		}

		//마우스 오른쪽 클릭 이벤트 핸들러
		,
	mouseRightClick: function (e, scene) {
			console.log("마우스 오른쪽 클릭");

			//contextmenu가 있으면 삭제하고 없으면 다음으로
			this.deleteContextmenu();

			console.log("마우스 오른쪽 클릭");
			var projector = new THREE.Projector();
			var vector = new THREE.Vector3(this.mouse.x, this.mouse.y, 1);
			projector.unprojectVector(vector, camera);
			var raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
			var intersects = raycaster.intersectObjects(this.targetList);
			console.log(intersects);

			//선택되었을때만 메뉴가 나타나도록
			if (intersects.length > 0 && (this.isGroupSelected || this.isObjSelected)) {
				console.log(intersects[0].object.name + "==" + this.selectedGroup.children[0].name);
				if (intersects[0].object.name == this.selectedGroup.children[0].name) {
					console.log("intersect의 길이가 0 이상이고 isGroupSelected = " + this.isGroupSelected +
						" isObjSelected = " + this.isObjSelected + "일 때");

					//팝업메뉴용 div
					var div = "<div class = 'contextM'>";
					div += "<label data-order = 'move'>이동</label>";
					div += "<br>";
					div += "<label data-order = 'delete'>삭제</label>";
					div += "<br>";
					div += "<label data-order = 'rotationL'>왼쪽으로 45도 회전</label>";
					div += "<br>";
					div += "<label data-order = 'rotationR'>오른쪽으로 45도 회전</label>";
					div += "<br>";
					div += "<label data-order = 'setTool'>도구로 설정</label>";
					console.log(this.selectedGroup.children[0].canvas_availability);
					console.log(this.selectedGroup.children[0].item_type);
					if(this.selectedGroup.children[0].canvas_availability == "available"){
						div += "<br>";
						div += "<label data-order = 'addHint'>힌트 추가</label>";
					}
//					if(this.selectedGroup.children[0].item_type == "trick"){
//						div += "<br>";
//						div += "<label data-order = 'setAnswer'>정답 설정</label>";
//					}
					div += "</div>";

					$("#canvas").append(div);

					//div 스타일 설정, 이벤트 걸기
					$(".contextM").css({
							"position": "absolute",
							"top": e.clientY,
							"left": e.clientX,
							"color": "white",
							"background": "rgba(0,0,0,0.5)",
							"padding": "5px",
							"z-index": "10",
							"text-align": "center"
						})
						.on("click", function (e) {
							e.stopPropagation();
						});
					$(".contextM label")
						.css({
							"width": "100px"
						})
						.on("mousedown", function (e) {
							e.stopPropagation();
							var order = $(this).data("order");
							if (order == "delete") {
								console.log("삭제버튼 누름");
								scene = MapEdit02.delSelectedGroup(scene);

							} else if (order == "move") {
								console.log("이동버튼 누름");
								//마우스 포인터 위치가 어긋나는 오류를 잡아야함
								scene = MapEdit02.moveGroup(scene);

							} else if (order == "rotationL" || order == "rotationR") {
								console.log("selectGroup 회전");

								//각도 정하기
								var rotation;
								if (order == "rotationL") rotation = Math.PI / 4;
								else rotation = -(Math.PI / 4);
								console.log("회전 각도 : " + rotation);

								//targetGroup에서 삭제작업 : 그룹일 경우와 오브젝트일 경우
								tempMesh = MapEdit02.selectedGroup.children.find(function (item) {
									return item.name == "temp";
								});
								if (tempMesh.parentGroupName) {
									console.log("오브젝트일 때");
									for (var i in MapEdit02.selectedGroup.children) {
										MapEdit02.selectedGroup.children[i].rotation.y += rotation;
									}
								} else {
									console.log("그룹일 때");
									//회전 중심축이 되는 그룹의 첫번째 오브젝트를 회전시킴과 동시에 좌표를 구해준다.
									rotcenter = MapEdit02.selectedGroup.children[0].position;
//									MapEdit02.selectedGroup.children[0].rotation.y += rotation;
									
									//그룹의 나머지 오브젝트를 중심축에 맞춰 회전시킨 후 더한다.
									for(var i in MapEdit02.selectedGroup.children){
										MapEdit02.selectedGroup.children[i].rotation.y += rotation;
										p = MapEdit02.selectedGroup.children[i].position;
										position = rotationGroup(rotcenter.x, rotcenter.z, rotation, p);
										MapEdit02.selectedGroup.children[i].position.set(position.x, position.y, position.z);
										
									}
									function rotationGroup(px, pz, rotation, p){
										s = Math.sin(rotation);
										c = Math.cos(rotation);
										//현재 오브젝트의 좌표에서 회전 중심축을 빼기 : 회전시 그리는 원의 반지름
										p.x -= px;
										p.z -= pz;
										//회전시 움직이는 위치 계산
										xnew = p.z * s + p.x * c;
										znew = p.z * c - p.x * s;
										p.x = xnew + px;
										p.z = znew + pz;
										return p;
									}
								}
								MapEdit02.deleteContextmenu();
								
							} else if(order == "addHint"){
								//힌트 더하기
								var canvas = document.createElement("canvas");
								$("#hintcanvas").append(canvas);

								//쪽지 텍스쳐 설정  : 힌트를 가구에도 설정하려면 가구의 텍스쳐파일도 받아와야하는걸로 수정해야한다
								var img = new Image();
								img.src = "resources/images/hintTexture/pape_texture1.jpg";
								var imagesize = {width : img.width, height : img.height};
								
								//literalCanvas
								var lc = LC.init(document.getElementById("hintcanvas")
										, {imageURLPrefix: 'resources/images/literallyCanvas'
											,imageSize : imagesize});
								
								lc.saveShape(LC.createShape('Image', {x : 0, y : 0, image : img}));
								var applyTexture = '<input type = "button" value = "힌트 저장" id = "saveHint">';
								applyTexture += '<input type = "button" value = "오브젝트에 힌트 붙이기" id = "addHint">';
								applyTexture += '<input type = "button" value = "취소" id = "hintCancel">';
								
								$("#hintcanvas").append(applyTexture);
								$("#hintCancel").on("click", function(){
									$("#hintcanvas").empty();
								});
								
								$("#saveHint").on("click", function(){
									map_no = $("#map_no").text();
									item_name = MapEdit02.selectedGroup.children[0].name;
									console.log("맵 번호 : " + map_no + " / 아이템 이름 : " + item_name);
									image = lc.getImage().toDataURL("image/png", 1.0);
									$.ajax({
										url : "saveHint"
										, type : "POST"
										, data : {"image" : image , "map_no" : parseInt(map_no) , "item_name" : item_name}
										, contentType: "application/x-www-form-urlencoded; charset=utf-8"
										, dataType : "text"
										, success : function(resp){
											if(resp){
												alert("Detail Hint 저장에 성공하였습니다.");
											}else{
												alert("Detail Hint 저장에 실패하였습니다.");
											}
										}
										, error : function(resp){
											console.log(resp);
										}
									});
								});
							MapEdit02.deleteContextmenu();
						} else if(order == "setTool"){
							//사용자 도구 설정하기
							map_no = $("#map_no").text();
							item_name = MapEdit02.selectedGroup.children[0].name;
							item_no = MapEdit02.selectedGroup.children[0].item_no;
							data = {
								"map_no" : map_no
								, "item_name" : item_name
								, "item_no" : item_no
								, "retractable" : "true"
							}
							console.log(MapEdit02.selectedGroup.children[0]);
							console.log("툴 설정할 때 보낼 데이터", data);
							$.ajax({
								url : "setTools"
								, data : data
								, dataType : "text"
								, type : "get"
								, success : function(resp){
									alert(resp);
								}
								, error : function(resp){
									console.log(resp);
								}
							});
							
							MapEdit02.deleteContextmenu();
						} 
					});
				}
			}
			return scene;
		}

		,
	findTempMesh: function (arr) {
			temp = arr.find(function (item) {
				return item.name == "temp";
			});
			return temp;
		}

		//contextmenu 삭제
		,
	deleteContextmenu: function () {
			var contextmenu = $(".contextM");
			if (contextmenu.length != 0) contextmenu.remove();
		}

		,
	addMesh: function (obj) {
		//선택에 사용할 임시 매쉬 만들기 : intersect에 잡힌 객체의 지오메트리 + 메테리얼 추가
		var scale;	
		var geom = new THREE.Geometry();
			if (obj instanceof THREE.Mesh) {
				console.log("Object일 때");
				geom = obj.geometry;
				scale = obj.scale;
				console.log(scale);
			} else {
				console.log("Group일 때 : 통합 지오메트리 생성");
				meshes = obj.children;
				for (var i in meshes) {
					console.log(meshes[i].geometry);
					console.log(meshes[i].matrix);
					geom.merge(meshes[i].geometry, meshes[i].matrix);
				}
			}
			
			console.log(geom);
			
			mat1 = new THREE.MeshLambertMaterial({
				color: 0x42f5ff,
				opacity: 0.3,
				transparent: true
			});
			mat2 = new THREE.MeshBasicMaterial({
				color: 0x42f5ff,
				wireframe: true
			});

			var mesh = new THREE.SceneUtils.createMultiMaterialObject(geom, [mat1, mat2]);
			if(scale)scale *= 1.006;
			else scale = 1.006;
			mesh.scale.set(scale, scale, scale);
			//이 Mesh의 Position은 상대적
			mesh.rotation.y = obj.rotation.y;
			

			console.log(obj.position);
			if(obj instanceof THREE.Mesh) mesh.position.set(obj.position.x, obj.position.y, obj.position.z);
		
			mesh.name = "temp"
			//부모 객제가 있을 경우 부모 객체의 이름 정보를 넣어줌
			if (obj.parent) mesh.parentGroupName = obj.parent.name;
			console.log(mesh.position);
			return mesh;
		}

		//타겟 그룹 리스트에서 타겟리스트와 같은 이름의 그룹 찾기
		,
	findtargetGroup: function (object) {
		return this.targetGroup.find(function (item) {
			return item.name == object.name;
		});
	}
		
	//변화된 canvas 를 Texture로 만들어 뭍인다
	, addHintItem : function(scene, canvas){
		
	}
}

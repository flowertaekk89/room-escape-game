package sesoc.global.escape.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import sesoc.global.escape.repository.ItemRepository;
import sesoc.global.escape.repository.ItemsRepository;
import sesoc.global.escape.repository.RoomRepository;
import sesoc.global.escape.vo.Inventory;
import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.Room;
import sesoc.global.escape.vo.Set_Items;
import sesoc.global.escape.vo.WaitingUsers;
import sesoc.global.escape.vo.WebsocketVO;
import sesoc.global.escape.webSocket.WebSocketHandler;

@Controller
public class ItemController {
	
	final static private Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	ItemRepository item_repo;

	@Autowired
	ItemsRepository irepo;
	
	@Autowired
	RoomRepository room_repo;
	
	/**
	 * 인테리어, 도구 등의 오브젝트 정보 불러오기
	 * @param theme
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/call_items_sec", method = RequestMethod.GET)
	public Map<String, ArrayList<Items>> callItemsSec(String theme){
		System.out.println("call_items_sec - " + theme);
		Map<String, ArrayList<Items>> items = irepo.getSecondItems(theme);
		System.out.println("call_items_sec - \n" + items);
		return items;
	}//callItemSec
	
	/**
	 * 에디터에 뿌릴 아이템의 썸네일 이미지 다운로드
	 * @param icon 아이템의 썸네일 이미지 이름
	 * @param response 이미지를 보내줄 outputStream에 이용함
	 */
	@RequestMapping(value = "/down_Icon_img", method = RequestMethod.GET)
	public void downIconImg(String icon, ServletResponse response){
		System.out.println("다운로드");
		String downloadPath = "/assets/icon/";
		System.out.println(downloadPath + icon);
		if(icon != null){
			try {
				FileInputStream fis = new FileInputStream(downloadPath + icon);
				ServletOutputStream sos = response.getOutputStream();
				FileCopyUtils.copy(fis, sos);
				fis.close();
				sos.close();
			} catch (IOException e) {
				System.out.println("일치하는 아이콘 이미지 없음!");
			}
		}
	}//downIconImg
	
	
	/**
	 * 사용자가 플레이 화면 내에서 아이템을 들고 매칭정보가 존재하는 아이템을 선택했을 경우 동작
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/searchMatchingItem", method = RequestMethod.GET)
	public boolean searchMatchingItem(Items item){
		boolean result = false;
		int itemNo = item.getNo();
		Items searchitem = new Items();
		searchitem.setNo(itemNo);
		searchitem = irepo.selectOneItem(searchitem);
		//나중에 은지씨랑 얘기해보고 수정하기
		if(searchitem.getMatching_item() != 0){
			Items findItem = irepo.selectMatchingItem(item);
			if(findItem != null) result = true;
		}
		return result;
	}//searchMatchingItem
	
	/**
	 * Editor : 사용자 지정 오브젝트의 json 파일의 이름을 불러온다
	 * @param item 사용자가 클릭한 아이템의 정보
	 * @return json 파일의 정보를 담는 객체
	 */
	@ResponseBody
	@RequestMapping(value = "/getJSON", method = RequestMethod.GET)
	public Items getJSON(Items item){
		Items finditem = irepo.selectOneItem(item);
		return finditem;
	}//getJSON
	
	/**
	 * 타입별 아이템의 번호와 json파일 이름을 받아오는 메소드
	 * 17.9.27 추가
	 * @param type 타입
	 * @return 일치하는 타입을 가진 아이템 목록
	 */
	@ResponseBody
	@RequestMapping(value = "/getTypeJSON", method = RequestMethod.GET)
	public ArrayList<Items> getTypeJSON(Items item){
		System.out.println(item);
		ArrayList<Items> jsons = irepo.getTypeJSON(item);
		logger.info("받아온 아이템 =>" + jsons);
		System.out.println(jsons);
		return jsons;
	}//getTypeJSON
	
	
	/**
	 * Set_Items에서 일치하는 아이템의 정보를 받아와 뿌려줌 17.9.25
	 * @param item
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSavedItems", method = RequestMethod.POST)
	public ArrayList<Set_Items> getSavedItems(Set_Items item){
		//if : 저장된 Map이 있을 경우 정보 가져와서 뿌려주기
		System.out.println(item);
		ArrayList<Set_Items> savedItems = irepo.selecteSetItem(item);
		for(Set_Items i : savedItems){
			System.out.println(i);
		}
		if(savedItems.size() != 0){
			return savedItems;
		}else System.out.println("저장된 정보 없음");
		return null;
	}
	
	
	/**
	 * Trick Editor에서 매칭되는도구 아이템을 설정할 경우 바로 동작
	 * 17.9.29 수정
	 * @param item 도구 정보를 업데이트 할 아이템
	 * @return
	 */
	@RequestMapping(value = "/setTools", method = RequestMethod.GET)
	public String setMatching(Set_Items item){
		//맵, 아이템, 매칭 정보를 가져와서 매칭 정보만 업데이트
		String message;
		System.out.println(item);
		int i = irepo.updateSetItems(item);
		if(i == 1) message = "Tool 설정에 성공하였습니다.";
		else message = "Tool 설정 실패";
		System.out.println(message);
		return message;
	}
	
	
	/**
	 * Scene 내부에 있는 기본 매쉬를 제외한 사용자 지정 오브젝트의 정보 저장
	 * 17.9.27 수정
	 * @param item Scene 내부에 위치한 사용자 지정 오브젝트
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveScene", method = RequestMethod.POST)
	public String saveScene(Set_Items item){
		System.out.println("저장 or 업데이트" + item);
		ArrayList<Set_Items> findItem = irepo.selecteSetItem(item);
		System.out.println("찾은 아이템 : " + findItem);
		//새 정보 업데이트
		if(findItem.size() == 0){
			if(irepo.insertSetItems(item)==1)System.out.println("저장완료" + item.getItem_name());
			else System.out.println("저장 실패...ㅇㅅㅇ`" + item.getItem_name());
		}else{
			if(irepo.updateSetItems(item)==1)System.out.println("업데이트 완료" + item.getItem_name());
			else System.out.println("업데이트 실패...ㅇㅅㅇ`" + item.getItem_name());
		}
		return null;
	}
	
	/**
	 * 2017.9.25 추가 - LSH
	 * 캔버스에 그린 힌트를 로컬파일로 저장
	 * @param image
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveHint", method = RequestMethod.POST)
	public String saveHint(String image, int map_no, String item_name, HttpServletRequest req){
		logger.info("저장할 이미지 ->" + image);
		logger.info("캔버스의 번호를 저장할 setItems 테이블 내의 아이템 정보를 찾기위한 검색자 ->" + map_no + item_name);
		
		//저장할 경로 설정하기
		String folder = "/hint/";
		String[] imgsplit = image.split(",");
		//변환된 이미지 url에서 data:image/png;base64 까지 앞부분을 자름
		String imagepart = imgsplit[1];
		SimpleDateFormat sdf= new SimpleDateFormat("yyMMdd_hhmmss");
		String fileName = "paperHint" + sdf.format(new Date()).toString() + ".png";
		String fullPath = null;
		BufferedImage bufimg = null;
		byte[] byteimg;
		Decoder decoder = java.util.Base64.getDecoder();
		byteimg = decoder.decode(imagepart);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(byteimg);
		try {
			bufimg = ImageIO.read(bis);
			bis.close();
			fullPath = folder + fileName;
			//폴더 생성
			File folderobj = new File(folder);
			if(!(folderobj.isDirectory())) folderobj.mkdir();
			
			//파일 생성
			File file = new File(fullPath);
			if(file.exists()) file.delete();
			ImageIO.write(bufimg, "png", file);
			
			//파일 생성 후 canvas table 안에 저장
			int insr = irepo.insertHint(fullPath);
			if(insr == 1){
				Set_Items setitem = new Set_Items();
				setitem.setFullPath(fullPath);
				int canvas_no = irepo.selectHint(setitem).getCanvas_no();
				setitem.setItem_name(item_name);
				setitem.setMap_no(map_no);
				setitem.setCanvas_no(canvas_no);
				
				logger.info("집어넣을 정보를 가지고있는 객체 ==>"+setitem);
				
				if(irepo.updateSetItems(setitem) == 1) System.out.println("업데이트 성공");
				else System.out.println("업데이트 실패");
			}
		} catch (IOException e) {
			System.out.println("파일 저장에 실패하였습니다.");
		}
		return fullPath;
	}
	
	
	   /**
	    * 아이템 획득 시 관련 정보를 DB에 저장
	    * @param inven
	    * @throws IOException 
	    */
	   @ResponseBody
	   @RequestMapping(value = "whenGetItem", method = RequestMethod.POST)
	   public void whenGetItem(Inventory inven) throws IOException{
		   
	      String item_name = inven.getItem_name();
	      WaitingUsers UIR_info = room_repo.getURIInfo(inven.getUser_id());
	      int room_no = UIR_info.getRoom_no();
	      Items item = item_repo.itemInfo(item_name);
	      
	      
	      if(UIR_info != null && item != null){
	         int result = item_repo.isExisted(new Inventory(item_name, room_no));
	         if(result == 0){
	            inven.setItem_no(item.getNo());
	            inven.setUIR_no(UIR_info.getNo());
	            System.out.println("whenGetItem : insertItem 쿼리 실행");
	            item_repo.insertItem(inven);
	            
	            List<WebsocketVO> list = WebSocketHandler.sessionList;
	            for (WebsocketVO soc_vo : list) {
	               if(room_no == soc_vo.getRoomNum()){
	            	   soc_vo.getSession().sendMessage(new TextMessage("|whenGetItem|"+item_name));
	            	   soc_vo.getSession().sendMessage(new TextMessage(item_name + " 획득!"));
	               }//if
	            }//for
	            
	         }//inner if
	      }//if
	   }//whenGetItem
	
	@ResponseBody
	@RequestMapping(value = "inventory", method = RequestMethod.POST)
	public List<Items> inventory(Items item) {
		List<Items> inventory_setItems = item_repo.selectInventorySetItems(item);
		List<Items> inventory_canvas = item_repo.selectInventoryCanvas(item);
		List<Items> invenItems = new ArrayList<>();
		for (Items items : inventory_setItems) {
			Items temp;
			temp = item_repo.selectInvenItems(items);
			temp.setSI_no(items.getSI_no());
			temp.setNo(items.getNo());
			temp.setInven_no(items.getInven_no());
			temp.setUser_id(items.getUser_id());
			invenItems.add(temp);
		}
		for (Items items : inventory_canvas) {
			Items temp;
			temp = item_repo.selectInvenCanvas(items);
			System.out.println(temp.toString());
			temp.setCanvas_no(items.getCanvas_no());
			temp.setInven_no(items.getInven_no());
			temp.setUser_id(items.getUser_id());
			invenItems.add(temp);
		}
		System.out.println(invenItems.toString());
		return invenItems;
	}
	
	//17.9.29추가
	@ResponseBody
	@RequestMapping(value = "/insertTrick", method = RequestMethod.POST)
	public int insertTrick(Items item){
		System.out.println(item.getSI_no());
		System.out.println(item.getContent());
		System.out.println(item.getMap_no());
		System.out.println(item.getSolved());
		int result =  irepo.insertTrick(item);
		if(result == 1) System.out.println("저장됨");
		else System.out.println("저장실패");
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "trickCheck", method = RequestMethod.POST)
	public boolean trickCheck(Items item){
		if(item_repo.selectTrickCheck(item) == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "selectClickItem", method = RequestMethod.POST)
	public Items trickType(Items item){
		item = item_repo.selectClickItem(item);
		System.out.println(item.toString());
		return item;
	}
	
	@ResponseBody
	@RequestMapping(value = "matchingItem", method = RequestMethod.POST)
	public Items matchingItem(Items item){
		item = item_repo.matchingItem(item);
		return item;
	}
	
	@ResponseBody
	@RequestMapping(value = "matchingCanvas", method = RequestMethod.POST)
	public Items matchingCanvas(Items item){
		item = item_repo.matchingCanvas(item);
		return item;
	}
	
	@ResponseBody
	@RequestMapping(value = "selectItemCanvas", method = RequestMethod.POST)
	public Items selectItemCanvas(Items item){
		item = item_repo.selectItemCanvas(item);
		return item;
	}
	
	@ResponseBody
	@RequestMapping(value = "selectMatchingItem", method = RequestMethod.POST)
	public Items selectMatchingItem(Items item){
		item = item_repo.selectMatchingItem(item);
		return item;
	}
	
	//근택 추가 ### 0927 whenSolvedPuzzle
	@ResponseBody
	@RequestMapping(value = "whenSolvedPuzzle", method = RequestMethod.GET)
	public void whenSolvedPuzzle(Items item) throws IOException{
		String item_name = item_repo.selectSiCanvas(item).getItem_name();
		WaitingUsers UIR_info = room_repo.getURIInfo(item.getUser_id());
		int room_no = UIR_info.getRoom_no();
		//Items item = item_repo.itemInfo(item_name);
		
		item.setSolved("true");
		item_repo.updateSolved(item);
		
		item.setUIR_no(UIR_info.getNo());
		
		if(item.getCanvas_no() == 0) {
			item.setSI_no(item_repo.selectObtain(item).getObtainable_si());
			System.out.println(item.toString());
			item_repo.insertSetItemInven(item);
		}
		else {
			item_repo.insertCanvasInven(item);
		}
		
		List<WebsocketVO> list = WebSocketHandler.sessionList;
        for (WebsocketVO soc_vo : list) {
           if(room_no == soc_vo.getRoomNum()){
              soc_vo.getSession().sendMessage(new TextMessage(item_name + " 트릭을 풀었습니다!"));
              soc_vo.getSession().sendMessage(new TextMessage("인벤토리에 새로운 아이템이 추가되었습니다."));
           }//if
        }//for
	}//whenSolvedPuzzle
	
	
	
	@ResponseBody
	@RequestMapping(value = "whenFailedPuzzle", method = RequestMethod.GET)
	public void whenFailedPuzzle(Items item) throws IOException{
		
		WaitingUsers UIR_info = room_repo.getURIInfo(item.getUser_id());
		int room_no = UIR_info.getRoom_no();
		
		List<WebsocketVO> list = WebSocketHandler.sessionList;
        for (WebsocketVO soc_vo : list) {
           if(room_no == soc_vo.getRoomNum()){
              soc_vo.getSession().sendMessage(new TextMessage("트릭풀기에 실패했습니다."));
           }//if
        }//for
	}//whenFailedPuzzle
	
	@ResponseBody
	@RequestMapping(value = "solvedCheck", method = RequestMethod.POST)
	public Items solvedCheck(Items item){
		item = item_repo.solvedCheck(item);
		return item;
	}
	
	
}

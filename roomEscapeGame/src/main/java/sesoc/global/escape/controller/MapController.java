package sesoc.global.escape.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sesoc.global.escape.repository.ItemsRepository;
import sesoc.global.escape.repository.MapRepository;
import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.RoomMap;
import sesoc.global.escape.vo.Users;

@Controller
public class MapController {

	@Autowired
	MapRepository mrepo;
	
	@Autowired
	ItemsRepository irepo;
	
	private static final Logger logger = LoggerFactory.getLogger(MapController.class);	

	@RequestMapping(value = "/createMap", method = RequestMethod.GET)
	public String createMappage(HttpSession session, Model model){
		Users user = (Users) session.getAttribute("loginUser");
		String id = user.getId();
		RoomMap rm = new RoomMap();
		rm.setUser_id(id);
		ArrayList<RoomMap> maps = mrepo.selectAllMap(rm);
		model.addAttribute("maps", maps);
		return "editor/createMap";
	}
	
	@RequestMapping(value = "/createMap", method = RequestMethod.POST)
	public String createMap(RoomMap map, HttpSession session){
		//Map 등록작업
		logger.info(map.toString());
		if(mrepo.insertMap(map) == 1){
			logger.info("등록완료");
			RoomMap rm = mrepo.selectRoomMap(map);
			System.out.println(rm);
			session.setAttribute("map_no", rm.getNo());
		}
		return "redirect:/callEditor ";
	}
	
	@RequestMapping(value = "/setMapno", method = RequestMethod.GET)
	public String setMapnoSession(int map_no, HttpSession session){
		System.out.println(map_no);
		session.setAttribute("map_no", map_no);
		return "redirect:/callEditor ";
	}
	
	
	@RequestMapping(value = "/callEditor", method = RequestMethod.GET)
	public String callEditor(HttpSession session, Model model){
		//Editor에 필요한거 : Map title, 분류한 Items 가져오기
		int map_no = (int) session.getAttribute("map_no");
		Users user = (Users) session.getAttribute("loginUser");
		String id = user.getId();
		RoomMap rm = new RoomMap();
		rm.setNo(map_no);
		rm.setUser_id(id);
		rm = mrepo.selectRoomMap(rm);
		System.out.println(rm);
		
		//items : Controller에 가져와서 뿌려주기
		Map<String, ArrayList<Items>> items = irepo.selectItemsMatchingTheme(rm.getTheme());
		model.addAttribute("items", items);
		model.addAttribute("mapinfo", rm);
		return "editor/Editor";
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteMap", method = RequestMethod.GET)
	public int deleteMap(int map_no){
		int result = mrepo.deleteMap(map_no);
		return result;
	}
	
	@RequestMapping(value = "mapList", method = RequestMethod.GET)
	public String mapList(){
		return "room/mapList";
	}//mapList
	
	@ResponseBody
	@RequestMapping(value = "getMapList", method = RequestMethod.GET)
	public List<RoomMap> getMapList(){
		return mrepo.getExistingMap();
	}//mapList
	
	@ResponseBody
	@RequestMapping(value = "mapNo_update", method = RequestMethod.GET)
	public int mapNo_update(int map_no){
		return mrepo.mapNo_update(map_no);
	}//mapList
	

	
}

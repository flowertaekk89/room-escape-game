package sesoc.global.escape.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sesoc.global.escape.repository.RoomRepository;
import sesoc.global.escape.repository.UserRepository;
import sesoc.global.escape.vo.Room;
import sesoc.global.escape.vo.SocketData;
import sesoc.global.escape.vo.Users;
import sesoc.global.escape.vo.WaitingUsers;
import sesoc.global.escape.vo.WebsocketVO;
import sesoc.global.escape.webSocket.WebSocketHandler;

@Controller
public class RoomController {
	
	@Autowired
	UserRepository user_repo;
	
	@Autowired
	RoomRepository room_repo;

	@RequestMapping(value = "waitingRoom", method = RequestMethod.GET)
	public String waitingRoom(String room_no, Users user, String roomTitle, Model model) {
		
		List<WaitingUsers> userList = user_repo.findAllUserId();
		
		
		Users selectedUser = null;
		if(user.getNickname() != null){
			selectedUser = user_repo.selectNickName(user);
			if(isInRoom(userList, selectedUser.getId()) == false){
				model.addAttribute("message", "이미 접속 중입니다.");
				model.addAttribute("mapping", "");
				return "message";
			}// inner if
		}else{
			selectedUser = user_repo.selectId(user);
			if(isInRoom(userList, selectedUser.getId()) == false){
				model.addAttribute("message", "이미 접속 중입니다.");
				model.addAttribute("mapping", "");
				return "message";
			}// inner if
		}//outer if
		
		Room roomInfo = room_repo.roomInfo(room_no);
		selectedUser.setRoom_title(roomTitle);
		
		model.addAttribute("user", selectedUser);
		model.addAttribute("room", roomInfo);
		return "room/waitingRoom";
		
	}// waitingRoom
	
	public boolean isInRoom(List<WaitingUsers> userList, String id){
		boolean result = true;
		
		for (WaitingUsers waitingUsers : userList) {
			if(waitingUsers.getUser_id().equals(id)){
				result = false;
				break;
			}//if
		}//for
		
		return result;
	}//if
	
	

	@ResponseBody
	@RequestMapping(value = "renew", method = RequestMethod.GET)
	public List<WaitingUsers> renew(String userId, String userPw, String roomNum) {
		return user_repo.selectWaitingUser(new Room(Integer.parseInt(roomNum), 0, null, null, null, 0, null));
	}// waitingRoom
	
	@RequestMapping(value = "makingRoomPopUp", method = RequestMethod.GET)
	public String makingRoomPopUp(String id, Model model) {
		model.addAttribute("user_id", id);
		return "room/makingRoomPopUp";
	}
	
	@ResponseBody
	@RequestMapping(value = "makingRoom", method = RequestMethod.POST)
	public int makingRoom(Room room, String id) {

		List<WaitingUsers> userList = user_repo.findAllUserId();
		boolean multi_access = isInRoom(userList, id);
		
		if(multi_access){
			int room_no = room_repo.selectNextRoomNo();
			room.setNo(room_no);
			room.setMaster_id(id);
			room.setMap_no(1); // 기본 Map_no
			room.setStatus("waiting");
			int result = room_repo.insertRoom(room);
			if(result == 1) {
				return room_no;
			}else{
				return -1;
			}// inner if
		}else{
			return -1;
		}// outer if
	}
	
	@RequestMapping(value = "roomList", method = RequestMethod.GET)
	public String roomList(String nickname, Model model) {
		model.addAttribute("nickname", nickname);
		return "room/roomList";
	}// waitingRoom
	
	@ResponseBody
	@RequestMapping(value="roomListRenew", method=RequestMethod.GET)
	public List<Room> roomListRenew(String selectedWord, String searchWord) {
		Map<String, String> map = new HashMap<>();
		map.put("selectedWord", selectedWord);
		map.put("searchWord", searchWord);
		
		return room_repo.selectAllRoom(map);
	}// waitingRoom
	
	@RequestMapping(value = "testRoom", method = RequestMethod.GET)
	public String testRoom(String nickname, String roomNum, String userId, Model model) {
		Users user = new Users();
		user.setNickname(nickname);
		user = user_repo.selectNickName(user);

		// 방장 아이디 불러오기
		Users master = room_repo.findMaster(roomNum);
		
		// room 클래스에 상태 추가
		if(master != null && master.getId().equals(userId)){
			Room room = room_repo.roomInfo(roomNum);
			room_repo.deleteRoom(new Room(Integer.parseInt(roomNum), 0, null, null, null, 0, null));
			room_repo.insertRoom(new Room(Integer.parseInt(roomNum), room.getMap_no(), user.getId(), null, null, 0, "Playing"));
		}//if
		
		model.addAttribute("room_no", roomNum);
		model.addAttribute("user", user);
		return "room/testRoom";
	}// testRoom
	
	
}

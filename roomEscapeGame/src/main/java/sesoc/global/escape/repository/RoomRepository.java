package sesoc.global.escape.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sesoc.global.escape.dao.RoomDAO;
import sesoc.global.escape.vo.Room;
import sesoc.global.escape.vo.Users;
import sesoc.global.escape.vo.WaitingUsers;

@Repository
public class RoomRepository {
	
	@Autowired
	SqlSession sqlSession;
	
	public int selectNextRoomNo() {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.selectNextRoomNo();
	}
	
	public int insertRoom(Room room) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.insertRoom(room);
	}
	
	public int deleteRoom(Room room) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.deleteRoom(room);
	}
	
	public int insertWaitingUser(WaitingUsers watingUser) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.insertWaitingUser(watingUser);
	}
	
	public List<WaitingUsers> selectWaitingUser(Room room) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.selectWaitingUser(room);
	}
	
	public int deleteWatingUser(WaitingUsers watingUser) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.deleteWaitingUser(watingUser);
	}

	public List<Room> selectAllRoom(Map<String, String> map) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.selectAllRoom(map);
	}

	public WaitingUsers selectBySessionId(WaitingUsers waitinguser) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.selectBySessionId(waitinguser);
	}
	
	public Room roomInfo(String room_no) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.roomInfo(room_no);
	}
	
	public Users findMaster(String roomNum) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.findMaster(roomNum);
	}

	/**
	 * user_id 로 users_in_room 테이블에 있는 특정 유저 no 값을 가져옴.
	 * @param user_id
	 * @return
	 */
	public WaitingUsers getURIInfo(String user_id) {
		RoomDAO dao = sqlSession.getMapper(RoomDAO.class);
		return dao.getURIInfo(user_id);
	}
}

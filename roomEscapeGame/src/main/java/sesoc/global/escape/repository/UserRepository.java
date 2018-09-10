package sesoc.global.escape.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sesoc.global.escape.dao.UserDAO;
import sesoc.global.escape.vo.Room;
import sesoc.global.escape.vo.Users;
import sesoc.global.escape.vo.WaitingUsers;

@Repository
public class UserRepository {
	
	@Autowired
	SqlSession sqlSession;
	
	public int insertUser(Users user) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.insertUser(user);
	}
	
	public Users selectNickName(Users user) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.selectNickName(user);
	}
	
	public Users selectEmail(Users user) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.selectEmail(user);
	}
	
	public Users selectId(Users user) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.selectId(user);
	}
	
	public int updateUser(Users user) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.updateUser(user);
	}

	public int deleteNormalUser(String sessionId) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.deleteNormalUser(sessionId);
	}

	public WaitingUsers findUser(WaitingUsers waitinguser) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.findUser(waitinguser);
	}
	
	public List<WaitingUsers> selectWaitingUser(Room room) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.selectWaitingUser(room);
	}

	public int numberOfUsers(String sessionId) {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.numberOfUsers(sessionId);
	}
	/**
	 * users_inroom에 있는 모든 유저의 ID를 가져온다
	 * @return
	 */
	public List<WaitingUsers> findAllUserId() {
		UserDAO dao = sqlSession.getMapper(UserDAO.class);
		return dao.findAllUserId();
	}
}

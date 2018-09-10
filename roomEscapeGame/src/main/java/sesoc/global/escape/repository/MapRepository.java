package sesoc.global.escape.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sesoc.global.escape.dao.MapDAO;
import sesoc.global.escape.vo.RoomMap;

@Repository
public class MapRepository {

	@Autowired
	SqlSession sqlsession;
	
	public int insertMap(RoomMap map){
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.insertRoomMap(map);
	}
	
	public RoomMap selectRoomMap(RoomMap map){
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.selectRoomMap(map);
	}
	
	public ArrayList<RoomMap> selectAllMap(RoomMap map){
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.selectAllMap(map);
	}
	public List<RoomMap> getExistingMap() {
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.getExistingMap();
	}

	public int mapNo_update(int map_no) {
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.mapNo_update(map_no);
	}

	public int deleteMap(int map_no) {
		MapDAO dao = sqlsession.getMapper(MapDAO.class);
		return dao.deleteMap(map_no);
	}
}

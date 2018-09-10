package sesoc.global.escape.dao;

import java.util.ArrayList;
import java.util.List;

import sesoc.global.escape.vo.RoomMap;

public interface MapDAO {
	public int insertRoomMap(RoomMap roommap);
	public RoomMap selectRoomMap(RoomMap roommap);
	public ArrayList<RoomMap> selectAllMap(RoomMap roommap);
	/**
	 * 모든 맵을 가져온다.
	 * @return
	 */
	public List<RoomMap> getExistingMap();
	
	/**
	 * map list에서 선택한 map의 번호를 room table에 적용한다.
	 * @param map_no 
	 * @return
	 */
	public int mapNo_update(int map_no);
	public int deleteMap(int map_no);
}

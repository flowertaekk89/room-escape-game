package sesoc.global.escape.dao;

import sesoc.global.escape.vo.RoomMap;

public interface RoomMapDAO {
	public int insertRoomMap(RoomMap roommap);
	public int updateRoomMap(RoomMap roommap);
	public int deleteRoomMap(int roomno);
	public RoomMap selectRoomMap(RoomMap roommap);
}

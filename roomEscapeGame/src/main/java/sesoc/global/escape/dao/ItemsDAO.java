package sesoc.global.escape.dao;

import java.util.ArrayList;

import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.RoomMap;
import sesoc.global.escape.vo.Set_Items;

public interface ItemsDAO {
	public ArrayList<Items> selectItemsMatchingTheme(String theme);
	//	17.9.27 추가
	public ArrayList<Items> getTypeJSON(Items item);
	
	public Items selectOneItem(Items item);
	public Items selectMatchingItem(Items item);
	public ArrayList<Items> getSecondItems(String theme);
	//Setted Items
	public int insertSetItems (Set_Items item);
	public int updateSetItems (Set_Items item);
	public int deleteSetItems (Set_Items item);
	public ArrayList<Set_Items> selectSetItem(Set_Items item);

	//canvas
	public int insertHint(String hintPath);
	public int deleteHint(int no);
	public Set_Items selectHint(Set_Items item);
	
	//trick
	public int insertTrick(Items item);
	
	
}



package sesoc.global.escape.dao;

import java.util.List;

import sesoc.global.escape.vo.Inventory;
import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.Room;

public interface ItemDAO {
	
	/*public List<Items> selectInventory(Items item);
	public Items selectInvenItems(Items item);
	public Items selectTrickCheck(Items item);
	public Items selectClickItem(Items item);
	public int insertItem(Inventory inven);
	public Items itemInfo(String item_name);
	public int isExisted(Inventory inventory);
	public Items matchingCanvas(Items item);
	public Items selectCanvas(Items item);
	public Items solvedCheck(Items item);
	public Items selectSiCanvas(Items item);
	public int updateSolved(Items item);
	public int insertCanvasInven(Items itme);
	public Items selectInvenCanvas(Items item);*/

	public List<Items> selectInventorySetItems(Items item);
	public Items selectInvenItems(Items item);
	public Items selectTrickCheck(Items item);
	public Items selectClickItem(Items item);
	public int insertItem(Inventory inven);
	public Items itemInfo(String item_name);
	public int isExisted(Inventory inventory);
	public Items matchingItem(Items item);
	public Items matchingCanvas(Items item);
	public Items selectItemCanvas(Items item);
	public Items solvedCheck(Items item);
	public Items selectSiCanvas(Items item);
	public int updateSolved(Items item);
	public int insertCanvasInven(Items itme);
	public Items selectInvenCanvas(Items item);
	public Items selectMatchingItem(Items item);
	public List<Items> selectInventoryCanvas(Items item);
	public int insertSetItemInven(Items item);
	public Items selectObtain(Items item);
}

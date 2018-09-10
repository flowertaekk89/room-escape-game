package sesoc.global.escape.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sesoc.global.escape.dao.ItemDAO;
import sesoc.global.escape.vo.Inventory;
import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.Room;

@Repository
public class ItemRepository {
	
	@Autowired
	SqlSession sqlSession;

	public Items solvedCheck(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.solvedCheck(item);
	}
	
	public Items matchingItem(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.matchingItem(item);
	}
	
	public Items matchingCanvas(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.matchingCanvas(item);
	}
	
	public Items selectItemCanvas(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectItemCanvas(item);
	}

	public List<Items> selectInventorySetItems(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectInventorySetItems(item);
	}
	
	public Items selectInvenItems(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectInvenItems(item); 
	}
	
	public Items selectTrickCheck(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectTrickCheck(item); 
	}

	public int insertItem(Inventory inven) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.insertItem(inven);
	}//insertItem

	public Items itemInfo(String item_name) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.itemInfo(item_name);
	}
	public int isExisted(Inventory inventory) {
	      ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
	      return dao.isExisted(inventory);
	   }
	
	public Items selectClickItem(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectClickItem(item); 
	}
	
	public Items selectSiCanvas(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectSiCanvas(item); 
	}
	
	public int updateSolved(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.updateSolved(item); 
	}
	
	public int insertCanvasInven(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.insertCanvasInven(item); 
	}
	
	public Items selectInvenCanvas(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectInvenCanvas(item);
	}
	
	public Items selectMatchingItem(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectMatchingItem(item);
	}
	
	public List<Items> selectInventoryCanvas(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectInventoryCanvas(item);
	}
	
	public int insertSetItemInven(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.insertSetItemInven(item); 
	}
	
	public Items selectObtain(Items item) {
		ItemDAO dao = sqlSession.getMapper(ItemDAO.class);
		return dao.selectObtain(item);
	}
	
}

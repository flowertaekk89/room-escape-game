package sesoc.global.escape.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sesoc.global.escape.dao.ItemsDAO;
import sesoc.global.escape.vo.Items;
import sesoc.global.escape.vo.Set_Items;

@Repository
public class ItemsRepository {
	@Autowired
	SqlSession sqlsession;
	
	public Map<String, ArrayList<Items>> selectItemsMatchingTheme(String theme){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		ArrayList<Items> tempList = dao.selectItemsMatchingTheme(theme);
		System.out.println(tempList);
		
		Map<String, ArrayList<Items>> map = new HashMap<>();
		//type : wall / floor / furniture
		for(Items item : tempList){
			String type = item.getType();
			ArrayList<Items> arrlist = map.get(type);
			if(arrlist == null){
				map.put(type, new ArrayList<Items>());
			}
			map.get(type).add(item);
		}
		System.out.println(map);
		return map;
	}
	
	//	17.9.27 추가
	public ArrayList<Items> getTypeJSON(Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.getTypeJSON(item);
	}
	
	public Map<String, ArrayList<Items>> getSecondItems(String theme){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		ArrayList<Items> items = dao.getSecondItems(theme);
		Map<String, ArrayList<Items>> map = new HashMap<>();
		//type : lock / key / deco / tool
		for(Items item : items){
			String type = item.getType();
			ArrayList<Items> arrlist = map.get(type);
			if(arrlist == null){
				map.put(type, new ArrayList<Items>());
			}
			map.get(type).add(item);
		}
		return map;
	}
	
	public Items selectOneItem(Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.selectOneItem(item);
	}
	
	public Items selectMatchingItem(Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.selectMatchingItem(item);
	}

	
	//setItems
	public ArrayList<Set_Items> selecteSetItem(Set_Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.selectSetItem(item);
	}
	
	public int insertSetItems(Set_Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.insertSetItems(item);
	}
	
	public int updateSetItems(Set_Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.updateSetItems(item);
	}
	
	public int deleteSetItems(Set_Items item){
		ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
		return dao.deleteSetItems(item);
	}
	
	//9.26 추가
		//canvas : hint
		public int insertHint(String fullPath){
			ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
			return dao.insertHint(fullPath);
		}
		
		public int deleteHint(int no){
			ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
			return dao.deleteHint(no);
		}
		
		public Set_Items selectHint(Set_Items items){
			ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
			return dao.selectHint(items);
		}
		
		//9.29추가
		//trick
		public int insertTrick(Items item){
			ItemsDAO dao = sqlsession.getMapper(ItemsDAO.class);
			return dao.insertTrick(item);
		}
}

package sesoc.global.escape.vo;

public class Set_Items {
	//set_Items
		private int no;
		private int item_no;
		private String item_name;
		private int map_no;
		private int matching_item;
		private String rotation;
		private String position;
		private int canvas_no;
		private String setting;
		private String retractable;
		
		//canvas
		private String fullPath;
		
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public int getItem_no() {
			return item_no;
		}
		public void setItem_no(int item_no) {
			this.item_no = item_no;
		}
		public int getMap_no() {
			return map_no;
		}
		public void setMap_no(int map_no) {
			this.map_no = map_no;
		}
		public int getMatching_item() {
			return matching_item;
		}
		public void setMatching_item(int matching_item) {
			this.matching_item = matching_item;
		}
		public String getRotation() {
			return rotation;
		}
		public void setRotation(String rotation) {
			this.rotation = rotation;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getItem_name() {
			return item_name;
		}
		public void setItem_name(String item_name) {
			this.item_name = item_name;
		}
		public int getCanvas_no() {
			return canvas_no;
		}
		public void setCanvas_no(int canvas_no) {
			this.canvas_no = canvas_no;
		}
		public String getFullPath() {
			return fullPath;
		}
		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}
		public String getSetting() {
			return setting;
		}
		public void setSetting(String setting) {
			this.setting = setting;
		}
		
		public String getRetractable() {
			return retractable;
		}
		public void setRetractable(String retractable) {
			this.retractable = retractable;
		}
		@Override
		public String toString() {
			return "Set_Items [no=" + no + ", item_no=" + item_no + ", item_name=" + item_name + ", map_no=" + map_no
					+ ", matching_item=" + matching_item + ", rotation=" + rotation + ", position=" + position
					+ ", canvas_no=" + canvas_no + ", setting=" + setting + ", retractable=" + retractable + ", fullPath="
					+ fullPath + "]";
		}
	
}

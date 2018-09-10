package sesoc.global.escape.vo;

public class Inventory {
	private int no;
	private int item_no;
	private int UIR_no;
	private String item_name;
	private String type;
	private String theme;
	private String json_file;
	private String icon_file;
	private int maching_item;
	private String user_id;
	private int room_no; // 근택 ### 추가
	private int canvas_no; // 은지 ### 추가
	
	public Inventory() {
		// TODO Auto-generated constructor stub
	}
	
	public Inventory(String item_name, int room_no) {
		this.item_name = item_name;
		this.room_no = room_no;
	}// ITEM 획득시 중복여부확인에 사용

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

	public int getUIR_no() {
		return UIR_no;
	}

	public void setUIR_no(int uIR_no) {
		UIR_no = uIR_no;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getJson_file() {
		return json_file;
	}

	public void setJson_file(String json_file) {
		this.json_file = json_file;
	}

	public String getIcon_file() {
		return icon_file;
	}

	public void setIcon_file(String icon_file) {
		this.icon_file = icon_file;
	}

	public int getMaching_item() {
		return maching_item;
	}

	public void setMaching_item(int maching_item) {
		this.maching_item = maching_item;
	}

	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public int getRoom_no() {
		return room_no;
	}

	public void setRoom_no(int room_no) {
		this.room_no = room_no;
	}

	public int getCanvas_no() {
		return canvas_no;
	}

	public void setCanvas_no(int canvas_no) {
		this.canvas_no = canvas_no;
	}

	@Override
	public String toString() {
		return "Inventory [no=" + no + ", item_no=" + item_no + ", UIR_no=" + UIR_no + ", item_name=" + item_name
				+ ", type=" + type + ", theme=" + theme + ", json_file=" + json_file + ", icon_file=" + icon_file
				+ ", maching_item=" + maching_item + ", user_id=" + user_id + ", room_no=" + room_no + ", canvas_no="
				+ canvas_no + "]";
	}
	
	

}

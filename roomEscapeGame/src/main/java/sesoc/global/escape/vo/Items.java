package sesoc.global.escape.vo;

public class Items {
	/*
	 * items Table
	 */
	private int no;
	private String type;
	private String theme;
	private String json_file;
	private String icon_file;
	private double scale;
	private String canvas_availability;
	private String action_availability;
	private String trick_type;

	/*
	 * set_items Table
	 */
	private int SI_no; // primary no
	private String item_name;
	private int map_no;
	private int matching_item;
	private int position;
	private int rotation;
	private String retractable;

	/*
	 * inventory Table
	 */
	private int inven_no; // primary no

	/*
	 * users_inRoom Table
	 */
	private int UIR_no; // primary no
	private String user_id;
	private int room_no;

	/*
	 * trick Table
	 */
	private int trick_no; // primary no
	private String content;
	private String solved;
	private int obtainable_si;

	/*
	 * canvas Table
	 */
	private int canvas_no; // primary no
	private String img_file;

	public Items() {
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
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

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public String getCanvas_availability() {
		return canvas_availability;
	}

	public void setCanvas_availability(String canvas_availability) {
		this.canvas_availability = canvas_availability;
	}

	public String getAction_availability() {
		return action_availability;
	}

	public void setAction_availability(String action_availability) {
		this.action_availability = action_availability;
	}

	public String getTrick_type() {
		return trick_type;
	}

	public void setTrick_type(String trick_type) {
		this.trick_type = trick_type;
	}

	public int getSI_no() {
		return SI_no;
	}

	public void setSI_no(int sI_no) {
		SI_no = sI_no;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public String getRetractable() {
		return retractable;
	}

	public void setRetractable(String retractable) {
		this.retractable = retractable;
	}

	public int getInven_no() {
		return inven_no;
	}

	public void setInven_no(int inven_no) {
		this.inven_no = inven_no;
	}

	public int getUIR_no() {
		return UIR_no;
	}

	public void setUIR_no(int uIR_no) {
		UIR_no = uIR_no;
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

	public int getTrick_no() {
		return trick_no;
	}

	public void setTrick_no(int trick_no) {
		this.trick_no = trick_no;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSolved() {
		return solved;
	}

	public void setSolved(String solved) {
		this.solved = solved;
	}

	public int getCanvas_no() {
		return canvas_no;
	}

	public void setCanvas_no(int canvas_no) {
		this.canvas_no = canvas_no;
	}

	public String getImg_file() {
		return img_file;
	}

	public void setImg_file(String img_file) {
		this.img_file = img_file;
	}

	public int getObtainable_si() {
		return obtainable_si;
	}

	public void setObtainable_si(int obtainable_si) {
		this.obtainable_si = obtainable_si;
	}

	@Override
	public String toString() {
		return "Items [no=" + no + ", type=" + type + ", theme=" + theme + ", json_file=" + json_file + ", icon_file="
				+ icon_file + ", scale=" + scale + ", canvas_availability=" + canvas_availability
				+ ", action_availability=" + action_availability + ", trick_type=" + trick_type + ", SI_no=" + SI_no
				+ ", item_name=" + item_name + ", map_no=" + map_no + ", matching_item=" + matching_item + ", position="
				+ position + ", rotation=" + rotation + ", retractable=" + retractable + ", inven_no=" + inven_no
				+ ", UIR_no=" + UIR_no + ", user_id=" + user_id + ", room_no=" + room_no + ", trick_no=" + trick_no
				+ ", content=" + content + ", solved=" + solved + ", obtainable_si=" + obtainable_si + ", canvas_no="
				+ canvas_no + ", img_file=" + img_file + "]";
	}

	
}

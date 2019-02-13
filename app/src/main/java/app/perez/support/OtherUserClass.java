package app.perez.support;

public class OtherUserClass {
	private int id;
	private String name;
	private String rcvnumber;
	private String sendnumber;
	private int position;

	public OtherUserClass() {
		this.id = -1;
		this.name = "";
		this.rcvnumber = "";
		this.sendnumber = "";
		this.position = 1;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRcvnumber() {
		return rcvnumber;
	}

	public void setRcvnumber(String rcvnumber) {
		this.rcvnumber = rcvnumber;
	}

	public String getSendnumber() {
		return sendnumber;
	}

	public void setSendnumber(String sendnumber) {
		this.sendnumber = sendnumber;
	}

	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String toString() {
		return this.name;
	}
}

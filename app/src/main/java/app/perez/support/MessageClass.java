package app.perez.support;

public class MessageClass {
	private int idmessage;
	private int idOtherUser;
	private String message;
	private String date;
	private int sendORrcv; // send=1 -
	// rcv=0

	public MessageClass() {
		this.idmessage = -1;
		this.idOtherUser = -1;
		this.message = "";
		this.date = "";
		this.sendORrcv = 0; // send=1 -
		// rcv=0
	}

	public int getIdmessage() {
		return idmessage;
	}

	public void setIdmessage(int idmessage) {
		this.idmessage = idmessage;
	}

	public int getIdOtherUser() {
		return idOtherUser;
	}

	public void setIdOtherUser(int idOtherUser) {
		this.idOtherUser = idOtherUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSendORrcv() {
		return sendORrcv;
	}

	public void setSendORrcv(int sendORrcv) {
		this.sendORrcv = sendORrcv;
	}
	
	public String toString() {
		return this.message;
	}

}
package app.perez.support;

import android.provider.BaseColumns;

public class SMScontract {

	public SMScontract() {
	}

	// * Inner class that defines the table contents */
	// User table
	public static abstract class OtherUser implements BaseColumns {
		public static final String TABLE_NAME = "OtherUser";
		public static final String COLUMN_ENTRY_ID = "idotheruser";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_NUM_RCV = "rcvnumber";
		public static final String COLUMN_NUM_SEND = "sendnumber";
		public static final String COLUMN_SMS_TO_READ = "smstoread";
		public static final String COLUMN_POSITION = "position";
	}

	public static abstract class Messages implements BaseColumns {
		public static final String TABLE_NAME = "Messages";
		public static final String COLUMN_ENTRY_ID = "idmessage";
		public static final String COLUMN_OTHER_USER_ID = "idotheruser";
		public static final String COLUMN_MESSAGE = "message";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_SEND_OR_RCV = "sendorrcv"; // send=1 -
																		// rcv=0
	}

	public static final String OTHER_USER_CREATE = "CREATE TABLE "
			+ OtherUser.TABLE_NAME + "(" + OtherUser.COLUMN_ENTRY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + OtherUser.COLUMN_NAME
			+ " TEXT UNIQUE," + OtherUser.COLUMN_NUM_RCV + " TEXT,"
			+ OtherUser.COLUMN_NUM_SEND + " TEXT,"
			+ OtherUser.COLUMN_SMS_TO_READ + " INTEGER,"
			+ OtherUser.COLUMN_POSITION + " INTEGER" + ");";

	public static final String MESSAGES_CREATE = "CREATE TABLE "
			+ Messages.TABLE_NAME + "(" + Messages.COLUMN_ENTRY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Messages.COLUMN_OTHER_USER_ID + " INTEGER,"
			+ Messages.COLUMN_MESSAGE + " TEXT," + Messages.COLUMN_DATE
			+ " TEXT," + Messages.COLUMN_SEND_OR_RCV + " INTEGER" + ");";
}
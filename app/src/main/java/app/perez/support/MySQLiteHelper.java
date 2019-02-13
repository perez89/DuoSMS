package app.perez.support;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import app.perez.support.SMScontract.Messages;
import app.perez.support.SMScontract.OtherUser;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "SMSyup.db";
	private static final int DATABASE_VERSION = 4;
	private Context context;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase dbase) {
		System.out.println("onCreate-1");
		dbase.execSQL(SMScontract.OTHER_USER_CREATE);
		System.out.println("onCreate-2");
		dbase.execSQL(SMScontract.MESSAGES_CREATE);
		System.out.println("onCreate-3");
	}

	@Override
	public void onOpen(SQLiteDatabase dbase) {
		System.out.println("onOpen");
		super.onOpen(dbase);
		if (!dbase.isReadOnly()) {
			// Enable foreign key constraints
			dbase.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		System.out.println("close db");
	
		super.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("onUpgrade-1");
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		if (newVersion > oldVersion) {
			System.out.println("onUpgrade-2");
			db.execSQL("DROP TABLE IF EXISTS OtherUser");
			System.out.println("onUpgrade-3");
			db.execSQL("DROP TABLE IF EXISTS Messages");
			System.out.println("onUpgrade-4");
			onCreate(db);
		}
	}

	public int onUpgradeOtherUser(OtherUserClass user) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		System.out.println("new name= " + user.getName());
		values.put(OtherUser.COLUMN_NAME, user.getName());
		values.put(OtherUser.COLUMN_NUM_RCV, user.getRcvnumber());
		values.put(OtherUser.COLUMN_NUM_SEND, user.getSendnumber());

		if ((db.update("OtherUser", values, "idotheruser=" + user.getId(), null)) < 0)
			return 0;
		values.clear();
		Cursor cursor = db.rawQuery(
				"SELECT name FROM OtherUser where idotheruser= '"
						+ user.getId() + "'", null);
		cursor.moveToFirst();
		System.out.println("result= " + cursor.getString(0));
		return 1;
	}

	public int onInsertOtherUser(OtherUserClass user) {
		// insert
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(OtherUser.COLUMN_NAME, user.getName());
		values.put(OtherUser.COLUMN_NUM_RCV, user.getRcvnumber());
		values.put(OtherUser.COLUMN_NUM_SEND, user.getSendnumber());
		values.put(OtherUser.COLUMN_SMS_TO_READ, 0);
		values.put(OtherUser.COLUMN_POSITION, 1);

		long idOtherUser = db.insert("OtherUser", null, values);
		if (idOtherUser < 0)
			return 0;
		values.clear();

		return ((int) idOtherUser);
	}

	public int getIdOtherUser(String numero) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT idotheruser FROM OtherUser where rcvnumber= '" + numero
						+ "' OR sendnumber = '" + numero + "'", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		return -1;
	}

	public int getIdFromNumber(String number) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT rcvnumber FROM OtherUser", null);
		System.out.println("number1= " + number);
		while (cursor.moveToNext()) {
			System.out.println("number2= " + cursor.getString(0));
			if (PhoneNumberUtils.compare(number, cursor.getString(0))) {
				System.out.println("yah");
				return getIdOtherUser(cursor.getString(0));
			}

		}

		cursor = db.rawQuery("SELECT sendnumber FROM OtherUser", null);

		while (cursor.moveToNext()) {
			System.out.println("number3= " + cursor.getString(0));
			if (PhoneNumberUtils.compare(number, cursor.getString(0))) {
				System.out.println("yah 2");
				return getIdOtherUser(cursor.getString(0));
			}

		}
		System.out.println("no");
		return -1;
	}

	public String getName(int idOtherUser) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT name FROM OtherUser where idotheruser= '" + idOtherUser
						+ "'", null);

		if (cursor.getCount() < 1)
			return "";
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	public int checkname(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT name FROM OtherUser where name= '"
				+ name + "'", null);
		if (cursor.getCount() > 0) {

			return 0;
		}
		return 1;
	}

	public int checknumber(int number) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT rcvnumber FROM OtherUser where rcvnumber= '" + number
						+ "'", null);
		if (cursor.getCount() > 0) {
			return 0;
		}
		return 1;
	}

	public int onInsertMessage(MessageClass message) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(Messages.COLUMN_OTHER_USER_ID, message.getIdOtherUser());
		values.put(Messages.COLUMN_MESSAGE, message.getMessage());
		values.put(Messages.COLUMN_DATE, message.getDate());
		values.put(Messages.COLUMN_SEND_OR_RCV, message.getSendORrcv());// send=1
																		// - //
																		// rcv=0
		long result = db.insert("Messages", null, values);
		if (result < 0)
			return 0;
		values.clear();
		values.put(OtherUser.COLUMN_POSITION, -1);
		db.update("OtherUser", values,
				"idotheruser=" + message.getIdOtherUser(), null);

		values.clear();
		List<OtherUserClass> ListOfUsers = new ArrayList<OtherUserClass>();
		ListOfUsers = getAllOtherUsers();
		for (int i = 0; i < ListOfUsers.size(); i++) {
			values.put(OtherUser.COLUMN_POSITION, (ListOfUsers.get(i)
					.getPosition() + 1));

			db.update("OtherUser", values, "idotheruser="
					+ ListOfUsers.get(i).getId(), null);
			values.clear();
		}
		return 1;
	}

	public List<MessageClass> getMessages(int idOtherUser) {
		List<MessageClass> ListOfMessages = new ArrayList<MessageClass>();
		SQLiteDatabase db = this.getWritableDatabase();
		SharedPreferences prefs;
		prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

		Cursor cursor = db.rawQuery(
				"SELECT idmessage, message, date, sendorrcv FROM Messages where idotheruser= '"
						+ idOtherUser + "'", null);

		if ((cursor.getCount()) == 0)
			return ListOfMessages;

		int maxNumbSms = prefs.getInt("maxNumberSMS", 500);
		boolean cbBooleanDelOld = prefs.getBoolean("cbDelOldSMS", true);
		if ((cursor.getCount() > maxNumbSms) && (maxNumbSms > 0)
				&& cbBooleanDelOld) {
			for (int i = 0; i < 1000; i++) {
				cursor.moveToNext();
				deleteMessages(0, cursor.getInt(0));
			}
			cursor = db.rawQuery(
					"SELECT idmessage, message, date, sendorrcv FROM Messages where idotheruser= '"
							+ idOtherUser + "'", null);
		}
		if (cursor.getCount() > 5000) {
			for (int i = 0; i < 2500; i++) {
				cursor.moveToNext();
				deleteMessages(0, cursor.getInt(0));
			}
			cursor = db.rawQuery(
					"SELECT idmessage, message, date, sendorrcv FROM Messages where idotheruser= '"
							+ idOtherUser + "'", null);
		}
		while (cursor.moveToNext()) {
			System.out.println("message= " + cursor.getString(1) + " id= "
					+ cursor.getInt(0));
			MessageClass mensagem = new MessageClass();
			mensagem.setIdOtherUser(idOtherUser);
			mensagem.setIdmessage(cursor.getInt(0));
			mensagem.setMessage(cursor.getString(1));
			mensagem.setDate(cursor.getString(2));
			mensagem.setSendORrcv(cursor.getInt(3));
			ListOfMessages.add(mensagem);
		}
		return ListOfMessages;

	}

	public MessageClass getMessageInfo(int smsID) {
		MessageClass smsInfo = new MessageClass();
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT  date, sendorrcv FROM Messages where idmessage= '"
						+ smsID + "'", null);

		if ((cursor.getCount()) == 0)
			return smsInfo;
		cursor.moveToFirst();
		smsInfo.setIdmessage(smsID);
		smsInfo.setDate(cursor.getString(0));
		smsInfo.setSendORrcv(cursor.getInt(1));
		return smsInfo;
	}

	public int deleteMessages(int idOtherUser, int type) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (type == 0) {// remover tudo
			if ((db.delete("Messages", Messages.COLUMN_OTHER_USER_ID + "="
					+ idOtherUser, null)) < 0)
				return 0;
			return 1;
		} else if (type > 0) {// remover especifica
			System.out.println("delete antes");

			if ((db.delete("Messages", Messages.COLUMN_ENTRY_ID + "=" + type,
					null)) < 0)
				return 0;
			return 1;
		}
		return 0;
	}

	public int deleteUser(int idOtherUser) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"DELETE FROM OtherUser where idotheruser= '" + idOtherUser
						+ "'", null);
		if ((cursor.getCount()) > 0)
			return 1;
		else
			return 0;
	}

	public List<OtherUserClass> getAllOtherUsers() {
		List<OtherUserClass> ListOfUsers = new ArrayList<OtherUserClass>();

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"SELECT idotheruser,name,rcvnumber,sendnumber,position FROM OtherUser order by position ASC",
						null);

		if ((cursor.getCount()) == 0)
			return ListOfUsers;

		while (cursor.moveToNext()) {
			OtherUserClass otheruser = new OtherUserClass();
			otheruser.setId(cursor.getInt(0));
			otheruser.setName(cursor.getString(1));
			otheruser.setRcvnumber(cursor.getString(2));
			otheruser.setSendnumber(cursor.getString(3));
			otheruser.setPosition(cursor.getInt(4));

			ListOfUsers.add(otheruser);
		}

		return ListOfUsers;
	}

	public boolean getSmsToRead(int idOtherUser) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT smstoread FROM OtherUser where idotheruser= '"
						+ idOtherUser + "'", null);

		cursor.moveToFirst();
		if (cursor.getInt(0) == 1)
			return true;
		else
			return false;
	}

	public void setSmsToRead(int idOtherUser, int value) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();

		values.put(OtherUser.COLUMN_SMS_TO_READ, value);
		db.update("OtherUser", values, "idotheruser=" + idOtherUser, null);

		values.clear();
	}

	public MyResult getLastSms(int idOtherUser) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT idmessage, message, date FROM Messages where idotheruser= '"
								+ idOtherUser
								+ "' and sendorrcv = 0 order by idmessage DESC limit 1",
						null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			MyResult newResult = new MyResult(cursor.getString(1),
					cursor.getString(2));
			return newResult;
		} else {
			MyResult newResult2 = new MyResult("", "");
			return newResult2;
		}
	}

	final class MyResult {
		private final String text;
		private final String date;

		public MyResult(String text, String date) {
			this.text = text;
			this.date = date;
		}

		public String getText() {
			return this.text;
		}

		public String getDate() {
			return this.date;
		}
	}
}

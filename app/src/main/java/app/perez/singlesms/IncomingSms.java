package app.perez.singlesms;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import app.perez.support.MessageClass;

public class IncomingSms extends BroadcastReceiver {
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	private static Context mContext;
	MessageClass mensagem;
	SharedPreferences getPrefs;

	public void onReceive(Context context, Intent intent) {
		mContext = context;
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();
		try {
			System.out.println("Exception-notification-0");
			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				int id = 0;
				SmsMessage currentMessage = null;
				String phoneNumber = "";
				String message = "";
				for (int i = 0; i < pdusObj.length; i++) {
					currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					phoneNumber = currentMessage.getDisplayOriginatingAddress();
					message = message + currentMessage.getDisplayMessageBody();
					if (id < 1) {

						id = MainSms.dbConnect.getIdFromNumber(phoneNumber);

						if (id < 1)
							break;
					}

				}
				if (id > 0) {
					System.out.println("buhhhh-0");
					abortBroadcast();
					String otherUserName = null;
					try {

						otherUserName = MainSms.dbConnect.getName(id);

					} catch (Exception e) {
						System.out.println("Exception-notification");
						Log.e("SmsReceiver", "Exception notification" + e);

					}
					String date = PlaceholderFragment.getdatenow();
					mensagem = new MessageClass();
					mensagem.setDate(date);
					mensagem.setIdOtherUser(id);
					mensagem.setMessage(message);
					System.out.println("buhhhh-2");
					try {

						MainSms.dbConnect.onInsertMessage(mensagem);

					} catch (Exception e) {
						System.out.println("Exception-notification");
						Log.e("SmsReceiver", "Exception notification" + e);

					}
					System.out.println("buhhhh-3");
					getPrefs = mContext.getSharedPreferences("MyPrefs",
							Context.MODE_PRIVATE);
					System.out.println("buhhhh-4");

					boolean cbBoolean = getPrefs.getBoolean("cbNotifications",
							true);
					System.out.println("resuuuuuu= " + cbBoolean);
					if (cbBoolean) {
						notification(context, message, otherUserName, id);
					}
					try {

						MainSms.dbConnect.setSmsToRead(id, 1);

					} catch (Exception e) {
						System.out.println("Exception-notification");
						Log.e("SmsReceiver", "Exception notification" + e);

					}

					try {
						if (id == (ListUsers.OTHERUSER.getId())) {
							PlaceholderFragment.ListOfMessages.add(mensagem);
							PlaceholderFragment.adapter.notifyDataSetChanged();
							PlaceholderFragment.scrollMyListViewToBottom();
							// MainSms.cancelNotifictgations(MainSms.OTHERUSER.getId());
							// NotificationManager nMgr =
							// (NotificationManager)
							// context.getSystemService(Context.NOTIFICATION_SERVICE);
							// nMgr.cancel(id);
						} else {

							MainSms.dbConnect.setSmsToRead(id, 1);

						}
					} catch (Exception e) {

					} finally {
						ListUsers.refreshList();
					}

					// notification(context);
				}

				// end for loop
			} // bundle is null
		} catch (Exception e) {
			System.out.println("error-smsReceiver");
			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}

	private void notification(Context context, String mensagem,
			String otherUserName, int id) {
		System.out.println("notification-buhh");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		String tickerText = null;

		if (mensagem.length() > 12)
			tickerText = mensagem.substring(0, 12);
		else
			tickerText = mensagem.substring(0, (mensagem.length()));
		getPrefs = mContext.getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		boolean cbBooleanVibration = getPrefs.getBoolean("cb_vibration", true);
		long[] powers = new long[5];
		if (cbBooleanVibration) {
			powers[0] = 1000;
			powers[1] = 1000;
			powers[2] = 1000;
			powers[3] = 1000;
			powers[4] = 1000;
		} else {
			powers[0] = 0;
			powers[1] = 0;
			powers[2] = 0;
			powers[3] = 0;
			powers[4] = 0;
		}
		Intent notificationIntent = new Intent(context, MainSms.class);
		notificationIntent.putExtra("item_id", id);
		mBuilder.setContentTitle(otherUserName)
				.setContentText(mensagem)
				.setOngoing(true)
				.setSound(alarmSound)
				.setContentIntent(
						PendingIntent.getActivity(
								context,
								10,
								notificationIntent
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
								0))
				.setTicker(otherUserName + ": " + tickerText)
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
				.setVibrate(powers).setLights(Color.BLUE, 3000, 3000)
				.setSmallIcon(R.drawable.ic_launcher).build();

		// Gets an instance of the NotificationManager service
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Builds the notification and issues it.
		mNotificationManager.notify(id, mBuilder.build());
	}

	public static String getdate(SmsMessage currentMessage) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentMessage.getTimestampMillis());

		int dia = calendar.get(Calendar.DATE);
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		return (hour + "#" + min + "#" + dia + "#" + getMonth(mes) + "#" + ano);
	}

	public static String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month];
	}

	public static Context getContext() {
		return mContext;
	}
}

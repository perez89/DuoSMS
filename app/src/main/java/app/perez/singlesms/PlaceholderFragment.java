package app.perez.singlesms;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import app.perez.support.ListMessagesArrayAdapter;
import app.perez.support.MessageClass;
import app.perez.support.MessageOptionsDialog;
import app.perez.support.MySQLiteHelper;

public class PlaceholderFragment extends Fragment {

	private static FragmentActivity myContext;
	public boolean isSoftKeyboardDisplayed = false;
	MessageClass mensagem;
	static ListMessagesArrayAdapter adapter;
	static ListView lvMensagens;
	EditText etSendText;
	ImageButton btSendSMS;
	String draftText;
	public static List<MessageClass> ListOfMessages;
	SharedPreferences prefs;
	int idOtherUser;
	String rcvd_numb;
	String send_numb;
	String prefVarName;
	static Context context;
	public static BroadcastReceiver sendBroadcastReceiver;
	public static BroadcastReceiver deliveryBroadcastReciever ;
	public static ArrayList<PendingIntent> sentIntents;
	public static ArrayList<PendingIntent> deliveryIntents;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	public PlaceholderFragment() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		sendBroadcastReceiver = new sentReceiver();
		 deliveryBroadcastReciever = new deliverReceiver();
		prefVarName = ListUsers.OTHERUSER.toString() + "varPref";

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		scrollMyListViewToBottom();
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);

		MainSms.actionBar.setTitle(ListUsers.OTHERUSER.toString());
		ListUsers.cancelNotifictgations(ListUsers.OTHERUSER.getId());
		prefs = getActivity().getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		draftText = prefs.getString(prefVarName, "");
		etSendText.setText(draftText);
		etSendText.setSelection(etSendText.getText().length());
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		System.out.println("place onresume");
		Editor editor = prefs.edit();
		if (!(etSendText.equals(""))) {
			editor.putString(prefVarName, etSendText.getText().toString());
		} else {
			editor.putString(prefVarName, "");
		}
		editor.commit();
		super.onPause();
		try {
			context.unregisterReceiver(sendBroadcastReceiver);
			context.unregisterReceiver(deliveryBroadcastReciever);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			context.unregisterReceiver(sendBroadcastReceiver);
			context.unregisterReceiver(deliveryBroadcastReciever);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		sendBroadcastReceiver = new sentReceiver();
		 deliveryBroadcastReciever = new deliverReceiver();
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		try {
			rcvd_numb = ListUsers.OTHERUSER.getRcvnumber();
			send_numb = ListUsers.OTHERUSER.getSendnumber();
		} catch (Exception e) {
			System.out.println("ERROR0 = " + e.getMessage());
		}
		ListOfMessages = new ArrayList<MessageClass>();

		/*
		 * savedInstanceState = this.getArguments(); idOtherUser =
		 * savedInstanceState.getInt("idOtherUser", -1); if(idOtherUser==-1){
		 * idOtherUser =
		 * ((MainSms)getActivity()).ListOfOtherUsers.get(0).getId(); }
		 */
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.frag_main_sms, container,
				false);

		etSendText = (EditText) rootView.findViewById(R.id.etSendText);
		btSendSMS = (ImageButton) rootView.findViewById(R.id.btSendMensage);
		lvMensagens = (ListView) rootView.findViewById(R.id.lvMensagens);
		lvMensagens.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// ListOfMessages = dbConnect
		// .getMessages(((MainSms) getActivity()).OTHERUSER.getId());

	
		try {
			ListOfMessages = MainSms.dbConnect.getMessages(ListUsers.OTHERUSER.getId());
		} catch (Exception e) {

		}

		adapter = new ListMessagesArrayAdapter(getActivity(), ListOfMessages);
		lvMensagens.setAdapter(adapter);

		lvMensagens.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle args = new Bundle();
				args.putString("copysmsKey", ListOfMessages.get(position)
						.getMessage());
				args.putInt("smsidKey", ListOfMessages.get(position)
						.getIdmessage());
				args.putString("smsdateKey", ListOfMessages.get(position)
						.getDate());
				args.putInt("otheruseridKey", ListOfMessages.get(position)
						.getIdOtherUser());
				args.putString("sendnumberKey",
						ListUsers.OTHERUSER.getSendnumber());

				System.out.println("sms= "
						+ ListOfMessages.get(position).toString());
				MessageOptionsDialog df = new MessageOptionsDialog();
				FragmentManager fm = myContext.getSupportFragmentManager();

				df.setArguments(args);
				df.show(fm, "DIALOG_SMS_OPTIONS");
				// System.out.println("position= " +position + " id= " +id);
				return false;
			}

		});

		lvMensagens.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// Log.d("Manoj", "onTouch entered");
				if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER
						|| event.getAction() == MotionEvent.ACTION_DOWN) {
					try {
						ListUsers.cancelNotifictgations(ListUsers.OTHERUSER
								.getId());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				return false;
			}
		});
		etSendText.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// Log.d("Manoj", "onTouch entered");
				if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER
						|| event.getAction() == MotionEvent.ACTION_DOWN) {
					try {
						ListUsers.cancelNotifictgations(ListUsers.OTHERUSER
								.getId());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				return false;
			}
		});
		btSendSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String sms_text = etSendText.getText().toString();
				try {

					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";

					sentIntents = new ArrayList<PendingIntent>();
					deliveryIntents = new ArrayList<PendingIntent>();

					PendingIntent sentPI = PendingIntent.getBroadcast(context,
							0, new Intent(SENT), 0);
					sentIntents.add(sentPI);

					PendingIntent deliveredPI = PendingIntent.getBroadcast(
							context, 0, new Intent(DELIVERED), 0);
					deliveryIntents.add(deliveredPI);

					context.registerReceiver(sendBroadcastReceiver,
							new IntentFilter(SENT));

					context.registerReceiver(deliveryBroadcastReciever,
							new IntentFilter(DELIVERED));
					SmsManager smsManager = SmsManager.getDefault();

					ArrayList<String> sms_text_multi = new ArrayList<String>();
					sms_text_multi = smsManager.divideMessage(sms_text);
					smsManager.sendMultipartTextMessage(send_numb, null,
							sms_text_multi, sentIntents, deliveryIntents);
					// smsManager.sendTextMessage(send_numb, null, sms_text,
					// null, null);
					etSendText.setText("");
					mensagem = new MessageClass();

					mensagem.setDate(getdatenow());
					mensagem.setIdOtherUser(ListUsers.OTHERUSER.getId());
					mensagem.setMessage(sms_text);
					mensagem.setSendORrcv(1);
					try {
						
						MainSms.dbConnect.onInsertMessage(mensagem);
						
						ListOfMessages.add(mensagem);
						adapter.notifyDataSetChanged();
						// scrollMyListViewToBottom();
						/*
						 * ListOfMessages = dbConnect.getMessages(5);
						 * 
						 * adapter = new ListMessagesArrayAdapter(
						 * getActivity(), ListOfMessages); // System.out.println
						 * ("buhh-90= "+ObjectItemData.get( 1).getBottleName());
						 * lvMensagens.setAdapter(adapter);
						 */

					} catch (Exception e) {
						System.out
								.println("Error ao guardar na base de dados!");
						System.out.println(e.getMessage());
					}
				} catch (Exception e) {
					// Toast.makeText(getBaseContext(),
					// "SMS faild, please try again later!",
					// Toast.LENGTH_LONG).show();
					Toast.makeText(getActivity().getBaseContext(), "Error!",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
					System.out.println("Error!");
				}
			}
		});
		return rootView;
	}

	public static void scrollMyListViewToBottom() {
		// TODO Auto-generated method stub
		lvMensagens.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				lvMensagens.setSelection(adapter.getCount() - 1);
			}
		});
	}

	public static String getdatenow() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		int dia = calendar.get(Calendar.DATE);
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		String mins = "" + min;

		if (min < 10)
			mins = "0" + mins;
		return (hour + "#" + mins + "#" + dia + "#" + getMonth(mes) + "#" + ano);
	}

	public static String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month];
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.main_sms, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity) activity;
		super.onAttach(activity);
	}

	public static void refreshSMS() {
		
		try {
			ListOfMessages = MainSms.dbConnect.getMessages(ListUsers.OTHERUSER.getId());
		} catch (Exception e) {

		}
	
		adapter = new ListMessagesArrayAdapter(context, ListOfMessages);
		lvMensagens.setAdapter(adapter);

		lvMensagens.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle args = new Bundle();
				args.putString("copysmsKey", ListOfMessages.get(position)
						.getMessage());
				args.putInt("smsidKey", ListOfMessages.get(position)
						.getIdmessage());
				args.putString("smsdateKey", ListOfMessages.get(position)
						.getDate());
				args.putInt("otheruseridKey", ListOfMessages.get(position)
						.getIdOtherUser());

				System.out.println("sms= "
						+ ListOfMessages.get(position).toString());
				MessageOptionsDialog df = new MessageOptionsDialog();
				FragmentManager fm = myContext.getSupportFragmentManager();

				df.setArguments(args);
				df.show(fm, "DIALOG_SMS_OPTIONS");
				// System.out.println("position= " +position + " id= " +id);
				return false;
			}

		});
	}

	public class deliverReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent arg1) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "SMS delivered.", Toast.LENGTH_SHORT)
						.show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(context, "SMS not delivered!",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	}

	public class sentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent arg1) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
				// startActivity(new Intent(SendSMS.this, ChooseOption.class));
				// overridePendingTransition(R.anim.animation,
				// R.anim.animation2);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT)
						.show();
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				Toast.makeText(context, "No service", Toast.LENGTH_SHORT)
						.show();
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
				break;
			}

		}
	}

}
package app.perez.support;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import app.perez.singlesms.ListUsers;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.PlaceholderFragment;
import app.perez.singlesms.R;

public class MessageOptionsDialog extends DialogFragment {
	public static MessageOptionsDialog newInstance() {
		MessageOptionsDialog frag = new MessageOptionsDialog();
		return frag;
	}

	Context context;
	EditText etTitle;
	EditText etContent;
	String message;
	int otherUserId;
	int messageID;
	String messageDate;
	String sendNumber;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		context = getActivity();
		Bundle mArgs = getArguments();
		message = mArgs.getString("copysmsKey", "");
		messageID = mArgs.getInt("smsidKey", -1);
		messageDate = mArgs.getString("smsdateKey", "");
		otherUserId = mArgs.getInt("otheruseridKey", -1);
		sendNumber = mArgs.getString("sendnumberKey", "");
		LayoutInflater li = LayoutInflater.from(context);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		View promptsView = li.inflate(R.layout.message_options, null);
		// etTitle = (EditText) promptsView.findViewById(R.id.et_title);

		ListView optionsSmsList = (ListView) promptsView
				.findViewById(R.id.lv_men_opt);
		String[] stringArray = new String[] { "Copy message",
				"Delete message", "Resend", "View message details" };
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, stringArray);
		optionsSmsList.setAdapter(modeAdapter);
		optionsSmsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		optionsSmsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					System.out.println("0");
					if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
						android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
								.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(message);
					} else {
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
								.getSystemService(Context.CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData
								.newPlainText("Copied Text", message);
						clipboard.setPrimaryClip(clip);
					}
					break;
				case 1:
					
					try {
						System.out.println("delete - 1" + otherUserId + " "
								+ messageID);
						MainSms.dbConnect.deleteMessages(otherUserId, messageID);
						System.out.println("delete - 2");
						PlaceholderFragment.refreshSMS();
						System.out.println("delete - 3");
						Toast.makeText(context, "The message was removed!",
								Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						System.out.println("error= " + e.getMessage());
					}
				
					break;
				case 2:
					sendSMS();
					break;
				case 3:
					Bundle args = new Bundle();

					args.putInt("smsidKey", messageID);

					MessageDetailsDialog df = new MessageDetailsDialog();
					FragmentManager fm = getActivity()
							.getSupportFragmentManager();

					df.setArguments(args);
					df.show(fm, "DIALOG_SMS_DETAILS");
					// System.out.println("position= " +position + " id= " +id);
					break;
				default:
					break;
				}
				dismiss();
			}

			private void sendSMS() {
				// TODO Auto-generated method stub
				try {

					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";

					PlaceholderFragment.sentIntents = new ArrayList<PendingIntent>();
					PlaceholderFragment.deliveryIntents = new ArrayList<PendingIntent>();

					PendingIntent sentPI = PendingIntent.getBroadcast(context,
							0, new Intent(SENT), 0);
					PlaceholderFragment.sentIntents.add(sentPI);

					PendingIntent deliveredPI = PendingIntent.getBroadcast(
							context, 0, new Intent(DELIVERED), 0);
					PlaceholderFragment.deliveryIntents.add(deliveredPI);

					context.registerReceiver(
							PlaceholderFragment.sendBroadcastReceiver,
							new IntentFilter(SENT));

					context.registerReceiver(
							PlaceholderFragment.deliveryBroadcastReciever,
							new IntentFilter(DELIVERED));
					SmsManager smsManager = SmsManager.getDefault();

					ArrayList<String> sms_text_multi = new ArrayList<String>();
					sms_text_multi = smsManager.divideMessage(message);
					smsManager.sendMultipartTextMessage(sendNumber, null,
							sms_text_multi, PlaceholderFragment.sentIntents,
							PlaceholderFragment.deliveryIntents);

					// smsManager.sendTextMessage(send_numb, null, sms_text,
					// null, null);

					MessageClass mensagem = new MessageClass();

					mensagem.setDate(PlaceholderFragment.getdatenow());
					mensagem.setIdOtherUser(otherUserId);
					mensagem.setMessage(message);
					mensagem.setSendORrcv(1);
					try {
						
						MainSms.dbConnect.onInsertMessage(mensagem);
						
						PlaceholderFragment.refreshSMS();
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
		alertDialogBuilder.setView(optionsSmsList);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setTitle("Message Options");
		return alertDialogBuilder.create();

	}
}

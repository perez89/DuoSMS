package app.perez.support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import app.perez.singlesms.MainSms;

public class MessageDetailsDialog extends DialogFragment {
	Context context;

	public static MessageDetailsDialog newInstance() {
		MessageDetailsDialog frag = new MessageDetailsDialog();
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		context = getActivity();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		Bundle mArgs = getArguments();
		int messageID = mArgs.getInt("smsidKey", -1);
		MessageClass smsInfo = new MessageClass();
		smsInfo = MainSms.dbConnect.getMessageInfo(messageID);
		String[] splited = smsInfo.getDate().split("#");
		String dataS = "";
		if (smsInfo.getSendORrcv() == 1)
			dataS = "Sent: ";
		else if (smsInfo.getSendORrcv() == 0)
			dataS = "Received: ";
		dataS = dataS + splited[2] + " " + splited[3] + " " + splited[4] + "\n"
				+ splited[0] + "h" + splited[1] + "min";
		alertDialogBuilder.setTitle("Message Details");
		alertDialogBuilder.setMessage("Type: Text message\n" + dataS);
		return alertDialogBuilder.create();

	}
}

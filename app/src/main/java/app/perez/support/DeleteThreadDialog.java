package app.perez.support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;
import app.perez.singlesms.ListUsers;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.PlaceholderFragment;

public class DeleteThreadDialog extends DialogFragment {
	Context context;

	public static DeleteThreadDialog newInstance() {
		DeleteThreadDialog frag = new DeleteThreadDialog();
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		context = getActivity();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setTitle("Delete thread");
		alertDialogBuilder.setMessage("One conversation will be deleted.");
		alertDialogBuilder.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						try {
							
							MainSms.dbConnect.deleteMessages(ListUsers.OTHERUSER.getId(), 0);
							Toast toast = Toast.makeText(context,
									"Conversation deleted.", Toast.LENGTH_SHORT);
							toast.show();
							
							PlaceholderFragment.refreshSMS();
						} catch (Exception e) {
							System.out.println("error= " + e.getMessage());
						}

						//remover conversacao
						dialog.dismiss();
					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		return alertDialogBuilder.create();

	}
}

package app.perez.singlesms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import app.perez.support.MySQLiteHelper;
import app.perez.support.OtherUserClass;
import app.perez.support.RemoveProfileDialogFragment;

public class RemoveOtherUser extends Fragment implements OnClickListener {
	private EditText etNameOtherUser;
	private EditText etRcvNumber;
	private EditText etSendNumber;
	private ImageView searchNameAdd;
	private ImageView searchRcvNumber;
	private ImageView searchSendNumber;
	private Button btCancel;
	private Button btSave;
	View rootView;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);
		MainSms.actionBar.setTitle("Remove Profile");
		super.onResume();
	}

	public RemoveOtherUser() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		rootView = inflater
				.inflate(R.layout.remove_edit_user, container, false);

		populate();
		populateEditText();
		return rootView;
	}

	private void populateEditText() {
		// TODO Auto-generated method stub
		etNameOtherUser.setText(ListUsers.OTHERUSER.getName().toString());
		etRcvNumber.setText(ListUsers.OTHERUSER.getRcvnumber().toString());
		etSendNumber.setText(ListUsers.OTHERUSER.getSendnumber().toString());
	}

	private void populate() {
		// TODO Auto-generated method stub
		etNameOtherUser = (EditText) rootView
				.findViewById(R.id.et_name_other_user_remove);
		etRcvNumber = (EditText) rootView
				.findViewById(R.id.et_rcv_number_remove);
		etSendNumber = (EditText) rootView
				.findViewById(R.id.et_send_number_remove);

		searchNameAdd = (ImageView) rootView
				.findViewById(R.id.iv_search_name_remove);
		searchRcvNumber = (ImageView) rootView
				.findViewById(R.id.iv_search_rcv_remove);
		searchSendNumber = (ImageView) rootView
				.findViewById(R.id.iv_search_send_remove);
		etNameOtherUser.setText("");

		btCancel = (Button) rootView.findViewById(R.id.bt_remove_remove);
		btSave = (Button) rootView.findViewById(R.id.bt_save_remove);
		btCancel.setOnClickListener(this);
		btSave.setOnClickListener(this);

		searchNameAdd.setOnClickListener(this);
		searchRcvNumber.setOnClickListener(this);
		searchSendNumber.setOnClickListener(this);

	}

	public void onClick(View v) {
		Toast toast;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_remove_remove:

			// RemoveProfileDialogFragment newFragment = new
			// RemoveProfileDialogFragment();
			// newFragment.show(getActivity().getSupportFragmentManager(),
			// "missiles");
			RemoveProfileDialogFragment newFragment = RemoveProfileDialogFragment
					.newInstance();
			newFragment.setTargetFragment(this, 3);
			newFragment.show(getActivity().getSupportFragmentManager(),
					"dialog");
			break;
		case R.id.bt_save_remove:
			if ((!etNameOtherUser.equals("")) && (!etRcvNumber.equals(""))
					&& (!etSendNumber.equals(""))) {
				if (((etRcvNumber.length()) >= 3)
						&& ((etSendNumber.length()) >= 3)) {
					try {

						OtherUserClass user = new OtherUserClass();
						user.setName(etNameOtherUser.getText().toString());
						user.setRcvnumber(etRcvNumber.getText().toString());
						user.setSendnumber(etSendNumber.getText().toString());
						user.setId(ListUsers.OTHERUSER.getId());
						
						int resultOnInsertOtherUser = MainSms.dbConnect
								.onUpgradeOtherUser(user);
						
						if (resultOnInsertOtherUser < 1) {
							toast = Toast.makeText(getActivity(),
									"Something went wrong!!!",
									Toast.LENGTH_SHORT);
							toast.show();
						} else if (resultOnInsertOtherUser > 0) {
							ListUsers.OTHERUSER.setName(etNameOtherUser.getText().toString());
							toast = Toast.makeText(getActivity(),
									"Profile saved with success!",
									Toast.LENGTH_SHORT);
							toast.show();
							// ((MainSms) getActivity()).populateSpinner();
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					toast = Toast.makeText(getActivity(),
							"Please check the recieve/send number!",
							Toast.LENGTH_SHORT);
					toast.show();
				}
			} else {
				toast = Toast.makeText(getActivity(),
						"Please fill out all required fields!",
						Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		case R.id.iv_search_name_remove:
			Intent IntentName = new Intent(Intent.ACTION_PICK,
					Uri.parse("content://contacts"));
			IntentName.setType(Phone.CONTENT_TYPE); // Show user only contacts
													// w/ phone numbers
			startActivityForResult(IntentName, 0);
			break;
		case R.id.iv_search_rcv_remove:
			Intent IntentRcvNumber = new Intent(Intent.ACTION_PICK,
					Uri.parse("content://contacts"));
			IntentRcvNumber.setType(Phone.CONTENT_TYPE); // Show user only
															// contacts w/ phone
															// numbers
			startActivityForResult(IntentRcvNumber, 1);
			break;
		case R.id.iv_search_send_remove:
			Intent IntentSendNumber = new Intent(Intent.ACTION_PICK,
					Uri.parse("content://contacts"));
			IntentSendNumber.setType(Phone.CONTENT_TYPE); // Show user only
															// contacts w/ phone
															// numbers
			startActivityForResult(IntentSendNumber, 2);
			break;
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (0):
			if (resultCode == Activity.RESULT_OK) {

				Uri contactUri = data.getData();
				// We only need the NUMBER column, because there will be only
				// one row in the result
				String[] projection = { Phone.DISPLAY_NAME };

				// Perform the query on the contact to get the NUMBER column
				// We don't need a selection or sort order (there's only one
				// result for the given URI)
				// CAUTION: The query() method should be called from a separate
				// thread to avoid blocking
				// your app's UI thread. (For simplicity of the sample, this
				// code doesn't do that.)
				// Consider using CursorLoader to perform the query.
				Cursor cursor = this.getActivity().getContentResolver()
						.query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				// Retrieve the phone number from the NUMBER column

				int column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
				String name = cursor.getString(column);

				// Do something with the phone number...
				etNameOtherUser.setText(name);
			}
			break;
		case (1):
			if (resultCode == Activity.RESULT_OK) {

				Uri contactUri = data.getData();
				// We only need the NUMBER column, because there will be only
				// one row in the result
				String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

				// Perform the query on the contact to get the NUMBER column
				// We don't need a selection or sort order (there's only one
				// result for the given URI)
				// CAUTION: The query() method should be called from a separate
				// thread to avoid blocking
				// your app's UI thread. (For simplicity of the sample, this
				// code doesn't do that.)
				// Consider using CursorLoader to perform the query.
				Cursor cursor = this.getActivity().getContentResolver()
						.query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				// Retrieve the phone number from the NUMBER column
				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);

				column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
				String name = cursor.getString(column);

				// Do something with the phone number...
				etRcvNumber.setText(number);

				if (etNameOtherUser.getText().toString().equals(""))
					etNameOtherUser.setText(name);
			}
			break;
		case (2):
			if (resultCode == Activity.RESULT_OK) {

				Uri contactUri = data.getData();
				// We only need the NUMBER column, because there will be only
				// one row in the result
				String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

				// Perform the query on the contact to get the NUMBER column
				// We don't need a selection or sort order (there's only one
				// result for the given URI)
				// CAUTION: The query() method should be called from a separate
				// thread to avoid blocking
				// your app's UI thread. (For simplicity of the sample, this
				// code doesn't do that.)
				// Consider using CursorLoader to perform the query.
				Cursor cursor = this.getActivity().getContentResolver()
						.query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				// Retrieve the phone number from the NUMBER column
				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);

				column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
				String name = cursor.getString(column);

				// Do something with the phone number...
				etSendNumber.setText(number);

				if (etNameOtherUser.getText().toString().equals(""))
					etNameOtherUser.setText(name);
			}
			break;
		case (3):
			if (resultCode == Activity.RESULT_OK) {
				deleteUserandMessages();

			}
			break;
		}

	}

	private void deleteUserandMessages() {
		// TODO Auto-generated method stub
		try {

		
			MainSms.dbConnect.deleteMessages(ListUsers.OTHERUSER.getId(), 0);
			MainSms.dbConnect.deleteUser(ListUsers.OTHERUSER.getId());
			
			String prefVarName = ListUsers.OTHERUSER.toString() + "varPref";
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getActivity());

			prefs = getActivity().getSharedPreferences("MyPrefs",
					Context.MODE_PRIVATE);
			if (prefs.contains(prefVarName)) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.remove(prefVarName);
				editor.commit();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ListUsers.refreshList();
			Fragment fragment = getActivity().getSupportFragmentManager()
					.findFragmentByTag("SMS_LIST_TAG");
			if (fragment != null) {
				getActivity().getSupportFragmentManager().beginTransaction()
						.remove(fragment).commit();

				getActivity().getSupportFragmentManager().popBackStack();
			}
			fragment = null;
			fragment = getActivity().getSupportFragmentManager()
					.findFragmentByTag("TAG_REMOVE_USER");
			if (fragment != null) {
				getActivity().getSupportFragmentManager().beginTransaction()
						.remove(fragment).commit();

				getActivity().getSupportFragmentManager().popBackStack();
			}

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_add_rm_profile, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}

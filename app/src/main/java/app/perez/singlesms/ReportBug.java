package app.perez.singlesms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReportBug extends Fragment {

	Button btSendReport, btSet;
	static TextView TvReportText;
	CheckBox cbAgrred;
	static public EditText etReportText;
	Context context;

	public ReportBug() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);
		MainSms.actionBar.setTitle("Report Bug");
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);

		View rootView = inflater.inflate(R.layout.reportbug, container, false);
		btSendReport = (Button) rootView.findViewById(R.id.bt_Send_Report);
		btSet = (Button) rootView.findViewById(R.id.bt_Set);
		TvReportText = (TextView) rootView.findViewById(R.id.tv_Report_Text);
		// cbAgrred = (CheckBox) rootView.findViewById(R.id.cb_Agrre);
		etReportText = (EditText) rootView.findViewById(R.id.et_Report_Text);
		getDeviceName();
		// cbAgrred.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// //is chkIos checked?
		// if (((CheckBox) v).isChecked()) {
		// //Case 1
		// ((CheckBox) v).setChecked(false);
		// btSendReport.setEnabled(false);
		// }
		// else {
		// ((CheckBox) v).setChecked(true);
		// btSendReport.setEnabled(true);
		// }
		// //case 2
		//
		// }
		// });
		etReportText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!getTvReportText().equals("")) {
					etReportText.setText(getTvReportText());
					etReportText.setSelection(etReportText.getText().length());
				}
			}
		});

		btSendReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TvReportText.getText().length() < 30) {
					Toast toast = Toast.makeText(context,
							"Your report is to small!", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					// SendReportDialogFragment df = new
					// SendReportDialogFragment();
					//
					// FragmentManager fm = getActivity()
					// .getSupportFragmentManager();
					//
					// df.show(fm, "DIALOG");
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(context);

					prefs = context.getSharedPreferences("MyPrefs",
							Context.MODE_PRIVATE);
					int numbReport = prefs.getInt("numbReport", 0);
					String sms_text = "DuoSMS" + "\r\n#Report(" + numbReport
							+ ")= " + ReportBug.getTvReportText()
							+ "\r\n\r\n#Device= " + getDeviceName()
							+ "\r\n#Android API Version= "
							+ android.os.Build.VERSION.SDK_INT
							+ "\r\n#Android Version= "
							+ android.os.Build.VERSION.CODENAME
							+ "\r\n#Android Release Version= "
							+ android.os.Build.VERSION.RELEASE;
					String[] recipients = { "perezappshelp@gmail.com" };

					Intent email = new Intent(Intent.ACTION_SEND, Uri
							.parse("mailto:"));

					// prompts email clients only

					email.setType("message/rfc822");

					email.putExtra(Intent.EXTRA_EMAIL, recipients);

					email.putExtra(Intent.EXTRA_SUBJECT, ("DuoSMS bug report"
							+ getDeviceName() + ""));

					email.putExtra(Intent.EXTRA_TEXT, sms_text.toString());

					try {

						// the user can choose the email client

						startActivity(Intent.createChooser(email,
								"Choose an email client from..."));
					} catch (android.content.ActivityNotFoundException ex) {

						Toast.makeText(getActivity(),
								"No email client installed.",

								Toast.LENGTH_LONG).show();

					}
				}
			}
		});
		btSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTvReportText(etReportText.getText().toString());
				etReportText.setText("");
				hideSoftKeyboard();
			}
		});
		return rootView;
	}

	public static String getTvReportText() {
		return TvReportText.getText().toString();
	}

	public static void setTvReportText(String ReportText) {
		TvReportText.setText(ReportText);
	}

	private void hideSoftKeyboard() {
		if (getActivity().getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(), 0);
		}
	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

}

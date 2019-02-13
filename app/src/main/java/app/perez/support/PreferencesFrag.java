package app.perez.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.R;

public class PreferencesFrag extends Fragment {

	SharedPreferences prefs;
	Context context;

	public PreferencesFrag() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);
		MainSms.actionBar.setTitle("Preferences");
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		context = getActivity();
		View rootView = inflater.inflate(R.layout.prefs, container, false);

		final CheckBox cbNot = (CheckBox) rootView
				.findViewById(R.id.cb_Notifications);

		prefs = getActivity().getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
		boolean cbBoolean = prefs.getBoolean("cbNotifications", true);
		cbNot.setChecked(cbBoolean);
		cbNot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("onClick");

				Editor editor = prefs.edit();
				if (cbNot.isChecked()) {
					System.out.println("onClick2");

					editor.putBoolean("cbNotifications", true);
				} else {
					System.out.println("onClick3");

					editor.putBoolean("cbNotifications", false);
				}
				editor.commit();
			}
		});
		final CheckBox cbDelOldSms = (CheckBox) rootView
				.findViewById(R.id.cb_Del_Old_SMS);
		boolean cbBooleanDelOld = prefs.getBoolean("cbDelOldSMS", true);
		cbDelOldSms.setChecked(cbBooleanDelOld);
		cbDelOldSms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("onClick");

				Editor editor = prefs.edit();
				if (cbDelOldSms.isChecked()) {
					System.out.println("onClick2");

					editor.putBoolean("cbDelOldSMS", true);
				} else {
					System.out.println("onClick3");

					editor.putBoolean("cbDelOldSMS", false);
				}
				editor.commit();
			}
		});
		final CheckBox cbPowerSaver = (CheckBox) rootView
				.findViewById(R.id.cb_power_saver);
		boolean cbBooleanPowerSaver = prefs.getBoolean("cb_Power_Saver", true);
		cbPowerSaver.setChecked(cbBooleanPowerSaver);
		cbPowerSaver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("onClick");

				Editor editor = prefs.edit();
				if (cbPowerSaver.isChecked()) {
					System.out.println("onClick2");

					editor.putBoolean("cb_Power_Saver", true);
				} else {
					System.out.println("onClick3");

					editor.putBoolean("cb_Power_Saver", false);
				}
				editor.commit();
			}
		});
		
//		final CheckBox cbDelivery = (CheckBox) rootView
//				.findViewById(R.id.cb_delivery);
//		boolean cbBooleanDelivery = prefs.getBoolean("cb_delivery", true);
//		cbDelivery.setChecked(cbBooleanDelivery);
//		cbDelivery.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				System.out.println("onClick");
//
//				Editor editor = prefs.edit();
//				if (cbDelivery.isChecked()) {
//					System.out.println("onClick2");
//
//					editor.putBoolean("cb_delivery", true);
//				} else {
//					System.out.println("onClick3");
//
//					editor.putBoolean("cb_delivery", false);
//				}
//				editor.commit();
//			}
//		});
		
		final CheckBox cbVibration = (CheckBox) rootView
				.findViewById(R.id.cb_vibration);
		boolean cbBooleanVibration= prefs.getBoolean("cb_vibration", true);
		cbVibration.setChecked(cbBooleanVibration);
		cbVibration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("onClick");

				Editor editor = prefs.edit();
				if (cbVibration.isChecked()) {
					System.out.println("onClick2");

					editor.putBoolean("cb_vibration", true);
				} else {
					System.out.println("onClick3");

					editor.putBoolean("cb_vibration", false);
				}
				editor.commit();
			}
		});
		return rootView;
	}

}

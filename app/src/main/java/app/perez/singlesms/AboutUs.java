package app.perez.singlesms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUs extends Fragment {

	public AboutUs() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);
		MainSms.actionBar.setTitle("About us");
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);

		View rootView = inflater.inflate(R.layout.aboutus, container, false);

		return rootView;
	}

}

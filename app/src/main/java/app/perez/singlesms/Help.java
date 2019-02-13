package app.perez.singlesms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Help extends Fragment {

	public Help() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);
		MainSms.actionBar.setDisplayShowTitleEnabled(true);
		MainSms.actionBar.setTitle("Help");
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainSms.actionBar.setDisplayHomeAsUpEnabled(true);

		View rootView = inflater.inflate(R.layout.help, container, false);

		return rootView;
	}

}

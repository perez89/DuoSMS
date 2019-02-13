package app.perez.support;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.PlaceholderFragment;
import app.perez.singlesms.R;

public class welcome extends Fragment {
	
	List<OtherUserClass> ListOfOtherUsers = new ArrayList<OtherUserClass>();
	public welcome() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.welcome, container, false);
		
		ListOfOtherUsers = MainSms.dbConnect.getAllOtherUsers();
		
		if (ListOfOtherUsers.size() >0) {
				((MainSms)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment()).addToBackStack(null).commit();
		} 
		return rootView;
	}
}
package app.perez.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.R;

public class ListBgColor extends Fragment {

	SharedPreferences pref;
	RadioGroup radioGroup;

	public ListBgColor() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		System.out.println("buhh-00");
		View rootView = inflater.inflate(R.layout.list_bg_color,
				container, false);
		System.out.println("buhh-0");
		radioGroup = (RadioGroup) rootView.findViewById(R.id.rgBgColor);
		System.out.println("buhh-1");
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Editor editor = pref.edit();
				switch (checkedId) {
				case R.id.rb1:
					editor.putInt("theme", 1);
					MainSms.changeBG(1);
					break;
				case R.id.rb2:
					editor.putInt("theme", 2);
					MainSms.changeBG(2);
					break;
				case R.id.rb3:
					editor.putInt("theme", 3);
					MainSms.changeBG(3);
					break;
				case R.id.rb4:
					editor.putInt("theme", 4);
					MainSms.changeBG(4);
					break;
				case R.id.rb5:
					editor.putInt("theme", 5);
					MainSms.changeBG(5);
					break;
				case R.id.rb6:
					editor.putInt("theme", 6);
					MainSms.changeBG(6);
					break;
				case R.id.rb7:
					editor.putInt("theme", 7);
					MainSms.changeBG(7);
					break;
				case R.id.rb8:
					editor.putInt("theme", 8);
					MainSms.changeBG(8);
					break;
				case R.id.rb9:
					editor.putInt("theme", 9);
					MainSms.changeBG(9);
					break;
				case R.id.rb10:
					editor.putInt("theme", 10);
					MainSms.changeBG(10);
					break;
				}
				editor.commit();
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pref = this.getActivity().getSharedPreferences("MyPrefs",
				Context.MODE_PRIVATE);
	}

}
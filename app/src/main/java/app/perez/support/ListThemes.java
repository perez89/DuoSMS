package app.perez.support;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import app.perez.singlesms.R;

public class ListThemes extends Fragment {

	SharedPreferences pref;
	RadioGroup radioGroup;
	final RadioButton[] rb = null;
	List<ThemeColors> ListThemes;
	ThemeColors tColor;
	public ListThemes() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.themeslist, container, false);
		radioGroup = (RadioGroup) rootView.findViewById(R.id.rgThemes);
		setThemes();
		setRadioButtons(rootView);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

			}
		});
		return rootView;
	}
	
	private void setThemes() {
		// TODO Auto-generated method stub
		ListThemes =  new ArrayList<ThemeColors>();
		tColor = new ThemeColors("#e9e0d6", "#542733", "#e94c6f", "#5a6a62", "#c6d5cd");
		ListThemes.add(tColor);
		tColor = new ThemeColors("#fdf200", "#542733", "#e94c6f", "#5a6a62", "#c6d5cd");
		ListThemes.add(tColor);
		tColor = new ThemeColors("#588c73", "#f2e394", "#f2ae72", "#8c4646", "#d96459");
		ListThemes.add(tColor);
		tColor = new ThemeColors("#d0c91f", "#008bba", "#85c4b9", "#dc403b", "#df514c");
		ListThemes.add(tColor);
		tColor = new ThemeColors("#29aba4", "#354458", "#3a9ad9", "#e9e0d6", "#eb7260");
		ListThemes.add(tColor);
	}

	private View setRadioButtons(View rootView) {
		// TODO Auto-generated method stub
			int NumberOfThemes= 5 ;
			//rb = new RadioButton[NumberOfThemes];
			String strThemeID = "bg_gradiant_list_sms_1";
			for(int i = 0 ; i < NumberOfThemes ; i++){
				//rb[i]  = (RadioGroup) rootView.findViewById(R.id);
				
			    rb[i].setText("");
			    //rb[i].setBackground();
			    radioGroup.addView(rb[i]);
			}		    
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
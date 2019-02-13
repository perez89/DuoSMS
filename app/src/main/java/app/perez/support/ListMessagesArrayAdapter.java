package app.perez.support;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.perez.singlesms.IncomingSms;
import app.perez.singlesms.R;

public class ListMessagesArrayAdapter extends ArrayAdapter<MessageClass> {
	private final Context context;
	private final List<MessageClass> items;
	
	public ListMessagesArrayAdapter(Context context, List<MessageClass> items) {
		super(context, R.layout.row_list_message, items);
		this.context = context;
		this.items = items;
		
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		TextView tv_row = null;
		TextView tv_row_hour = null;
		LinearLayout llayout = null;
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		int num_speech_chat = getPrefs.getInt("numb_speech_chat", 4);
		if (items.get(position).getSendORrcv() == 0) {
			convertView = inflater.inflate(R.layout.row_list_message, parent,
					false);
			llayout = (LinearLayout) convertView.findViewById(R.id.layoutLeft);
			tv_row = (TextView) convertView.findViewById(R.id.tv_row_rcv);
			tv_row_hour = (TextView) convertView.findViewById(R.id.tv_row_rcv_hour);
			switch(num_speech_chat){
			case 0:
				llayout.setBackgroundResource(R.drawable.defaultspeechballonleft);
				
				break;
			case 1:
				llayout.setBackgroundResource(R.drawable.speechballonleft);
				break;
			case 2:
				llayout.setBackgroundResource(R.drawable.speechballonleft2);
				break;
//			case 3:
//				llayout.setBackgroundResource(R.drawable.chat_left3);
				//break;
			case 4:
				llayout.setBackgroundResource(R.drawable.shadowleft_0);
				break;
			}
			
		} else if (items.get(position).getSendORrcv() == 1) {
			convertView = inflater.inflate(R.layout.row_list_message_send,
					parent, false);
			llayout = (LinearLayout) convertView.findViewById(R.id.layoutRight);
			tv_row = (TextView) convertView.findViewById(R.id.tv_row_send);
			tv_row_hour = (TextView) convertView.findViewById(R.id.tv_row_send_hour);
			
			switch(num_speech_chat){
			case 0:
				llayout.setBackgroundResource(R.drawable.defaultspeechballonright);
				break;
			case 1:
				llayout.setBackgroundResource(R.drawable.speechballonright);
				break;
			case 2:
				llayout.setBackgroundResource(R.drawable.speechballonright2);
				break;
//			case 3:
//				llayout.setBackgroundResource(R.drawable.chat_right3);
			//	break;
			case 4:
				llayout.setBackgroundResource(R.drawable.shadowright_0);
				break;
			}
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		int dia = calendar.get(Calendar.DATE);
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);

		String[] splited = items.get(position).getDate().split("#");
		String time = "";

		try {
			if (Integer.parseInt(splited[2]) == dia
					&& splited[3].equals((IncomingSms.getMonth(mes)))
					&& Integer.parseInt(splited[4]) == ano) {
				int x = 1;
				if (x == 1) {
					if (((Integer.parseInt(splited[0])) > 12) && ((Integer.parseInt(splited[0])) <24)) {
						hour = (Integer.parseInt(splited[0])) - 12;
						time = hour + ":" + splited[1] + "PM";
					} else {
						if (((Integer.parseInt(splited[0])) == 0) || ((Integer.parseInt(splited[0])) == 24)) {
							
							time = "12:" + splited[1]
									+ "AM";
						}else{
							time =(Integer.parseInt(splited[0]))+ ":" + splited[1]
									+ "AM";
						}
					}

				} else {
					time = splited[0] + "h" + splited[1];
				}
			} else {
				time = splited[3].substring(0, 3) + " " + splited[2];
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		tv_row.setText(items.get(position).toString() + "\n");
		
		tv_row_hour.setText(time);
		
		
	
		
		return convertView;
	}

//	private void onTouch(View convertView) {
//		// TODO Auto-generated method stub
//		convertView.onLongClick(new View.OnTouchListener()
//		{
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(event.getAction()==MotionEvent.){
//					System.out.println(v);
//					return true;
//				}
//				
//				return false;
//			}
//		    // Implementation;
//		});
//		
//	}
//	
	

}

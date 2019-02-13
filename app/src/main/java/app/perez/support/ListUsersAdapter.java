package app.perez.support;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.perez.singlesms.IncomingSms;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.R;
import app.perez.support.MySQLiteHelper.MyResult;

public class ListUsersAdapter extends ArrayAdapter<OtherUserClass> {

	Context context;
	int layoutResourceId;
	List<OtherUserClass> listOfOtherUsers = null;
	SharedPreferences prefs;

	public ListUsersAdapter(Context context, int layoutResourceId,
			List<OtherUserClass> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.listOfOtherUsers = data;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.row_users_list, parent, false);

			holder = new UserHolder();

			holder.tvUserName = (TextView) row.findViewById(R.id.tv_User_Name);
			holder.tvHourDate = (TextView) row.findViewById(R.id.tv_hour_date);
			holder.tvText = (TextView) row.findViewById(R.id.tv_text);
			holder.tvNewDraft = (TextView) row.findViewById(R.id.tv_new_draft);

			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}

	
		MyResult newResult = MainSms.dbConnect.getLastSms(listOfOtherUsers
				.get(position).getId());
		

		OtherUserClass x = listOfOtherUsers.get(position);
		holder.tvUserName.setText(x.toString());
		holder.tvUserName.setTypeface(null, Typeface.BOLD);
		

		String mensagem = newResult.getText();

		if (mensagem.length() > 30)
			mensagem = mensagem.substring(0, 30);
		else
			mensagem = mensagem.substring(0, (mensagem.length()));
		mensagem = mensagem + "...";
		if (MainSms.dbConnect.getSmsToRead(listOfOtherUsers.get(position).getId())) {

			holder.tvNewDraft.setText("new");
			holder.tvNewDraft.setTextColor(Color.parseColor("#007fc1"));
			holder.tvNewDraft.setTypeface(null, Typeface.BOLD);
			holder.tvText.setText(mensagem);
			holder.tvText.setTextColor(Color.parseColor("#007fc1"));
			holder.tvText.setTypeface(null, Typeface.BOLD);
			holder.tvHourDate.setText(formatDate(newResult.getDate()));
			holder.tvHourDate.setTextColor(Color.parseColor("#007fc1"));
			holder.tvHourDate.setTypeface(null, Typeface.BOLD);

		} else {
			prefs = getContext().getSharedPreferences("MyPrefs",
					Context.MODE_PRIVATE);
			String prefVarName = listOfOtherUsers.get(position).toString()
					+ "varPref";
			String draftText = prefs.getString(prefVarName, "");

			if (!draftText.equals("")) {
				
				holder.tvNewDraft.setText("Draft");
				holder.tvNewDraft.setTextColor(Color.parseColor("#FF0000"));
				holder.tvNewDraft.setTypeface(null, Typeface.BOLD);
			}else{
				holder.tvNewDraft.setText("");
			}

			
			holder.tvText.setText(mensagem);
			holder.tvText.setTypeface(null, Typeface.NORMAL);
			holder.tvHourDate.setText(formatDate(newResult.getDate()));
			holder.tvHourDate.setTypeface(null, Typeface.NORMAL);
		}
		

		return row;
	}

	private String formatDate(String string) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		int dia = calendar.get(Calendar.DATE);
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);

		String[] splited = string.split("#");
		String timeText = null;

		try {
			if (Integer.parseInt(splited[2]) == dia
					&& splited[3].equals((IncomingSms.getMonth(mes)))
					&& Integer.parseInt(splited[4]) == ano) {
				int x = 1;
				if (x == 1) {
					if (((Integer.parseInt(splited[0])) > 12)
							&& ((Integer.parseInt(splited[0])) < 24)) {
						hour = (Integer.parseInt(splited[0])) - 12;
						timeText = hour + ":" + splited[1] + "PM";
					} else {
						if (((Integer.parseInt(splited[0])) == 0)
								|| ((Integer.parseInt(splited[0])) == 24)) {

							timeText = "12:" + splited[1] + "AM";
						} else {
							timeText = (Integer.parseInt(splited[0])) + ":"
									+ splited[1] + "AM";
						}
					}

				} else {
					timeText = splited[0] + "h" + splited[1];
				}
			} else {
				timeText = splited[3].substring(0, 3) + " " + splited[2];
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return timeText;
	}

	static class UserHolder {
		TextView tvUserName;
		TextView tvHourDate;
		TextView tvText;
		TextView tvNewDraft;
	}
}
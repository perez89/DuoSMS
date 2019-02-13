package app.perez.singlesms;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.perez.support.MySQLiteHelper;
import app.perez.support.OtherUserClass;


public class TitleNavigationAdapter extends BaseAdapter {
	private final Context context;
	private final List<OtherUserClass> items;
	int layoutResourceId;
	private LayoutInflater inflater;

	public TitleNavigationAdapter(Context context, int textViewResourceId,
			List<OtherUserClass> items) {
		// super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.layoutResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(MainSms.id>-1){
		
			position=MainSms.id;
		}
		
		if (convertView == null || convertView.getTag() == null) {
			convertView = inflater.inflate(R.layout.spinner_dropdown, null);
			convertView.setTag(new Object());
		}
		TextView tvOtherUser = (TextView) convertView
				.findViewById(R.id.tv_spinner_other_user);
		tvOtherUser.setText(items.get(position).toString());

		return convertView;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			convertView = inflater.inflate(R.layout.spinner_dropdown, null);

			convertView.setTag(new Object());
		}
		TextView tv_row = (TextView) convertView
				.findViewById(R.id.tv_spinner_other_user);

		tv_row.setText(items.get(position).toString());

		ImageView icon = (ImageView) convertView
				.findViewById(R.id.icon_sms_to_read);


		if (MainSms.dbConnect.getSmsToRead(items.get(position).getId())) {
			icon.setVisibility(View.VISIBLE);
		} else {
			icon.setVisibility(View.INVISIBLE);
		}

		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// @Override
	// public View getView(int position, View convertView, final ViewGroup
	// parent) {
	// LayoutInflater inflater = (LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View rowView = null;
	//
	// rowView = inflater.inflate(R.layout.row_spinner_other_user, parent,
	// false);
	//
	// ImageView icon =(ImageView) rowView
	// .findViewById(R.id.icon_sms_to_read);
	// dbConnect = new MySQLiteHelper(getContext());
	// if(dbConnect.getSmsToRead(items.get(position).getId()))
	// {
	// icon.setVisibility(View.VISIBLE);
	// }else{
	// icon.setVisibility(View.INVISIBLE);
	// }
	// dbConnect.close();
	// TextView tv_row = (TextView) rowView
	// .findViewById(R.id.tv_spinner_other_user);
	// tv_row.setText(items.get(position).toString());
	//
	// return rowView;
	// }
}
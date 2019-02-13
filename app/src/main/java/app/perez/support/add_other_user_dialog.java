package app.perez.support;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import app.perez.singlesms.MainSms;
import app.perez.singlesms.R;

public class add_other_user_dialog extends DialogFragment implements
		OnClickListener {
	private EditText etNameOtherUser;
	private EditText etRcvNumber;
	private EditText etSendNumber;
	private ImageView searchRcvNumber;
	private ImageView searchSendNumber;
	private Button btCancel;
	private Button btSave;

	public add_other_user_dialog() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_other_user, container);

		etNameOtherUser = (EditText) view
				.findViewById(R.id.et_name_other_user_add);
		etRcvNumber = (EditText) view.findViewById(R.id.et_rcv_number_add);
		etSendNumber = (EditText) view.findViewById(R.id.et_send_number_add);
		searchRcvNumber = (ImageView) view.findViewById(R.id.iv_search_rcv_add);
		searchSendNumber = (ImageView) view
				.findViewById(R.id.iv_search_send_add);
		btCancel = (Button) view.findViewById(R.id.bt_cancel_add);
		btSave = (Button) view.findViewById(R.id.bt_save_add);
		btCancel.setOnClickListener(this);
		btSave.setOnClickListener(this);
		getDialog().setTitle("Add new profile");
		return view;
	}

	@Override
	public void onClick(View v) {
		Toast toast;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_cancel_add:
			dismiss();
			break;
			
		case R.id.bt_save_add:
			String userName = etNameOtherUser.getText().toString();
			String RcvNumber = etRcvNumber.getText().toString();
		
			int checkName =0;
			checkName=MainSms.dbConnect.checkname(userName);
			int checkNumber = 0;
			checkNumber=MainSms.dbConnect.checkname(RcvNumber);
			if(checkName == 0){
				toast = Toast.makeText(getActivity(), "Profile name already exist!!!",  Toast.LENGTH_SHORT );
				toast.show();
			}else if(checkNumber ==0){
				toast = Toast.makeText(getActivity(), "The recieve number already exist!!!",  Toast.LENGTH_SHORT );
				toast.show();
			}else{
				if((!etNameOtherUser.equals("")) && (!etRcvNumber.equals("")) && (!etSendNumber.equals("")))
				{
					if(((etRcvNumber.length())>=9) && ((etSendNumber.length())>=9))
					{
						try{
							MainSms.dbConnect = new MySQLiteHelper(getActivity());
							OtherUserClass user=new OtherUserClass();
							user.setName(etNameOtherUser.getText().toString());
							user.setRcvnumber(etRcvNumber.getText().toString());
							user.setSendnumber(etSendNumber.getText().toString());
							int resultOnInsertOtherUser = MainSms.dbConnect.onInsertOtherUser(user);
							if(resultOnInsertOtherUser<=0){
								toast = Toast.makeText(getActivity(), "Something went wrong!!!",  Toast.LENGTH_SHORT );
								toast.show();
							}else if(resultOnInsertOtherUser==1){
								toast = Toast.makeText(getActivity(), "Profile added with success!",  Toast.LENGTH_SHORT );
								toast.show();
							}
						}catch(Exception e){
							System.out.println(e.getMessage());
						}
					}else{
						toast = Toast.makeText(getActivity(), "Please check the recieve/send number!",  Toast.LENGTH_SHORT );
						toast.show();
					}
				}else{
					toast = Toast.makeText(getActivity(), "Please fill out all required fields!",  Toast.LENGTH_SHORT );
					toast.show();
				}
				break;
			}
		}
	}
}

package app.perez.singlesms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import app.perez.support.ListUsersAdapter;
import app.perez.support.MySQLiteHelper;
import app.perez.support.OtherUserClass;

public class ListUsers extends Fragment {

	static List<OtherUserClass> ListOfOtherUsers = new ArrayList<OtherUserClass>();
	static Activity context;
	static public OtherUserClass OTHERUSER;
	private static ListView lvUsers;
	static Context cn;
	public ListUsers() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		MainSms.actionBar.setDisplayHomeAsUpEnabled(false);
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_users, container, false);
		context = getActivity();
		 cn = getActivity();
		System.out.println("onCreateView() ListUsersx");
		MainSms.actionBar.setTitle("DuoSMS");
	
		ListOfOtherUsers = MainSms.dbConnect.getAllOtherUsers();

		lvUsers = (ListView) rootView.findViewById(R.id.lv_Users);
		List<OtherUserClass> x = new ArrayList<OtherUserClass>();
		x = ListOfOtherUsers;
		ListUsersAdapter adapterListUsers = new ListUsersAdapter(context,
				R.layout.row_users_list, x);
		lvUsers.setAdapter(adapterListUsers);

		lvUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				if (id > -1) {
					
					pos = (int) id;
					id = -1;
					startFragmentUser(pos);
				}
			}

		});
		return rootView;
	}


	static public void startFragmentUser(int pos) {
		// TODO Auto-generated method stub
		System.out.println("startFragmentUser");

		FragmentActivity activityF = (FragmentActivity) context;

		FragmentManager manager = activityF.getSupportFragmentManager();
		PlaceholderFragment myFragment = (PlaceholderFragment) manager
				.findFragmentByTag("SMS_LIST_TAG");

		FragmentTransaction transaction = manager.beginTransaction();
		if (myFragment != null) {
			transaction.remove(myFragment);
		}
		// System.out.println("onNavigationItemSelected-2+");
		// System.out.println("agr0= " + pos + " arg1= " + arg1
		// + " itemList=" + itemList.get(pos).toString()
		// + " ListOfOtherUsers="
		// + ListOfOtherUsers.get(pos).toString() + "id= "
		// + ListOfOtherUsers.get(pos).getId());

		OTHERUSER = new OtherUserClass();
		OTHERUSER.setId(ListOfOtherUsers.get(pos).getId());
		OTHERUSER.setName(ListOfOtherUsers.get(pos).getName());
		OTHERUSER.setRcvnumber(ListOfOtherUsers.get(pos).getRcvnumber());
		OTHERUSER.setSendnumber(ListOfOtherUsers.get(pos).getSendnumber());

		PlaceholderFragment newFragment = new PlaceholderFragment();

		// Replace whatever is in the fragment_container view
		// with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.container, newFragment, "SMS_LIST_TAG");
		transaction.addToBackStack("SMS_LIST_TAG");
		// Commit the transaction
		
		transaction.commit();

		lastUserSmsReaded(OTHERUSER.getId());
		// adapterActionBar.notifyDataSetChanged();
	}

	static public void refreshList() {
	
		ListOfOtherUsers = MainSms.dbConnect.getAllOtherUsers();
	

		ListUsersAdapter adapterListUsers = new ListUsersAdapter(context,
				R.layout.row_users_list, ListOfOtherUsers);
		lvUsers.setAdapter(adapterListUsers);

		lvUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				if (id > -1) {
					pos = (int) id;
					id = -1;
					startFragmentUser(pos);
				}
			}

		});
	}

	public static void lastUserSmsReaded(int userID) {
	
		MainSms.dbConnect.setSmsToRead(userID, 0);
	
	}
	
	static void cancelNotifictgations(int idUser) {
		try {
			NotificationManager nMgr = (NotificationManager) context
					.getBaseContext().getSystemService(
							Context.NOTIFICATION_SERVICE);
			nMgr.cancel(idUser);
			lastUserSmsReaded(idUser);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	    inflater.inflate(R.menu.menu_list_profiles, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
}

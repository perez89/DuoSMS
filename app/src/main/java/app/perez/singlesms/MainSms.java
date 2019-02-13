package app.perez.singlesms;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import app.perez.support.DeleteThreadDialog;
import app.perez.support.MySQLiteHelper;
import app.perez.support.OtherUserClass;
import app.perez.support.PreferencesFrag;

public class MainSms extends AppCompatActivity implements OnItemClickListener {
	public static int id = -1;
	public static AdRequest adRequest;
	public static String android_id;
	public static AdView adView = null;
	public static MySQLiteHelper dbConnect = null;
	private static List<OtherUserClass> ListOfOtherUsers = new ArrayList<OtherUserClass>();
	TitleNavigationAdapter adapterActionBar;
	public static ActionBar actionBar;
	public static String current_fragment = "";
	static public OtherUserClass OTHERUSER;
	SharedPreferences pref;
	int themeNumb;
	public static Activity activity;

	ArrayList<String> itemList = new ArrayList<String>();

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		

		super.onResume();
		android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);

		hideSoftKeyboard();
		System.out.println("onResume - mainsms");
		dbConnect = new MySQLiteHelper(this);
		ListOfOtherUsers = dbConnect.getAllOtherUsers();
		adView.resume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("onPause - mainsms");
		super.onPause();
		if (adView != null)
			adView.pause();
		if (dbConnect != null) {
			dbConnect.close();
		}
	}

	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null)
			adView.destroy();
		if (dbConnect != null) {
			dbConnect.close();
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Duo SMS onCreate");
		hideSoftKeyboard();
		android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		System.out.println("2-Duo SMS onCreate");
		dbConnect = new MySQLiteHelper(this);
		System.out.println("3-Duo SMS onCreate");
		ListOfOtherUsers = dbConnect.getAllOtherUsers();
		System.out.println("4-Duo SMS onCreate");
		setContentView(R.layout.activity_main_sms);
		actionBar = getSupportActionBar();
		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("DuoSMS");

		// Spinner title navigation data

		activity = this;
		// this.findViewById(R.layout.activity_main_sms).setBackground(getResources().getDrawable(R.drawable.bg_gradiant_list_sms_5));
		// Set the color
		pref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		themeNumb = pref.getInt("theme", 1);
		changeBG(themeNumb);

		current_fragment = "USERS_LIST_TAG";
		adView = (AdView) this.findViewById(R.id.adViewX);
		adRequest = new AdRequest.Builder()

		.addTestDevice(android_id).build();
		adView.loadAd(MainSms.adRequest);

		// if (ListOfOtherUsers.size() < 1) {
		// if (savedInstanceState == null) {
		//
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container, new welcome())
		// .addToBackStack("EMPTY_TAG").commit();
		// }
		// } else {
		FragmentActivity activityF;
		FragmentManager manager;
		FragmentTransaction transaction;

		activityF = (FragmentActivity) activity;

		manager = activityF.getSupportFragmentManager();
		ListUsers myFragment = (ListUsers) manager
				.findFragmentByTag("USERS_LIST_TAG");

		transaction = manager.beginTransaction();
		if (myFragment != null) {
			transaction.remove(myFragment);
		}

		ListUsers newFragment = new ListUsers();
		// Replace whatever is in the fragment_container view
		// with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack("USERS_LIST_TAG");
		// Commit the transaction

		transaction.commit();

		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			id = extras.getInt("item_id", -1);
			if (id > -1) {
				for (int i = 0; i < ListOfOtherUsers.size(); i++)
					if (ListOfOtherUsers.get(i).getId() == id) {

						activityF = (FragmentActivity) activity;

						manager = activityF.getSupportFragmentManager();
						PlaceholderFragment myFragmentx = (PlaceholderFragment) manager
								.findFragmentByTag("SMS_LIST_TAG");

						transaction = manager.beginTransaction();
						if (myFragmentx != null) {
							transaction.remove(myFragmentx);
						}
						// System.out.println("onNavigationItemSelected-2+");
						// System.out.println("agr0= " + pos + " arg1= " +
						// arg1
						// + " itemList=" + itemList.get(pos).toString()
						// + " ListOfOtherUsers="
						// + ListOfOtherUsers.get(pos).toString() + "id= "
						// + ListOfOtherUsers.get(pos).getId());

						ListUsers.OTHERUSER = new OtherUserClass();
						ListUsers.OTHERUSER.setId(ListOfOtherUsers.get(i)
								.getId());
						ListUsers.OTHERUSER.setName(ListOfOtherUsers.get(i)
								.getName());
						ListUsers.OTHERUSER.setRcvnumber(ListOfOtherUsers
								.get(i).getRcvnumber());
						ListUsers.OTHERUSER.setSendnumber(ListOfOtherUsers.get(
								i).getSendnumber());

						PlaceholderFragment newFragmentx = new PlaceholderFragment();

						// Replace whatever is in the fragment_container
						// view
						// with this
						// fragment,
						// and add the transaction to the back stack
						transaction.replace(R.id.container, newFragmentx,
								"SMS_LIST_TAG");
						transaction.addToBackStack("SMS_LIST_TAG");
						// Commit the transaction

						transaction.commit();

						lastUserSmsReaded(ListUsers.OTHERUSER.getId());
						break;
					}
			}
			System.out.println(id);
		}
		// }
	}

	@Override
	public void onBackPressed() {

		// if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
		//
		// ListUsers.OTHERUSER = null;
		// }
		try {
			super.onBackPressed();

			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

				ListUsers.OTHERUSER = null;
				finish();
				return;

			}
		} catch (Exception e) {
		}
		//
		// if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
		// System.out.println("CABUM-0");
		// Fragment fragmentBeforeBackPress = getCurrentFragment();
		// if (fragmentBeforeBackPress != null) {
		// System.out.println("CABUM-1" +fragmentBeforeBackPress.getTag());
		// if (fragmentBeforeBackPress.getTag().equals("USERS_LIST_TAG")) {
		// System.out.println("CABUM-2");
		// ListUsers.OTHERUSER = null;
		// finish();
		//
		// }
		// }
		// }
		// actionBar.setDisplayHomeAsUpEnabled(false);
		//
		// ListUsers.refreshList();
		//
		// super.onBackPressed();
		// if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
		//
		// Fragment fragmentAfterBackPress = getCurrentFragment();
		//
		// if (fragmentAfterBackPress.getTag().equals("SMS_LIST_TAG")) {
		//
		// MainSms.actionBar.setTitle(ListUsers.OTHERUSER.getName());
		// } else {
		//
		// dbConnect = new MySQLiteHelper(getBaseContext());
		// ListOfOtherUsers = dbConnect.getAllOtherUsers();
		// dbConnect.close();
		// if (fragmentAfterBackPress.getTag().equals("EMPTY_TAG")
		// && ListOfOtherUsers.size() > 0) {
		// FragmentActivity activityF;
		// FragmentManager manager;
		// FragmentTransaction transaction;
		//
		// activityF = (FragmentActivity) activity;
		//
		// manager = activityF.getSupportFragmentManager();
		// ListUsers myFragment = (ListUsers) manager
		// .findFragmentByTag("USERS_LIST_TAG");
		//
		// transaction = manager.beginTransaction();
		// if (myFragment != null) {
		// transaction.remove(myFragment);
		// }
		//
		// ListUsers newFragment = new ListUsers();
		// // Replace whatever is in the fragment_container view
		// // with this
		// // fragment,
		// // and add the transaction to the back stack
		// transaction.replace(R.id.container, newFragment);
		// transaction.addToBackStack("USERS_LIST_TAG");
		// // Commit the transaction
		//
		// transaction.commit();
		// }
		// }
		//
		// }
		// if (current_fragment.equals("SMS_LIST_TAG")
		// || current_fragment.equals("TAG_WELCOME")) {
		// // super.onBackPressed();
		// finish();
		// } else {
		// be_or_not_be_welcome();
		// }
	}

	private Fragment getCurrentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		String fragmentTag = fragmentManager.getBackStackEntryAt(
				fragmentManager.getBackStackEntryCount() - 1).getName();
		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentByTag(fragmentTag);
		return currentFragment;
	}

	public void populatteSpinner() {
		// TODO Auto-generated method stub
		itemList = new ArrayList<String>();

		if (ListOfOtherUsers.size() > 0) {
			// Enabling Spinner dropdown navigation
			for (int i = 0; i < ListOfOtherUsers.size(); i++)
				itemList.add(ListOfOtherUsers.get(i).toString());

			// adapterActionBar = new TitleNavigationAdapter(this,
			// android.R.layout.simple_spinner_dropdown_item,
			// ListOfOtherUsers);

			// ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this,
			// R.layout.row_spinner_other_user, R.id.tv_spinner_other_user,
			// itemList);
			// actionBar.setListNavigationCallbacks(adapterActionBar, this);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_list_profiles, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ListOfOtherUsers = dbConnect.getAllOtherUsers();

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			hideSoftKeyboard();
			// NavUtils.navigateUpFromSameTask(this);
			onBackPressed();
			return true;
		case R.id.action_call:

			if (ListUsers.OTHERUSER != null) {
				try {
					String uri = "tel:"
							+ ListUsers.OTHERUSER.getSendnumber().trim();
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse(uri));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				Toast toast = Toast.makeText(getBaseContext(),
						"No profile selected!", Toast.LENGTH_SHORT);
				toast.show();
			}
			return true;
		case R.id.action_add_new_other_user:
			if (ListUsers.ListOfOtherUsers.size() <= 300) {

				AddOtherUser myFragment = (AddOtherUser) getSupportFragmentManager()
						.findFragmentByTag("TAG_ADD_USER");
				if (myFragment != null) {

					// add your code here

					// getSupportFragmentManager().beginTransaction().replace(R.id.container,
					// new AddOtherUser()).addToBackStack(null).commit();

					// Create new fragment and transaction

					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();

					// Replace whatever is in the fragment_container view with
					// this
					// fragment,
					// and add the transaction to the back stack
					transaction.replace(R.id.container, myFragment,
							"TAG_ADD_USER");

					actionBar
							.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

					// Commit the transaction
					current_fragment = "TAG_ADD_USER";
					transaction.commit();
				} else {

					AddOtherUser newFragment = new AddOtherUser();
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();

					// Replace whatever is in the fragment_container view with
					// this
					// fragment,
					// and add the transaction to the back stack
					transaction.replace(R.id.container, newFragment,
							"TAG_ADD_USER");
					transaction.addToBackStack("TAG_ADD_USER");
					actionBar
							.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					actionBar.setDisplayShowTitleEnabled(false);
					// Commit the transaction
					current_fragment = "TAG_ADD_USER";
					transaction.commit();
				}
			} else {
				Toast toast = Toast
						.makeText(
								getBaseContext(),
								"Free version - you are only allowed to have 300 profiles",
								Toast.LENGTH_SHORT);
				toast.show();
			}

			return true;
		case R.id.action_remove_other_user:

			if (ListOfOtherUsers.size() > 0 && ListUsers.OTHERUSER != null) {
				RemoveOtherUser myFragment1 = (RemoveOtherUser) getSupportFragmentManager()
						.findFragmentByTag("TAG_REMOVE_USER");
				if (myFragment1 != null) {

					// add your code here

					// getSupportFragmentManager().beginTransaction().replace(R.id.container,
					// new AddOtherUser()).addToBackStack(null).commit();

					// Create new fragment and transaction

					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();

					// Replace whatever is in the fragment_container view with
					// this
					// fragment,
					// and add the transaction to the back stack
					transaction.replace(R.id.container, myFragment1,
							"TAG_REMOVE_USER");

					actionBar
							.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

					// Commit the transaction
					current_fragment = "TAG_REMOVE_USER";
					transaction.commit();
				} else {

					RemoveOtherUser newFragment = new RemoveOtherUser();
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();

					// Replace whatever is in the fragment_container view with
					// this
					// fragment,
					// and add the transaction to the back stack
					transaction.replace(R.id.container, newFragment,
							"TAG_REMOVE_USER");
					transaction.addToBackStack("TAG_REMOVE_USER");
					actionBar
							.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					actionBar.setDisplayShowTitleEnabled(false);
					// Commit the transaction
					current_fragment = "TAG_REMOVE_USER";
					transaction.commit();
				}
			} else {
				Toast toast = Toast.makeText(getBaseContext(),
						"No profile selected!", Toast.LENGTH_SHORT);
				toast.show();
			}
			return true;
		case R.id.action_preferences:
			PreferencesFrag myFragment0 = (PreferencesFrag) getSupportFragmentManager()
					.findFragmentByTag("TAG_PREFS");
			if (myFragment0 != null) {

				// add your code here

				// getSupportFragmentManager().beginTransaction().replace(R.id.container,
				// new AddOtherUser()).addToBackStack(null).commit();

				// Create new fragment and transaction

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.container, myFragment0, "TAG_PREFS");

				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

				// Commit the transaction
				current_fragment = "TAG_PREFS";
				transaction.commit();
			} else {

				PreferencesFrag newFragment = new PreferencesFrag();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.container, newFragment, "TAG_PREFS");
				transaction.addToBackStack("TAG_PREFS");
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				actionBar.setDisplayShowTitleEnabled(false);
				// Commit the transaction
				current_fragment = "TAG_PREFS";
				transaction.commit();
			}

			return true;
		case R.id.action_about:
			AboutUs myFragment1 = (AboutUs) getSupportFragmentManager()
					.findFragmentByTag("TAG_ABOUT_US");
			if (myFragment1 != null) {

				// add your code here

				// getSupportFragmentManager().beginTransaction().replace(R.id.container,
				// new AddOtherUser()).addToBackStack(null).commit();

				// Create new fragment and transaction

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction
						.replace(R.id.container, myFragment1, "TAG_ABOUT_US");

				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

				// Commit the transaction
				current_fragment = "TAG_ABOUT_US";
				transaction.commit();
			} else {

				AboutUs newFragment = new AboutUs();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction
						.replace(R.id.container, newFragment, "TAG_ABOUT_US");
				transaction.addToBackStack("TAG_ABOUT_US");
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				actionBar.setDisplayShowTitleEnabled(false);
				// Commit the transaction
				current_fragment = "TAG_ABOUT_US";
				transaction.commit();
			}
			return true;
		case R.id.action_report:
			ReportBug myFragment3 = (ReportBug) getSupportFragmentManager()
					.findFragmentByTag("TAG_REPORT");
			if (myFragment3 != null) {

				// add your code here

				// getSupportFragmentManager().beginTransaction().replace(R.id.container,
				// new AddOtherUser()).addToBackStack(null).commit();

				// Create new fragment and transaction

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.container, myFragment3, "TAG_REPORT");

				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

				// Commit the transaction
				current_fragment = "TAG_REPORT";
				transaction.commit();
			} else {

				ReportBug newFragment = new ReportBug();
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this
				// fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.container, newFragment, "TAG_REPORT");
				transaction.addToBackStack("TAG_REPORT");
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				actionBar.setDisplayShowTitleEnabled(false);
				// Commit the transaction
				current_fragment = "TAG_REPORT";
				transaction.commit();
			}
			return true;
		case R.id.action_delete_thread:
			DeleteThreadDialog df = new DeleteThreadDialog();
			FragmentManager fm = getSupportFragmentManager();

			df.show(fm, "DIALOG_DELETE_THREAD");
			// System.out.println("position= " +position + " id= " +id);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	public static void lastUserSmsReaded(int userID) {

		dbConnect.setSmsToRead(userID, 0);

	}

	static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		public Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		public static boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	private void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	@SuppressWarnings("deprecation")
	static public void changeBG(int themeId) {

		String strThemeID = "bg_gradiant_list_sms_1";
		strThemeID = strThemeID + String.valueOf(themeId);

		int themeID = activity.getResources().getIdentifier(strThemeID,
				"drawable", activity.getPackageName());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

			activity.findViewById(android.R.id.content)
					.getRootView()
					.setBackgroundDrawable(
							activity.getResources().getDrawable(themeID));
		} else {
			activity.findViewById(android.R.id.content).setBackgroundResource(
					themeID);
		}

	}

}

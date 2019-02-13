package app.perez.singlesms;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;

public class SMSservice extends IntentService {

	private IncomingSms mSMSreceiver;
	private IntentFilter mIntentFilter;

	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public SMSservice() {
		super("HelloSMSservice");
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// SMS event receiver
		synchronized (this) {
			mSMSreceiver = new IncomingSms();
			mIntentFilter = new IntentFilter();
			mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
			registerReceiver(mSMSreceiver, mIntentFilter);
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		// Unregister the SMS receiver
		unregisterReceiver(mSMSreceiver);
	}
}
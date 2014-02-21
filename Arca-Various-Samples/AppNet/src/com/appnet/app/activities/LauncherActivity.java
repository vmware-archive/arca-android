package com.appnet.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.appnet.app.R;
import com.appnet.app.accounts.AccountsManager;
import com.appnet.app.accounts.AppNetAuthenticator;
import com.appnet.app.providers.AppNetContentProvider;

public class LauncherActivity extends Activity {

	private static final int FIVE_MINUTES = 5 * 60;
	
	private static final int LAUNCH_MSG = 100;
	private static final int LAUNCH_DURATION = 2000;
	
	private final LaunchHandler mHandler = new LaunchHandler(this);

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		setupAccount(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mHandler.sendEmptyMessageDelayed(LAUNCH_MSG, LAUNCH_DURATION);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeMessages(LAUNCH_MSG);
	}

	public void launch() {
		PostListActivity.newInstance(this);
		finish();
	}
	
	private static void setupAccount(final Context context) {
		final String authority = AppNetContentProvider.AUTHORITY;
		final AccountsManager manager = new AccountsManager(context, AppNetAuthenticator.ACCOUNT_TYPE);
		final boolean hasAccount = manager.hasAccount(AppNetAuthenticator.ACCOUNT_NAME);
		
		if (!hasAccount) {
			final Account account = manager.createAccount(AppNetAuthenticator.ACCOUNT_NAME);
			ContentResolver.setIsSyncable(account, authority, 1);
			ContentResolver.setSyncAutomatically(account, authority, true);
			ContentResolver.addPeriodicSync(account, authority, new Bundle(), FIVE_MINUTES);
			manager.addAccount(account, null);
			
		} else {
			final Account account = manager.getAccount(AppNetAuthenticator.ACCOUNT_NAME);
			ContentResolver.requestSync(account, authority, new Bundle());
		}
	}
	
	private static final class LaunchHandler extends Handler {
		
		private final LauncherActivity mActivity;
		
		public LaunchHandler(final LauncherActivity activity) {
			mActivity = activity;
		}
		
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == LAUNCH_MSG) {
				mActivity.launch();
			}
		}
	}
}
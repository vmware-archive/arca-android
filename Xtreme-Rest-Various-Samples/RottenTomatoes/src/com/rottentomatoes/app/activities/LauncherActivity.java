package com.rottentomatoes.app.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.accounts.AccountsManager;
import com.rottentomatoes.app.accounts.RottenTomatoesAuthenticator;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;

public class LauncherActivity extends Activity {

	private static final int LAUNCH_MSG = 100;
	private static final int LAUNCH_DURATION = 2000;
	
	private final LaunchHandler mHandler = new LaunchHandler(this);

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		setupAccount();
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
		MovieListActivity.newInstance(this);
		finish();
	}
	
	private void setupAccount() {
		final String authority = RottenTomatoesContentProvider.AUTHORITY;
		final AccountsManager manager = new AccountsManager(this, RottenTomatoesAuthenticator.ACCOUNT_TYPE);
		final boolean hasAccount = manager.hasAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
		
		if (!hasAccount) {
			final Account account = manager.createAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
			ContentResolver.setIsSyncable(account, authority, 1);
			ContentResolver.setSyncAutomatically(account, authority, true);
			manager.addAccount(account, null);
			
		} else {
			final Account account = manager.getAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
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
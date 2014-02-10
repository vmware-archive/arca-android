package com.rottentomatoes.app.accounts;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;

public class AccountsUtil {

	private static final int ONE_DAY_IN_SECONDS = 60 * 60 * 24;

	public static void setupAccount(final Context context) {
		final String authority = RottenTomatoesContentProvider.AUTHORITY;
		final AccountsManager manager = new AccountsManager(context, RottenTomatoesAuthenticator.ACCOUNT_TYPE);
		final boolean hasAccount = manager.hasAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
		
		if (!hasAccount) {
			final Account account = manager.createAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
			ContentResolver.setIsSyncable(account, authority, 1);
			ContentResolver.setSyncAutomatically(account, authority, true);
			ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, ONE_DAY_IN_SECONDS);
			manager.addAccount(account, null);
			
		} else {
			final Account account = manager.getAccount(RottenTomatoesAuthenticator.ACCOUNT_NAME);
			ContentResolver.requestSync(account, authority, new Bundle());
		}
	}
}

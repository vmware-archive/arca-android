package com.rottentomatoes.app.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.os.Bundle;

public class AccountsManager {
	
	private final String mAccountType;
	private final AccountManager mAccountManager;

	public AccountsManager(final Context context, final String accountType) {
		mAccountManager = AccountManager.get(context);
		mAccountType = accountType;
	}

	public boolean hasAccounts() {
		final Account[] accounts = getAccounts();
		return accounts != null && accounts.length > 0;
	}

	public boolean hasAccount(final String accountName) {
		final Account account = getAccount(accountName);
		return account != null;
	}
	
	public boolean addAccount(final Account account, final Bundle userData) {
		return mAccountManager.addAccountExplicitly(account, null, userData);
	}
	
	public boolean addAccount(final String accountName, final Bundle userData) {
		final Account account = new Account(accountName, mAccountType);
		return mAccountManager.addAccountExplicitly(account, null, userData);
	}
	
	public Account createAccount(final String accountName) {
		final Account account = new Account(accountName, mAccountType);
		return account;
	}

	public Account[] getAccounts() {
		return mAccountManager.getAccountsByType(mAccountType);
	}

	public Account getAccount(final String accountName) {
		final Account[] accounts = getAccounts();
		if (accounts == null) return null;
		
		for (final Account account : accounts) {
			if (account.name.equals(accountName)) {
				return account;
			}
		}
		return null;
	}
	
	public String getAccountData(final String accountName, final String key) {
		final Account account = getAccount(accountName);
		if (account != null) {
			return mAccountManager.getUserData(account, key);
		} else {
			return null;
		}
	}

	public void deleteAccounts() {
		deleteAccounts(null);
	}

	public void deleteAccounts(final AccountManagerCallback<Boolean> callback) {
		final Account[] accounts = getAccounts();
		if (accounts == null) return;
		
		for (final Account account : accounts) {
			mAccountManager.removeAccount(account, callback, null);
		}
	}
	
	public void deleteAccount(final String accountName) {
		deleteAccount(accountName, null);
	}

	public void deleteAccount(final String accountName, final AccountManagerCallback<Boolean> callback) {
		final Account account = getAccount(accountName);
		if (account != null) {
			mAccountManager.removeAccount(account, callback, null);
		}
	}
}

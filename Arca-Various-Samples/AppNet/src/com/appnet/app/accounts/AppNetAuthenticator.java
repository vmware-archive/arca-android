package com.appnet.app.accounts;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appnet.app.activities.LauncherActivity;

public class AppNetAuthenticator extends AbstractAccountAuthenticator {

	public static final String ACCOUNT_NAME = "App.net";
	public static final String ACCOUNT_TYPE = "com.appnet";
	public static final String AUTH_TOKEN_TYPE = "com.appnet.token";

	private final Context mContext;

	public AppNetAuthenticator(final Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Bundle addAccount(final AccountAuthenticatorResponse response, final String accountType, final String authTokenType, final String[] requiredFeatures, final Bundle options) throws NetworkErrorException {
		final Intent intent = new Intent(mContext, LauncherActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public Bundle confirmCredentials(final AccountAuthenticatorResponse response, final Account account, final Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle editProperties(final AccountAuthenticatorResponse response, final String accountType) {
		return null;
	}

	@Override
	public Bundle getAuthToken(final AccountAuthenticatorResponse response, final Account account, final String authTokenType, final Bundle options) throws NetworkErrorException {

		if (!authTokenType.equals(AUTH_TOKEN_TYPE)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE, "Invalid Auth Token Type");
			return result;
		}

		final AccountManager am = AccountManager.get(mContext);
		final String password = am.getPassword(account);
		if (password != null) {
			// TODO get auth token form the server
			// probably shouldn't store passwords though
		}

		return addAccount(response, account.type, authTokenType, null, options);
	}

	@Override
	public String getAuthTokenLabel(final String authTokenType) {
		return null;
	}

	@Override
	public Bundle hasFeatures(final AccountAuthenticatorResponse response, final Account account, final String[] features) throws NetworkErrorException {
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
		return result;
	}

	@Override
	public Bundle updateCredentials(final AccountAuthenticatorResponse response, final Account account, final String authTokenType, final Bundle options) throws NetworkErrorException {
		return null;
	}
}

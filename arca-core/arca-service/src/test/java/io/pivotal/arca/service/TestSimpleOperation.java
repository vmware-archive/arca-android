package io.pivotal.arca.service;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class TestSimpleOperation extends SimpleOperation {

    private final ContentValues[] mData;
    private final Exception mNetworkingException;
    private final Exception mProcessingException;

    public TestSimpleOperation(final Uri uri) {
        this(uri, null, null, null);
    }

    public TestSimpleOperation(final ContentValues[] data) {
        this(Uri.EMPTY, data, null, null);
    }

    public TestSimpleOperation(final Exception networking, final Exception processing) {
        this(Uri.EMPTY, null, networking, processing);
    }

    private TestSimpleOperation(final Uri uri, final ContentValues[] data, final Exception networking, final Exception processing) {
        super(uri);
        mData = data;
        mNetworkingException = networking;
        mProcessingException = processing;
    }

    @Override
    public ContentValues[] onExecute(final Context context) throws Exception {
        if (mNetworkingException != null) {
            throw mNetworkingException;
        }
        return mData;
    }

    @Override
    public void onPostExecute(final Context context, final ContentValues[] data) throws Exception {
        if (mProcessingException != null) {
            throw mProcessingException;
        } else {
            super.onPostExecute(context, data);
        }
    }
}

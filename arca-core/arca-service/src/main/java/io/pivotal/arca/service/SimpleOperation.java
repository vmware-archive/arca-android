package io.pivotal.arca.service;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;

public abstract class SimpleOperation extends TaskOperation<ContentValues[]> {

    public SimpleOperation(final Uri uri, final Priority priority) {
        super(uri, priority);
    }

    public SimpleOperation(final Uri uri) {
        super(uri);
    }

    public SimpleOperation(final Parcel in) {
        super(in);
    }
}

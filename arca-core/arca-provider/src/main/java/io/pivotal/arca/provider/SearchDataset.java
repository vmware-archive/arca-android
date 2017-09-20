package io.pivotal.arca.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public abstract class SearchDataset extends ContextDataset {

    public abstract Cursor search(String[] args) throws Exception;

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        try {
            return search(selectionArgs);
        } catch (final Exception e) {
            return new MatrixCursor(new String[] { "_id" });
        }
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int bulkInsert(final Uri uri, final ContentValues[] values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}

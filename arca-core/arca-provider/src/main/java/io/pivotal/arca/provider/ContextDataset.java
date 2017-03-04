package io.pivotal.arca.provider;

import android.content.Context;

public abstract class ContextDataset implements Dataset {

    private Context mContext;

    /* package */ void setContext(final Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }
}

package io.pivotal.arca.service.test.mock;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import io.pivotal.arca.service.ServiceError;
import io.pivotal.arca.service.SimpleOperation;
import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;

public class TestSimpleOperation extends SimpleOperation<String> {

    private final String mData;
    private final Exception mNetworkingException;
    private final Exception mProcessingException;

    public TestSimpleOperation(final String data) {
        this(data, null, null);
    }

    public TestSimpleOperation(final Exception networking, final Exception processing) {
        this(null, networking, processing);
    }

    private TestSimpleOperation(final String data, final Exception networking, final Exception processing) {
        super(Uri.EMPTY);
        mData = data;
        mNetworkingException = networking;
        mProcessingException = processing;
    }

    @Override
    public Identifier<?> onCreateIdentifier() {
        return new Identifier<String>("test");
    }

    @Override
    public String onExecuteNetworking(final Context context) throws Exception {

        if (mNetworkingException != null) {
            throw mNetworkingException;
        }

        return mData;
    }

    @Override
    public void onExecuteProcessing(final Context context, final String data) throws Exception {
        if (mProcessingException != null) {
            throw mProcessingException;
        }
    }

    @Override
    public void onSuccess(final Context context, final List<Task<?>> completed) {

    }

    @Override
    public void onFailure(final Context context, final ServiceError error) {

    }
}

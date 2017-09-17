package io.pivotal.arca.dispatcher;

import android.content.ContentProviderOperation;
import android.net.Uri;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Batch extends Request<Integer> {

	private final ArrayList<ContentProviderOperation> mOperations;

	public Batch(final Uri uri) {
		super(uri);
        mOperations = new ArrayList<>();
	}

    public Batch(final Uri uri, final ArrayList<ContentProviderOperation> operations) {
        super(uri);
        mOperations = operations;
    }

    public Batch(final Uri uri, final ContentProviderOperation... operations) {
        super(uri);
        mOperations = new ArrayList<>(Arrays.asList(operations));
    }

	public Batch(final Parcel in) {
		super(in);
        mOperations = in.createTypedArrayList(ContentProviderOperation.CREATOR);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(mOperations);
	}

    public String getAuthority() {
        return getUri().getAuthority();
    }

    public void addOperation(final ContentProviderOperation operation) {
        mOperations.add(operation);
    }

    public void addOperations(final List<ContentProviderOperation> operations) {
        mOperations.addAll(operations);
    }

	public ArrayList<ContentProviderOperation> getOperations() {
		return mOperations;
	}

	public static final Creator<Batch> CREATOR = new Creator<Batch>() {
		@Override
		public Batch createFromParcel(final Parcel in) {
			return new Batch(in);
		}

		@Override
		public Batch[] newArray(final int size) {
			return new Batch[size];
		}
	};
}

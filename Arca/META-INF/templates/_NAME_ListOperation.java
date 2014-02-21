package ${base_package}.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.broadcasts.RestBroadcaster;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.Task;

public class ${name}ListOperation extends Operation {


	public ${name}ListOperation(final Uri uri) {
		super(uri);
	}

	public ${name}ListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		set.add(new ${name}ListTask());
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null, false);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		RestBroadcaster.broadcast(context, getUri(), error.getCode(), error.getMessage());
	}
	
	public static final Parcelable.Creator<${name}ListOperation> CREATOR = new Parcelable.Creator<${name}ListOperation>() {
		@Override
		public ${name}ListOperation createFromParcel(final Parcel in) {
			return new ${name}ListOperation(in);
		}

		@Override
		public ${name}ListOperation[] newArray(final int size) {
			return new ${name}ListOperation[size];
		}
	};

}

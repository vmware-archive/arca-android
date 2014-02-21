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

public class ${name}Operation extends Operation {

	public ${name}Operation(final Uri uri) {
		super(uri);
	}

	public ${name}Operation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		final String id = getUri().getLastPathSegment();
		set.add(new ${name}Task(id));
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
	
	public static final Parcelable.Creator<${name}Operation> CREATOR = new Parcelable.Creator<${name}Operation>() {
		@Override
		public ${name}Operation createFromParcel(final Parcel in) {
			return new ${name}Operation(in);
		}

		@Override
		public ${name}Operation[] newArray(final int size) {
			return new ${name}Operation[size];
		}
	};

}

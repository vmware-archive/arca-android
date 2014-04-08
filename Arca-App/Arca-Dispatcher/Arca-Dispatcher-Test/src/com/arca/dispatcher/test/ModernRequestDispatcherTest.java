/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arca.dispatcher.test;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.test.LoaderTestCase;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import com.arca.dispatcher.Delete;
import com.arca.dispatcher.DeleteListener;
import com.arca.dispatcher.DeleteResult;
import com.arca.dispatcher.Error;
import com.arca.dispatcher.Insert;
import com.arca.dispatcher.InsertListener;
import com.arca.dispatcher.InsertResult;
import com.arca.dispatcher.ModernRequestDispatcher;
import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryListener;
import com.arca.dispatcher.QueryResult;
import com.arca.dispatcher.RequestDispatcher;
import com.arca.dispatcher.RequestExecutor;
import com.arca.dispatcher.RequestExecutor.DefaultRequestExecutor;
import com.arca.dispatcher.Update;
import com.arca.dispatcher.UpdateListener;
import com.arca.dispatcher.UpdateResult;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernRequestDispatcherTest extends LoaderTestCase {

	private static final String AUTHORITY = "com.test";

	private static final Uri TEST_URI = Uri.parse("content://" + AUTHORITY);

	public void testSychronousQuery() {
		final Query query = new Query(TEST_URI);
		final QueryResult response = getDispatcher().execute(query);
		final Cursor result = response.getResult();
		assertNotNull(result);
		result.close();
	}

	public void testAsynchronousQuery() {
		final Query query = new Query(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getDispatcher().execute(query, new QueryListener() {
			@Override
			public void onRequestComplete(final QueryResult result) {
				latch.countDown();
				final Cursor cursor = result.getResult();
				assertNotNull(cursor);
				cursor.close();
			}

			@Override
			public void onRequestReset() {

			}

		});
		latch.assertComplete();
	}

	public void testAsynchronousQueryReset() {
		final Query query = new Query(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getResetDispatcher().execute(query, new QueryListener() {
			@Override
			public void onRequestComplete(final QueryResult result) {

			}

			@Override
			public void onRequestReset() {
				latch.countDown();
			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousQueryError() {
		final Query query = new Query(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getErrorDispatcher().execute(query, new QueryListener() {
			@Override
			public void onRequestComplete(final QueryResult result) {
				latch.countDown();
				assertNotNull(result.getError());
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testSychronousInsert() {
		final ContentValues values = new ContentValues();
		final Insert insert = new Insert(TEST_URI, values);
		final InsertResult response = getDispatcher().execute(insert);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}

	public void testAsynchronousInsert() {
		final ContentValues values = new ContentValues();
		final Insert insert = new Insert(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getDispatcher().execute(insert, new InsertListener() {
			@Override
			public void onRequestComplete(final InsertResult result) {
				latch.countDown();
				final Integer count = result.getResult();
				assertEquals(Integer.valueOf(1), count);
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousInsertReset() {
		final ContentValues values = new ContentValues();
		final Insert insert = new Insert(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getResetDispatcher().execute(insert, new InsertListener() {
			@Override
			public void onRequestComplete(final InsertResult result) {

			}

			@Override
			public void onRequestReset() {
				latch.countDown();
			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousInsertError() {
		final ContentValues values = new ContentValues();
		final Insert insert = new Insert(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getErrorDispatcher().execute(insert, new InsertListener() {
			@Override
			public void onRequestComplete(final InsertResult result) {
				latch.countDown();
				assertNotNull(result.getError());
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testSychronousUpdate() {
		final ContentValues values = new ContentValues();
		final Update update = new Update(TEST_URI, values);
		final UpdateResult response = getDispatcher().execute(update);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}

	public void testAsynchronousUpdate() {
		final ContentValues values = new ContentValues();
		final Update update = new Update(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getDispatcher().execute(update, new UpdateListener() {
			@Override
			public void onRequestComplete(final UpdateResult result) {
				latch.countDown();
				final Integer count = result.getResult();
				assertEquals(Integer.valueOf(1), count);
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousUpdateReset() {
		final ContentValues values = new ContentValues();
		final Update update = new Update(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getResetDispatcher().execute(update, new UpdateListener() {
			@Override
			public void onRequestComplete(final UpdateResult result) {

			}

			@Override
			public void onRequestReset() {
				latch.countDown();
			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousUpdateError() {
		final ContentValues values = new ContentValues();
		final Update update = new Update(TEST_URI, values);
		final AssertionLatch latch = new AssertionLatch(1);
		getErrorDispatcher().execute(update, new UpdateListener() {
			@Override
			public void onRequestComplete(final UpdateResult result) {
				latch.countDown();
				assertNotNull(result.getError());
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testSychronousDelete() {
		final Delete delete = new Delete(TEST_URI);
		final DeleteResult response = getDispatcher().execute(delete);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}

	public void testAsynchronousDelete() {
		final Delete delete = new Delete(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getDispatcher().execute(delete, new DeleteListener() {
			@Override
			public void onRequestComplete(final DeleteResult result) {
				latch.countDown();
				final Integer count = result.getResult();
				assertEquals(Integer.valueOf(1), count);
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousDeleteReset() {
		final Delete delete = new Delete(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getResetDispatcher().execute(delete, new DeleteListener() {
			@Override
			public void onRequestComplete(final DeleteResult result) {

			}

			@Override
			public void onRequestReset() {
				latch.countDown();
			}
		});
		latch.assertComplete();
	}

	public void testAsynchronousDeleteError() {
		final Delete delete = new Delete(TEST_URI);
		final AssertionLatch latch = new AssertionLatch(1);
		getErrorDispatcher().execute(delete, new DeleteListener() {
			@Override
			public void onRequestComplete(final DeleteResult result) {
				latch.countDown();
				assertNotNull(result.getError());
			}

			@Override
			public void onRequestReset() {

			}
		});
		latch.assertComplete();
	}

	private RequestDispatcher getDispatcher() {
		final MockContentResolver resolver = new MockContentResolver();
		resolver.addProvider(AUTHORITY, mProvider);
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, getContext(), mLoaderManager);
	}

	private RequestDispatcher getResetDispatcher() {
		final RequestExecutor executor = new DefaultRequestExecutor(null);
		return new ModernRequestDispatcher(executor, getContext(), mResetLoaderManager);
	}

	private RequestDispatcher getErrorDispatcher() {
		final RequestExecutor executor = new ErrorRequestExecutor();
		return new ModernRequestDispatcher(executor, getContext(), mLoaderManager);
	}

	private final MockContentProvider mProvider = new MockContentProvider(getContext()) {
		@Override
		public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
			return new MatrixCursor(new String[] {});
		};

		@Override
		public int bulkInsert(final Uri uri, final ContentValues[] values) {
			return values.length;
		};

		@Override
		public Uri insert(final Uri uri, final ContentValues values) {
			return uri;
		};

		@Override
		public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
			return 1;
		};

		@Override
		public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
			return 1;
		};
	};

	private final LoaderManager mLoaderManager = new LoaderManager() {

		@Override
		public <D> Loader<D> restartLoader(final int id, final Bundle bundle, final LoaderCallbacks<D> callbacks) {
			final Loader<D> loader = callbacks.onCreateLoader(id, bundle);
			final D result = getLoaderResultSynchronously(loader);
			callbacks.onLoadFinished(loader, result);
			return loader;
		}

		@Override
		public <D> Loader<D> initLoader(final int id, final Bundle bundle, final LoaderCallbacks<D> callbacks) {
			return null;
		}

		@Override
		public <D> Loader<D> getLoader(final int id) {
			return null;
		}

		@Override
		public void destroyLoader(final int id) {

		}

		@Override
		public void dump(final String prefix, final FileDescriptor fd, final PrintWriter writer, final String[] args) {

		}
	};

	private final LoaderManager mResetLoaderManager = new LoaderManager() {

		@Override
		public <D> Loader<D> restartLoader(final int id, final Bundle bundle, final LoaderCallbacks<D> callbacks) {
			final Loader<D> loader = callbacks.onCreateLoader(id, bundle);
			callbacks.onLoaderReset(loader);
			return loader;
		}

		@Override
		public <D> Loader<D> initLoader(final int id, final Bundle bundle, final LoaderCallbacks<D> callbacks) {
			return null;
		}

		@Override
		public <D> Loader<D> getLoader(final int id) {
			return null;
		}

		@Override
		public void destroyLoader(final int id) {

		}

		@Override
		public void dump(final String prefix, final FileDescriptor fd, final PrintWriter writer, final String[] args) {

		}
	};

	public class ErrorRequestExecutor implements RequestExecutor {

		@Override
		public QueryResult execute(final Query request) {
			final Error error = new Error(0, null);
			return new QueryResult(error);
		}

		@Override
		public UpdateResult execute(final Update request) {
			final Error error = new Error(0, null);
			return new UpdateResult(error);
		}

		@Override
		public InsertResult execute(final Insert request) {
			final Error error = new Error(0, null);
			return new InsertResult(error);
		}

		@Override
		public DeleteResult execute(final Delete request) {
			final Error error = new Error(0, null);
			return new DeleteResult(error);
		}

	}

	public class AssertionLatch extends CountDownLatch {

		public AssertionLatch(final int count) {
			super(count);
		}

		@Override
		public void countDown() {
			final long count = getCount();
			if (count == 0) {
				Assert.fail("This latch has already finished.");
			} else {
				super.countDown();
			}
		}

		public void assertComplete() {
			try {
				Assert.assertTrue(await(0, TimeUnit.SECONDS));
			} catch (final InterruptedException e) {
				Assert.fail();
			}
		}
	}
}

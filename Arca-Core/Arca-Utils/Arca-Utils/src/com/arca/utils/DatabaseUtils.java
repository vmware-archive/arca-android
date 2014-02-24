package com.arca.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


public class DatabaseUtils {

	private static final String SQLITE_VERSION = "sqlite_version";
	private static final String SQLITE_MEMORY = ":memory:";

	public static String checkSQLiteVersion() {
		Cursor cursor = null;
		
		try {
			
			final SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(SQLITE_MEMORY, null); 
			cursor = database.rawQuery("select sqlite_version() AS " + SQLITE_VERSION, null);

			String version = "";
			while (cursor.moveToNext()) {
			   version += cursor.getString(cursor.getColumnIndex(SQLITE_VERSION));
			}
			
			Logger.i("SQLite Version: %s", version);
			
			return version;
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public static String copyDBToExternalStorage(final String packageName, final String databaseName) {

		final File sdCard = Environment.getExternalStorageDirectory();
		final File data = Environment.getDataDirectory();

		if (sdCard.canWrite()) {
			final File sourceDB = new File(data, "data/" + packageName + "/databases/" + databaseName);
			final File destinationDB = new File(sdCard, databaseName);

			if (!sourceDB.exists()) {
				Logger.ex(new IllegalArgumentException("Database does not exist"));
				return null;
			}
			
			try {
				final String destination = transferFile(sourceDB, destinationDB);
				
				Logger.i("SQLite Database Copied: %s", destination);
				
				return destination;
				
			} catch (final Exception e) {
				Logger.ex(e);
			}
		} else {
			Logger.ex(new IllegalStateException("You don't have permission to write to the sd card."));
		}
		
		return null;
	}

	private static String transferFile(final File srcFile, final File dstFile) throws IOException {
		
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		FileChannel srcChannel = null;
		FileChannel dstChannel = null;
		
		try {
			
			inStream = new FileInputStream(srcFile);
			outStream = new FileOutputStream(dstFile);
			srcChannel = inStream.getChannel();
			dstChannel = outStream.getChannel();
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
			
			return dstFile.getPath(); 

		} finally {
			if (inStream != null) inStream.close();
			if (outStream != null) outStream.close();
			if (srcChannel != null) srcChannel.close();
			if (dstChannel != null) dstChannel.close();
		}
	}

}

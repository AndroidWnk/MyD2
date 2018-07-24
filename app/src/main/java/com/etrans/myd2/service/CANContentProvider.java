package com.etrans.myd2.service;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.etrans.myd2.dbhelper.CANContentConst;
import com.etrans.myd2.dbhelper.VehicleStateDBHelper;


public class CANContentProvider extends ContentProvider
{
  private static final int BASE_VEHICLE_STATE = 2;
  private static final int HABIT_VEHICLE_RECORD = 3;
  private static final int STA_VEHICLE_STATE = 1;
  private static final UriMatcher Urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
  private VehicleStateDBHelper dbHelper;
  private ContentResolver resolver;
  private Context context;

  static
  {
    Urimatcher.addURI("com.etrans.myd2.can", CANContentConst.VehicleStatisticsStates.URI_PATH, 1);
    Urimatcher.addURI("com.etrans.myd2.can", CANContentConst.VehicleBasicStates.URI_PATH, 2);
    Urimatcher.addURI("com.etrans.myd2.can", CANContentConst.VehicleDrivingRecord.URI_PATH, 3);
  }

  private String getTableName(Uri paramUri)
  {
    switch (Urimatcher.match(paramUri))
    {
    case STA_VEHICLE_STATE:
      return VehicleStateDBHelper.TABLE_NAME_STA;
    case BASE_VEHICLE_STATE:
      return VehicleStateDBHelper.TABLE_NAME_BASE;
    case HABIT_VEHICLE_RECORD:
      return VehicleStateDBHelper.TABLE_NAME_HABIT;
      default:
        return null;
    }
  }

  @Override
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {

    SQLiteDatabase localSQLiteDatabase = dbHelper.getWritableDatabase();
    int i = -1;
    String str = getTableName(paramUri);
    if (str != null)
      i = localSQLiteDatabase.delete(str, paramString, paramArrayOfString);
    if (i != -1)
      resolver.notifyChange(paramUri, null);
    return i;
  }

  @Override
  public String getType(Uri paramUri)
  {
    switch (Urimatcher.match(paramUri)) {
    case STA_VEHICLE_STATE:
      return CANContentConst.VehicleStatisticsStates.CONTENT_TYPE;
    case BASE_VEHICLE_STATE:
      return CANContentConst.VehicleBasicStates.CONTENT_TYPE;
    case HABIT_VEHICLE_RECORD:
      return CANContentConst.VehicleDrivingRecord.CONTENT_TYPE;
      default:
        return null;
    }

  }

  @Override
  public Uri insert(Uri paramUri, ContentValues paramContentValues) {
    SQLiteDatabase localSQLiteDatabase = dbHelper.getWritableDatabase();
    String str = getTableName(paramUri);
    if (str != null)
    {
      long l = localSQLiteDatabase.insert(str, null, paramContentValues);
      if (l > 0L)
      {
        Uri localUri = ContentUris.withAppendedId(paramUri, l);
        Log.i("hlj CANContentProvider","--->>插入成功, id=" + l);
        Log.i("hlj CANContentProvider","--->>插入成功, resultUri=" + localUri.toString());
        resolver.notifyChange(localUri, null);
        return localUri;
      }
    }
    return null;
  }

  @Override
  public boolean onCreate() {

    context = this.getContext();
    dbHelper = new VehicleStateDBHelper(context);
    resolver = context.getContentResolver();

    return true;
  }

  @Override
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    SQLiteDatabase localSQLiteDatabase = dbHelper.getReadableDatabase();
    String str = getTableName(paramUri);
    Cursor localCursor = null;
    if (str != null)
      localCursor = localSQLiteDatabase.query(str, paramArrayOfString1, paramString1, paramArrayOfString2, null, null, null);
    return localCursor;
  }

  @Override
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    int i = this.dbHelper.getWritableDatabase().update(getTableName(paramUri), paramContentValues, paramString, paramArrayOfString);
    if (i != -1)
      resolver.notifyChange(paramUri, null);
    return i;
  }
}

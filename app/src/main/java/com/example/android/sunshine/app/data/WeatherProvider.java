package com.example.android.sunshine.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by paulshi on 9/23/14.
 */
public class WeatherProvider extends ContentProvider{

    private static final int WEATHER = 100;
    private static final int WEATHER_WITH_LOCATION = 101;
    private static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    private static final int LOCATION = 300;
    private static final int LOCATION_ID = 301;

    private static final UriMatcher wUriMatcher = buildUriMatcher();
    private WeatherDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI("CONTENT://COM.EXAMPLE.ANDROID.SUNSHINE.APP/","WEATHER", WEATHER);
        sUriMatcher.addURI("CONTENT://COM.EXAMPLE.ANDROID.SUNSHINE.APP/","WEATHER/*", WEATHER_WITH_LOCATION);
        sUriMatcher.addURI("CONTENT://COM.EXAMPLE.ANDROID.SUNSHINE.APP/","WEATHER/*/*", WEATHER_WITH_LOCATION_AND_DATE);
        sUriMatcher.addURI("CONTENT://COM.EXAMPLE.ANDROID.SUNSHINE.APP/","LOCATION", LOCATION);
        sUriMatcher.addURI("CONTENT://COM.EXAMPLE.ANDROID.SUNSHINE.APP/","LOCATION/#", LOCATION_ID);
        return sUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

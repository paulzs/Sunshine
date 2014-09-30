package com.example.android.sunshine.app;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.sunshine.app.data.WeatherContract.LocationEntry;
import com.example.android.sunshine.app.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.app.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by paulshi on 9/18/14.
 */
public class TestProvider extends AndroidTestCase{

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    String TEST_CITY = "North Pole";
    String TEST_LOCATION = "99705";
    String TEST_DATE = "20141205";

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    //Set dummy values for location

    static ContentValues createNorthPoleLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 64.7488);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -147.353);

        return testValues;
    }

    public void testInsertReadProvider() {
        //Test data

        //Make DB for testing
        WeatherDbHelper dbHelper =
                new WeatherDbHelper(mContext);
        SQLiteDatabase sqLiteDb = dbHelper.getWritableDatabase();
        assertEquals(true, sqLiteDb.isOpen());

        ContentValues values = createNorthPoleLocationValues();

        long locationRowId;
        locationRowId = sqLiteDb.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues weatherValues = TestDb.createWeatherValues(locationRowId);

        long weatherRowId = sqLiteDb.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        /*Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        validateCursor(weatherValues,weatherCursor);*/

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        addAllContentValues(weatherValues, values);
        // Get the joined Weather and Location data
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestDb.validateCursor(weatherValues, weatherCursor);

        // Get the joined Weather and Location data with a start date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TEST_LOCATION, TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestDb.validateCursor(weatherValues, weatherCursor);

        // A cursor is your primary interface to the query results.
        /*Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        validateCursor(values, cursor);

        // Now see if we can successfully query if we include the row id
        cursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        validateCursor(values, cursor);*/

        sqLiteDb.close();
    }

    static void validateCursor(ContentValues expectedValues, Cursor valueCursor) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();

    }

    public void testGetType(){
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        assertEquals(WeatherEntry.CONTENT_TYPE,type);

        String testLocation = "94074";
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocation(testLocation));
        assertEquals(WeatherEntry.CONTENT_TYPE,type);

        String testDate = "20140612";
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocationWithDate(testLocation,testDate));
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }
}

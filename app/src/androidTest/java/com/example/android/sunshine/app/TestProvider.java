package com.example.android.sunshine.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public void testInsertReadDb() {
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

        Cursor cursor = sqLiteDb.query(
                LocationEntry.TABLE_NAME, //Table to Query
                null, //Just get all columns
                null, //Columns for the "where" clause
                null, //Values for the "where" clause
                null, //columns to group by
                null, //columns to filter by row groups
                null //sort order
        );

        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);

        }
        else {
            fail("No values returned. Womp Womp :(");
        }

        ContentValues weatherValues = TestDb.createWeatherValues(locationRowId);

        long weatherRowId = sqLiteDb.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        Cursor weatherCursor = sqLiteDb.query(
                WeatherEntry.TABLE_NAME, //Table to Query
                null,
                null, //Columns for the "where" clause
                null, //Values for the "where" clause
                null, //columns to group by
                null, //columns to filter by row groups
                null //sort order
        );

        validateCursor(weatherValues,weatherCursor);

        /*if (weatherCursor.moveToFirst()) {

            validateCursor(weatherValues, weatherCursor);

            Get value in each column by finding column index
            int dateIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT);
            String date = weatherCursor.getString(dateIndex);

            int degreesIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DEGREES);
            double degrees = weatherCursor.getDouble(degreesIndex);

            int humidityIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
            double humidity = weatherCursor.getDouble(humidityIndex);

            int pressureIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
            double pressure = weatherCursor.getDouble(pressureIndex);

            int maxIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP);
            double max = weatherCursor.getDouble(maxIndex);

            int minIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP);
            double min = weatherCursor.getDouble(minIndex);

            int shortDescIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);
            String shortDesc = weatherCursor.getString(shortDescIndex);

            int windIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED);
            double wind = weatherCursor.getDouble(windIndex);

            int weatherIdIndex = weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID);
            double weatherId = weatherCursor.getDouble(weatherIdIndex);

            assertEquals(testDate, date);
            assertEquals(testDegrees, degrees);
            assertEquals(testHumidity, humidity);
            assertEquals(testPressure, pressure);
            assertEquals(testMaxTemp, max);
            assertEquals(testMinTemp, min);
            assertEquals(testShortDesc, shortDesc);
            assertEquals(testWind, wind);
            assertEquals(testWeatherId, weatherId);

        }
        else {
            fail("No weather data returned. Womp Womp :(");
        }
        dbHelper.close();*/
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

}

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

    /*ContentValues getLocationContentValues(){

        String testLocationSetting = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.355;

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);

        return values;
    }

    ContentValues getWeatherContentValues(long locationRowId){

        String testDate = "20141205";
        double testDegrees = 1.1;
        double testHumidity = 1.2;
        double testPressure = 1.3;
        double testMaxTemp = 75;
        double testMinTemp = 65;
        String testShortDesc = "Asteroids";
        double testWind = 5.5;
        double testWeatherId = 321;

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        return weatherValues;

    }*/

    public void testInsertReadDb() {
        //Test data

        //Make DB for testing
        WeatherDbHelper dbHelper =
                new WeatherDbHelper(mContext);
        SQLiteDatabase sqLiteDb = dbHelper.getWritableDatabase();
        assertEquals(true, sqLiteDb.isOpen());

        ContentValues values = TestDb.createNorthPoleLocationValues();

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

            /*Get value in each column by finding column index
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);

            assertEquals(TEST_CITY, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);*/

        }
        else {
            fail("No values returned. Womp Womp :(");
        }

        ContentValues weatherValues = TestDb.createNorthPoleLocationValues();

        long weatherRowId;
        weatherRowId = sqLiteDb.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
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

        System.out.println(weatherCursor);

        if (weatherCursor.moveToFirst()) {

            validateCursor(weatherValues, weatherCursor);

            /*Get value in each column by finding column index
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
            assertEquals(testWeatherId, weatherId);*/

        }
        else {
            fail("No weather data returned. Womp Womp :(");
        }
        dbHelper.close();
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

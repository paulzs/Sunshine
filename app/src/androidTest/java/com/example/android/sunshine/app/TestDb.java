package com.example.android.sunshine.app;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.android.sunshine.app.data.WeatherDbHelper;

/**
 * Created by paulshi on 9/18/14.
 */
public class TestDb extends AndroidTestCase{
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

}

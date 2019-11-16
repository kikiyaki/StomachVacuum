package com.sportsandhealth.iamkerel.stomachvacuum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    protected static DB db = null;
    protected static SQLiteDatabase writableDB = null;

    protected DB(Context context) {
        super(context, "SlimDB",null, 1);
        writableDB = this.getWritableDatabase();
    }

    public static DB getInstance(Context context) {
        if (db == null) {
            db = new DB(context);
        }
        return db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {
    }

    public int[] getLevelProgress() {
        Cursor cursor0 = writableDB.rawQuery("SELECT AVG(done) FROM DATA WHERE level=?",
                new String[] {"0"});
        cursor0.moveToFirst();
        int progress0 = (int) (cursor0.getDouble(0)*100);

        Cursor cursor1 = writableDB.rawQuery("SELECT AVG(done) FROM DATA WHERE level=?",
                new String[] {"1"});
        cursor1.moveToFirst();
        int progress1 = (int) (cursor1.getDouble(0)*100);

        Cursor cursor2 = writableDB.rawQuery("SELECT AVG(done) FROM DATA WHERE level=?",
                new String[] {"2"});
        cursor2.moveToFirst();
        int progress2 = (int) (cursor2.getDouble(0)*100);

        return  new int[]{progress0, progress1, progress2};
    }

    //get int array with each day progress in percent
    public int[] getDailyProgress(int level) {
        Cursor cursor = writableDB.rawQuery("SELECT AVG(done) FROM DATA WHERE level=? GROUP BY DAY",
                new String[] {String.valueOf(level)});
        int[] progress = new int[14];

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            progress[cursor.getPosition()] = (int) (cursor.getDouble(0)*100);
            cursor.moveToNext();
        }
        return progress;
    }

    public Cursor getDaylyRoutine(int level, int day) {
        Cursor cursor = writableDB.query("DATA", new String[] {"num", "exercise", "time", "done"},
                "level=? and day=?", new String[]{String.valueOf(level), String.valueOf(day)},
                null, null, "num ASC", null);
        return cursor;
    }

    public Cursor getNotDoneExercise(int level, int day) {
        Cursor cursor = writableDB.query("DATA", new String[] {"exercise", "time", "id"},
                "level=? and day=? and done=0", new String[]{String.valueOf(level), String.valueOf(day)},
                null, null, "num ASC", "1");
        return cursor;
    }

    public void setDone(int id) {
        ContentValues values = new ContentValues();
        values.put("done", 1);
        writableDB.update("DATA", values, "id=?", new String[]{String.valueOf(id)});
    }



    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    /**
     * Проверяет постевлено ли напоминание
     *
     * @return isSet
     */
    public boolean isSetNotification() {
        Cursor cursor = writableDB.rawQuery("SELECT * FROM NOTIFICATION", null);

        boolean isSet;

        Log.e("QQQee", String.valueOf(cursor.getCount()));

        if (cursor.getCount() == 1) {
            isSet = true;
        } else {
            isSet = false;
        }

        return isSet;
    }


    /**
     * Делает запись в БД об уведомлении
     *
     * @param hour
     * @param minute
     */
    public void setNotification(int hour, int minute) {
        ContentValues values = new ContentValues();
        values.put("hour", hour);
        values.put("minute", minute);
        writableDB.insert("NOTIFICATION", null, values);
    }


    /**
     * Удаляет запись в БД об уведомлении
     */
    public void deleteNotification() {
        writableDB.execSQL("DELETE FROM NOTIFICATION");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE NOTIFICATION ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +"hour INTEGER, "
                +"minute INTEGER);");

        db.execSQL("CREATE TABLE DATA ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +"level INTEGER, "      // уровень
                +"day INTEGER, "        // день внутри уровня
                +"num INTEGER, "        //
                +"time INTEGER, "
                +"exercise INTEGER, "
                +"done INTEGER);");

        // Создаем базу данных
        ContentValues values = new ContentValues();

        values.put("level", 0);values.put("day", 0);values.put("num", 0);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 0);values.put("num", 1);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 0);values.put("num", 2);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 0);values.put("num", 3);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 1);values.put("num", 0);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 1);values.put("num", 1);values.put("time", 10);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 1);values.put("num", 2);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 1);values.put("num", 3);values.put("time", 10);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 2);values.put("num", 0);values.put("time", 12);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 2);values.put("num", 1);values.put("time", 12);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 2);values.put("num", 2);values.put("time", 12);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 2);values.put("num", 3);values.put("time", 12);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 3);values.put("num", 0);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 3);values.put("num", 1);values.put("time", 14);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 3);values.put("num", 2);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 3);values.put("num", 3);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 4);values.put("num", 0);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 4);values.put("num", 1);values.put("time", 14);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 4);values.put("num", 2);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 4);values.put("num", 3);values.put("time", 14);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 5);values.put("num", 0);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 5);values.put("num", 1);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 5);values.put("num", 2);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 5);values.put("num", 3);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 6);values.put("num", 0);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 6);values.put("num", 1);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 6);values.put("num", 2);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 6);values.put("num", 3);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 6);values.put("num", 4);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 7);values.put("num", 0);values.put("time", 16);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 7);values.put("num", 1);values.put("time", 16);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 7);values.put("num", 2);values.put("time", 16);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 7);values.put("num", 3);values.put("time", 16);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 7);values.put("num", 4);values.put("time", 16);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 8);values.put("num", 0);values.put("time", 16);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 8);values.put("num", 1);values.put("time", 16);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 8);values.put("num", 2);values.put("time", 16);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 8);values.put("num", 3);values.put("time", 16);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 8);values.put("num", 4);values.put("time", 16);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 9);values.put("num", 0);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 9);values.put("num", 1);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 9);values.put("num", 2);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 9);values.put("num", 3);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 9);values.put("num", 4);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 10);values.put("num", 0);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 10);values.put("num", 1);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 10);values.put("num", 2);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 10);values.put("num", 3);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 10);values.put("num", 4);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 11);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 11);values.put("num", 1);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 11);values.put("num", 2);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 11);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 11);values.put("num", 4);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 12);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 12);values.put("num", 1);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 12);values.put("num", 2);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 12);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 12);values.put("num", 4);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 13);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 13);values.put("num", 1);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 13);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 13);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 0);values.put("day", 13);values.put("num", 4);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 0);values.put("num", 0);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 0);values.put("num", 1);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 0);values.put("num", 2);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 0);values.put("num", 3);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 0);values.put("num", 4);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 1);values.put("num", 0);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 1);values.put("num", 1);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 1);values.put("num", 2);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 1);values.put("num", 3);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 1);values.put("num", 4);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 2);values.put("num", 0);values.put("time", 15);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 2);values.put("num", 1);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 2);values.put("num", 2);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 2);values.put("num", 3);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 2);values.put("num", 4);values.put("time", 15);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 3);values.put("num", 0);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 3);values.put("num", 1);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 3);values.put("num", 2);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 3);values.put("num", 3);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 3);values.put("num", 4);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 4);values.put("num", 0);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 4);values.put("num", 1);values.put("time", 18);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 4);values.put("num", 2);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 4);values.put("num", 3);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 4);values.put("num", 4);values.put("time", 18);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 5);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 5);values.put("num", 1);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 5);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 5);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 5);values.put("num", 4);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 6);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 6);values.put("num", 1);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 6);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 6);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 6);values.put("num", 4);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 1);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 4);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 7);values.put("num", 5);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 0);values.put("time", 22);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 1);values.put("time", 22);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 2);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 3);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 4);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 8);values.put("num", 5);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 0);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 1);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 2);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 3);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 4);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 9);values.put("num", 5);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 0);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 1);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 2);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 3);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 4);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 10);values.put("num", 5);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 0);values.put("time", 27);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 1);values.put("time", 27);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 2);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 3);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 4);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 11);values.put("num", 5);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 0);values.put("time", 28);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 1);values.put("time", 28);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 2);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 3);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 4);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 12);values.put("num", 5);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 0);values.put("time", 28);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 1);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 2);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 3);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 4);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 1);values.put("day", 13);values.put("num", 5);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 1);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 4);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 0);values.put("num", 5);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 0);values.put("time", 20);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 1);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 2);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 3);values.put("time", 20);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 4);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 1);values.put("num", 5);values.put("time", 20);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 0);values.put("time", 22);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 1);values.put("time", 22);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 2);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 3);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 4);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 2);values.put("num", 5);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 0);values.put("time", 22);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 1);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 2);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 3);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 4);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 3);values.put("num", 5);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 0);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 1);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 2);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 3);values.put("time", 22);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 4);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 4);values.put("num", 5);values.put("time", 22);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 0);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 1);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 2);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 3);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 4);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 5);values.put("num", 5);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 0);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 1);values.put("time", 25);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 2);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 3);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 4);values.put("time", 25);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 5);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 6);values.put("num", 6);values.put("time", 25);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 0);values.put("time", 27);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 1);values.put("time", 27);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 2);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 3);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 4);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 5);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 7);values.put("num", 6);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 0);values.put("time", 27);values.put("exercise", 0);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 1);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 2);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 3);values.put("time", 27);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 4);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 5);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 8);values.put("num", 6);values.put("time", 27);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 0);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 1);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 2);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 3);values.put("time", 28);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 4);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 5);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 6);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 9);values.put("num", 7);values.put("time", 28);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 0);values.put("time", 30);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 1);values.put("time", 30);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 2);values.put("time", 30);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 3);values.put("time", 30);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 4);values.put("time", 30);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 5);values.put("time", 30);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 6);values.put("time", 30);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 7);values.put("time", 30);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 10);values.put("num", 8);values.put("time", 30);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 0);values.put("time", 32);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 1);values.put("time", 32);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 2);values.put("time", 32);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 3);values.put("time", 32);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 4);values.put("time", 32);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 5);values.put("time", 32);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 6);values.put("time", 32);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 7);values.put("time", 32);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 11);values.put("num", 8);values.put("time", 32);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 0);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 1);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 2);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 3);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 4);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 5);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 6);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 7);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 12);values.put("num", 8);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 0);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 1);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 2);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 3);values.put("time", 35);values.put("exercise", 1);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 4);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 5);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 6);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 7);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);
        values.put("level", 2);values.put("day", 13);values.put("num", 8);values.put("time", 35);values.put("exercise", 2);values.put("done", 0);db.insert("DATA", null, values);


    }

}

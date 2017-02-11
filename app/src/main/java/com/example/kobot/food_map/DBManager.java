package com.example.kobot.food_map;

/**
 * Created by Kobot on 2017-01-21.
 * 싱글톤으로 바꾸기
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;
import android.widget.Toast;

public class DBManager extends SQLiteOpenHelper  {


    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE FOOD_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT, category TEXT, memo TEXT);");
        db.execSQL("CREATE TABLE FOOD_MAP(_id INTEGER PRIMARY KEY AUTOINCREMENT, food_id REAL,lati REAL, longi REAL);");
        db.execSQL("CREATE TABLE FOOD_PICTURE(_id INTEGER PRIMARY KEY AUTOINCREMENT, picture_id REAL, picture BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public long getLastId(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT _id from FOOD_LIST order by _id DESC limit 1";
        long lastId=0;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        db.close();
        return lastId;
    }
}
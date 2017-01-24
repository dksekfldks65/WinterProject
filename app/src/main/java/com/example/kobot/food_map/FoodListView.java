package com.example.kobot.food_map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodListView extends AppCompatActivity {

    static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_view);

        TextView title = (TextView) findViewById(R.id.title);
        TextView memo = (TextView) findViewById(R.id.memo);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("itemi");

        //db열기
        dbManager= new DBManager(getApplicationContext(), "Food4.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor = db.rawQuery("SELECT * FROM FOOD_LIST", null);

        //db에 접근하여 현재 리스트뷰 내용 화면에 출력
        while(cursor.moveToNext())
        {

            //cursor2.moveToNext();
            //position+1 이라는거에 주의
            if(cursor.getString(1).equals(name) )
            {
                title.setText(cursor.getString(1));
                memo.setText(cursor.getString(2));
                break;
            }
        }

    }
}

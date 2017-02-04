package com.example.kobot.food_map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FoodListView extends AppCompatActivity {

    ListView listview ;
    ListViewAdapter adapter;
    static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_view);

        TextView title = (TextView) findViewById(R.id.title);
        TextView memo = (TextView) findViewById(R.id.memo);
        Button save = (Button) findViewById(R.id.save);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("itemi");

        //db열기
        dbManager= new DBManager(getApplicationContext(), "Food6.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor =db.rawQuery("SELECT _id, name, category, memo FROM FOOD_LIST", null);

        //db에 접근하여 현재 리스트뷰 내용 화면에 출력
        while(cursor.moveToNext())
        {

            if(cursor.getString(1).equals(name) )
            {
                title.setText(cursor.getString(1));
                memo.setText(cursor.getString(3));
                break;
            }
        }

    }

    //데이터 저장 및 리스트뷰 출력
    public void onclicksave(View v)
    {

    }
}

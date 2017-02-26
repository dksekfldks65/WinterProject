package com.example.kobot.food_map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class FoodListView extends AppCompatActivity {

    static DBManager dbManager;
    static byte[] food_image=null;
    Bitmap foodBit=null;
    static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_view);

        TextView title = (TextView) findViewById(R.id.title);
        TextView memo = (TextView) findViewById(R.id.memo);

        Intent intent = getIntent();

        //현재 id값 받음
        id = intent.getExtras().getInt("itemi");

        //db열기
        dbManager= new DBManager(getApplicationContext(), "Food3.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor =db.rawQuery("SELECT _id, name, memo FROM FOOD", null);

        //db에 접근하여 id가 같으면 현재 리스트뷰 내용 화면에 출력
        while(cursor.moveToNext()) {
            if (cursor.getInt(0) == id) {
                title.setText(cursor.getString(1));
                memo.setText(cursor.getString(2));
                break;
            }
        }
    }

    //데이터 저장 및 업데이트 기능 구현
    public void onclickedsave(View v)
    {
        TextView title = (TextView) findViewById(R.id.title);
        Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        TextView memo = (TextView) findViewById(R.id.memo);

        //db열기
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor =db.rawQuery("SELECT _id, name, memo FROM FOOD", null);

        //db에 접근하여 id가 같으면 db수정 후 intent 종료
        while(cursor.moveToNext())
        {
            if(cursor.getInt(0) == id )
            {

                String temp_title = title.getText().toString();
                String temp_spinner = spinner.getSelectedItem().toString();
                String temp_memo = memo.getText().toString();
                String sql1 = "update FOOD set name = '"+temp_title+"' where _id = "+id;
                String sql2 = "update FOOD_CATEGORY set category = '"+temp_spinner+"' where food_id = "+id;
                String sql3 = "update FOOD set memo = '"+temp_memo+"' where _id = "+id;

                dbManager.update(sql1);
                dbManager.update(sql2);
                dbManager.update(sql3);

                finish();
                break;
            }
        }
    }

    public void galleryClicked(View v){
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.putExtra("itemid", id);
        startActivity(intent);
    }
}

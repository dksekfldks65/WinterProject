package com.example.kobot.food_map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class FoodListView extends AppCompatActivity {

    ListView listview ;
    ListViewAdapter adapter;
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
        ImageView img = (ImageView) findViewById(R.id.imageView2);
        Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        Button save = (Button) findViewById(R.id.save);

        Intent intent = getIntent();

        //현재 id값 받음
        id = intent.getExtras().getInt("itemi");

        //db열기
        dbManager= new DBManager(getApplicationContext(), "Food11.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor =db.rawQuery("SELECT _id, name, category, memo FROM FOOD_LIST", null);
        Cursor cursor2 = db.rawQuery("SELECT _id, picture_id, picture FROM FOOD_PICTURE", null);

        //db에 접근하여 id가 같으면 현재 리스트뷰 내용 화면에 출력
        while(cursor.moveToNext())
        {
            if(cursor.getInt(0) == id )
            {
                title.setText(cursor.getString(1));
                memo.setText(cursor.getString(3));
                break;
            }
        }


        while(cursor2.moveToNext())
        {
            if(cursor2.getInt(1) == id){
                food_image = cursor2.getBlob(2);
                foodBit = byteArrayToBitmap(food_image);
                img.setImageBitmap(foodBit);
                food_image = null;
                break;
            }
        }


    }

    //데이터 저장 및 리스트뷰 출력
    public void onclickedsave(View v)
    {
        TextView title = (TextView) findViewById(R.id.title);
        Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        TextView memo = (TextView) findViewById(R.id.memo);

        //db열기
        dbManager= new DBManager(getApplicationContext(), "Food11.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        //커서지정
        Cursor cursor =db.rawQuery("SELECT _id, name, category, memo FROM FOOD_LIST", null);

        //db에 접근하여 id가 같으면 db수정 후 intent 종료
        while(cursor.moveToNext())
        {
            if(cursor.getInt(0) == id )
            {

                String temp_title = title.getText().toString();
                String temp_spinner = spinner.getSelectedItem().toString();
                String temp_memo = memo.getText().toString();
                String sql1 = "update FOOD_LIST set name = '"+temp_title+"' where _id = "+id;
                String sql2 = "update FOOD_LIST set category = '"+temp_spinner+"' where _id = "+id;
                String sql3 = "update FOOD_LIST set memo = '"+temp_memo+"' where _id = "+id;

                dbManager.update(sql1);
                dbManager.update(sql2);
                dbManager.update(sql3);

                title.setText(temp_title);
                memo.setText(temp_memo);
                finish();
                break;
            }
        }
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        byteArray = null;
        return bitmap;
    }
}

package com.example.kobot.food_map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Spinner;

public class FoodListViewActivity extends AppCompatActivity {

    static DBManager dbManager;
    static byte[] food_image=null;
    Bitmap foodBit=null;
    static int id;

    static byte[][] foodpicture_save;
    Bitmap [] foodBit2;
    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_view2);

        TextView title = (TextView) findViewById(R.id.title);
        TextView memo = (TextView) findViewById(R.id.memo);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

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



        Cursor cursor2 =db.rawQuery("SELECT _id, food_id, picture FROM FOOD_PICTURE", null);

        foodpicture_save = new byte [100][];
        foodBit2 = new Bitmap[100];

        Gallery g2 = (Gallery) findViewById(R.id.gallery1);
        ImageView iv2 = (ImageView)findViewById(R.id.imageView1);


        while(cursor2.moveToNext()){
            if(cursor2.getInt(1) == id) {

                g2.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);

                foodpicture_save[i] = cursor2.getBlob(2);
                foodBit2[i]  = byteArrayToBitmap(foodpicture_save[i]);
                i++;
            }
        }

        i=0;

        Cursor cursor3 =db.rawQuery("SELECT _id, food_id, category FROM FOOD_CATEGORY", null);

        while(cursor3.moveToNext()) {
            if (cursor3.getInt(0) == id) {

                if(cursor3.getString(2).equals("카페"))
                    spinner.setSelection(0);
                else if(cursor3.getString(2).equals("술집"))
                    spinner.setSelection(1);
                else if(cursor3.getString(2).equals("고기"))
                    spinner.setSelection(2);
                else if(cursor3.getString(2).equals("일식"))
                    spinner.setSelection(3);
                else if(cursor3.getString(2).equals("일식"))
                    spinner.setSelection(4);
                else if(cursor3.getString(2).equals("중식"))
                    spinner.setSelection(5);
                else if(cursor3.getString(2).equals("한식"))
                    spinner.setSelection(6);
                else if(cursor3.getString(2).equals("분식"))
                    spinner.setSelection(7);
                break;
            }
        }



        //    1. 다량의 데이터
        //    2. Adapter
        //    3. AdapterView : Gallery

        // adapter
        MyAdapter adapter = new MyAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.row, foodBit2);

        // adapterView
        Gallery g = (Gallery)findViewById(R.id.gallery1);
        g.setAdapter(adapter);

        final ImageView iv = (ImageView)findViewById(R.id.imageView1);

        g.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) { // 선택되었을 때 콜백메서드
                iv.setImageBitmap(foodBit2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    //비트맵으로 재변환
    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap resized = null;

        while (height > 118) {
            resized = Bitmap.createScaledBitmap(bitmap, (width * 118) / height, 118, true);
            height = resized.getHeight();
            width = resized.getWidth();
        }

        return resized;
    }
}

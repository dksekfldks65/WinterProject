package com.example.kobot.food_map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryActivity extends AppCompatActivity {

    static int id =0;
    DBManager dbManager;
    static byte[][] foodpicture_save;
    Bitmap [] foodBit;
    DisplayMetrics mMetrics;
    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("itemid");

        //DBManager객체 생성
        dbManager = new DBManager(getApplicationContext(), "Food.db", null, 1);

        //db접근 및 테이블 지정
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT _id, food_id, picture FROM FOOD_PICTURE", null);

        foodpicture_save = new byte [100][];
        foodBit = new Bitmap[100];

        while(cursor.moveToNext()){
            if(cursor.getInt(1) == id) {
                foodpicture_save[i] = cursor.getBlob(2);
                foodBit[i]  = byteArrayToBitmap(foodpicture_save[i]);
                i++;
            }
        }

        i=0;

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener
            = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


        }
    };

    //비트맵으로 재변환
    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //byteArray = null;
        return bitmap;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }


        public int getCount() {
            int i=0;
            for(int j=0;j<foodBit.length;j++)
            {
                if(foodBit[j] == null){
                    break;
                }

                else{
                    i++;
                }
            }
            return i;
        }

        public Object getItem(int position) {
            return foodBit[position];
        }

        public long getItemId(int position) {
            return position;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            int rowWidth = (mMetrics.widthPixels) / 3;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth,rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageBitmap(foodBit[position]);
            return imageView;
        }
    }

}

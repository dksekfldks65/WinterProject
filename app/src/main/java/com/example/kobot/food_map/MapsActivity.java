package com.example.kobot.food_map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView listview ;
    ListViewAdapter adapter;
    Button save;
    Button picture_btn;
    static DBManager dbManager;
    private final int REQ_CODE_GALLERY = 100;
    static byte[] food;
    static byte[][] foodpicture_save;
    static double longi;
    static double lati;
    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //DBManager객체 생성
        dbManager = new DBManager(getApplicationContext(), "Food3.db", null, 1);

        // 화면을 portrait 세로화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        save = (Button) findViewById(R.id.save);
        picture_btn = (Button) findViewById(R.id.registerpicture);

        // Adapter 생성, 리스트뷰 참조 및 Adapter달기
        adapter = new ListViewAdapter() ;
        listview = (ListView) findViewById(R.id.foodlist);
        listview.setAdapter(adapter);

        foodpicture_save = new byte [30][];

        //Tab 메뉴바 생성
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("맛집 지도");

        TabHost.TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("스토리");
        spec2.setContent(R.id.tab2);

        TabHost.TabSpec spec3=tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("리스트");
        spec3.setContent(R.id.tab3);

        TabHost.TabSpec spec4=tabHost.newTabSpec("Tab 4");
        spec4.setIndicator("맛집 추가");
        spec4.setContent(R.id.tab4);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        tabHost.addTab(spec4);

        //db접근 및 테이블 지정
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT _id, name, memo FROM FOOD", null);
        Cursor cursor2 =db.rawQuery("SELECT _id, food_id, category FROM FOOD_CATEGORY", null);

        //데이터 베이스 내용 리스트뷰 출력
        while(cursor.moveToNext())
        {
            cursor2.moveToNext();
            //리스트뷰 초기화
            String temp_title = cursor.getString(1);
            String temp_category = cursor2.getString(2);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher), temp_title, temp_category, cursor.getInt(0)) ;
        }

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListItem item = (ListItem) parent.getItemAtPosition(position) ;
                Intent intent = new Intent(getApplicationContext(), FoodListView.class);
                intent.putExtra("itemi", item.getId());
                startActivity(intent);

                // TODO : use item data.
            }
        }) ;

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//길게 클릭했을 때
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("삭제");
                alert.setMessage("이 리스트를 삭제하시겠습니까?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                        long get_id;
                        get_id = adapter.getItemId(position);

                        //DB delete하는 명령, 리스트뷰 갱신
                        dbManager.delete("delete from FOOD where _id = '" + get_id + "';");
                        dbManager.delete("delete from FOOD_MAP where food_id = '" + get_id + "';");
                        dbManager.delete("delete from FOOD_CATEGORY where food_id = '" + get_id + "';");
                        dbManager.delete("delete from FOOD_PICTURE where food_id = '" + get_id + "';");

                        adapter.deleteItem(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                alert.show();
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Adapter 생성, 리스트뷰 참조 및 Adapter달기
        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.foodlist);
        listview.setAdapter(adapter);

        //db접근 및 테이블 지정
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT _id, name, memo FROM FOOD", null);
        Cursor cursor2 =db.rawQuery("SELECT _id, food_id, category FROM FOOD_CATEGORY", null);

        //데이터 베이스 내용 리스트뷰 출력
        while(cursor.moveToNext())
        {
            cursor2.moveToNext();
            //리스트뷰 초기화
            String temp_title = cursor.getString(1);
            String temp_category = cursor2.getString(2);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher), temp_title, temp_category, cursor.getInt(0)) ;
        }
    }


    //데이터 저장 및 리스트뷰 출력
    public void onclickedsave(View v)
    {
        int food_id=0;

        // Adapter 생성, 리스트뷰 참조 및 Adapter달기
        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.foodlist);
        listview.setAdapter(adapter);

        final EditText editTitle = (EditText) findViewById(R.id.title);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        final EditText editMemo = (EditText) findViewById(R.id.grade);

        String title = editTitle.getText().toString();
        String spinnertext = spinner.getSelectedItem().toString();
        String memo = editMemo.getText().toString();

        dbManager.insert("insert into FOOD values(null, '" + title + "', '" +memo + "');");

        //db접근 및 테이블 지정
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT _id, name, memo FROM FOOD", null);
        Cursor cursor2 =db.rawQuery("SELECT _id, food_id, category FROM FOOD_CATEGORY", null);

        cursor.moveToLast();
        food_id = cursor.getInt(0);

        dbManager.insert("insert into FOOD_CATEGORY values(null, '" + food_id + "', '" + spinnertext + "');");

        for(int j=0; j<30;j++) {
            if(foodpicture_save[j] == null) {
                break;
            }

            else if(foodpicture_save[j] != null) {
                dbManager.insert(food_id, foodpicture_save[j]);
                Toast.makeText(getApplicationContext(), "사진 db에 저장됨", Toast.LENGTH_SHORT).show();
            }
        }

        editTitle.setText("");
        editMemo.setText("");

        cursor.moveToFirst();
        cursor.moveToPrevious();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getApplicationContext(), currentDateTimeString, Toast.LENGTH_SHORT).show();

        //데이터 베이스 내용 리스트뷰 출력
        while(cursor.moveToNext()) {
            //리스트뷰 초기화
            String temp_title = cursor.getString(1);
            String temp_category = cursor.getString(2);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher), temp_title, temp_category, cursor.getInt(0)) ;
        }

        dbManager.insert("insert into FOOD_MAP values(null, "+food_id+","+lati+", "+longi+");");

        i = 0;

        for(int i=0;i<30;i++) {
            if(foodpicture_save[i] != null) {
                foodpicture_save[i] = null;
                Toast.makeText(getApplicationContext(), "picture save 지워짐", Toast.LENGTH_SHORT).show();
            }

            else{
                break;
            }
        }
        food = null;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE) ;
        GPSListener gpsListener = new GPSListener();
        long minTime = 60000;
        float minDistance = 0;


        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
        }

        catch (Exception E) {
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
        }

        Toast.makeText(getApplicationContext(), "위치 확인 시작함. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
    }

    private class GPSListener implements LocationListener {
        Double latitude;
        Double longitude;
        Location location;

        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            lati = latitude;
            longi = longitude;

            showCurrentLocation(latitude,longitude);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
            lati = location.getLatitude();
            longi = location.getLongitude();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void showCurrentLocation(final Double latitude, final Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        mMap.clear();
        LatLng curPoint = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curPoint));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        MarkerOptions optFirst = new MarkerOptions();
        optFirst.position(curPoint);// 위도 • 경도
        optFirst.title("Current Position");// 제목 미리보기
        optFirst.snippet("Snippet");
        optFirst.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        mMap.addMarker(optFirst).showInfoWindow();
    }

    public void onclickedpicture(View v) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(i, REQ_CODE_GALLERY);
    }

    //겔러리로부터 이미지 경로를 받아와 비트맵을 byteArray로 전환후 db에 저장
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(requestCode == REQ_CODE_GALLERY){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();

                try {

                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                    food = bitmapToByteArray(bm);

                    foodpicture_save[i] = food;

                    Toast.makeText(getApplicationContext(), "사진이 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                    i++;

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "한번에 추가 할 수 있는 사진의 갯수를 초과했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //비트맵을 byteArray로 변환
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}

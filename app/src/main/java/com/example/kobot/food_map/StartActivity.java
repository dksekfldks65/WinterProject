package com.example.kobot.food_map;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {

    ImageView imgview1;
    private AnimationDrawable mAnimationDrawable_2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 화면을 portrait 세로화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_start);
        imgview1 = (ImageView) findViewById(R.id.imageView);

        BitmapDrawable mBitmapDrawable_1 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice1);
        BitmapDrawable mBitmapDrawable_2 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice2);
        BitmapDrawable mBitmapDrawable_3 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice3);
        BitmapDrawable mBitmapDrawable_4 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice4);
        BitmapDrawable mBitmapDrawable_5 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice5);
        BitmapDrawable mBitmapDrawable_6 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice6);
        BitmapDrawable mBitmapDrawable_7 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice7);
        BitmapDrawable mBitmapDrawable_8 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice8);
        BitmapDrawable mBitmapDrawable_9 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice9);
        BitmapDrawable mBitmapDrawable_10 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice10);
        BitmapDrawable mBitmapDrawable_11 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice11);
        BitmapDrawable mBitmapDrawable_12 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice12);
        BitmapDrawable mBitmapDrawable_13 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice13);

        mAnimationDrawable_2 = new AnimationDrawable();
        mAnimationDrawable_2.setOneShot(false);

        int duration = 140;

        mAnimationDrawable_2.addFrame(mBitmapDrawable_1, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_2, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_3, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_4, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_5, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_6, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_7, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_8, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_9, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_10, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_11, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_12, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_13, duration);

        imgview1.setBackgroundDrawable(mAnimationDrawable_2);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 2000); // 5초 후에 hd Handler 실행

    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MapsActivity.class)); // 로딩이 끝난후 이동할 Activity
            StartActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // 어플에 포커스가 갈때 시작된다
            mAnimationDrawable_2.start();
        } else {
            // 어플에 포커스를 떠나면 종료한다
            mAnimationDrawable_2.stop();
        }
    }
}

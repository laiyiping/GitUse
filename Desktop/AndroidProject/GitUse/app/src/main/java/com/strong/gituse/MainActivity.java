package com.strong.gituse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "添加好友");
        Log.e("MainActivity", "开始聊天");
        Log.e("MainActivity", "开始约会");
        Log.e("MainActivity", "开始相爱");
        Log.e("MainActivity", "开始争吵");
        Log.e("MainActivity", "开始分手");



    }
}

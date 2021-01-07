package com.daipro.nhasachphuongnam.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void openActivity(Class target){
        Intent intent = new Intent(this,target);
        startActivity(intent);
    }
    public void openActivity(Class target,boolean isFinish){
        Intent intent = new Intent(this,target);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }
}

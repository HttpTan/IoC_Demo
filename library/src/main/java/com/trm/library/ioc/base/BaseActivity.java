package com.trm.library.ioc.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.trm.library.ioc.InjectManager;


// 已实现注解注入
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InjectManager.inject(this);
    }

}

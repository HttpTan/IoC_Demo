package com.trm.ioc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trm.library.ioc.annotation.ContentView;
import com.trm.library.ioc.annotation.InjectView;
import com.trm.library.ioc.annotation.OnClick;
import com.trm.library.ioc.annotation.OnLongClick;
import com.trm.library.ioc.base.BaseActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @InjectView(R.id.show_text)
    private TextView textView;

    @InjectView(R.id.btn)
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "默认点击", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "默认长按", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        // 这样 注入的方法会拦截上面的方法
        //InjectManager.inject(this);
    }


    @OnClick({R.id.show_text, R.id.btn})
    private void onClick(View view) {
        Toast.makeText(MainActivity.this, "点击了控件 id: " + view.getId(), Toast.LENGTH_SHORT).show();
    }

    @OnLongClick({R.id.show_text, R.id.btn})
    private boolean onLongClick(View view) {
        Toast.makeText(MainActivity.this, "自定义Btn长按", Toast.LENGTH_SHORT).show();
        return false;
    }
}

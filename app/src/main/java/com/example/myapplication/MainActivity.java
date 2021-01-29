package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.view.CustomTopActionBar;
import com.example.myapplication.view.CustomView;
import com.example.myapplication.view.MyClockView;

public class MainActivity extends AppCompatActivity {

    private CustomTopActionBar ctab_actionbar;
    private MyClockView mcv_myClock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctab_actionbar=findViewById(R.id.ctab_actionbar);
        mcv_myClock=findViewById(R.id.mcv_myClock);
        ctab_actionbar.setOnClickLeftListener(v -> Toast.makeText(MainActivity.this,"返回",Toast.LENGTH_SHORT).show());
        ctab_actionbar.setOnClickTitleListener(v -> Toast.makeText(MainActivity.this,ctab_actionbar.getTitle(),Toast.LENGTH_SHORT).show());
        ctab_actionbar.setOnClickRightListener(v -> Toast.makeText(MainActivity.this,"设置",Toast.LENGTH_SHORT).show());
        ctab_actionbar.setTitle("不是标题");
        mcv_myClock.start();
    }
}
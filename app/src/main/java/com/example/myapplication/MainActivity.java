package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.view.MyClockView;

public class MainActivity extends AppCompatActivity {

    private MyClockView mcv_myClock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcv_myClock=findViewById(R.id.mcv_myClock);
        mcv_myClock.start();
    }
}
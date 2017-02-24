package com.wenld.smoothcheckbox_;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SmoothCheckBox_01 scb = (SmoothCheckBox_01) findViewById(R.id.scb);
        scb.setOnCheckedChangeListener(new SmoothCheckBox_01.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox_01 checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
            }
        });
    }
}

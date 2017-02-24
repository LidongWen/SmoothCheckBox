package com.wenld.smoothcheckbox_;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wenld.smoothcheckbox.SmoothCheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.scb);
//        scb.setChecked(boolean checked);
//        scb.setChecked(boolean checked, boolean animate);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
            }
        });
    }
}

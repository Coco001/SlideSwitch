package com.gkk.slideswitch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SlideSwitchView switchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchView = (SlideSwitchView) findViewById(R.id.my_ssv);
        switchView.setBackground(R.drawable.switch_background);
        switchView.setPicture(R.drawable.slide_button_background);

        switchView.setOnSwitchListener(new SlideSwitchView.OnSwitchListener() {
            @Override
            public void onSwitchChanged(boolean isOpened) {
                Toast.makeText(getApplicationContext(), isOpened ? "打开状态" : "关闭状态", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

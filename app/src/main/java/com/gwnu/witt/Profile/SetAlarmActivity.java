package com.gwnu.witt.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.R;
import com.suke.widget.SwitchButton;

public class SetAlarmActivity extends AppCompatActivity {

    SwitchButton switchButton;
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalarm);

        back_btn = findViewById(R.id.backtosetting);
        switchButton = (com.suke.widget.SwitchButton)
                findViewById(R.id.vibrate_set_btn);

        backToSetting();
        setSwitchButton();


    }

    public void backToSetting() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setSwitchButton() {
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    Log.d("체크됨", String.valueOf(isChecked));

                }else {
                    Log.d("체크안됨", String.valueOf(isChecked));
                }
            }
        });
    }


}
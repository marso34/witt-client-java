package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.util.AttributeSet;


public class Sub_rutin extends LinearLayout {

    public Sub_rutin(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_rutin,this,true);
    }

//
}
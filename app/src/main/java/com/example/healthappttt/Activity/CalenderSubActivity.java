package com.example.healthappttt.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.healthappttt.R;

import java.util.Calendar;


public class CalenderSubActivity extends Activity {
    public TextView diaryTextView2;
    Button btn_sub;
    EditText mMemoEdit = null;
    CalenderTextFileManager mTextFileManager = new CalenderTextFileManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_sub);
        btn_sub = findViewById(R.id.button_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMemoEdit = (EditText)findViewById(R.id.memo_edit);

        diaryTextView2=findViewById(R.id.diaryTextView2);

    }

    public void onClick(View v){
        switch (v.getId()){
            //1. 파일에 저장된 메모 텍스트 파일 불러오기
            case R.id.load_btn: {
                String memoData = mTextFileManager.load();
                mMemoEdit.setText(memoData);

                Toast.makeText(this,"불러오기 완료",Toast.LENGTH_LONG).show();
                break;
            }
            //2. 에디트 텍스트에 입력된 메모를 텍스트 파일로 저장하기
            case R.id.delete_btn:{
                mTextFileManager.delete();
                mMemoEdit.setText("");

                Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show();
            }
        }
    }

    //시간선택
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    //---
    //날짜선택
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    //---

    //이미지

}

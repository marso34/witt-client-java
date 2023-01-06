package com.example.healthappttt.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.healthappttt.R;

import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;


public class CalenderSubActivity extends Activity {
    public TextView diaryTextView2;
    Button btn_sub;
    EditText mMemoEdit = null;
    CalenderTextFileManager mTextFileManager = new CalenderTextFileManager(this);
    ImageView imageView;

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

        //이미지
       imageView = (ImageView) findViewById(R.id.image1);
        //profile imageView 에 사진 넣는 경우

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);

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
    //이미지뷰 클릭시 intent 실행
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지 뷰를 클릭하면 시작되는 함수

        if(requestCode== 0 && resultCode==RESULT_OK && data!=null) {
            //response에 getData , return data 부분 추가해주어야 한다

            Uri selectedImage = data.getData();
            Uri photoUri = data.getData();
            Bitmap bitmap = null;
            //bitmap 이용
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoUri);

                bitmap = rotateImage(bitmap, 90);
                //사진이 돌아가 있는 경우 rotateImage 함수 이용해서 사진 회전 가능
            } catch (IOException e) {
                e.printStackTrace();
            }

            //이미지뷰에 이미지 불러오기

            imageView.setImageBitmap(bitmap);

            //아래 커서 이용해서 사진의 경로 불러오기
            Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            String mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            Log.d("경로 확인 >> ", "$selectedImg  /  $absolutePath");

        }else{
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}

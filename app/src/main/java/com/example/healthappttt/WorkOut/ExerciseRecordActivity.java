package com.example.healthappttt.WorkOut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineComparator;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Record.ExerciseResultFragment;
import com.example.healthappttt.interface_.ServiceApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseRecordActivity extends AppCompatActivity implements ERSelectRoutineFragment.OnFragmentInteractionListener, ERSelectUserFragment.OnFragmentInteractionListener, ERRecordingFragment.OnFragmentInteractionListener, ExerciseResultFragment.OnFragmentInteractionListener {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper prefhelper;

    private ArrayList<UserChat> users;
    private ArrayList<RoutineData> routines; // 지금은 요일에 루틴 하나를 만들지만
//    private RoutineData routine;          // 여러개 만들 수도 있음
    private RecordData record;

    private int dayOfWeek;
    private int oUserID, promiseID;
    private String name;
    private String startTime, endTime, runTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        prefhelper = new PreferenceHelper("UserTB",this);

        sqLiteUtil.setInitView(this, "RT_TB");
        routines = sqLiteUtil.SelectRoutine(dayOfWeek);

        if (routines != null) {
            for (int i = 0; i < routines.size(); i++) {
                sqLiteUtil.setInitView(this, "EX_TB");
                routines.get(i).setExercises(sqLiteUtil.SelectExercise(routines.get(i).getID(), true));
            }

            Collections.sort(routines, new RoutineComparator());
        }

        sqLiteUtil.setInitView(this, "CHAT_ROOM_TB");
        users = sqLiteUtil.selectChatRoom(Integer.toString(prefhelper.getPK()));

        if (routines.size() > 0)
            replaceFragment(ERSelectRoutineFragment.newInstance(dayOfWeek, routines.get(0)));
        else
            replaceFragment(ERSelectRoutineFragment.newInstance(dayOfWeek, null));
    }

    private void replaceFragment (Fragment fragment) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void SaveToDB() {
        service.createRecord(record).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(ExerciseRecordActivity.this, "기록 성공", Toast.LENGTH_SHORT).show();
                    Log.d("성공", "운동 기록 성공");

                    List<Integer> list = response.body();

                    int PID = 0, i = 0, Cat = 0;

                    for (int resultID : list) {
                        if (i == 0) {
                            PID = resultID; // 루틴ID
                            record.setID(PID);
                        } else {
                            record.getExercises().get(i-1).setParentID(PID);
                            record.getExercises().get(i-1).setID(resultID);
                        }
                        i++;
                    }

                    SaveToDev();

                    replaceFragment(ExerciseResultFragment.newInstance(routines.get(0), record, ""));

                } else {
                    Toast.makeText(ExerciseRecordActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "respone 실패");
                    finish();

//                Terminate(false); // 운동 기록 액티비티 종료
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(ExerciseRecordActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
                finish();
//                Terminate(false); // 운동 기록 액티비티 종료
            }
        });
    }

    private void SaveToDev() {
        sqLiteUtil.setInitView(this, "RECORD_TB");
        sqLiteUtil.insert(record);

        for (ExerciseData e : record.getExercises()) {
            sqLiteUtil.setInitView(this, "EX_TB");
            sqLiteUtil.insert(e, true);
        }
    }

    @Override
    public void onSelectRoutine(boolean isBack) {
        if (isBack)    finish();
        else if (users.size() > 0)
            replaceFragment(ERSelectUserFragment.newInstance(users));
        else
            replaceFragment(ERRecordingFragment.newInstance(routines.get(0).getExercises()));
    }

    @Override
    public void onSelectUser(int oUserID, String name) { // 나중에 유저 전달로 변경
        if (oUserID < 0) // 뒤로가기 버튼
            replaceFragment(ERSelectRoutineFragment.newInstance(dayOfWeek, routines.get(0)));
        else if (oUserID == 0) { // 유저 선택 안 함
            replaceFragment(ERRecordingFragment.newInstance(routines.get(0).getExercises())); // ERRecordingFragment로 이동
        } else { // 유저 선택
            this.oUserID = oUserID;
            this.name = name;
            replaceFragment(ERRecordingFragment.newInstance(routines.get(0).getExercises())); // ERRecordingFragment로 이동
        }
    }

    @Override
    public void onRecordExercises(String StartTime, String EndTime, String RunTime, ArrayList<ExerciseData> recordExercises) {
        startTime = StartTime;
        endTime = EndTime;
        runTime = RunTime;

        int cat = 0;
        for (ExerciseData e : recordExercises)
            cat |= e.getCat();

        record = new RecordData(prefhelper.getPK(), 0, 0, startTime, endTime, runTime, cat); // 나중에 userID prefhelper.getPK()로 수정
        record.setExercises(recordExercises);

        SaveToDB();
    }

    @Override
    public void onFinish() {
        if (oUserID > 0) {
            replaceFragment(ERReviewFragment.newInstance(name, oUserID));
        } else {
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록, 일단 유지

    // 나중에 뒤로가기 눌렀을 종료하시겠습니까? 저장 안 됩니다~ 뜨게 하고 종료하게 할지
    // 아니면 뒤로가고 다시 돌아왔을 때 운동 상택가 유지되게 할지 결정하고 수정
}
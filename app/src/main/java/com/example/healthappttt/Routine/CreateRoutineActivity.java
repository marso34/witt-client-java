package com.example.healthappttt.Routine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoutineActivity extends AppCompatActivity implements CRSetTimeFragment.OnFragmentInteractionListener, CRSelectExerciseFragment.OnFragmentInteractionListener, CRInputDetailFragment.OnFragmentInteractionListener {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper prefhelper;

    private int dayOfWeek, startTime, endTime;
    private RoutineData routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        prefhelper = new PreferenceHelper("UserTB",this);

        routine = new RoutineData();

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        replaceFragment(new CRSetTimeFragment());
    }

    private String TimeToString(int Time) {
        if (Time >= 240) Time-= 240;

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", Time/10, Time % 10 * 6, 0);

        return result;
    }

    private void SaveToDB() {
        ArrayList<ExerciseData> list = new ArrayList<>();

        int CAT = 0;
        for (ExerciseData e : routine.getExercises())
            CAT |= e.getCat();

        routine.setUserID(prefhelper.getPK()); // 나중에 userID prefhelper.getPK()로 수정
        routine.setCat(CAT);

        int finalCAT = CAT;

        service.createRoutine(routine).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(CreateRoutineActivity.this, "루틴 생성 성공!!!", Toast.LENGTH_SHORT).show();
                    Log.d("성공", "루틴 생성 성공");

                    List<Integer> list = response.body();
                    int PID = 0, i = 0, Cat = 0;

                    for (int resultID : list) {
                        if (i == 0) {
                            PID = resultID; // 루틴ID
                            routine.setID(PID);
                        } else {
                            routine.getExercises().get(i-1).setParentID(PID);
                            routine.getExercises().get(i-1).setID(resultID);
                        }
                        i++;
                    }

                    SaveToDev();
                    Terminate(true); // 루틴 생성 액티비티 종료
                } else {
                    Toast.makeText(CreateRoutineActivity.this, "루틴 생성에 실패하였습니다. 인터넷 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "respone 실패");
                    Terminate(false); // 루틴 생성 액티비티 종료
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(CreateRoutineActivity.this, "루틴 생성에 실패하였습니다. 인터넷 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
                Terminate(false); // 루틴 생성 액티비티 종료
            }
        });
    }

    private void SaveToDev() {
        sqLiteUtil.setInitView(this, "RT_TB");
        sqLiteUtil.insert(routine);

        sqLiteUtil.setInitView(this, "EX_TB");
        for (ExerciseData e : routine.getExercises())
            sqLiteUtil.insert(e, false);
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

    private void fragmentToSetTime() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new CRSetTimeFragment(), bundle);
    }

    private void fragmentToAddEx() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putSerializable("exercises", routine.getExercises());
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new CRSelectExerciseFragment(), bundle);
    }

    private void fragmentToExDetail() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putSerializable("exercises", routine.getExercises());
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new CRInputDetailFragment(), bundle);
    }

    private void Terminate(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("routine", routine);
            intent.putExtra("check", 1); // 루틴 추가를 의미
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    private void replaceFragment (Fragment fragment, Bundle bundle) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRoutineSetTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        routine.setDayOfWeek(dayOfWeek);
        routine.setStartTime(TimeToString(startTime));
        routine.setEndTime(TimeToString(endTime));

        fragmentToAddEx();
    }  // SetRoutineTime에서 호출하는 메서드

    @Override
    public void onRoutineAddEx(ArrayList<ExerciseData> selectExercises) {
        this.routine.setExercises(selectExercises);

        fragmentToExDetail();
    } // AddExerciseFragment에서 호출하는 메서드

    @Override
    public void onRoutineExDetail(ArrayList<ExerciseData> exercises) {
        this.routine.setExercises(exercises);

        SaveToDB(); // 받은 운동 정보 토대로 DB에 루틴, 운동 생성
    } // ExerciseDetailFragment에서 호출하는 메서드

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("루틴 추가를 취소할까요?");
        alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // "예" 클릭시 종료
            }
        });
        alert_ex.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  // 아니오 누를 시 아무것도 안 함
            }
        });
        alert_ex.setTitle("테스트");

        AlertDialog alert = alert_ex.create();
        alert.show();
    } // 뒤로가기 버튼 눌렀을 때 루틴 입력을 취소할지, CreateRoutineActivity를 종료할 지 확인
}
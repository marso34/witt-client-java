package com.gwnu.witt.Routine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.R;
import com.gwnu.witt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoutineActivity extends AppCompatActivity implements CRSetTimeFragment.OnFragmentInteractionListener, CRSelectExerciseFragment.OnFragmentInteractionListener, CRInputDetailFragment.OnFragmentInteractionListener {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper prefhelper;

    private int dayOfWeek, time;
    private RoutineData routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
        time = -1;

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        prefhelper = new PreferenceHelper("UserTB",this);

        routine = new RoutineData();
        routine.setDayOfWeek(dayOfWeek);
        routine.setTime(time);

        replaceFragment(CRSetTimeFragment.newInstance(time, dayOfWeek));
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

        for (ExerciseData e : routine.getExercises()) {
            sqLiteUtil.setInitView(this, "EX_TB");
            sqLiteUtil.insert(e, false);
        }
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

    private void replaceFragment(Fragment fragment) { //프래그먼트 설정
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

    private void replaceFragmentLeft(Fragment fragment) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.to_left, R.anim.from_left);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragmentRight(Fragment fragment) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.to_right, R.anim.from_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRoutineSetTime(int time) {
        if (-1 < time && time < 4) {
            this.time = time;

        routine.setTime(time);

            replaceFragment(CRSelectExerciseFragment.newInstance(routine));
        } else {
            Terminate();
        }
    }  // SetRoutineTime에서 호출하는 메서드

    @Override
    public void onRoutineAddEx(ArrayList<ExerciseData> selectExercises) {
        if (selectExercises != null) {
            this.routine.setExercises(selectExercises);

            replaceFragment(CRInputDetailFragment.newInstance(routine));
        } else {
            replaceFragment(CRSetTimeFragment.newInstance(time, dayOfWeek));
        }
    } // AddExerciseFragment에서 호출하는 메서드

    @Override
    public void onRoutineExDetail(ArrayList<ExerciseData> exercises, boolean isFinish) {
        this.routine.setExercises(exercises);

        if (isFinish)   SaveToDB(); // 받은 운동 정보 토대로 DB에 루틴, 운동 생성
        else            replaceFragment(CRSelectExerciseFragment.newInstance(routine));
    } // ExerciseDetailFragment에서 호출하는 메서드

//    @Override
//    public void onBackPressed() {
//        Terminate();
//    } // 뒤로가기 버튼 눌렀을 때 루틴 입력을 취소할지, CreateRoutineActivity를 종료할 지 확인

    private void Terminate() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("루틴 생성을 취소할까요?");
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
        alert_ex.setTitle("루틴 생성 취소");

        AlertDialog alert = alert_ex.create();
        alert.show();
    }
}
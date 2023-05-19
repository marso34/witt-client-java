package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.RoutineComparator;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Fragment.ERRecordingFragment;
import com.example.healthappttt.Fragment.ERSelectRoutineFragment;
import com.example.healthappttt.Fragment.ERSelectUserFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.Collections;

public class ExerciseRecordActivity extends AppCompatActivity implements ERSelectRoutineFragment.OnFragmentInteractionListener, ERSelectUserFragment.OnFragmentInteractionListener {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private int dayOfWeek;
    private ArrayList<Routine> routines;

    private Routine routine;
    private ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "RT_TB");

        routines = sqLiteUtil.SelectRoutine(dayOfWeek);

        if (routines != null)
            Collections.sort(routines, new RoutineComparator());

        Bundle bundle = new Bundle();
        bundle.putSerializable("routines", routines);
        bundle.putInt("dayOfWeek", dayOfWeek);

        replaceFragment(new ERSelectRoutineFragment(), bundle);
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
    public void onSelectRoutine(Routine routine, ArrayList<Exercise> exercises) {
        this.routine = routine;
        this.exercises = exercises;

        replaceFragment(new ERSelectUserFragment());
    }


    @Override
    public void onSelectUser() { // 나중에 유저 전달로 변경
        Bundle bundle = new Bundle();
        bundle.putSerializable("routines", routines);
        bundle.putSerializable("exercises", exercises);

//        if (user != null) {}

        replaceFragment(new ERRecordingFragment(), bundle);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록, 일단 유지

    // 나중에 뒤로가기 눌렀을 종료하시겠습니까? 저장 안 됩니다~ 뜨게 하고 종료하게 할지
    // 아니면 뒤로가고 다시 돌아왔을 때 운동 상택가 유지되게 할지 결정하고 수정
}
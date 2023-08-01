package com.gwnu.witt.Routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class AddExerciseActivity extends AppCompatActivity implements CRSelectExerciseFragment.OnFragmentInteractionListener {
    ActivityAddExerciseBinding binding;

    private RoutineData routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        routine = (RoutineData) intent.getSerializableExtra("routine");

        if (routine != null) {
            Log.d("AddExerciseActivity", "루틴 있음");

            Bundle bundle = new Bundle();
            bundle.putSerializable("routine", routine);
            replaceFragment(new CRSelectExerciseFragment(), bundle);
        }
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
            fragmentTransaction.remove(fragment);
            fragment = new Fragment();
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRoutineAddEx(ArrayList<ExerciseData> selectExercises) {
        this.routine.setExercises(selectExercises);

        Intent intent = new Intent();
        intent.putExtra("routine", routine);
        setResult(RESULT_OK, intent);
        finish();
    }
}
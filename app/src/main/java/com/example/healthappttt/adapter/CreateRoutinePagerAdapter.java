package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.healthappttt.Fragment.ExerciseDetailFragment;
import com.example.healthappttt.Fragment.RoutineChildFragment;
import com.example.healthappttt.Fragment.SetRoutineTimeFragment;
import com.example.healthappttt.Fragment.AddExerciseFragment;

import java.util.ArrayList;

public class CreateRoutinePagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private int dayOfWeek;

    public CreateRoutinePagerAdapter(@NonNull FragmentActivity fragmentActivity, int dayOfWeek) {
        super(fragmentActivity);
        this.dayOfWeek = dayOfWeek;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: addFragment(new SetRoutineTimeFragment()); break;
            case 1: addFragment(new AddExerciseFragment(dayOfWeek));    break;
            case 2: addFragment(new ExerciseDetailFragment()); break;
            default:
                return null;
        }
        return mFragmentList.get(position);
    }

    private void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

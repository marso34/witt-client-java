package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.healthappttt.Fragment.RoutineChildFragment;

import java.util.ArrayList;

public class RoutinePagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public RoutinePagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        addFragment(new RoutineChildFragment(position));

        return mFragmentList.get(position);
    }

    private void addFragment(RoutineChildFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}

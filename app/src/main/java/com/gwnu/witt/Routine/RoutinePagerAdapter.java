package com.gwnu.witt.Routine;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class RoutinePagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private int code;

    public RoutinePagerAdapter(@NonNull Fragment fragmentActivity, int code) {
        super(fragmentActivity);

        this.code = code;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: addFragment(new RoutineChildFragment(position, code)); break;
            default:
                return null;
        }

        return mFragmentList.get(position);
    }

    private void addFragment(RoutineChildFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getItemCount() { return 7; }
}

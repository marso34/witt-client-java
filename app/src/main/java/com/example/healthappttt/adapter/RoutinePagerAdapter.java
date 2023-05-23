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
        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: addFragment(new RoutineChildFragment(position)); break;
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

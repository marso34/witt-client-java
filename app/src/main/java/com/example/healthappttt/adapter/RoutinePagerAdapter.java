package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.healthappttt.Fragment.RoutineChildFragment;

import java.util.ArrayList;

public class RoutinePagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private RoutineChildFragment fragment1;
    private RoutineChildFragment fragment2;
    private RoutineChildFragment fragment3;
    private RoutineChildFragment fragment4;
    private RoutineChildFragment fragment5;
    private RoutineChildFragment fragment6;
    private RoutineChildFragment fragment7;

    public RoutinePagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                fragment1 = new RoutineChildFragment(0);
                addFragment(fragment1);
                break;
            case 1:
                fragment2 = new RoutineChildFragment(1);
                addFragment(fragment2);
                break;
            case 2:
                fragment3 = new RoutineChildFragment(2);
                addFragment(fragment3);
                break;
            case 3:
                fragment4 = new RoutineChildFragment(3);
                addFragment(fragment4);
                break;
            case 4:
                fragment5 = new RoutineChildFragment(4);
                addFragment(fragment5);
                break;
            case 5:
                fragment6 = new RoutineChildFragment(5);
                addFragment(fragment6);
                break;
            case 6:
                fragment7 = new RoutineChildFragment(6);
                addFragment(fragment7);
                break;
            default:
                return null;
        }

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

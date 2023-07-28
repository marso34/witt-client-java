package com.example.healthappttt.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Home.HomeChildFragment;

import java.util.ArrayList;


public class UserPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<RoutineData> routines;
    private int position;


    public UserPagerAdapter(@NonNull Fragment fragmentActivity, ArrayList<RoutineData> routines) {
        super(fragmentActivity);

        this.routines = routines;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        this.position = position;
        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: addFragment(HomeChildFragment.newInstance(position, routines.get(position))); break;
            default:
                return null;
        }

        return mFragmentList.get(position);
    }

    private void addFragment(HomeChildFragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getItemCount() { return 7; }
    public int getPosition(){
        return position;
    }
}


package com.gwnu.witt.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class WittPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> WFragmentList = new ArrayList<>();
    private int position;

    public WittPagerAdapter(@NonNull WHFragment fragmentActivity) {
        super(fragmentActivity);
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
                addFragment(new WHChildFragment(position)); break;
            default:
                return null;
        }
        return WFragmentList.get(position);
    }
    private void addFragment(WHChildFragment fragment) {
        WFragmentList.add(fragment);
    }
    @Override
    public int getItemCount() {
        return 4;
    }
    public int getPosition() {
        return position;
    }
}

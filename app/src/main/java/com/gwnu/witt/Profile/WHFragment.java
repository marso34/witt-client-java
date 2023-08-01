package com.gwnu.witt.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.gwnu.witt.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WHFragment extends Fragment {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    WittPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                                            @Nullable ViewGroup container,
                                            @Nullable Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_wh, container, false);


        viewPager = view.findViewById(R.id.wh_view_pager);
        tabLayout = view.findViewById(R.id.wh_tab_layout);

        //selectedTablayout();// 텝 레이아웃 xml


        pagerAdapter = new WittPagerAdapter(this);
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);
        pagerAdapter.createFragment(3);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(setText(position))).attach();

        viewPager.setCurrentItem(0, true); //현재 아이템 첫번째 + 부드러운 스크롤

        return view;
    }
    //탭 항목 이름 지정
    private String setText(int position) {
        String period = "";

        switch (position) {
            case 0: period = "전체"; break;
            case 1: period = "1주"; break;
            case 2: period = "1개월"; break;
            case 3: period = "1년"; break;
        }
        return period;
    }

}

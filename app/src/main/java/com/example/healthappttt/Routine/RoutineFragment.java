package com.example.healthappttt.Routine;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.Date;

public class RoutineFragment extends Fragment {
    Context context;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RoutinePagerAdapter pagerAdapter;

    private PreferenceHelper prefhelper;
    private int code;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RoutineFragment() {
    }

    public static RoutineFragment newInstance(String param1, String param2) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_routine, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        if (getArguments() != null) {
            code = getArguments().getInt("code");
        } else {
            prefhelper = new PreferenceHelper("UserTB", getContext());
            code = prefhelper.getPK(); // 나중에 PreferenceHelper 이용해서 유저pk로 수정
            Log.d("루틴 프래그먼트", "유저 키 테스트 " + code);
        }

        pagerAdapter = new RoutinePagerAdapter(this, code);
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);
        pagerAdapter.createFragment(3);
        pagerAdapter.createFragment(4);
        pagerAdapter.createFragment(5);
        pagerAdapter.createFragment(6);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(setText(position))
        ).attach();

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        viewPager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK) - 1, false);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                viewPager.setCurrentItem(tab.getPosition(), false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        애니메이션 삭제 코드, 애니메이션 삭제하면 너무 딱딱하게 느껴짐. 없으면 탭 클릭 시 애니메이션 과함

        return view;
    }

    public String setText(int position) {

        String dayOfWeek = "";

        switch (position) {
            case 0: dayOfWeek = "일"; break;
            case 1: dayOfWeek = "월"; break;
            case 2: dayOfWeek = "화"; break;
            case 3: dayOfWeek = "수"; break;
            case 4: dayOfWeek = "목"; break;
            case 5: dayOfWeek = "금"; break;
            case 6: dayOfWeek = "토"; break;
        }

        return dayOfWeek;
    }
}
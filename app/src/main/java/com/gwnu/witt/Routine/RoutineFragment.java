package com.gwnu.witt.Routine;

import static com.google.android.gms.ads.AdSize.BANNER;
import static com.gwnu.witt.BuildConfig.myBannerAds_id;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.R;

public class RoutineFragment extends Fragment {
    Context context;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RoutinePagerAdapter pagerAdapter;

    private PreferenceHelper prefhelper;

    private AdView mAdview; //애드뷰 변수 선언x


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DAY_OF_WEEK = "dayOfWeek";
    private static final String ARG_CODE = "code";


    // TODO: Rename and change types of parameters
    private int dayOfWeek;
    private int code;

    public RoutineFragment() {
    }

    public static RoutineFragment newInstance(int dayOfWeek, int code) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        args.putInt(ARG_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefhelper = new PreferenceHelper("UserTB", getContext());

        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
            code = getArguments().getInt(ARG_CODE);
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

        pagerAdapter = new RoutinePagerAdapter(this, code);

        for (int i = 0; i < 7; i++)
            pagerAdapter.createFragment(i);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(setText(position))
        ).attach();

        viewPager.setCurrentItem(dayOfWeek, false);

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
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdview = view.findViewById(R.id.adView);
        mAdview.setAdUnitId(myBannerAds_id);
        mAdview.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
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
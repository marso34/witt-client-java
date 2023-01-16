package com.example.healthappttt.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.healthappttt.Activity.CalenderActivity;
import com.example.healthappttt.Activity.CreateRoutineActivity;
import com.example.healthappttt.Activity.ExercizeRecordActivity;
import com.example.healthappttt.Activity.LoginActivity;
import com.example.healthappttt.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //fragment_main에 인플레이션을 함
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        TextView text1 = (TextView) rootView.findViewById(R.id.MainText);
        TextView text2 = (TextView) rootView.findViewById(R.id.SecondText);

        Button StartBtn = (Button) rootView.findViewById(R.id.startBtn); // 운동 시작 버튼
        Button RoutineBtn = (Button) rootView.findViewById(R.id.AddEx);
        Button btn_main = rootView.findViewById(R.id.calender);
        Button loginBtn = rootView.findViewById(R.id.login);
        Button Witt = rootView.findViewById(R.id.Witt);


        String name = "조성현";
        String tname = "운동"; //운동이름
        String hour1 = "N" + "시"; //시간1
        String minute1 = "n" + "분"; //분1
        String hour2 = "M" + "시"; //시간2
        String minute2 = "m" + "분"; //분2

        text1.setText("안녕하세요. " + name + "님!");
        text2.setText("오늘은 " + getCurrentWeek() + ", " + tname+"하는 날이에요.\n" +
                        hour1 + " " + minute1 + "부터 " + hour2 + " " + minute2 + "까지\n열심히 달려볼까요?" );



        StartBtn.setOnClickListener(new View.OnClickListener() { //운동시작 버튼 누를시 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExercizeRecordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });



        RoutineBtn.setOnClickListener(new View.OnClickListener() { //오늘의 운동 추가하기 버튼 누를시 이벤트
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), CreateRoutineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //화면전환 버튼
        btn_main.setOnClickListener(new View.OnClickListener() { //캘린터 버튼 누를시 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalenderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() { //로그인 버튼 누를시 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        Witt.setOnClickListener(new View.OnClickListener() { //위트버튼 누를시 이벤트
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetFragment = new BottomSheetDialog(getActivity());
                bottomSheetFragment.setContentView(R.layout.fragment_bottom);
                bottomSheetFragment.show();
                //bottomSheetFragment.dismiss();
            }
        });
        //화면전환 함수

        return rootView;
    }


    public static String getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (dayOfWeekNumber) {
            case 1 :
                day = "일요일";
                break;
            case 2 :
                day = "월요일";
                break;
            case 3 :
                day = "화요일";
                break;
            case 4 :
                day = "수요일";
                break;
            case 5 :
                day = "목요일";
                break;
            case 6 :
                day = "금요일";
                break;
            case 7 :
                day = "토요일";
                break;
        }

        return day;
    }
}
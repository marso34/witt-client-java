package com.example.healthappttt.Home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthappttt.Data.Exercise.ExerciseComparator;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.Routine.ExerciseAdapter;
import com.example.healthappttt.User.AreaAdapter;
import com.example.healthappttt.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.function.ObjIntConsumer;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";

    private int dayOfWeek;

    private AreaAdapter catAdapter;
    private UserPagerAdapter pagerAdapter;

    private SQLiteUtil sqLiteUtil;
    private ArrayList<RoutineData> routines;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineChildFragment.
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(getContext(), "RT_TB");

        ArrayList<RoutineData> temp = sqLiteUtil.SelectAllRoutine();
        routines = new ArrayList<>();

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 0; i < 7; i++) {
            int j = 0;
            for (RoutineData r : temp) {
                if (r.getDayOfWeek() == i) {
                    routines.add(r);
                    break;
                }
                j++;
            }

            if (j == temp.size())
                routines.add(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pagerAdapter = new UserPagerAdapter(this, routines);

        for (int i = 0; i < 7; i++)
            pagerAdapter.createFragment(i); // 7개(일주일) 페이지어댑터 할당

        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout,  binding.viewPager,
                (tab, position) -> tab.setText(setText(position))
        ).attach();

        setRoutines(routines.get(dayOfWeek)); // 오늘 요일에 맞는 루틴 할당

        binding.viewPager.setCurrentItem(dayOfWeek, false);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("탭 포지션", tab.getPosition() + "");

                setRoutines(routines.get(tab.getPosition())); // 오늘 요일에 맞는 루틴 할당
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
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

    private void setRoutines(RoutineData r) {
        if (r != null) {
            binding.routineLayout.setVisibility(View.VISIBLE);
            binding.emptyTxt.setVisibility(View.GONE);

            switch (r.getTime()) {
                case 0:
                    binding.timeIcon.setImageResource(R.drawable.baseline_brightness_5_24);
                    binding.timeTxt.setText("아침");
                    binding.timeTxt.setTextColor(Color.parseColor(Orange));
                    break;
                case 1:
                    binding.timeIcon.setImageResource(R.drawable.baseline_wb_sunny_24);
                    binding.timeTxt.setText("점심");
                    binding.timeTxt.setTextColor(Color.parseColor(Yellow));
                    break;
                case 2:
                    binding.timeIcon.setImageResource(R.drawable.baseline_brightness_3_24);
                    binding.timeTxt.setText("저녁");
                    binding.timeTxt.setTextColor(Color.parseColor(Blue));
                    break;
                case 3:
                    binding.timeIcon.setImageResource(R.drawable.baseline_flare_24);
                    binding.timeTxt.setText("새벽");
                    binding.timeTxt.setTextColor(Color.parseColor(Purple));
                    break;
            }

            setCatRecyclerView(r.getCat());
        } else {
            binding.routineLayout.setVisibility(View.GONE);
            binding.emptyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void setCatRecyclerView(int cat) {
        ArrayList<String> eCat = new ArrayList<>();

        if ((cat & 0x1)  == 0x1)        eCat.add("가슴");
        if ((cat & 0x2)  == 0x2)        eCat.add("등");
        if ((cat & 0x4)  == 0x4)        eCat.add("어깨");
        if ((cat & 0x8)  == 0x8)        eCat.add("하체");
        if ((cat & 0x10) == 0x10)       eCat.add("팔");
        if ((cat & 0x20) == 0x20)       eCat.add("복근");
        if ((cat & 0x40) == 0x40)       eCat.add("유산소");

        catAdapter = new AreaAdapter(eCat); // 나중에 routine
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.recyclerView.setAdapter(catAdapter);
    }

//    private void setRoutine() {
//        db.collection("routines").document(UserUid +"_" + dayOfWeek).
//                get().
//                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d(TAG, "Document exists!");
//                                routine = new Routine(
//                                        document.getData().get("title").toString(),
//                                        Integer.parseInt(document.getData().get("exerciseCategories").toString()),
//                                        document.getData().get("startTime").toString(),
//                                        document.getData().get("endTime").toString()
//                                );
//
//                                setExercises();
//
//                                StartTime.setText(routine.getStartTime());
//                                EndTime.setText(routine.getEndTime());
//                            } else {
//                                Log.d(TAG, "Document does not exist!");
//                            }
//                        } else {
//                            Log.d(TAG, "Failed with: ", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private void setExercises() {
//        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
//                collection("exercises").
//                get().
//                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                routine.addExercise(new Exercise(
//                                        document.getData().get("title").toString(),
//                                        document.getData().get("state").toString(),
//                                        Integer.parseInt(document.getData().get("count").toString()),
//                                        Integer.parseInt(document.getData().get("volume").toString())
//                                ));
//                            }
//
//                            setRecyclerView();
//
//                        } else {
//                        }
//                    }
//                });
//    }
//
//    private void saveRoutine() {
//        routine.setExerciseCategories(exerciseCat);
//
//        db.collection("routines").document(UserUid +"_" + dayOfWeek).
//                set(routine).
//                addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                }).
//                addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }

//    private void saveExercise(Exercise exercise) {
//        // 루틴을 DB에 저장, document 이름 다시 생각할 것 동일 운동 저장 못 함
//        db.collection("routines").document(UserUid +"_" + dayOfWeek).
//                collection("exercises").document(exercise.getTitle()).
//                set(exercise).
//                addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                }).
//                addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }
//
//    private void deleteExercise(int position) {
//        // db에서 운동 삭제, document 이름 다시 생각할 것 동일 운동 저장 못 함
//
//
//        String cat = routine.getExercises().get(position).getState();
//        int cnt = 0, tCat = 0;
//
//        switch (cat) {
//            case "가슴" : tCat = 0x1; break;
//            case "등" : tCat = 0x2; break;
//            case "어깨" : tCat = 0x4; break;
//            case "하체" : tCat = 0x8; break;
//            case "팔" : tCat = 0x10; break;
//            case "복근" : tCat = 0x20; break;
//            case "유산소" : tCat = 0x40; break;
//        }
//
//
//        for (Exercise e : routine.getExercises()) {
//            if (e.getState().equals(cat))
//                cnt++;
//        }
//
//        if (cnt == 1)
//            exerciseCat ^= tCat;
//
//        db.collection("routines").document(mAuth.getCurrentUser().getUid()+"_"+dayOfWeek).
//                collection("exercises").document(routine.getExercises().get(position).getTitle())
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
//                    }
//                });
//
//        saveRoutine();
//    }

//    private void setRecyclerView() {
//        adapter = new setExerciseAdapter(routine, true); // 나중에 routine
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//        if (adapter != null) {
//            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() {
//                @Override
//                public void onExerciseClick(int postion) {
//                    deleteExercise(postion);
//                    adapter.removeItem(postion);
//                    adapter.notifyDataSetChanged();
//
////                    saveRoutine(routine.getExercises().get(postion));
//                }
//            });
//        }
//    }
//
//    public void clickBtn(View v) {
//        for (Button btn : weekBtn) {
//            btn.setBackgroundResource(R.color.transparent);
//            btn.setTextColor(Color.parseColor("#000000"));
//        }
//
//        ((Button) v).setBackgroundResource(R.drawable.round_button_green);
//        ((Button) v).setTextColor(Color.parseColor("#ffffff") );
//
//        Log.d("test", Integer.toString(v.getId()));
//
//        switch(((Button) v).getText().toString()) {
//            case "일": dayOfWeek = "sun"; break; // 일
//            case "월": dayOfWeek = "mon"; break; // 월
//            case "화": dayOfWeek = "tue"; break; // 화
//            case "수": dayOfWeek = "wed"; break; // 수
//            case "목": dayOfWeek = "thu"; break; // 목
//            case "금": dayOfWeek = "fri"; break; // 금
//            case "토": dayOfWeek = "sat"; break; // 토
//        }
//
//        setRoutine();
//    }

}
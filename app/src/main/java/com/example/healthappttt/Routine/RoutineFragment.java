package com.example.healthappttt.Routine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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
            code = 285; // 나중에 PreferenceHelper 이용해서 유저pk로 수정
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
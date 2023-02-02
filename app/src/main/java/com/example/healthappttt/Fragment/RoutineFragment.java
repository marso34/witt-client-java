package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Activity.MainActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseAdapter;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RoutineFragment extends Fragment {
    Context context;

    private RecyclerView recyclerView;
    private setExerciseAdapter adapter;

    private Button[] weekBtn;
    private ToggleButton[] exerciseTxt;
    private TextView StartTime, EndTime;
    private CardView addCard;

    private Routine routine;
    private ArrayList<Exercise> exercises;

    private String dayOfWeek;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수
    private FirebaseFirestore db;


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

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        weekBtn = new Button[7];
        exerciseTxt = new ToggleButton[7];

        weekBtn[0] = (Button) view.findViewById(R.id.sun); // 일요일부터 배치
        weekBtn[1] = (Button) view.findViewById(R.id.mon);
        weekBtn[2] = (Button) view.findViewById(R.id.tue);
        weekBtn[3] = (Button) view.findViewById(R.id.wed);
        weekBtn[4] = (Button) view.findViewById(R.id.thu);
        weekBtn[5] = (Button) view.findViewById(R.id.fri);
        weekBtn[6] = (Button) view.findViewById(R.id.sat);

        exerciseTxt[0] = (ToggleButton) view.findViewById(R.id.chestTxt);
        exerciseTxt[1] = (ToggleButton) view.findViewById(R.id.backTxt);
        exerciseTxt[2] = (ToggleButton) view.findViewById(R.id.shoulderTxt);
        exerciseTxt[3] = (ToggleButton) view.findViewById(R.id.lowBodyTxt);
        exerciseTxt[4] = (ToggleButton) view.findViewById(R.id.armTxt);
        exerciseTxt[5] = (ToggleButton) view.findViewById(R.id.absTxt);
        exerciseTxt[6] = (ToggleButton) view.findViewById(R.id.cardioTxt);

        StartTime = (TextView) view.findViewById(R.id.startTime);
        EndTime = (TextView) view.findViewById(R.id.endTime);
        addCard = (CardView) view.findViewById(R.id.plusExercise);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        getCurrentWeek();
        setRoutine();

        final ExerciseListFragment exerciseListFragment = new ExerciseListFragment(getContext()); // 운동 부위 정보도 추가

        exerciseListFragment.setOnExerciseClickListener(new ExerciseListFragment.OnExerciseClick() {
            @Override
            public void onExerciseClick(ExerciseName exerciseName) {
                routine.addExercise(new Exercise(exerciseName.getName(), exerciseName.getCat()));
                saveRoutine(new Exercise(exerciseName.getName(), exerciseName.getCat()));
                adapter.notifyDataSetChanged();
            }
        });

        for (Button btn : weekBtn) {
            btn.setOnClickListener(v -> clickBtn(v)); // 요일 버튼
        }

        for (ToggleButton txt : exerciseTxt) { // 운동 부위 버튼
            txt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        compoundButton.setBackgroundResource(R.drawable.green_border_layout);
                        compoundButton.setTextColor(Color.parseColor("#05c78c"));
                    } else {
                        compoundButton.setBackgroundResource(R.drawable.border_layout);
                        compoundButton.setTextColor(Color.parseColor("#747474"));
                    }
                }
            });
        }

        addCard.setOnClickListener(v -> {
//            exerciseListFragment.ExerciseCat("ttt"); // 운동 부위 전달
            exerciseListFragment.show(getActivity().getSupportFragmentManager(), exerciseListFragment.getTag());
        });

        return view;
    }

    public void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 0: weekBtn[0].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[0].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "sun"; break; // 일
            case 1: weekBtn[1].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[1].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "mon";break; // 월
            case 2: weekBtn[2].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[2].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "tue";break; // 화
            case 3: weekBtn[3].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[3].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "wed"; break; // 수
            case 4: weekBtn[4].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[4].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "thu";break; // 목
            case 5: weekBtn[5].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[5].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "fri";break; // 금
            case 6: weekBtn[6].setBackgroundResource(R.drawable.round_button_green);
                    weekBtn[6].setTextColor(Color.parseColor("#ffffff"));
                    dayOfWeek = "sat";break; // 토
        }
    }

    private void setRoutine() {
        // 없으면 빈 루틴 생성
        Log.d("현재 유저 Uid ", mAuth.getCurrentUser().getUid());;

        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
                get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Document exists!");
                                routine = new Routine(
                                        document.getData().get("title").toString(),
                                        document.getData().get("exerciseCategories").toString(),
                                        document.getData().get("startTime").toString(),
                                        document.getData().get("endTime").toString()
                                );

                                setExercises();

                                StartTime.setText(routine.getStartTime());
                                EndTime.setText(routine.getEndTime());
                            } else {
                                Log.d(TAG, "Document does not exist!");
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    }

    private void setExercises() {
        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
                collection("exercises").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                routine.addExercise(new Exercise(
                                        document.getData().get("title").toString(),
                                        document.getData().get("state").toString(),
                                        Integer.parseInt(document.getData().get("count").toString()),
                                        Integer.parseInt(document.getData().get("volume").toString())
                                ));
                            }

                            setRecyclerView();

                        } else {
                        }
                    }
                });
    }

    private void saveRoutine(Exercise exercise) {
        // 루틴을 DB에 저장, document 이름 다시 생각할 것 동일 운동 저장 못 함
        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
                collection("exercises").document(exercise.getTitle()).
                set(exercise).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void deleteExercise(int position) {
        // db에서 운동 삭제, document 이름 다시 생각할 것 동일 운동 저장 못 함
        db.collection("routines").document(mAuth.getCurrentUser().getUid()+"_"+dayOfWeek).
                collection("exercises").document(routine.getExercises().get(position).getTitle())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void setRecyclerView() {
        adapter = new setExerciseAdapter(routine, true); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() {
                @Override
                public void onExerciseClick(int postion) {
                    adapter.notifyDataSetChanged();
                    deleteExercise(postion);
                    adapter.removeItem(postion);
//                    saveRoutine(routine.getExercises().get(postion));
                }
            });
        }
    }

    public void clickBtn(View v) {
        for (Button btn : weekBtn) {
            btn.setBackgroundResource(R.color.transparent);
            btn.setTextColor(Color.parseColor("#000000"));
        }

        ((Button) v).setBackgroundResource(R.drawable.round_button_green);
        ((Button) v).setTextColor(Color.parseColor("#ffffff") );

        Log.d("test", Integer.toString(v.getId()));

        switch(((Button) v).getText().toString()) {
            case "일": dayOfWeek = "sun"; break; // 일
            case "월": dayOfWeek = "mon"; break; // 월
            case "화": dayOfWeek = "tue"; break; // 화
            case "수": dayOfWeek = "wed"; break; // 수
            case "목": dayOfWeek = "thu"; break; // 목
            case "금": dayOfWeek = "fri"; break; // 금
            case "토": dayOfWeek = "sat"; break; // 토
        }

        setRoutine();
    }
}
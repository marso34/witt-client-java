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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    private int dayOfWeek;


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
                adapter.notifyDataSetChanged();
                saveRoutine();
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
            exerciseListFragment.show(getActivity().getSupportFragmentManager(), exerciseListFragment.getTag());
        });

        return view;
    }

    public void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        switch (dayOfWeek) {
            case 0: weekBtn[0].setBackgroundResource(R.drawable.round_button_green); weekBtn[0].setTextColor(Color.parseColor("#ffffff") ); break; // 일
            case 1: weekBtn[1].setBackgroundResource(R.drawable.round_button_green); weekBtn[1].setTextColor(Color.parseColor("#ffffff") ); break; // 월
            case 2: weekBtn[2].setBackgroundResource(R.drawable.round_button_green); weekBtn[2].setTextColor(Color.parseColor("#ffffff") ); break; // 화
            case 3: weekBtn[3].setBackgroundResource(R.drawable.round_button_green); weekBtn[3].setTextColor(Color.parseColor("#ffffff") ); break; // 수
            case 4: weekBtn[4].setBackgroundResource(R.drawable.round_button_green); weekBtn[4].setTextColor(Color.parseColor("#ffffff") ); break; // 목
            case 5: weekBtn[5].setBackgroundResource(R.drawable.round_button_green); weekBtn[5].setTextColor(Color.parseColor("#ffffff") ); break; // 금
            case 6: weekBtn[6].setBackgroundResource(R.drawable.round_button_green); weekBtn[6].setTextColor(Color.parseColor("#ffffff") ); break; // 토
        }
    }

    private void setRoutine() {
        // 없으면 빈 루틴 생성
        Log.d("현재 유저 Uid ", mAuth.getCurrentUser().getUid());

        String str = "";

        switch (dayOfWeek) {
            case 0: str = "sun"; break;
            case 1: str = "mon"; break;
            case 2: str = "tue"; break;
            case 3: str = "wed"; break;
            case 4: str = "thu"; break;
            case 5: str = "fri"; break;
            case 6: str = "sat"; break;
        }

//        db.collection("users").document(mAuth.getCurrentUser().getUid()).
//                collection("routines").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error writing document", task.getException());
//                        }
//                    }
//                });





//
//                document(str).set(new Routine(str,"1")).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "DocumentSnapshot successfully written!");
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//            }
//        });




        // mAuth.getCurrentUser().getUid(), 유저 id와
        // dayOfWeek, 요일 정보를 토대로 DB 접근
        // 루틴 정보 있으면 루틴 생성
        // 없으면 빈 루틴 생성

        routine = new Routine("월", "전신");

        StartTime.setText(routine.getStartTime());
        EndTime.setText(routine.getEndTime());

        setRecyclerView();
    }

    private void saveRoutine() {
        // 루틴을 DB에 저장

    }

    private void setRecyclerView() {
        adapter = new setExerciseAdapter(routine); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
                @Override
                public void onExerciseClick(int postion) { // 운동 기록과 운동 메모를 전달 받아
                    adapter.notifyDataSetChanged();
                    adapter.removeItem(postion);
                    saveRoutine();
                }
            });
        }
    }

    public void clickBtn(View v) {
        for (Button btn : weekBtn) {
            btn.setBackgroundResource(R.color.transparent);
            btn.setTextColor(Color.parseColor("#000000"));
        }

        Button button = (Button) v;

        button.setBackgroundResource(R.drawable.round_button_green);
        button.setTextColor(Color.parseColor("#ffffff") );
        dayOfWeek = Integer.parseInt((String) v.getTag()); // v.getTag가 (int)로 형변환 안 됨 귀찮으니 방법 나중에 찾아보기

        setRoutine();
    }
}
package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class RoutineFragment extends Fragment {
    Context context;

    private Button[] weekBtn;
    private TextView StartTime, EndTime;
    private CardView addCard;

    private RecyclerView recyclerView;
    private setExerciseAdapter adapter;

    private Routine routine;

    private int exerciseCat = 0;
    private String UserUid;
    private String dayOfWeek = "";

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

        UserUid = mAuth.getCurrentUser().getUid();

        weekBtn = new Button[7];

        weekBtn[0] = (Button) view.findViewById(R.id.sun); // 일요일부터 배치
        weekBtn[1] = (Button) view.findViewById(R.id.mon);
        weekBtn[2] = (Button) view.findViewById(R.id.tue);
        weekBtn[3] = (Button) view.findViewById(R.id.wed);
        weekBtn[4] = (Button) view.findViewById(R.id.thu);
        weekBtn[5] = (Button) view.findViewById(R.id.fri);
        weekBtn[6] = (Button) view.findViewById(R.id.sat);

        StartTime = (TextView) view.findViewById(R.id.startTime);
        EndTime = (TextView) view.findViewById(R.id.endTime);
        addCard = (CardView) view.findViewById(R.id.plusExercise);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        getCurrentWeek();
        setRoutine();

        final ExerciseListFragment exerciseListFragment = new ExerciseListFragment(getContext()); // 운동 부위 정보도 추가

        exerciseListFragment.setOnExerciseClickListener(new ExerciseListFragment.OnExerciseClick() {
            @Override
            public void onExerciseClick(ExerciseName exerciseName, int exerCat) {
                exerciseCat |= exerCat;
                routine.addExercise(new Exercise(exerciseName.getName(), exerciseName.getStrCat()));
                saveExercise(new Exercise(exerciseName.getName(), exerciseName.getStrCat()));
                saveRoutine();
                adapter.notifyDataSetChanged();
            }
        });

        for (Button btn : weekBtn) {
            btn.setOnClickListener(v -> clickBtn(v)); // 요일 버튼
        }

        StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d", hourOfDay, minute);

                        StartTime.setText(result);
                        routine.setStartTime(result);
                        saveRoutine();
                    }
                }, 00, 00, true);
                timePickerDialog.setTitle("시작 시간");
                timePickerDialog.show();
            }
        });

        EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d", hourOfDay, minute);

                        EndTime.setText(result);
                        routine.setEndTime(result);
                        saveRoutine();
                    }
                }, 00, 00, true);
                timePickerDialog.setTitle("종료 시간");
                timePickerDialog.show();
            }
        });

        addCard.setOnClickListener(v -> {
//            exerciseListFragment.ExerciseCat(exerciseCat); // 운동 부위 전달
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
        db.collection("routines").document(UserUid +"_" + dayOfWeek).
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
                                        Integer.parseInt(document.getData().get("exerciseCategories").toString()),
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

    private void saveRoutine() {
        routine.setExerciseCategories(exerciseCat);

        db.collection("routines").document(UserUid +"_" + dayOfWeek).
                set(routine).
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

    private void saveExercise(Exercise exercise) {
        // 루틴을 DB에 저장, document 이름 다시 생각할 것 동일 운동 저장 못 함
        db.collection("routines").document(UserUid +"_" + dayOfWeek).
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


        String cat = routine.getExercises().get(position).getState();
        int cnt = 0, tCat = 0;

        switch (cat) {
            case "가슴" : tCat = 0x1; break;
            case "등" : tCat = 0x2; break;
            case "어깨" : tCat = 0x4; break;
            case "하체" : tCat = 0x8; break;
            case "팔" : tCat = 0x10; break;
            case "복근" : tCat = 0x20; break;
            case "유산소" : tCat = 0x40; break;
        }


        for (Exercise e : routine.getExercises()) {
            if (e.getState().equals(cat))
                cnt++;
        }

        if (cnt == 1)
            exerciseCat ^= tCat;

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

        saveRoutine();
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
                    deleteExercise(postion);
                    adapter.removeItem(postion);
                    adapter.notifyDataSetChanged();

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
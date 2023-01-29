package com.example.healthappttt.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.RoutineAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class RoutineFragment extends Fragment {
    Context context;

    private Button weekBtn;
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private ListView ExerciseList;

    private CardView testCard;

    private Routine routine;

    private boolean testetstsetsfe;

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
        weekBtn = (Button) view.findViewById(R.id.mon);
        testCard = (CardView) view.findViewById(R.id.plusExercise);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ExerciseList = (ListView) view.findViewById(R.id.exerciseList);

        setRoutine();
        setRecyclerView();

        testetstsetsfe = false;

        testCard.setOnClickListener(v -> {
            testetstsetsfe = !testetstsetsfe;

            final BottomSheetDialog bottomSheetFragment = new BottomSheetDialog(getContext());
            bottomSheetFragment.setContentView(R.layout.fragment_bottom);
            bottomSheetFragment.show();

            // bottomSheetFragment.dismiss();



//            routine.setExerciseOne();
//            setRecyclerView();
        });


        return view;
    }

    private void setRoutine() {

        // DB 접근해서 요일에 맞는 루틴 가져와서 루틴 생성
        routine = new Routine("월", "전신");
    }

    private void setRecyclerView() {
        adapter = new RoutineAdapter(routine); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
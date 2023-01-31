package com.example.healthappttt.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthappttt.Activity.MainActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseAdapter;
import com.example.healthappttt.adapter.ExerciseListAdapter;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ExerciseListFragment extends BottomSheetDialogFragment {
    Context context;

    private ListView listView;
    private ExerciseListAdapter adapter;

    private OnExerciseClick onExerciseClick;

    public ExerciseListFragment(Context applicationContext) {
            this.context = context;
    } // 운동 부위 정보도 추가


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_bottom, container, false);


        ArrayList<ExerciseName> ttt = new ArrayList<>();
        ttt.add(new ExerciseName("팔굽혀펴기", "가슴"));
        ttt.add(new ExerciseName("스쿼트", "하체"));
        ttt.add(new ExerciseName("딥스", "가슴"));
        ttt.add(new ExerciseName("턱걸이", "등"));
        ttt.add(new ExerciseName("유산소1", "유산소"));
        ttt.add(new ExerciseName("유산소2", "유산소"));
        ttt.add(new ExerciseName("운동1", "가슴"));
        ttt.add(new ExerciseName("운동2", "복근"));
        ttt.add(new ExerciseName("운동3", "하체"));
        ttt.add(new ExerciseName("운동4", "등"));
        ttt.add(new ExerciseName("운동5", "가슴"));
        ttt.add(new ExerciseName("운동6", "복근"));
        ttt.add(new ExerciseName("운동7", "하체"));
        ttt.add(new ExerciseName("운동8", "등"));
        ttt.add(new ExerciseName("운동9", "가슴"));
        ttt.add(new ExerciseName("운동10", "복근"));
        ttt.add(new ExerciseName("운동11", "하체"));
        ttt.add(new ExerciseName("운동12", "등"));
        ttt.add(new ExerciseName("운동13", "하체"));
        ttt.add(new ExerciseName("운동14", "등"));
        ttt.add(new ExerciseName("운동15", "가슴"));
        ttt.add(new ExerciseName("운동16", "복근"));
        ttt.add(new ExerciseName("운동17", "하체"));
        ttt.add(new ExerciseName("운동18", "등"));
        ttt.add(new ExerciseName("운동19", "가슴"));
        ttt.add(new ExerciseName("운동20", "복근"));
        ttt.add(new ExerciseName("운동21", "하체"));
        ttt.add(new ExerciseName("운동22", "등"));

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ExerciseListAdapter(getContext(), ttt);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onExerciseClick.onExerciseClick(adapter.getItem(position));
                dismiss();
            }
        });

        return view;
    }

    private void Parse() {

    }

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(ExerciseName exerciseName);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
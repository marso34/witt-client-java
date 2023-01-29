package com.example.healthappttt.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseListAdapter;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class ExerciseListFragment extends BottomSheetDialogFragment {
    Context context;

    private ListView listView;
    private ExerciseListAdapter adapter;

    public ExerciseListFragment(Context applicationContext) {
            this.context = context;
    } // 운동 부위 정보도 추가


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_bottom, container, false);


        ArrayList<String> ttt = new ArrayList<>();
        ttt.add("운동1");
        ttt.add("운동2");
        ttt.add("운동3");
        ttt.add("운동4");
        ttt.add("운동5");
        ttt.add("운동6");
        ttt.add("운동7");
        ttt.add("운동8");
        ttt.add("운동9");
        ttt.add("운동10");
        ttt.add("운동1");
        ttt.add("운동2");
        ttt.add("운동3");
        ttt.add("운동4");
        ttt.add("운동5");
        ttt.add("운동6");
        ttt.add("운동7");
        ttt.add("운동8");
        ttt.add("운동9");
        ttt.add("운동10");
        ttt.add("운동6");
        ttt.add("운동7");
        ttt.add("운동8");
        ttt.add("운동9");
        ttt.add("운동10");
        ttt.add("운동1");
        ttt.add("운동2");
        ttt.add("운동3");
        ttt.add("운동4");
        ttt.add("운동5");
        ttt.add("운동6");
        ttt.add("운동7");
        ttt.add("운동8");
        ttt.add("운동9");
        ttt.add("운동10");

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ExerciseListAdapter(getContext(), ttt);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                adapter.getItem(position).
            }
        });

        return view;
    }



    private void Parse() {

    }
}
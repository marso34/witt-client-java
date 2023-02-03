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
import android.widget.ToggleButton;

import com.example.healthappttt.Activity.MainActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class ExerciseListFragment extends BottomSheetDialogFragment {
    Context context;

    private int exerciseCat = 0;

    private ListView listView;
    private ExerciseListAdapter adapter;

    private OnExerciseClick onExerciseClick;

    public ExerciseListFragment(Context applicationContext) {
            this.context = context;
    }

    // 여기는 그냥 동작만 가능하도록 대충 짜고 있고 정리 안 됨

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
        ttt.add(new ExerciseName("가슴운동1", "가슴"));
        ttt.add(new ExerciseName("어깨운동1", "어깨"));
        ttt.add(new ExerciseName("등운동1", "등"));
        ttt.add(new ExerciseName("하체운동1", "하체"));
        ttt.add(new ExerciseName("팔운동1", "팔"));
        ttt.add(new ExerciseName("복근운동1", "복근"));
        ttt.add(new ExerciseName("유산소운동1", "유산소"));
        ttt.add(new ExerciseName("가슴운동2", "가슴"));
        ttt.add(new ExerciseName("어깨운동2", "어깨"));
        ttt.add(new ExerciseName("등운동2", "등"));
        ttt.add(new ExerciseName("하체운동2", "하체"));
        ttt.add(new ExerciseName("팔운동2", "팔"));
        ttt.add(new ExerciseName("복근운동2", "복근"));
        ttt.add(new ExerciseName("유산소운동2", "유산소"));
        ttt.add(new ExerciseName("가슴운동3", "가슴"));
        ttt.add(new ExerciseName("어깨운동3", "어깨"));
        ttt.add(new ExerciseName("등운동3", "등"));
        ttt.add(new ExerciseName("하체운동3", "하체"));
        ttt.add(new ExerciseName("팔운동3", "팔"));
        ttt.add(new ExerciseName("복근운동3", "복근"));
        ttt.add(new ExerciseName("유산소운동3", "유산소"));

        ArrayList<ExerciseName> test = new ArrayList<>();

        for (ExerciseName e : ttt) {
            if ((exerciseCat & 0x1) == 0x1) {
                if (e.getCat().equals("가슴"))
                    test.add(e);
            }
            if ((exerciseCat & 0x2) == 0x2) {
                if (e.getCat().equals("등"))
                    test.add(e);
            }
            if ((exerciseCat & 0x4) == 0x4) {
                if (e.getCat().equals("어깨"))
                    test.add(e);
            }
            if ((exerciseCat & 0x8) == 0x8) {
                if (e.getCat().equals("하체"))
                    test.add(e);
            }
            if ((exerciseCat & 0x10) == 0x10) {
                if (e.getCat().equals("팔"))
                    test.add(e);
            }
            if ((exerciseCat & 0x20) == 0x20) {
                if (e.getCat().equals("복근"))
                    test.add(e);
            }
            if ((exerciseCat & 0x40) == 040) {
                if (e.getCat().equals("유산소"))
                    test.add(e);
            }
        }

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ExerciseListAdapter(getContext(), test);

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

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        bottomSheetDialog.setContentView(R.layout.fragment_bottom);

        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    } // BottomSheetDialogFragment 드래그 안 되게 하는 코드

    public void ExerciseCat(int cat) {
        exerciseCat = cat;
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
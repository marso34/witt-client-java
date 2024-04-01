package com.gwnu.witt.WorkOut;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.R;
import com.gwnu.witt.Routine.ExerciseListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ExerciseListFragment extends BottomSheetDialogFragment {
    Context context;

    private int exerciseCat = 0;

    private ToggleButton[] exerciseTxt;
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

        exerciseTxt = new ToggleButton[7];

        exerciseTxt[0] = (ToggleButton) view.findViewById(R.id.chestTxt);
        exerciseTxt[1] = (ToggleButton) view.findViewById(R.id.backTxt);
        exerciseTxt[2] = (ToggleButton) view.findViewById(R.id.shoulderTxt);
        exerciseTxt[3] = (ToggleButton) view.findViewById(R.id.lowBodyTxt);
        exerciseTxt[4] = (ToggleButton) view.findViewById(R.id.armTxt);
        exerciseTxt[5] = (ToggleButton) view.findViewById(R.id.absTxt);
        exerciseTxt[6] = (ToggleButton) view.findViewById(R.id.cardioTxt);



        ArrayList<ExerciseData> ttt = new ArrayList<>(); // 임시
//        ttt.add(new ExerciseName("팔굽혀펴기", 0x1));
//        ttt.add(new ExerciseName("스쿼트", 0x8));
//        ttt.add(new ExerciseName("딥스", 0x1));
//        ttt.add(new ExerciseName("턱걸이", 0x2));
//        ttt.add(new ExerciseName("달리기", 0x40));
//
//        ttt.add(new ExerciseName("가슴운동1", 0x1));
//        ttt.add(new ExerciseName("가슴운동2", 0x1));
//        ttt.add(new ExerciseName("가슴운동3", 0x1));
//
//
//        ttt.add(new ExerciseName("등운동1", 0x2));
//        ttt.add(new ExerciseName("등운동2", 0x2));
//        ttt.add(new ExerciseName("등운동3", 0x2));
//
//        ttt.add(new ExerciseName("어깨운동1", 0x4));
//        ttt.add(new ExerciseName("어깨운동2", 0x4));
//        ttt.add(new ExerciseName("어깨운동3", 0x4));
//
//        ttt.add(new ExerciseName("하체운동1", 0x8));
//        ttt.add(new ExerciseName("하체운동2", 0x8));
//        ttt.add(new ExerciseName("하체운동3", 0x8));
//
//        ttt.add(new ExerciseName("팔운동1", 0x10));
//        ttt.add(new ExerciseName("팔운동2", 0x10));
//        ttt.add(new ExerciseName("팔운동3", 0x10));
//
//        ttt.add(new ExerciseName("복근운동1", 0x20));
//        ttt.add(new ExerciseName("복근운동2", 0x20));
//        ttt.add(new ExerciseName("복근운동3", 0x20));
//
//        ttt.add(new ExerciseName("유산소운동1", 0x40));
//        ttt.add(new ExerciseName("유산소운동2", 0x40));
//        ttt.add(new ExerciseName("유산소운동3", 0x40));

        ArrayList<ExerciseData> test = new ArrayList<>(); // 임시



        for (ToggleButton txt : exerciseTxt) { // 운동 부위 버튼
            txt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        switch(compoundButton.getText().toString()) {
                            case "가슴":
                                exerciseCat = 0x1; break;
                            case "등":
                                exerciseCat = 0x2; break;
                            case "어깨":
                                exerciseCat = 0x4; break;
                            case "하체":
                                exerciseCat = 0x8; break;
                            case "팔":
                                exerciseCat = 0x10; break;
                            case "복근":
                                exerciseCat = 0x20; break;
                            case "유산소":
                                exerciseCat = 0x40; break;
                        }

                        compoundButton.setBackgroundResource(R.drawable.green_border_layout);
                        compoundButton.setTextColor(Color.parseColor("#05c78c"));

                        for (ToggleButton txt : exerciseTxt) {
                            if (txt.equals(compoundButton)) {
                                continue;
                            }

                            txt.setChecked(false);
                        }
                    } else {
                        compoundButton.setBackgroundResource(R.drawable.border_layout);
                        compoundButton.setTextColor(Color.parseColor("#747474"));
                    }

                    test.clear();
                    for (ExerciseData e : ttt) {
                        if (exerciseCat == e.getCat())
                            test.add(e);
                    }

                    adapter.notifyDataSetChanged();
                }
            });
        }


        listView = (ListView) view.findViewById(R.id.listView);
//        adapter = new ExerciseListAdapter(test);

//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                onExerciseClick.onExerciseClick(adapter.getItem(position), exerciseCat);
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

    private void Parse() {

    }

    private void setList(int cat, boolean isChecked) {


    }

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(ExerciseData exerciseName, int exerciseCat);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
package com.gwnu.witt.Routine;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.ExerciseComparator;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.Data.pkData;
import com.example.healthappttt.R;
import com.gwnu.witt.User.AreaAdapter;
import com.gwnu.witt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;

    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private ArrayList<RoutineData> routines;
    private int attribute; // == 0 내 루틴, attribute > 0 운동 기록할 때 선택용, attribute < 0 남의 루틴

    private OnClickRoutine onClickRoutine;

    /**
     * @attribute attribute(운동 기록) > 0  or attribute == 0(내 루틴) or attribute < 0(다른 사람 루틴)
     */
    public RoutineAdapter(Context context, ArrayList<RoutineData> routines, int attribute) {
        this.routines = routines;
        this.context = context;
        this.attribute = attribute;

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, NullLayout;
        public ImageView TimeIcon, EditBtn;
        public TextView DayOfWeekTxt, TimeTxt;

        public RecyclerView CatRecyclerView;
        public AreaAdapter CatAdapter;

        public RecyclerView ExRecyclerView;
        public ExerciseAdapter ExAdapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = view.findViewById(R.id.routineLayout);
            this.NullLayout = view.findViewById(R.id.nullLayout);

            this.TimeIcon = view.findViewById(R.id.timeIcon);
            this.TimeTxt = view.findViewById(R.id.timeTxt);
            this.EditBtn = view.findViewById(R.id.editBtn);
            this.DayOfWeekTxt = view.findViewById(R.id.dayOfWeek);

            this.CatRecyclerView = view.findViewById(R.id.catRecyclerView);
            this.ExRecyclerView = view.findViewById(R.id.exRecyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.EditBtn.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View bottomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_edit_popup, null);

            TextView edit = bottomView.findViewById(R.id.edit);
            TextView deleteBtn = bottomView.findViewById(R.id.delete);

            bottomSheetDialog.setContentView(bottomView);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mainViewHolder.getAbsoluteAdapterPosition();
                    onClickRoutine.onClickRoutine(routines.get(position));
                    bottomSheetDialog.dismiss();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteToDB(position); // "예" 클릭시 삭제
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.show();
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.NullLayout.setVisibility(View.GONE);

            switch (routines.get(position).getTime()) {
                case 0:
                    holder.TimeIcon.setImageResource(R.drawable.baseline_brightness_5_24);
                    holder.TimeTxt.setText("아침");
                    holder.TimeTxt.setTextColor(Color.parseColor(Orange));
                    break;
                case 1:
                    holder.TimeIcon.setImageResource(R.drawable.baseline_wb_sunny_24);
                    holder.TimeTxt.setText("점심");
                    holder.TimeTxt.setTextColor(Color.parseColor(Yellow));
                    break;
                case 2:
                    holder.TimeIcon.setImageResource(R.drawable.baseline_brightness_3_24);
                    holder.TimeTxt.setText("저녁");
                    holder.TimeTxt.setTextColor(Color.parseColor(Blue));
                    break;
                case 3:
                    holder.TimeIcon.setImageResource(R.drawable.baseline_flare_24);
                    holder.TimeTxt.setText("새벽");
                    holder.TimeTxt.setTextColor(Color.parseColor(Purple));
                    break;
            }

            if (attribute == 0) { // 내 루틴

            } else if (attribute > 0) { // 운동 기록
                holder.EditBtn.setVisibility(View.GONE);
            } else { // 다른 사람 루틴
                holder.EditBtn.setVisibility(View.GONE);
                holder.DayOfWeekTxt.setVisibility(View.VISIBLE);
                holder.DayOfWeekTxt.setText(getDayOfWeek(routines.get(position).getDayOfWeek()));
            }

            setCatRecyclerView(holder.CatRecyclerView, holder.CatAdapter, position);
            setRecyclerView(holder.ExRecyclerView, holder.ExAdapter, position);
        } else {
            holder.RoutineLayout.setVisibility(View.GONE);
            holder.NullLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (routines.size() == 0)   return 1;

        return routines.size();
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0: return "일";
            case 1: return "월";
            case 2: return "화";
            case 3: return "수";
            case 4: return "목";
            case 5: return "금";
            case 6: return "토";
        }

        return "";
    }

    private void DeleteToDB(int position) {
        Log.d("루틴 포지션", position + "");

        Log.d("루틴 아이디", routines.get(position).getID() + "");

        service.deleteRoutine(new pkData(routines.get(position).getID())).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body() == 200) {
                    Log.d("성공", "루틴 삭제 성공");
                    DeleteToDev(position);
                    removeItem(position);
//                    Terminate(true, 2); // 루틴 삭제를 의미
                } else {
                    Toast.makeText(context, "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "루틴 삭제 실패");
//                    Terminate(false);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
//                Terminate(false);
            }
        });
    }

    private void DeleteToDev(int position) {
        sqLiteUtil.setInitView(context, "RT_TB");
        sqLiteUtil.delete(routines.get(position).getID());
    }

    public void removeItem(int position) {
        routines.remove(position);
        notifyItemRemoved(position);
    }

    private void setRecyclerView(RecyclerView recyclerView, ExerciseAdapter adapter, int positon) {
        ArrayList<ExerciseData> e = (ArrayList<ExerciseData>) routines.get(positon).getExercises();
        Collections.sort(e, new ExerciseComparator());

        adapter = new ExerciseAdapter(e);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void setCatRecyclerView(RecyclerView recyclerView, AreaAdapter adapter, int positon) {
        ArrayList<String> eCat = new ArrayList<>();
        int cat = routines.get(positon).getCat();

        if ((cat & 0x1)  == 0x1)        eCat.add("가슴");
        if ((cat & 0x2)  == 0x2)        eCat.add("등");
        if ((cat & 0x4)  == 0x4)        eCat.add("어깨");
        if ((cat & 0x8)  == 0x8)        eCat.add("하체");
        if ((cat & 0x10) == 0x10)       eCat.add("팔");
        if ((cat & 0x20) == 0x20)       eCat.add("복근");
        if ((cat & 0x40) == 0x40)       eCat.add("유산소");

        adapter = new AreaAdapter(eCat); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickRoutineListener(OnClickRoutine onClickRoutineListener) {
        this.onClickRoutine = onClickRoutineListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnClickRoutine {
        void onClickRoutine(RoutineData r);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
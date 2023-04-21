package com.example.healthappttt.adapter;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class setExerciseAdapter extends RecyclerView.Adapter<setExerciseAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;
    private boolean isChangeable;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수
    private FirebaseFirestore db;

    private OnExerciseClick onExerciseClick;

    public setExerciseAdapter(Routine routine) {
        this.exercises = routine.getExercises();
        this.isChangeable = false;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public setExerciseAdapter(Routine routine, boolean isChangeable) {
        this.exercises = routine.getExercises();
        this.isChangeable = isChangeable;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public setExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        this.isChangeable = false;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
//        this.exerciseCategories = routine.getExerciseCategories();
    }

    public setExerciseAdapter(ArrayList<Exercise> exercises, boolean isChangeable) { // 일단 테스트
        this.exercises = exercises;
        this.isChangeable = isChangeable;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
//        this.exerciseCategories = routine.getExerciseCategories();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout EditLayout;
        public TextView CatView, NameView, DetailView, EnterView;
        public ImageView DelImageVIew;
        public EditText EditVolume, EditCount;

        public String DetailViewTxt;

        public MainViewHolder(View view) {
            super(view);

            this.EditLayout = (LinearLayout) view.findViewById(R.id.editDetail);
            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
            this.DelImageVIew = (ImageView) view.findViewById(R.id.delBtn);

            this.EnterView = (TextView) view.findViewById(R.id.enter);
            this.EditVolume = (EditText) view.findViewById(R.id.editVolume);
            this.EditCount = (EditText) view.findViewById(R.id.editCount);

            this.DetailViewTxt = null;
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_set_exercise, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChangeable) {
                    mainViewHolder.EditLayout.setVisibility(View.VISIBLE);
                    mainViewHolder.DetailView.setVisibility(View.GONE);
                }
            }
        });

        mainViewHolder.DelImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mainViewHolder.getAdapterPosition();
                onExerciseClick.onExerciseClick(position);
            }
        });

        mainViewHolder.EnterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAdapterPosition();

                mainViewHolder.DetailView.setVisibility(View.VISIBLE);
                setTxt(mainViewHolder);
                mainViewHolder.DetailView.setText(mainViewHolder.DetailViewTxt);
                mainViewHolder.EditLayout.setVisibility(View.GONE);

                Date currentDate = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                String dayOfWeek = "";

                switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
                    case 0: dayOfWeek = "sun"; break; // 일
                    case 1: dayOfWeek = "mon";break; // 월
                    case 2: dayOfWeek = "tue";break; // 화
                    case 3: dayOfWeek = "wed"; break; // 수
                    case 4: dayOfWeek = "thu";break; // 목
                    case 5: dayOfWeek = "fri";break; // 금
                    case 6: dayOfWeek = "sat";break; // 토
                }

                db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
                        collection("exercises").document(exercises.get(position).getTitle()).
                        set(exercises.get(position)).
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
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        setTxt(holder);

        holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
        holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
        holder.DetailView.setText(holder.DetailViewTxt);

        if (!isChangeable)
            holder.DelImageVIew.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() { return exercises.size(); }

    public void removeItem(int position) { exercises.remove(position); }

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();

        if (holder.DetailViewTxt == null) {
            int count = this.exercises.get(position).getCount();
            int volume = this.exercises.get(position).getVolume();

            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        } else {
            String volume = holder.EditVolume.getText().toString();
            String count = holder.EditCount.getText().toString();

            this.exercises.get(position).setVolume(Integer.parseInt(volume));
            this.exercises.get(position).setCount(Integer.parseInt(count));

            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        }
    }

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(int position);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}

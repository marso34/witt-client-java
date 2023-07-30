package com.example.healthappttt.Routine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.healthappttt.Chat.ReviewActivity;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.GetRoutine;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.Exercise.RoutineComparator;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.R;
import com.example.healthappttt.SplashActivity;
import com.example.healthappttt.databinding.FragmentRoutineChildBinding;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineChildFragment extends Fragment {
    FragmentRoutineChildBinding binding;
    private ActivityResultLauncher<Intent> startActivityResult;
    private RoutineAdapter adapter;

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper prefhelper;

    private ArrayList<RoutineData> routines;
    private int dayOfWeek, code;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RoutineChildFragment() {}
    
    public RoutineChildFragment(final int dayOfWeek, int code) {
        this.dayOfWeek = dayOfWeek;
        this.code = code;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoutineChildFragment newInstance(String param1, String param2) {
        RoutineChildFragment fragment = new RoutineChildFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutineChildBinding.inflate(inflater);

        startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult data) {
                Log.d("TAG", "data : " + data);

                if (data.getResultCode() == Activity.RESULT_OK && data.getData() != null) {
                    Intent intent = data.getData();
                    RoutineData r = (RoutineData) intent.getSerializableExtra("routine");
                    int check = intent.getIntExtra("check", 0);

                    if (check == 0) {
                        for (int i = 0; i < routines.size(); i++) {
                            if (routines.get(i).getID() == r.getID()) {
                                routines.get(i).setTime(r.getTime());
                                routines.get(i).setCat(r.getCat());
                                routines.get(i).setExercises(r.getExercises());
                                break;
                            }
                        }
                    }
                    else if (check == 1) {
                        routines.add(r); // 루틴 추가
                        binding.addRoutine.setVisibility(View.GONE);
                    }

                    Collections.sort(routines, new RoutineComparator());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        prefhelper = new PreferenceHelper("UserTB", getContext());
        sqLiteUtil = SQLiteUtil.getInstance();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        routines = new ArrayList<>();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (code == prefhelper.getPK()) { // 내 루틴 표시
            sqLiteUtil.setInitView(getContext(), "RT_TB");

            routines = sqLiteUtil.SelectRoutine(dayOfWeek);

            if (routines != null) {
                for (int i = 0; i < routines.size(); i++) {
                    sqLiteUtil.setInitView(getContext(), "EX_TB");
                    routines.get(i).setExercises(sqLiteUtil.SelectExercise(routines.get(i).getID(), true));
                }

                setRecyclerView(0);

                if (routines.size() != 0)
                    binding.addRoutine.setVisibility(View.GONE);
            }
        } else { // 남의 루틴 표시, 여기는 서버에서 받아오는 코드
            binding.addRoutine.setVisibility(View.GONE);


            service.selectRoutine(new GetRoutine(code, dayOfWeek)).enqueue(new Callback<List<RoutineData>>() {
                @Override
                public void onResponse(Call<List<RoutineData>> call, Response<List<RoutineData>> response) {
                    if (response.isSuccessful()) {
                        Log.d("성공", "루틴 불러오기 성공");

                        routines = (ArrayList<RoutineData>) response.body();
                        for (int i = 0; i < response.body().size(); i++)
                            routines.get(i).setExercises(response.body().get(i).getExercises());

                        setRecyclerView(-1);
//                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "루틴 불러오기 실패!!!", Toast.LENGTH_SHORT).show();
                        Log.d("실패", "루틴 불러오기 실패");
                    }
                }

                @Override
                public void onFailure(Call<List<RoutineData>> call, Throwable t) {
                    Toast.makeText(getContext(), "서버 연결 실패..", Toast.LENGTH_SHORT).show();
                    Log.d("서버 연결 실패", t.getMessage());
                }
            });
        }

        binding.addRoutine.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateRoutineActivity.class);
            intent.putExtra("dayOfWeek", dayOfWeek);
//            Intent intent = new Intent(getContext(), ReviewActivity.class);
//            intent.putExtra("name", "이준");
//            intent.putExtra("code", 2);

            startActivityResult.launch(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기
    }

    private void DeleteToDB(int pk) {
        Log.d("루틴 아이디", pk + "");

        int position = 0;

        for (int i = 0; i < routines.size(); i++) {
            if (routines.get(i).getID() == pk)
                position = i;
        }

        int finalPosition = position;
        service.deleteRoutine(new pkData(pk)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body() == 200) {
                    Log.d("성공", "루틴 삭제 성공");
                    DeleteToDev(finalPosition);
                    adapter.removeItem(finalPosition);
                    binding.addRoutine.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "루틴 삭제 실패");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
//                Terminate(false);
            }
        });
    }

    private void DeleteToDev(int position) {
        sqLiteUtil.setInitView(getContext(), "RT_TB");
        sqLiteUtil.delete(routines.get(position).getID());
    }

    private void setRecyclerView(int attribute) {
        Collections.sort(routines, new RoutineComparator());

        adapter = new RoutineAdapter(getContext(), routines, attribute);  // attribute = code가 내 코드면 0, 아니면 -1
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnClickRoutineListener(new RoutineAdapter.OnClickRoutine() {
                @Override
                public void onClickRoutine(RoutineData r) {
                    Intent intent = new Intent(getContext(), EditRoutineActivity.class);
                    intent.putExtra("routine", r);
                    startActivityResult.launch(intent);
                }
            });
        }
    }
}
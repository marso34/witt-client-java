package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityReportHistoryBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportHistoryActivity extends AppCompatActivity {

    private ActivityReportHistoryBinding binding;
    private ServiceApi apiService;


    ReportHistory reportHistory;
    ArrayList<ReportHistory> ReportList;
//   BlockUserAdapter ReportAdapter;
//    RecyclerView recyclerView;
//    LinearLayoutManager linearLayoutManager;
    int PK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);
        binding = ActivityReportHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        //recyclerView = findViewById(R.id.report_recycle);

        ReportList = new ArrayList<>();

        Intent intent = getIntent();
        PK = Integer.parseInt(intent.getStringExtra("PK"));//넘겨 받은 PK
        UserKey userKey = new UserKey(PK);

        getReportList(userKey);

    }

    private void getReportList(UserKey userKey) {
        Call<List<ReportHistory>> call = apiService.getReport(userKey);
        call.enqueue(new Callback<List<ReportHistory>>() {
            @Override
            public void onResponse(Call<List<ReportHistory>> call, Response<List<ReportHistory>> response) {
                if(response.isSuccessful()) {
                    List<ReportHistory> list = response.body();
                    if(list != null) {
                        for (ReportHistory rpt : list) {
                            // 처리 로직
                            Log.d("ReportHistory데이터 ", String.valueOf(rpt.getCONT()));
                            int CONT = rpt.getCONT();
                            reportHistory = new ReportHistory(CONT);

                            ReportList.add(reportHistory);
                        }
                        selectCONT(ReportList);
                        //setView();

                    } else { Log.d("getReportList","리스트가 null"); }
                } else { Log.e("getReportList", "응답 오류 응답 코드: " + response.code()); }
            }

            @Override
            public void onFailure(Call<List<ReportHistory>> call, Throwable t) {
                Log.e("getReportList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });

    }

//    public void setView() {
//        textLineAdpater = new TextLineAdpater(ReportList);
//        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(textLineAdpater);
//        textLineAdpater.notifyDataSetChanged();
//    }

    public void selectCONT(ArrayList<ReportHistory> ReportList ){
        TextView listCnt0 = findViewById(R.id.listCnt0);
        TextView listCnt1 = findViewById(R.id.listCnt1);
        TextView listCnt2 = findViewById(R.id.listCnt2);
        TextView listCnt3 = findViewById(R.id.listCnt3);
        TextView listCnt4 = findViewById(R.id.listCnt4);
        TextView listCnt5 = findViewById(R.id.listCnt5);


        int[] ReportCNT = {0,0,0,0,0,0};
        for(ReportHistory cont : ReportList ) {
            bitcl(cont.getCONT(),ReportCNT);
        }

        // 텍스트뷰들을 배열에 저장
        TextView[] textViews = {listCnt5, listCnt4, listCnt3, listCnt2, listCnt1, listCnt0};

        for (int i = 5; i >= 0; i--) {
            String value = String.valueOf(ReportCNT[i]);
            Log.d("value ",value);
            TextView textView = textViews[i];
            View view = binding.getRoot().findViewById(
                i == 5 ? R.id.r0 :
                i == 4 ? R.id.r1 :
                i == 3 ? R.id.r2 :
                i == 2 ? R.id.r3 :
                i == 1 ? R.id.r4 :
                R.id.r5
            );

            if (value.equals("0")) {
                view.setVisibility(View.GONE);
            } else {
                textView.setText(value);
            }
        }

//        listCnt0.setText(String.valueOf(ReportCNT[5]));
//        listCnt1.setText(String.valueOf(ReportCNT[4]));
//        listCnt2.setText(String.valueOf(ReportCNT[3]));
//        listCnt3.setText(String.valueOf(ReportCNT[2]));
//        listCnt4.setText(String.valueOf(ReportCNT[1]));
//        listCnt5.setText(String.valueOf(ReportCNT[0]));

        Log.d("비트연산 최종: ", String.valueOf(ReportCNT[0]) + String.valueOf(ReportCNT[1])+ String.valueOf(ReportCNT[2]) +
                String.valueOf(ReportCNT[3]) + String.valueOf(ReportCNT[4]) + String.valueOf(ReportCNT[5])
        );

    }

    //2진수 해석
    public void bitcl(int num, int[] array) {
        int length = (int) (Math.log(num) / Math.log(2)) + 1;
        String binary = Integer.toBinaryString(num);
        int n = 6;

        String formatnum = String.format("%0"+ n+"d", Integer.parseInt(binary));
        //비트연산
        for (int i = 5; 0 <= i; i--) {
            char bit = formatnum.charAt(i);
            if (bit == '1') {
                array[i]++;
            }
        }
    }


}
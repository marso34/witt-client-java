package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportHistoryActivity extends AppCompatActivity {

    private ServiceApi apiService;

    androidx.appcompat.widget.SearchView searchView;
    ArrayList<ReportHistory> ReportList,filteredList;
    ReportHistory reportListdata;

    ArrayList<BlackListData> BlackList;
    ArrayList<WittListData> WittList;
    ArrayList<ReviewListData> ReviewList;

    BlockUserAdapter ReportAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String PK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        apiService = RetrofitClient.getClient().create(ServiceApi.class);

        searchView = findViewById(R.id.report_search);
        recyclerView = findViewById(R.id.report_recycle);

        filteredList = new ArrayList<>();

        Intent intent = getIntent();
        PK = intent.getStringExtra("PK");//넘겨 받은 PK
        ReportList = new ArrayList<>();
        int PKI = Integer.parseInt(PK);
        UserKey userKey = new UserKey(PKI);

        getReportList(userKey);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText);
                return false;
            }
        });
    }

    private void getReportList(UserKey userKey) {
        Call<List<ReportHistory>> call = apiService.getReport(userKey);
        call.enqueue(new Callback<List<ReportHistory>>() {
            @Override
            public void onResponse(Call<List<ReportHistory>> call, Response<List<ReportHistory>> response) {
                if(response.isSuccessful()) {
                    List<ReportHistory> list = response.body();
                    if(list != null) {
                        for(ReportHistory report : list) {
                            Log.d("reportlist 데이터: " , report.getUser_NM());
                            int RPT_CAT_FK = report.getRPT_CAT_FK();
                            String User_NM = report.getUser_NM();
                            String CONT = report.getCONT();
                            String TS = report.getTS();
                            String GYM_NM = report.getGYM_NM();
                            reportListdata = new ReportHistory(RPT_CAT_FK,User_NM,CONT,TS,GYM_NM);

                            ReportList.add(reportListdata);
                        }

                        setView();

                    } else { Log.d("getReportList","리스트가 null"); }
                } else { Log.e("getReportList", "응답 오류 응답 코드: " + response.code()); }
            }

            @Override
            public void onFailure(Call<List<ReportHistory>> call, Throwable t) {
                Log.e("getReportList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });

    }

    public void setView() {
        ReportAdapter = new BlockUserAdapter(BlackList, ReviewList, WittList, ReportList, this);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ReportAdapter);
        ReportAdapter.notifyDataSetChanged();
    }
    public void searchFilter(String searchText) {
        filteredList.clear();

        for(int i = 0; i < ReportList.size(); i++){
            if(ReportList.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(ReportList.get(i));
            }
        }
        ReportAdapter.filterReportList(filteredList);
    }


}
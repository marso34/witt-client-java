package com.example.healthappttt.Profile;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WHChildFragment extends Fragment {

    private int period;
    private ArrayList<WittListData> WittList, filteredList;
    private ArrayList<WittListData> oneWeekAgoList, oneMonthAgoList, oneYearAgoList;
    ArrayList<BlackListData> BlackList;
    ArrayList<ReviewListData> ReviewList;
    ArrayList<ReportHistory> ReportList;
    private List<String> dateList;
    WittListData wittListData;
    BlockUserAdapter WittHistoryAdapter;

    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    SQLiteUtil sqLiteUtil;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private boolean updating = false;
    private boolean topScrolled;
    Calendar calendar= Calendar.getInstance();
    Date today = calendar.getTime();
    Date oneWeekAgo, oneMonthAgo, oneYearAgo;
    String myPK,PK;

    public WHChildFragment() {}

    public WHChildFragment(int period) {//position
        this.period = period;
        WittList = new ArrayList<>();
        //Log.d("getFragment", String.valueOf(getFragment()));
    }

    public static WHChildFragment newInstance(int number){
        WHChildFragment wf = new WHChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        wf.setArguments(bundle);
        return wf;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserTB = new PreferenceHelper("UserTB", getContext());

        Intent intent = getActivity().getIntent();
        PK = intent.getStringExtra("PK");//넘겨 받은 PK
        myPK = String.valueOf(UserTB.getPK());// 로컬 내 PK

        filteredList = new ArrayList<>();
        oneWeekAgoList = new ArrayList<>();//1주                   날짜 비교하여 넣어지는 리스트
        oneMonthAgoList = new ArrayList<>();//1개월
        oneYearAgoList = new ArrayList<>();//1년

        if (PK.equals(myPK)) { //나의 위트 내역
            Log.d("나의 위트내역 ", PK + " - " + myPK);
            sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
            sqLiteUtil.setInitView(getContext(),"Witt_History_TB");//위트내역 목록 로컬 db
            WittList = sqLiteUtil.SelectWittHistoryUser();//SELECT * FROM Witt_History_TB ORDER BY TS DESC

            setView();
        } else { //상대 위트 내역
            Log.d("상대 위트내역 상대 pk: ", PK + " - " + myPK);
            apiService = RetrofitClient.getClient().create(ServiceApi.class);
            WittList = new ArrayList<>();
            int PKI = Integer.parseInt(PK);
            UserKey userKey = new UserKey(PKI);

            getWittHistory(userKey);
        }



//        Log.d("filerWittList1",filerWittList1.get(0).getTS());
//        Log.d("filerWittList2",filerWittList2.get(0).getTS());
//        Log.d("filerWittList3",filerWittList3.get(0).getTS());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_whchild_fragment, container, false);
        recyclerView = view.findViewById(R.id.wh_recyclerView);
        searchView = getActivity().findViewById(R.id.black_search);

//        if (!PK.equals(myPK)) {
//            Log.d("상대 위트내역 상대 pk: ", PK + " - " + myPK);
//            apiService = RetrofitClient.getClient().create(ServiceApi.class);
//            WittList = new ArrayList<>();
//            int PKI = Integer.parseInt(PK);
//            UserKey userKey = new UserKey(PKI);
//
//            getWittHistory(userKey);
//        }


        switch (period) {
            case 0:
                recyclerView.setAdapter(WittHistoryAdapter);
                SearchAction(WittList);
                break;
            case 1:
                WittHistoryAdapter = new BlockUserAdapter(oneWeekAgoList, getActivity(), 1);
                recyclerView.setAdapter(WittHistoryAdapter);
                SearchAction(oneWeekAgoList);
                break;
            case 2:
                WittHistoryAdapter = new BlockUserAdapter(oneMonthAgoList, getActivity(), 2);
                recyclerView.setAdapter(WittHistoryAdapter);
                SearchAction(oneMonthAgoList);
                break;
            case 3:
                WittHistoryAdapter = new BlockUserAdapter(oneYearAgoList, getActivity(), 3);
                recyclerView.setAdapter(WittHistoryAdapter);
                SearchAction(oneYearAgoList);
                break;
        }


        setRecyclerView();
        if (PK.equals(myPK)) {
            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.wh_swipe_layout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            int visibleItemCount = layoutManager.getChildCount();
                            int totalItemCount = layoutManager.getItemCount();
                            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                            int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                            if (totalItemCount - 3 <= lastVisibleItemPosition && !updating) {
                                postsUpdate(true);
                            }
    //
                            if (0 < firstVisibleItemPosition) {
                                topScrolled = false;
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                }
            });
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalItemCount = layoutManager.getItemCount();
            if (totalItemCount < 1 && !updating) {//총 아이템 수가 1보다 작고 업데이트 안될때 초기 계시물 로드
                postsUpdate(false);
            }
        }

        return view;
    }

    private void SearchAction(ArrayList<WittListData> List) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText, List);
                return false;
            }
        });
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(WittHistoryAdapter);

        WittHistoryAdapter.notifyDataSetChanged();
    }
    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업 (리스트 업데이트)
        updating = true;
        WittList.clear();
        sqLiteUtil.setInitView(getContext(),"Witt_History_TB");
        WittList.addAll(sqLiteUtil.SelectWittHistoryUser()); // 데이터 갱신
        WittHistoryAdapter.notifyDataSetChanged(); // 어댑터에 변경된 데이터 설정
        updating = false;
    }
    public void searchFilter(String searchText,ArrayList<WittListData> List) {
        filteredList.clear();

        for(int i = 0; i < List.size(); i++){
            if(List.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(List.get(i));
            }
        }
        WittHistoryAdapter.filterWittList(filteredList);
    }

    private void datePicker(ArrayList<WittListData> WittList) {
        //dateList = new ArrayList<>(); // wittList에서 TS시간을 String으로 받은 데이터들의 리스트

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int n = 0; n < WittList.size(); n++) {
            Log.d("위트리스트 사이즈: ", String.valueOf(WittList.size()));
            try {
                String Wdate = WittList.get(n).getTS();
                Date adata = dateFormat.parse(Wdate); //변환된 날짜
            /** 비교 조건 및 연산  **/

                calendar.setTime(today);//오늘 날짜로 다시 설정
                calendar.add(Calendar.WEEK_OF_YEAR,-1); //1주전
                oneWeekAgo = calendar.getTime();

                if (adata.after(oneWeekAgo) && adata.before(today)) {
                    // 1주 전부터 오늘 사이의 데이터
                    oneWeekAgoList.add(WittList.get(n));
                    Log.d("add1 일주일전", String.valueOf(adata.after(oneWeekAgo)));
                    Log.d("add1 데이터 시간 ",WittList.get(0).getTS());
                    Log.d("add1 일주일전", String.valueOf(adata.before(today)));
                }

                calendar.setTime(today);//오늘 날짜로 다시 설정
                calendar.add(Calendar.MONTH,-1); //1달전
                oneMonthAgo = calendar.getTime();

                if (adata.after(oneMonthAgo) && adata.before(today)) {
                    // 1개월 전부터 오늘 사이의 데이터
                    oneMonthAgoList.add(WittList.get(n));
                    Log.d("add2 데이터 시간 ",WittList.get(0).getTS());
                }

                calendar.setTime(today);
                calendar.add(Calendar.YEAR,-1);//1년전
                oneYearAgo = calendar.getTime();

                if (adata.after(oneYearAgo) && adata.before(today)) {
                    // 1년 전부터 오늘 사이의 데이터
                    oneYearAgoList.add(WittList.get(n));
                    Log.d("add3  ",WittList.get(0).getTS());
                }

            }catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    private void getWittHistory(UserKey userKey) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<List<WittListData>> call = apiService.getWittHistory(userKey);
                    call.enqueue(new Callback<List<WittListData>>() {
                        @Override
                        public void onResponse(Call<List<WittListData>> call, Response<List<WittListData>> response) {
                            if (response.isSuccessful()){
                                List<WittListData> resWittList = response.body();
                                if(resWittList != null){
                                    for(WittListData Witt : resWittList){
                                        Log.d("WittList데이터", String.valueOf(Witt.getUSER_FK()));
                                        int RECORD_PK = Witt.getRECORD_PK();
                                        int USER_FK = Witt.getUSER_FK();
                                        int OUser_FK = Witt.getOUser_FK();
                                        String TS = Witt.getTS();
                                        String User_NM = Witt.getUser_NM();
                                        byte[] User_Img = Witt.getUser_Img();

                                        wittListData = new WittListData(RECORD_PK,USER_FK,OUser_FK,TS,User_NM,User_Img);
                                        Log.d("WittHistory 프로필에서", String.valueOf(Witt.getUser_NM()));
                                        Log.d("WittHistory 프로필에서", String.valueOf(Witt.getTS()));
                                        //SaveWittList(wittList);//로컬db에 받은 후기 저장 매서드
                                        WittList.add(wittListData);
                                        Log.d("WittList 추가: ","완료");
                                    }

                                } else { Log.d("getWittHistory","리스트가 null");}
                            }else{
                                Log.e("getWittHistory", "API 요청 실패. 응답 코드: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<WittListData>> call, Throwable t) {
                            Log.e("getWittHistory", "API 요청실패, 에러메세지: " + t.getMessage());
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Log.d("쓰레드 잠들기:","0.3초");
            Thread.sleep(300);
            setView();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setView() {
        WittHistoryAdapter = new BlockUserAdapter(BlackList,ReviewList, WittList ,getActivity());
        Log.d("널인지 확인: ", String.valueOf(WittHistoryAdapter.getItemCount()));
        datePicker(WittList);// 데이터 날짜별로 1주,1달,1년으로 구분 작업

    }

}
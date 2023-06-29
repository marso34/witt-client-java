package com.example.healthappttt.Fragment;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.BlackListData;
import com.example.healthappttt.Data.ReviewListData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.BlockUserAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WHChildFragment extends Fragment {

    private int period;
    private ArrayList<WittListData> WittList;
    private ArrayList<WittListData> filerWittList1,filerWittList2,filerWittList3;
    ArrayList<BlackListData> BlackList;
    ArrayList<ReviewListData> ReviewList;
    private List<String> dateList,oneWeekAgoList, oneMonthAgoList, oneYearAgoList;
    BlockUserAdapter WittHistoryAdapter;

    SQLiteUtil sqLiteUtil;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private boolean updating = false;
    private boolean topScrolled;
    Calendar calendar= Calendar.getInstance();
    Date today = calendar.getTime();
    Date oneWeekAgo, oneMonthAgo, oneYearAgo;


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

        filerWittList1 = new ArrayList<>();//1주                   날짜 비교하여 넣어지는 리스트
        filerWittList2 = new ArrayList<>();//1개월
        filerWittList3 = new ArrayList<>();//1년

        sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
        sqLiteUtil.setInitView(getContext(),"Witt_History_TB");//위트내역 목록 로컬 db
        WittList = sqLiteUtil.SelectWittHistoryUser();//SELECT * FROM Witt_History_TB ORDER BY TS DESC
        WittHistoryAdapter = new BlockUserAdapter(BlackList,ReviewList, WittList ,getActivity());
        datePicker(WittList);
        Log.d("filerWittList1",filerWittList1.get(0).getTS());
        Log.d("filerWittList2",filerWittList2.get(0).getTS());
        Log.d("filerWittList3",filerWittList3.get(0).getTS());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view  = (ViewGroup) inflater.inflate(R.layout.activity_whchild_fragment, container,false);
        recyclerView = view.findViewById(R.id.wh_recyclerView);

        //WittList.addAll(sqLiteUtil.SelectWittHistoryUser());
        Log.d("WHChild_oncreateview",WittList.get(0).getTS());



        switch (period) {
            case 0 :
                recyclerView.setAdapter(WittHistoryAdapter);
                break;
            case 1 :
                WittHistoryAdapter = new BlockUserAdapter(filerWittList1,getActivity(),1);
                recyclerView.setAdapter(WittHistoryAdapter);
                break;
            case 2 :
                WittHistoryAdapter = new BlockUserAdapter(filerWittList2,getActivity(),2);
                recyclerView.setAdapter(WittHistoryAdapter);
                break;
            case 3 :
                WittHistoryAdapter = new BlockUserAdapter(filerWittList3,getActivity(),3);
                recyclerView.setAdapter(WittHistoryAdapter);
                break;
        }


        setRecyclerView();
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
                        int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                        if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                            postsUpdate(true);
                        }
//
                        if(0 < firstVisibleItemPosition){
                            topScrolled = false;
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if(totalItemCount < 1 && !updating){
            postsUpdate(false);
        }

        return view;
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(WittHistoryAdapter);

        WittHistoryAdapter.notifyDataSetChanged();
    }
    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업
        updating = true;
        WittList.clear();
        WittList.addAll(sqLiteUtil.SelectWittHistoryUser()); // 데이터 갱신
        WittHistoryAdapter.notifyDataSetChanged(); // 어댑터에 변경된 데이터 설정
        updating = false;
    }

    private void datePicker(ArrayList<WittListData> WittList) {
        //dateList = new ArrayList<>(); // wittList에서 TS시간을 String으로 받은 데이터들의 리스트

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
                    filerWittList1.add(WittList.get(n));
                    Log.d("add1 일주일전", String.valueOf(adata.after(oneWeekAgo)));
                    Log.d("add1 데이터 시간 ",WittList.get(0).getTS());
                    Log.d("add1 일주일전", String.valueOf(adata.before(today)));
                }

                calendar.setTime(today);//오늘 날짜로 다시 설정
                calendar.add(Calendar.MONTH,-1); //1달전
                oneMonthAgo = calendar.getTime();

                if (adata.after(oneMonthAgo) && adata.before(today)) {
                    // 1개월 전부터 오늘 사이의 데이터
                    filerWittList2.add(WittList.get(n));
                    Log.d("add2 데이터 시간 ",WittList.get(0).getTS());
                }

                calendar.setTime(today);
                calendar.add(Calendar.YEAR,-1);//1년전
                oneYearAgo = calendar.getTime();

                if (adata.after(oneYearAgo) && adata.before(today)) {
                    // 1년 전부터 오늘 사이의 데이터
                    filerWittList3.add(WittList.get(n));
                    Log.d("add3  ",WittList.get(0).getTS());
                }

            }catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

}
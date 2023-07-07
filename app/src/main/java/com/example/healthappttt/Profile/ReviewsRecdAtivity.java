package com.example.healthappttt.Profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;

import java.util.ArrayList;

public class ReviewsRecdAtivity extends AppCompatActivity {

    SQLiteUtil sqLiteUtil;

    androidx.appcompat.widget.SearchView searchView;
    ArrayList<BlackListData> BlackList;
    ArrayList<ReviewListData> ReviewList, filteredList;
    ArrayList<WittListData> WittList;
    BlockUserAdapter ReviewAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String ReviewCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_recd);


        TextView listCnt = findViewById(R.id.listCnt);
        searchView = findViewById(R.id.review_search);
        recyclerView = findViewById(R.id.Review_recycle);

        filteredList = new ArrayList<>();

        sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
        sqLiteUtil.setInitView(this,"REVIEW_TB");//리뷰 목록 로컬 db
        ReviewList = sqLiteUtil.SelectReviewUser();//SELECT * FROM REVIEW_TB
        //Log.d("ReviewActivity첫번째 PK값: ", String.valueOf(ReviewList.get(0).getReview_PK()));
        ReviewAdapter = new BlockUserAdapter(BlackList,ReviewList,WittList, this);//어뎁터에 차단 목록 생성

        ReviewCnt = String.valueOf(ReviewList.size());//리뷰 리스트 개수 표시
        Log.d("리뷰리스트 개수: ", ReviewCnt);
        listCnt.setText(ReviewCnt);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ReviewAdapter);


        ReviewAdapter.notifyDataSetChanged(); //변경점을 어뎁터에 알림


        //검색 관련 매서드
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 검색 버튼을 누를 때 호출됨
                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
                // 이곳에서 원하는 동작을 수행하면 됩니다.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 검색창의 텍스트가 변경될 때마다 호출됨
                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
                // 이곳에서 원하는 동작을 수행하면 됩니다.
                searchFilter(s);
                return false;
            }
        });
    }

    //검색창에서 텍스트가 바뀔때마다 해당 텍스트에 해당하는 요소를 보여주는 매서드
    public void searchFilter(String searchText) {
        filteredList.clear();

        for(int i = 0; i < ReviewList.size(); i++){
            if(ReviewList.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(ReviewList.get(i));
            }
        }
        ReviewAdapter.filterReviewList(filteredList);
    }
}
package com.example.healthappttt.User;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.Profile.BlackActivity;
import com.example.healthappttt.Profile.ReviewsRecdAtivity;
import com.example.healthappttt.Profile.WittHistoryActivity;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.ViewHolder> {

    private ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
    private Context context;
    private ArrayList<WittListData> mDataset;

    ArrayList<BlackListData> BlockList;
    ArrayList<ReviewListData> ReviewList;
    ArrayList<WittListData> WittList;
    ArrayList<ReportHistory> ReportList;
    Activity activity;
    SQLiteUtil database;
    String Date_format;
    int c; //filterWittList1,2,3구분용

//생성자 둘이 구분을 못해서 합치고 어떤 datalist가 null인지에 따라 객체화가 달라진다.
    public BlockUserAdapter(ArrayList<BlackListData> blackList, ArrayList<ReviewListData> reviewList, ArrayList<WittListData> wittList, Activity activity) {
        this.activity = activity;
        this.database = SQLiteUtil.getInstance();

        // 이 부분 와일드카드 써서 최적화 가능할듯
        
        if (blackList != null) {
            this.BlockList = blackList;
        }else if (reviewList != null) {
            this.ReviewList = reviewList;
        }else if(wittList != null) {
            this.WittList = wittList;
        }
        else {
            // 어떤 리스트도 주어지지 않은 경우 예외 처리 등을 수행\
            Log.d("Adapter","DataList가 모두 null이다. ");
        }
    }

    public BlockUserAdapter( ArrayList<WittListData> wittList, Activity activity, int c) {
        this.WittList = wittList;
        this.activity = activity;
        this.c = c;
    }

    @NonNull
    @Override
    public BlockUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_blockuser,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlockUserAdapter.ViewHolder holder, int position) {

        /** 차단하기 엑티비티일때 */
        if(activity instanceof BlackActivity) {

            ImageButton btn_delete = holder.itemView.findViewById(R.id.Block_cancel);
            btn_delete.setVisibility(View.VISIBLE);//삭제버튼 보이게 처리

            BlackListData Ddata = BlockList.get(position);//삭제할 위치의 데이터
            int BL_PK = Ddata.getBL_PK();// 삭제할 데이터의 pk값

            if(BlockList.size() > 0) {
                BlackListData data = BlockList.get(position);
                //이름 넣기
                holder.Block_NM.setText(data.getUser_NM());
                //이미지 넣기 - 나중에 처리
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data.getUser_Img(),0,data.getUser_Img().length);
//            holder.Block_Img.setImageBitmap(bitmap);
                //holder.GYM.setText();
            }
            // X버튼 누르면 삭제하는 메서드
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        BlockList.remove(position);//화면에서 삭제
                        notifyItemRemoved(position);//변경점을 알림
                        database.deleteFromBlackListTable(BL_PK);//로컬db에서 삭제
                        Log.d("sqldelete",BL_PK + "-> 로컬db에서 해당 PK 삭제완료!");
                        deleteFromServer(BL_PK);//서버db에서 삭제
                    }
                }
            });
        /** 받은 후기 엑티비티일때 */
        }else if(activity instanceof ReviewsRecdAtivity) {
            TextView reviewText = holder.itemView.findViewById(R.id.Review_text);
            reviewText.setVisibility(View.VISIBLE);//리뷰텍스트 보이게
            ImageButton cancle = holder.itemView.findViewById(R.id.Block_cancel);
            cancle.setEnabled(false); //삭제 버튼 작동 X

//            LinearLayout linearLayout = holder.itemView.findViewById(R.id.texts);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
//            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//            linearLayout.setLayoutParams(layoutParams);


            //텍스트 연결
            if(ReviewList.size() > 0) {
                ReviewListData data = ReviewList.get(position);
                holder.Block_NM.setText(data.getUser_NM());
                holder.Review_text.setText(data.getText_Con());
                //holder.GYM.setText(); 헬스장 정보
            }
        /** 위트 내역 엑티비티일때 **/
        }else if(activity instanceof WittHistoryActivity) {
            ImageButton cancel = holder.itemView.findViewById(R.id.Block_cancel);
            cancel.setEnabled(false); //삭제 버튼 작동 X
            TextView Adapter_date = holder.itemView.findViewById(R.id.adapter_date);
            Adapter_date.setVisibility(View.VISIBLE);

            if(WittList.size() > 0) {
                WittListData data = WittList.get(position);
                Date_format = formatDateString(data.getTS()); // 날짜 데이터 전처리 %04d년%02d월%02d일 %s요일

                holder.Block_NM.setText(data.getUser_NM());
                //holder.GYM.setText(); 헬스장 정보

                if(position == 0 || !Date_format.equals(formatDateString(WittList.get(position - 1).getTS()))){
                    holder.adapter_date.setText(Date_format);//최상단이거나 날짜가 같지 않은 경우에는 날짜 표시
                }else {
                    holder.adapter_date.setVisibility(View.GONE);//나머지는 날짜 표시 X
                }
            }
        }
//        else if(fragment instanceof WHChildFragment){
//            ImageButton cancel = holder.itemView.findViewById(R.id.Block_cancel);
//            cancel.setEnabled(false); //삭제 버튼 작동 X
//            TextView Adapter_date = holder.itemView.findViewById(R.id.adapter_date);
//            Adapter_date.setVisibility(View.VISIBLE);
//
//            if(WittList.size() > 0) {
//                WittListData data = WittList.get(position);
//                Date_format = formatDateString(data.getTS()); // 날짜 데이터 전처리 %04d년%02d월%02d일 %s요일
//
//                holder.Block_NM.setText(data.getUser_NM());
//                //holder.GYM.setText(); 헬스장 정보
//
//                if(position == 0 || !Date_format.equals(formatDateString(WittList.get(position - 1).getTS()))){
//                    holder.adapter_date.setText(Date_format);//최상단이거나 날짜가 같지 않은 경우에는 날짜 표시
//                }else {
//                    holder.adapter_date.setVisibility(View.GONE);//나머지는 날짜 표시 X
//                }
//            }
//
//        }
//        /** 신고 내역 엑티비티일때 **/
//        else if(activity instanceof ReportHistoryActivity) {
//
//            TextView reportText = holder.itemView.findViewById(R.id.Review_text);
//            reportText.setVisibility(View.VISIBLE);//리뷰텍스트 보이게
//            ImageButton cancle = holder.itemView.findViewById(R.id.Block_cancel);
//            cancle.setEnabled(false); //삭제 버튼 작동 X
//
//
//
//            //텍스트 연결
//            if(ReportList.size() > 0) {
//                ReportHistory data = ReportList.get(position);
//                holder.Block_NM.setText(data.getUser_NM());
//                holder.Review_text.setText(data.getCONT());
//                holder.GYM.setText(data.getGYM_NM()); //헬스장 정보
//            }
//
//        }
    }

    @Override
    public int getItemCount() {
        if(activity instanceof BlackActivity){
            Log.d("getItemCount","BlockActivity 아이템 개수: "+BlockList.size());
            return BlockList.size();
        }else if(activity instanceof ReviewsRecdAtivity){
            Log.d("getItemCount","ReviewsRecdAtivity 아이템 개수: "+ReviewList.size());
            return ReviewList.size();
        } else if (activity instanceof WittHistoryActivity) {
            Log.d("getItemCount", "WittHistoryActivity 아이템 개수: " + WittList.size());
            if(c == 1 )
                Log.d("getItemCount-> ","filerWittList1");
            if(c == 2 )
                Log.d("getItemCount-> ","filerWittList2");
            if(c == 3 )
                Log.d("getItemCount-> ","filerWittList3");
            return WittList.size();
        }
//         else if (fragment instanceof WHChildFragment) {
//            Log.d("getItemCount", "WHChildFragment 아이템 개수: " + WittList.size());
//            return WittList.size();}
         else {
            Log.d("getItemCount", "어뎁터 아이템 개수 0");
            return 0;
        }

    }


    private void deleteFromServer(Integer BL_PK) {
        Call<Integer> call = apiService.deleteFromServer(new UserKey(BL_PK)); //메모: 형식 원래 pkData에서 만들어놓고 했었음
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    Integer PK = response.body();
                    Log.d("BlockUserAdapter", "서버db에서 삭제 PK: " + PK);
                }else {
                    // 서버 응답이 실패했을때
                    Log.d("BlockUserAdapter", "서버 응답 실패. 상태코드: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("deleteFromServer", "에러메세지: " + t.getMessage());
            }
        });

    }


//  검색할때 해당 검색어가 포함되면 그 목록을 보여주는 매서드 (차단목록)
   public void filterBlackList(ArrayList<BlackListData> filteredList){
        BlockList = filteredList;
        notifyDataSetChanged();
    }
//  검색할때 해당 검색어가 포함되면 그 목록을 보여주는 매서드 (받은 후기 목록)
    public void filterReviewList(ArrayList<ReviewListData> filteredList){
        ReviewList = filteredList;
        notifyDataSetChanged();
    }
//  검색할때 해당 검색어가 포함되면 그 목록을 보여주는 매서드 (받은 후기 목록)
    public void filterWittList(ArrayList<WittListData> filteredList){
        WittList = filteredList;
        notifyDataSetChanged();
    }
//    public void filterReportList(ArrayList<ReportHistory> filteredList){
//        ReportList = filteredList;
//        notifyDataSetChanged();
//    }

//  adapter_blockuser 구성 요소들을 가리키는 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Block_NM,GYM,Review_text;
        TextView adapter_date;
        ImageButton btn_delete;
        ImageView Block_Img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Block_NM = itemView.findViewById(R.id.Block_NM);
            this.GYM = itemView.findViewById(R.id.GYM);
            this.btn_delete = itemView.findViewById(R.id.Block_cancel);
            this.Block_Img = itemView.findViewById(R.id.Block_Img);
            this.Review_text = itemView.findViewById(R.id.Review_text);

            this.adapter_date = itemView.findViewById(R.id.adapter_date);
        }
    }
// 날짜형식을 바꿔주는 매서드
    public static String formatDateString(String inputDateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(inputDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // '월'단위는 0부터 시작함
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //일요일=1 ~ 토요일=7

            String[] dayOfWeekNames = new String[]{"", "일", "월", "화", "수", "목", "금", "토"};
            String formattedDateString = String.format(Locale.getDefault(), "%04d년 %02d월 %02d일 %s요일", year, month, day, dayOfWeekNames[dayOfWeek]);

            return formattedDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
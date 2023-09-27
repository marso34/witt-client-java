package com.gwnu.witt.User;//package com.gwnu.witt.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.card.MaterialCardView;
import com.gwnu.witt.Data.AdViewHolder;
import com.gwnu.witt.Data.UserInfo;
import com.gwnu.witt.Profile.MyProfileActivity;
import com.gwnu.witt.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
    private Context mContext;
    private static final String Signature = "#05C78C";
    private static final String Background_2 = "#D1D8E2";
    private static final String Body = "#4A5567";
    private static final String White = "#ffffff";
    private static final String Transparent = "#00000000";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";
    private AdViewHolder holder;
    private boolean tureUser = true;
    private ArrayList<UserInfo> mDataset;
    private int dayOfWeek;
    private boolean adsFlag;
    public UserAdapter(Context mContext, ArrayList<UserInfo> myDataset, int dayOfWeek) {
        this.mContext = mContext;
        this.mDataset = myDataset;
        this.dayOfWeek = dayOfWeek; // 프로필 호출할 때 필요한 데이터, 삭제하면 안 됨
        this.adsFlag = adsFlag;
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView CardView;
        public LinearLayout DefaultLayout;
        public TextView Name, GymName, GymAdress;
        public ImageView MapIcon, TimeIcon;
        public TextView TimeTxt, Anymore;

        public RecyclerView recyclerView;

        public AreaAdapter Adapter;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);

            this.CardView = itemView.findViewById(R.id.cardView);
            this.DefaultLayout = itemView.findViewById(R.id.defaultLayout);

            this.Name =  itemView.findViewById(R.id.UNE);
            this.GymName = itemView.findViewById(R.id.gymName);
            this.GymAdress = itemView.findViewById(R.id.gymAdress);

            this.MapIcon = itemView.findViewById(R.id.mapIcon);
            this.TimeIcon = itemView.findViewById(R.id.timeIcon);
            this.TimeTxt = itemView.findViewById(R.id.timeTxt);

            this.recyclerView = itemView.findViewById(R.id.recyclerView);

            this.Anymore = itemView.findViewById(R.id.anymore);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.d(TAG, "뷰타입 "+viewType);
        if (viewType == UserInfo.ITEM_VIEW_TYPE_AD ) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ads, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        }
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                if (position != mDataset.size() && viewType == UserInfo.ITEM_VIEW_TYPE_CONTENT) {
                    Log.d("상세 프로필", "userAdapter에서 클릭처리");
                    int adapterUserKey = mDataset.get(position).getUserKey();
                    Intent intent = new Intent(mContext, MyProfileActivity.class);

                    intent.putExtra("PK", adapterUserKey);
                    intent.putExtra("dayOfWeek", dayOfWeek);
                    mContext.startActivity(intent);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        if(mDataset.size() > 0  && mDataset.get(position).adsFlag == UserInfo.ITEM_VIEW_TYPE_AD) {
            AdViewHolder adViewHolder = new AdViewHolder(holder.itemView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewHolder.mAdView.loadAd(adRequest);
        }
        else{
        if (mDataset.size() > 0 && position != mDataset.size()) {
            holder.CardView.setCardBackgroundColor(Color.parseColor(White));
            holder.CardView.setCardElevation(6);
            holder.DefaultLayout.setVisibility(View.VISIBLE);
            holder.Anymore.setVisibility(View.GONE);

            UserInfo userInfo = mDataset.get(position);
            Log.d("유저 이름!", userInfo.getName());
            Log.d("유저 PK!", String.valueOf(userInfo.getUserKey()));
            //  Log.d(TAG, "onBindViewHolder: "+ userInfo.getUserName().toString());
            //calculateDistances(//여기에);형원이 한테 받아서 하기.

            String str = userInfo.getGymName();

            holder.Name.setText(userInfo.getName());

            if (str == null) {
                holder.GymName.setText("선택된 헬스장이 없어요");
                holder.GymName.setTextColor(Color.parseColor(Background_2));
                holder.GymAdress.setVisibility(View.GONE);
                holder.MapIcon.setColorFilter(Color.parseColor(Background_2));
            } else {
                if (str.equals("")) {
                    holder.GymName.setText("선택된 헬스장이 없어요");
                    holder.GymName.setTextColor(Color.parseColor(Background_2));
                    holder.GymAdress.setVisibility(View.GONE);
                    holder.MapIcon.setColorFilter(Color.parseColor(Background_2));
                } else {
                    holder.GymName.setText(str);
                    holder.GymName.setTextColor(Color.parseColor(Body));
                    holder.GymAdress.setText(userInfo.getGymAdress());
                    holder.GymAdress.setVisibility(View.VISIBLE);
                    holder.MapIcon.setColorFilter(Color.parseColor(Signature));
                }
            }

            switch (userInfo.getTime()) {
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

            setCatRecyclerView(holder.recyclerView, holder.Adapter, position);
        } else {
            holder.CardView.setCardBackgroundColor(Color.parseColor(Transparent));
            holder.CardView.setCardElevation(0);
            holder.DefaultLayout.setVisibility(View.GONE);
            if(holder.Anymore !=null)
                holder.Anymore.setVisibility(View.VISIBLE);
        }
    }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDataset.size()) {
            UserInfo U = mDataset.get(position);
            if (U.adsFlag == 1) {
                return UserInfo.ITEM_VIEW_TYPE_AD;
            } else {
                return UserInfo.ITEM_VIEW_TYPE_CONTENT;
            }
        } else {
            // 적절한 기본값 또는 오류 처리를 여기에 추가합니다.
            return UserInfo.ITEM_VIEW_TYPE_CONTENT; // 예시로 기본값을 반환하도록 설정했습니다.
        }
    }


    private void setCatRecyclerView(RecyclerView recyclerView, AreaAdapter adapter, int positon) {
        ArrayList<String> eCat = new ArrayList<>();
        int cat = mDataset.get(positon).getRoutineCategory();

        if ((cat & 0x1)  == 0x1)        eCat.add("가슴");
        if ((cat & 0x2)  == 0x2)        eCat.add("등");
        if ((cat & 0x4)  == 0x4)        eCat.add("어깨");
        if ((cat & 0x8)  == 0x8)        eCat.add("하체");
        if ((cat & 0x10) == 0x10)       eCat.add("팔");
        if ((cat & 0x20) == 0x20)       eCat.add("복근");
        if ((cat & 0x40) == 0x40)       eCat.add("유산소");

        adapter = new AreaAdapter(eCat); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void calculateDistances(double myLatitude, double myLongitude) {
        for (UserInfo userInfo : mDataset) {
            double userLatitude = userInfo.getLatitude();
            double userLongitude = userInfo.getLongitude();

            // 거리 계산을 수행하여 distance 업데이트
            int distance = calculateDistance(myLatitude, myLongitude, userLatitude, userLongitude);
            userInfo.setDistance(distance);
        }
    }
    private int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 거리 계산 로직을 구현
        // 예시로 Haversine 공식 사용
        double R = 6371; // 지구의 반지름 (단위: km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        // 거리를 정수형으로 변환하여 반환
        return (int) distance;
    }

    public void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

//        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
//            case 0:
//                dayOfWeek = "sun"; break; // 일
//            case 1:
//                dayOfWeek = "mon";break; // 월
//            case 2:
//                dayOfWeek = "tue";break; // 화
//            case 3:
//                dayOfWeek = "wed"; break; // 수
//            case 4:
//                dayOfWeek = "thu";break; // 목
//            case 5:
//                dayOfWeek = "fri";break; // 금
//            case 6:
//                dayOfWeek = "sat";break; // 토
//        }
    }
}
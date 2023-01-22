package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
    private ArrayList<User> mDataset;
    private Activity activity;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public UserAdapter(Activity activity, ArrayList<User> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public UserAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView Name =  cardView.findViewById(R.id.UNE);
        TextView LocaName = cardView.findViewById(R.id.GT);
        ImageView photoImageVIew = cardView.findViewById(R.id.PRI);
        TextView PreferredTime = cardView.findViewById(R.id.GoodTime);
        TextView ExerciseArea = cardView.findViewById(R.id.EArea);

        User userInfo = mDataset.get(position);
//        if(mDataset.get(position).getPhotoUrl() != null){
//            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photoImageVIew);
//        } 사진넣기

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
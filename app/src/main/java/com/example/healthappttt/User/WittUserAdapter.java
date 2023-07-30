package com.example.healthappttt.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WittUserAdapter extends RecyclerView.Adapter<WittUserAdapter.MainViewHolder> {
    private ArrayList<UserInfo> Users;


    public WittUserAdapter(ArrayList<UserInfo> Users) {
        this.Users = Users;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout routineLayout;
        public TextView Name, GymName, GymAdress;
        public ImageView MapIcon, CheckIcon;


        public MainViewHolder(@NonNull View view) {
            super(view);

            this.routineLayout = view.findViewById(R.id.routine);
            this.Name =  view.findViewById(R.id.UNE);
            this.GymName = view.findViewById(R.id.gymName);
            this.GymAdress = view.findViewById(R.id.gymAdress);

            this.MapIcon = view.findViewById(R.id.mapIcon);
            this.CheckIcon = view.findViewById(R.id.check);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.routineLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

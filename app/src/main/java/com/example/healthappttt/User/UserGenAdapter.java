package com.example.healthappttt.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.R;

import java.util.ArrayList;

public class UserGenAdapter extends RecyclerView.Adapter<UserGenAdapter.MainViewHolder> {
    private Context context;

    private ArrayList<?> users;

    public UserGenAdapter(Context context, ArrayList<?> users) {
        this.context = context;
        this.users = users;
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder {

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_gen, null);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

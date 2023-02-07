package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.Activity.ProfileActivity;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
    private ArrayList<User> mDataset;
    private FirebaseStorage storage;
    private User userInfo;
    private Context mContext;
    private User thisUser;
    static class MainViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        MainViewHolder(View v) {
            super((View) v);
            cardView = v;
        }
    }

    public UserAdapter(Activity activity, ArrayList<User> myDataset) {
        this.mDataset = myDataset;
        this.mContext = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public UserAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭됐을시 행동 여기에 적으면 돼네.....시벌...
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        storage = FirebaseStorage.getInstance();
        View cardView = holder.cardView;
        TextView Name =  cardView.findViewById(R.id.UNE);
        TextView LocaName = cardView.findViewById(R.id.GT);
        ImageView photoImageVIew = cardView.findViewById(R.id.PRI);
        TextView PreferredTime = cardView.findViewById(R.id.GoodTime);
        TextView ExerciseArea = cardView.findViewById(R.id.EArea);
        User userInfo = mDataset.get(position);

        String fileName = userInfo.getKey_();

        File profilefile = null;

        try {
            profilefile = File.createTempFile("images","jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference sref  = storage.getReference().child("article/photo").child(fileName);
        File finalProfilefile = profilefile;
        sref.getFile(profilefile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Glide.with(mContext).load(finalProfilefile).into(photoImageVIew);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("User",userInfo);
                intent.putExtra("post",finalProfilefile);
                mContext.startActivity(intent);
            }
        });

      //  Log.d(TAG, "onBindViewHolder: "+ userInfo.getUserName().toString());
        Name.setText(userInfo.getUserName().toString());
        LocaName.setText(userInfo.getLocationName());
        PreferredTime.setText("11~13");
        ExerciseArea.setText("가슴");


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
package com.example.healthappttt.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.Activity.ChatActivity;
import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter  extends RecyclerView.Adapter<UserListAdapter.MainViewHolder>{

    private Context mContext;
    private ArrayList<User> userlist;
    private boolean ischat;
    FirebaseUser fuser;
    String theLastMessage;


    public UserListAdapter(Context context, ArrayList<User> userList) {
        this.mContext = context;
        this.userlist = userList;
        //this.ischat = ischat; ,boolean ischat
    }

    @NonNull
    @Override
    public UserListAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_layout, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MainViewHolder holder, int position) {
        final User user = userlist.get(position);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        holder.username.setText(user.getUserName());
        holder.location.setText(user.getLocationName());


        if (user.getProfileImg() != null){
//            Glide.with(mContext).load(user.getProfileImg()).into(holder.photoImageVIew);
//        } else {
            holder.photoImageVIew.setImageResource(R.drawable.profile);
        }
//
//        if (ischat){
//            lastMessage(user.getKey(), holder.last_msg);
//        } else {
//            holder.last_msg.setVisibility(View.GONE);
//        }
//


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userId",user.getKey());
                Log.i(ContentValues.TAG,user.getKey());
                intent.putExtra("username",user.getUserName());
                Log.i(ContentValues.TAG,user.getUserName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView location;
        public CircleImageView photoImageVIew;
        private TextView last_msg;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.txt_name);
            photoImageVIew = itemView.findViewById(R.id.img_rv_photo);
            location = itemView.findViewById(R.id.location);
            last_msg = itemView.findViewById(R.id.last_msg);

        }
    }
//    private void lastMessage(final String userid, final TextView last_msg){
//        theLastMessage = "default";
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Message chat = snapshot.getValue(Message.class);
//                    if (firebaseUser != null && chat != null) {
//                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
//                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
//                            theLastMessage = chat.getMessage();
//                        }
//                    }
//                }
//
//                switch (theLastMessage){
//                    case  "default":
//                        last_msg.setText("No Message");
//                        break;
//
//                    default:
//                        last_msg.setText(theLastMessage);
//                        break;
//                }
//
//                theLastMessage = "default";
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//
//        });
//    }
}

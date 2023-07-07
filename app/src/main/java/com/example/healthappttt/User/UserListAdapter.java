package com.example.healthappttt.User;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<UserChat> userList;
    private OnItemClickListener listener;
    private io.socket.client.Socket mSocket;
    private SocketSingleton socketSingleton;
    private Context context;
    private  UserChat user;
    public UserListAdapter(Context context, List<UserChat> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        UserListViewHolder viewHolder = new UserListViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                Log.d(TAG, "onClick: "+String.valueOf(position));
                    UserChat user = userList.get(position);
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("otherUserName",user.getUserNM());
                intent.putExtra("ChatRoomId",user.getChatRoomId());
                intent.putExtra("otherUserKey",user.getOtherUserKey());
                Log.d(TAG, "onClick: "+user.getUserNM());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        user = userList.get(position);
        holder.userName.setText(user.getUserNM());
        socketSingleton = SocketSingleton.getInstance(context);
        Log.d(TAG, "onBindViewHolder: "+String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UNE);
            String name = (String) userName.getText();

        }
    }



}


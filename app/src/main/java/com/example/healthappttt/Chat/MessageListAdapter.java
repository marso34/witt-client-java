package com.example.healthappttt.Chat;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.R;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<MSG> messageList;
    private String username;
    private String otherUserName;

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView messageTextView;
        public TextView timeTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
    public MessageListAdapter(List<MSG> messageList, String username, String otherUserName) {
        this.messageList = messageList;
        this.username = username;
        this.otherUserName = otherUserName;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG.TYPE_RECEIVED) {
            // 받은 메시지 뷰를 생성합니다.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        } else {
            // 보낸 메시지 뷰를 생성합니다.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MSG message = messageList.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.timeTextView.setText(DateUtils.getRelativeTimeSpanString(message.getTimestamp()));

        if (message.getMyFlag() == 1) {
            // 보낸 메시지인 경우
            holder.messageTextView.setBackgroundResource(R.drawable.shape_rounded_rectangle_primary);
        } else {
            holder.nameTextView.setText(otherUserName);
            // 받은 메시지인 경우
            holder.nameTextView.setVisibility(View.VISIBLE);
            ;
            holder.messageTextView.setBackgroundResource(R.drawable.received_message_background);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MSG message = messageList.get(position);
        if (message.getMyFlag() == 1) {
            return MSG.TYPE_SENT;
        } else {
            return MSG.TYPE_RECEIVED;
        }
    }
}
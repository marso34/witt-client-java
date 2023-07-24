package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<MSG> messageList;
    private String username;
    private String otherUserName;
    private String otherUserPk;
    private Context context;
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView messageTextView;
        public TextView timeTextView;
        public ImageView sendingView;

        LinearLayout parentLayout; // 부모 뷰의 ID에 맞게 수정하세요

        public MessageViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            sendingView = itemView.findViewById(R.id.sending);
            parentLayout = itemView.findViewById(R.id.msg); // 부모 뷰의 ID에 맞게 수정하세요

        }
    }
    public MessageListAdapter(List<MSG> messageList, String username, String otherUserName, String otherUserPk, Context context) {
        this.otherUserPk = otherUserPk;
        this.messageList = messageList;
        this.username = username;
        this.otherUserName = otherUserName;
        this.context = context;
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
        holder.timeTextView.setText(message.timestampString());
        if (message.getMyFlag() == 1) {
            // 보낸 메시지인 경우
            holder.messageTextView.setBackgroundResource(R.drawable.sent);
            if(message.getSuccess() == 1)
                holder.parentLayout.removeView(holder.sendingView);
            else if (message.getSuccess() == -1) {
                Log.d(TAG, "SUCCESS값 오류 -1나옴 ");
            }
            holder.messageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (message.getSuccess() != 1) {
                        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
                        sqLiteUtil.setInitView(view.getContext(), "CHAT_MSG_TB");
//                        sqLiteUtil.deleteMSG(message.getKey());
                        SocketSingleton socketSingleton = SocketSingleton.getInstance(context);
                        PreferenceHelper preferenceHelper = new PreferenceHelper("UserTB", context);
                        try {
                            JSONObject data = new JSONObject();
                            data.put("myUserKey", preferenceHelper.getPK());
                            data.put("otherUserKey", Integer.parseInt(otherUserPk));
                            data.put("chatRoomId", message.getChatRoomId());
                            data.put("messageText", message.getMessage());
                            data.put("chatPk", message.getKey());
                            data.put("TS", message.getTimestamp());
                            socketSingleton.sendMessage(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            holder.nameTextView.setText(otherUserName);
            // 받은 메시지인 경우
            holder.nameTextView.setVisibility(View.VISIBLE);
            ;
            holder.messageTextView.setBackgroundResource(R.drawable.receive);
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
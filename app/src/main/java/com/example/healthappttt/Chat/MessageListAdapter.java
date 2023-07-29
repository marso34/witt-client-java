package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.annotations.NonNull;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<MSG> messageList;
    private String username;
    private String otherUserName;
    private String otherUserPk;
    private Context context;
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView timeTextView;
        public ImageView sendingView;

        LinearLayout parentLayout; // 부모 뷰의 ID에 맞게 수정하세요

        public MessageViewHolder(View itemView) {
            super(itemView);
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
    public String extractName(String inputString) {
        String namePattern = "!!~(.*?)~!!"; // 정규표현식 패턴: !!~(이름)~!!
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(inputString);

        if (matcher.find()) {
            return matcher.group(1)+"님이 위트를 보냈습니다."; // 매칭된 그룹(이름) 반환
        } else {
            return null; // 이름이 매칭되지 않으면 빈 문자열 반환
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MSG message = messageList.get(position);
        if(extractName(message.getMessage())== null)
            holder.messageTextView.setText(message.getMessage());
        else  holder.messageTextView.setText(extractName(message.getMessage()));
        if(position + 1 < messageList.size()) {
            String nextTS = extractDateTime(messageList.get(position+1).timestampString());
            if(!extractDateTime(message.timestampString()).equals(nextTS))
                holder.timeTextView.setText(extractDateTime(message.timestampString()));
            else holder.timeTextView.setText("");
        }
        else holder.timeTextView.setText(extractDateTime(message.timestampString()));

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

            holder.messageTextView.setBackgroundResource(R.drawable.receive);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String extractDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(dateString == "-1"){
            return "";
        }
        else {
            // 문자열을 LocalDateTime 객체로 변환
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();

                // 날짜가 오늘이면 시간과 분 추출
                int hour = dateTime.getHour();
                int minute = dateTime.getMinute();
                return String.format("%02d:%02d", hour, minute);
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
        } else  {
            return MSG.TYPE_RECEIVED;
        }

    }
    public String getItemTS(int position){
        return messageList.get(position).timestampString();
    }
}
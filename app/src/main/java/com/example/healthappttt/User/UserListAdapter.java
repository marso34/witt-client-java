package com.example.healthappttt.User;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.lang.String.valueOf;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<UserChat> userList;
    private OnItemClickListener listener;
    private io.socket.client.Socket mSocket;
    private SocketSingleton socketSingleton;
    private SQLiteUtil sqLiteUtil;
    private Context context;
    private PreferenceHelper preferenceHelper;
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
//
        sqLiteUtil = SQLiteUtil.getInstance();
        preferenceHelper = new PreferenceHelper("UserTB", context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                Log.d(TAG, "onClick: "+ valueOf(position));
                    UserChat user = userList.get(position);
                Intent intent = new Intent(context, ChatActivity.class);
                view.findViewById(R.id.circleIMG).setVisibility(View.INVISIBLE);
                LinearLayout layout = view.findViewById(R.id.out);
                layout.setBackgroundResource(R.drawable.round_button_white);
                intent.putExtra("otherUserName",user.getUserNM());
                intent.putExtra("ChatRoomId", valueOf(user.getChatRoomId()));
                intent.putExtra("otherUserKey", valueOf(user.getOtherUserKey()));
                Log.d(TAG, "onClick: "+user.getUserNM());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        user = userList.get(position);
        holder.userName.setText(user.getUserNM());
        MSG m = null;
        if (context != null) {
            m = sqLiteUtil.selectLastMsg(context, String.valueOf(userList.get(position).getChatRoomId()), String.valueOf(preferenceHelper.getPK()), 1);

            Log.d(TAG, "onBindViewHolder: " + m.getISREAD() + "  " + m.getMessage());

            if (m.getISREAD() == 2 || m.getISREAD() == -1 || m.getMyFlag() == 1) {
                LinearLayout layout = holder.itemView.findViewById(R.id.out);
                layout.setBackgroundResource(R.drawable.round_button_white);
                holder.circleIMG.setVisibility(View.INVISIBLE);
            } else {
                LinearLayout layout = holder.itemView.findViewById(R.id.out);
                layout.setBackgroundResource(R.drawable.round_line);
                holder.circleIMG.setVisibility(View.VISIBLE);
            }

            if (extractName(m.getMessage()) == null)
                holder.lastChat.setText(m.getMessage());
            else holder.lastChat.setText(extractName(m.getMessage()));
            holder.lastChatTime.setText(extractDateTime(m.timestampString()));
            Log.d(TAG, "onBindViewHolder: " + m.getMessage());
            CircleImageView PR = holder.itemView.findViewById(R.id.PRI);
            if(preferenceHelper.getGender() == 0)
                PR.setImageResource( R.drawable.man_3d_light);
            else PR.setImageResource(R.drawable.woman_3d_light);
        }

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
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
            if (dateTime.toLocalDate().isEqual(currentDate)) {
                // 날짜가 오늘이면 시간과 분 추출
                int hour = dateTime.getHour();
                int minute = dateTime.getMinute();
                return String.format("%02d:%02d", hour, minute);
            } else {
                // 날짜가 오늘이 아니면 월과 일 추출
                int month = dateTime.getMonthValue();
                int day = dateTime.getDayOfMonth();
                return String.format("%02d월 %02d일", month, day);
            }
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView lastChat;
        public TextView lastChatTime;
        public ImageView circleIMG;
        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UNE);
            lastChat = itemView.findViewById(R.id.lastChat);
            lastChatTime = itemView.findViewById(R.id.lastChatTime);
            circleIMG = itemView.findViewById(R.id.circleIMG);
        }
    }


}


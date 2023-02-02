package com.example.healthappttt.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Message> messages;
    private String messageTime = "";
    private String date_up = "";

    FirebaseUser fuser;

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;


    public MessageAdapter(Context mContext, List<Message> messages){
        this.messages = messages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sent, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.receive, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        Message chat = messages.get(position);


        try {
            String date = getDate(chat.getTime());
           // Log.i(ContentValues.TAG,date);
            if(!date_up.equals(date)) {
                holder.msg_time.setText(date);
            }
            else
                holder.msg_time.setVisibility(View.GONE);
            setMessageTimeUP(date);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String time = ChangeMessageTime(chat.getTime());

        holder.txt_message.setText(chat.getMessage());

        if(!time.equals(getMessageTime()))
            holder.txt_time.setText(time);
        else
            holder.txt_time.setText("");


        setMessageTime(time);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_message;
        public TextView txt_time;
        public TextView msg_time;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_message = itemView.findViewById(R.id.txt_message);
            txt_time = itemView.findViewById(R.id.txt_time);
            msg_time = itemView.findViewById(R.id.timeText);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

//    "yy/MM/dd HH:mm:ss"


    public void setMessageTimeUP(String Time){
        date_up = Time;
    }

    public void setMessageTime(String Time){
        messageTime = Time;
    }

    public String getMessageTime(){
        return messageTime;
    }

    public String getDate(String Time) throws Exception {


        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        try {
            d1 = format.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(d1);

        String dateYear = String.valueOf(calendar.get(Calendar.YEAR));
        String dateMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String dataDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


        return dateYear  + "년 " + dateMonth + "월 " + dataDay + "일 " + getCurrentWeek(Time) + "요일";


    }


    public String ChangeMessageTime(String msgTime){

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        try {
            d1 = format.parse(msgTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long k = d1.getHours();

        if(k < 12){
            return "오전 " + k + ":" + d1.getMinutes();
        }
        else if(k > 12) {
            k = d1.getHours() - 12;
            return "오후" + k + ":" + d1.getMinutes();
        }
        else
            return "";

    }


    public String getCurrentWeek(String date)  throws Exception {

        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss") ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;

        }



        return day ;
    }


}
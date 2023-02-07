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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter  extends RecyclerView.Adapter<UserListAdapter.MainViewHolder>{

    private FirebaseStorage storage;
    private Context mContext;
    private ArrayList<User> userlist;
    FirebaseUser fuser;
    String theLastMessage ="";
    String messageTime ="";
    Integer num = 0;
    private CircleImageView photoImageVIew;

    public UserListAdapter(Context context, ArrayList<User> userList ){
        this.mContext = context;
        this.userlist = userList;
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
        storage = FirebaseStorage.getInstance();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        holder.photoImageVIew = holder.itemView.findViewById(R.id.img_rv_photo);

        holder.username.setText(user.getUserName());

//        if (user.getProfileImg() != null){
//            Glide.with(mContext).load(user.getProfileImg()).into(holder.photoImageVIew);
//        } else {
//            hold.photoImageVIew.setImageResource(R.drawable.profile);
//        }

        lastMessage(user.getKey_(), holder.last_msg, holder.time_msg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userId",user.getKey_());
                intent.putExtra("username",user.getUserName());
                mContext.startActivity(intent);
            }
        });

        String fileName = user.getKey_();

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
                Glide.with(mContext).load(finalProfilefile).into(holder.photoImageVIew);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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
        private TextView time_msg;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.txt_name);
            photoImageVIew = itemView.findViewById(R.id.img_rv_photo);
            last_msg = itemView.findViewById(R.id.last_msg);
            time_msg = itemView.findViewById(R.id.messagetime);
        }
    }
    private void lastMessage(final String userid, final TextView last_msg, final TextView time_msg){
        theLastMessage = "default";
        messageTime = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        String getRoom = userid + firebaseUser.getUid();
//        Log.i(ContentValues.TAG,userid);
//        Log.i(ContentValues.TAG,firebaseUser.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats").child(getRoom).child("messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message chat = snapshot.getValue(Message.class);
                    if (firebaseUser != null && chat != null) {
                        if(chat.getSender().equals(userid) && chat.getReceiver().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                            messageTime = chat.getTime();
                            messageTime = setTime(messageTime);
                        }
                    }
                }
                switch (theLastMessage){
                    case  "default":
                        last_msg.setText(" ");
                        time_msg.setText("");
                        messageTime = "default";
                        theLastMessage = "default";
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        time_msg.setText(messageTime);
                        messageTime = "default";
                        theLastMessage = "default";
                        break;
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    private String setTime(String messageTime) {

        String date1 = getTime();
        String date2 = messageTime; //날짜2

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date2);
            d2 = format.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }



// Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diffSeconds / (24*60*60);

        if(diffDays != 0 ){
            return diffDays + "일 전";
        }
        else if(diffHours != 0){
            return diffHours + "시간 전";
        }
        else if(diffMinutes != 0){
            return diffMinutes + "분 전";
        }
        else
            return "방금전";
    }
}

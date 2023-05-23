package com.example.healthappttt.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.MessageAdapter;
import com.example.healthappttt.adapter.UserListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference mDbRef;
    FirebaseAuth mAuth;
    FirebaseUser fuser;
    RecyclerView chatRecyclerView;
    ArrayList<Message> messageList;
    private EditText messageBox;
    private ImageView sendButton;
    private TextView toolbarName;

    String receiverRoom;
    String senderRoom;

    Intent intent;
    String receiverUid;
    String name;


    String time = "";


    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        receiverUid = intent.getStringExtra("userId");
        name = intent.getStringExtra("username");
        fuser = FirebaseAuth.getInstance().getCurrentUser();



        String senderUid = fuser.getUid(); //Firebase 인증 객체 초기화
        mDbRef = FirebaseDatabase.getInstance().getReference(); //Firebase에 데이터를 추가하거나 조회하기 위한 코드, 정의


        senderRoom = receiverUid + senderUid;
        receiverRoom = senderUid + receiverUid;

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sentButton);
        toolbarName = findViewById(R.id.toolbarName);

        toolbarName.setText(name);

        mAuth = FirebaseAuth.getInstance();
        messageList = new ArrayList<Message>();
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        messageAdapter = new MessageAdapter(ChatActivity.this, messageList);
        chatRecyclerView.setAdapter(messageAdapter);

        mDbRef.child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        messageList.add(message);
                        chatRecyclerView.scrollToPosition(messageList.size()-1);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString();
                if(!message.isEmpty()){
                    time = getTime();
                    Log.i(ContentValues.TAG,message);
                    Message messageObject = new Message(message,senderUid,receiverUid,time);
                    mDbRef.child("chats").child(senderRoom).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                             //       Message messageObject = new Message(message,receiverUid ,senderUid);
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject);
                                }
                            });
                    messageBox.setText("");
                    chatRecyclerView.scrollToPosition(messageList.size()-1);
                }
                else
                    return;
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

//    public void onBackPressed() {
////        Intent intent = new Intent(Intent.ACTION_MAIN);
////        intent.addCategory(Intent.CATEGORY_HOME);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startActivity(intent);
//    }
}
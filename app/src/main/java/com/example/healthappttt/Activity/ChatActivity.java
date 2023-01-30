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

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.User;
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

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference mDbRef;
    FirebaseAuth mAuth;
    FirebaseUser fuser;
    RecyclerView chatRecyclerView;
    ArrayList<Message> messageList;
    private EditText messageBox;
    private ImageView sendButton;

    String receiverRoom;
    String senderRoom;

    Intent intent;
    String receiverUid;
    String name;


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
                if(message != ""){
                    Message messageObject = new Message(message,senderUid);

                    mDbRef.child("chats").child(senderRoom).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject);
                                }
                            });
                    messageBox.setText("");
                    chatRecyclerView.scrollToPosition(messageList.size()-1);
                }
            }
        });

    }
}
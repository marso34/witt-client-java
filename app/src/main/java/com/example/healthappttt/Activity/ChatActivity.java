package com.example.healthappttt.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.MessageListAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;

    private EditText messageEditText;
    private ImageButton sendButton;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 인텐트에서 유저 이름을 가져옵니다.
        username = getIntent().getStringExtra("username");

        // 레이아웃을 초기화합니다.
        messageRecyclerView = findViewById(R.id.chatRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEditText = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sendButton);

        // 메시지 목록을 초기화합니다.
        messageList = new ArrayList<>();

        // 어댑터를 초기화하고 메시지 목록을 설정합니다.
        messageListAdapter = new MessageListAdapter(messageList, username);
        messageRecyclerView.setAdapter(messageListAdapter);

        // 서버로부터 메시지 목록을 가져와서 messageList에 저장합니다.
        getMessagesFromServer();

        // 보내기 버튼의 클릭 리스너를 설정합니다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.equals("")) {
                    // 서버로 메시지를 보냅니다.
                    sendMessageToServer(messageText);

                    // 메시지를 추가하고 어댑터를 갱신합니다.
                    Message newMessage = new Message(username, messageText, System.currentTimeMillis());
                    messageList.add(newMessage);
                    messageListAdapter.notifyDataSetChanged();

                    // 메시지 입력 필드를 비웁니다.
                    messageEditText.setText("");
                }
            }
        });
    }

    // 서버에서 메시지 목록을 가져오는 메소드입니다.
    private void getMessagesFromServer() {
        // 서버로부터 메시지 목록을 가져와서 List<Message>로 반환합니다.
        // TODO: 구현해야 함
    }

    // 서버로 메시지를 보내는 메소드입니다.
    private void sendMessageToServer(String messageText) {
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
        Message newMessage = new Message(username, messageText, System.currentTimeMillis());
        Call<Message> call = apiService.sendMessage(newMessage);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                // 메시지 전송이 성공한 경우
                if (response.isSuccessful()) {
                    Log.d("message", "Message sent successfully");
                } else {
                    Log.d("message", "Failed to send message");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                // 메시지 전송이 실패한 경우
                Log.d("message", "Failed to send message", t);
            }
        });
    }
}


//    private String getTime() {
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//        String getTime = dateFormat.format(date);
//
//        return getTime;
//    }

//    public void onBackPressed() {
////        Intent intent = new Intent(Intent.ACTION_MAIN);
////        intent.addCategory(Intent.CATEGORY_HOME);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startActivity(intent);
//    }

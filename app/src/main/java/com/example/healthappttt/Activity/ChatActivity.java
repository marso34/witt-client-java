package com.example.healthappttt.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SocketSingleton;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.MessageListAdapter;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;
    private PreferenceHelper preferenceHelper;
    private String userKey;
    private EditText messageEditText;
    private ImageButton sendButton;
    private MqttClient mqttClient;
    private String username;
    private SocketSingleton socketSingleton;
    private String otherUserName;
    private String chatRoomId;
    private String otherUserKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        socketSingleton = SocketSingleton.getInstance();
        // 인텐트에서 유저 이름을 가져옵니다.
        //username = getIntent().getExtra("username"); 이전 엑티비티에서 유저의 필요한 모든 정보 받아오기.
        preferenceHelper = new PreferenceHelper(this);
        //userKey = String.valueOf(preferenceHelper.getPK());


            otherUserName =  getIntent().getStringExtra("otherUserName");
            chatRoomId =  getIntent().getStringExtra("ChatRoomId");
            otherUserKey =  getIntent().getStringExtra("otherUserKey");
        Log.d(TAG, "onCreate:chatact "+otherUserKey);
        //otherUserKey로 로컬에서 프로필 데이터 가져오기. 일단 임시로 '284' 내 구글이메일
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
                    Message newMessage = new Message("김도현", messageText, System.currentTimeMillis());
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
        try {
            JSONObject data = new JSONObject();
            data.put("otherUserKey",Integer.parseInt(otherUserKey));
            data.put("chatRoomId", Integer.parseInt(chatRoomId));
            data.put("messageText", messageText);

            // Emit the 'sendMessage' event to the server with the message data
            socketSingleton.getSocket().emit("sendMessage", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

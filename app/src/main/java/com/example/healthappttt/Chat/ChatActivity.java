package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.R;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private List<MSG> messageList;
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
    private SQLiteUtil sqLiteUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        socketSingleton = SocketSingleton.getInstance(this);
        // 인텐트에서 유저 이름을 가져옵니다.
        //username = getIntent().getExtra("username"); 이전 엑티비티에서 유저의 필요한 모든 정보 받아오기.
        preferenceHelper = new PreferenceHelper("UserTB",this);
        //userKey = String.valueOf(preferenceHelper.getPK());
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
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
        messageListAdapter = new MessageListAdapter(messageList, username, otherUserName);
        messageRecyclerView.setAdapter(messageListAdapter);

        // 서버로부터 메시지 목록을 가져와서 messageList에 저장합니다.
        getMessagesFromRealTime();

        // 보내기 버튼의 클릭 리스너를 설정합니다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.equals("")) {
                    // 서버로 메시지를 보냅니다.
                    sendMessageToServer(messageText);

                    // 메시지를 추가하고 어댑터를 갱신합니다.
                    MSG newMessage = new MSG(1,Integer.parseInt(chatRoomId), messageText, String.valueOf(System.currentTimeMillis()));
                    messageList.add(newMessage);
                    messageListAdapter.notifyDataSetChanged();
                    getMessagesFromRealTime();
                    // 메시지 입력 필드를 비웁니다.
                    messageEditText.setText("");
                }
            }
        });
    }

    // 서버에서 메시지 목록을 가져오는 메소드입니다.
    private void getMessagesFromRealTime() {
        List<MSG>newMessages = null;
        // 서버로부터 메시지 목록을 가져와서 List<Message>로 반환합니다.
        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
        if(chatRoomId!= null) {
            newMessages = sqLiteUtil.SelectMSG(0,Integer.parseInt(chatRoomId));
        }
        for (MSG msg : newMessages) {
            messageList.add(msg);
            Log.d(TAG, "getMessagesFromServer: "+msg.getMessage());
        }
        messageListAdapter.notifyDataSetChanged();
        //메세지 리스트에 넣고 정렬하기.
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

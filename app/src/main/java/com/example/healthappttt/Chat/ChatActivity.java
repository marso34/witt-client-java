package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ServiceApi apiService;
    private  int out = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        socketSingleton = SocketSingleton.getInstance();
        socketSingleton.initialize(getBaseContext());

        socketSingleton.setChatActivity(this);
        // 인텐트에서 유저 이름을 가져옵니다.
        //username = getIntent().getExtra("username"); 이전 엑티비티에서 유저의 필요한 모든 정보 받아오기.
        preferenceHelper = new PreferenceHelper("UserTB",this);
        userKey = String.valueOf(preferenceHelper.getPK());
        Log.d(TAG, "onCreate: userkey"+userKey);
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
        getMSGFromServer(Integer.parseInt(userKey));
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
        getAllMSG();

        // 보내기 버튼의 클릭 리스너를 설정합니다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();

                if (!messageText.equals("")) {
                    // 서버로 메시지를 보냅니다.
                    sendMessageToServer(messageText);

                    // 현재 시간을 가져옵니다.
                    Date currentDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = format.format(currentDate);
                    System.out.println("Current Time: " + currentTime);

                    // 새로운 메시지를 생성합니다.
                    MSG newMessage = new MSG(1, Integer.parseInt(chatRoomId), messageText, currentTime);
                    SaveMyMessage(Integer.parseInt(userKey),1,messageText,Integer.parseInt(chatRoomId));
                    // 메시지를 리스트에 추가합니다.
                    messageList.add(newMessage);
//                    Collections.sort(messageList, new Comparator<MSG>() {
//                        @Override
//                        public int compare(MSG msg1, MSG msg2) {
//                            long timestamp1 = msg1.getTimestamp();
//                            long timestamp2 = msg2.getTimestamp();
//                            return Long.compare(timestamp1, timestamp2);
//                        }
//                    });
                    // 어댑터를 갱신합니다.
                    getAllMSG();
                    // 스크롤을 마지막 메시지로 이동합니다.
                    messageRecyclerView.scrollToPosition(messageList.size() - 1);

                    // 메시지 입력 필드를 비웁니다.
                    messageEditText.setText("");
                }
            }
        });

    }
    private void getMSGFromServer(int userKey){

        apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성
        Call<List<MSG>> call = apiService.getMSGFromServer(new pkData(userKey));
        call.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                if (response.isSuccessful()) {
                    List<MSG> msgList = response.body();
                    for (MSG msg : msgList) {
                        sqLiteUtil.setInitView(getBaseContext(), "CHAT_MSG_TB");
                        sqLiteUtil.insert(userKey,2, msg.getMessage(), msg.getChatRoomId(),1);

                    }
                } else {
                    Log.e("getMSGFromServer", "API 요청 실패. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {

            }
        });

    }
    public String getChatRoomId (){
        return chatRoomId;
    }
    private void SaveMyMessage(int userKey,int myFlag,String message,int chatRoomId){

        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this,"CHAT_MSG_TB");
        sqLiteUtil.insert(userKey,1,message,chatRoomId,0);

    }
    public void getAllMSG(){
        messageList.clear();
        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
        for (MSG M : sqLiteUtil.SelectAllMSG(userKey,Integer.parseInt(chatRoomId))){
            messageList.add(M);

        }

//        Collections.sort(messageList, new Comparator<MSG>() {
//            @Override
//            public int compare(MSG msg1, MSG msg2) {
//                long timestamp1 = msg1.getTimestamp();
//                long timestamp2 = msg2.getTimestamp();
//                return Long.compare(timestamp1, timestamp2);
//            }
//        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageListAdapter.notifyDataSetChanged();
            }
        });
    }
    // 서버에서 메시지 목록을 가져오는 메소드입니다.
    public void getMessagesFromRealTime() {
        List<MSG> newMessages = null;
        // 서버로부터 메시지 목록을 가져와서 List<Message>로 반환합니다.
        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
        if (chatRoomId != null) {
            newMessages = sqLiteUtil.SelectMSG(userKey,0, Integer.parseInt(chatRoomId));
        } else {
            Log.d(TAG, "getMessagesFromRealTime: error ");
        }


        for (MSG msg : newMessages) {
            messageList.add(msg);
        }
//        Collections.sort(messageList, new Comparator<MSG>() {
//            @Override
//            public int compare(MSG msg1, MSG msg2) {
//                long timestamp1 = msg1.getTimestamp();
//                long timestamp2 = msg2.getTimestamp();
//                return Long.compare(timestamp1, timestamp2);
//            }
//        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageListAdapter.notifyDataSetChanged();
            }
        });
        // TODO: 메시지 리스트에 넣고 정렬하기
    }

    // 서버로 메시지를 보내는 메소드입니다.
    private void sendMessageToServer(String messageText) {
        try {
            out =1;
            JSONObject data = new JSONObject();
            data.put("mySocketId",socketSingleton.getSocket().id());
            Log.d(TAG, "sendMessageToServer: "+socketSingleton.getSocket().id());
            data.put("otherUserKey",Integer.parseInt(otherUserKey));
            data.put("chatRoomId", Integer.parseInt(chatRoomId));
            data.put("messageText", messageText);
            performSendingAndWaiting(data);
            // Emit the 'sendMessage' event to the server with the message data

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void performSendingAndWaiting(JSONObject data) {
        socketSingleton.getSocket().emit("sendMessage", data);
        socketSingleton.getSocket().on("receiveReturn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0]; // 수신된 데이터 추출
                try {
                    String signal = data.getString("signal"); // signal 값을 추출하여 사용
                    // 처리할 작업 수행
                    out = Integer.parseInt(signal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
            // 1초 후에 다시 송신과 대기를 수행
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (out != 2)
                        performSendingAndWaiting(data);
                    else{
                        handler.removeCallbacksAndMessages(null);
                    }
                }
            }, 1000); // 1000 밀리초는 1초입니다.
        }

    }

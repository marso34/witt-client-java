package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity{

    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private List<MSG> messageList;
    private PreferenceHelper preferenceHelper;
    private String userKey;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageView menu;
    private SocketSingleton socketSingleton;
    private String otherUserName;
    private String chatRoomId;
    private String otherUserKey;
    private SQLiteUtil sqLiteUtil;
    private ServiceApi apiService;
    private boolean updatingMSG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
        sqLiteUtil = SQLiteUtil.getInstance();
            initViews();
            initSocketSingleton();
            retrieveIntentData();
            setupRecyclerView();
            setupSQLiteUtil();
            setupListeners();
            getMessagesFromServer();
            clickmenu();
        }
    private void initViews() {
        messageRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sendButton);
        menu = findViewById(R.id.menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        socketSingleton.setChatActivity(null);
        finish();
    }

    private void initSocketSingleton() {
        socketSingleton = SocketSingleton.getInstance(getBaseContext());
        socketSingleton.setChatActivity(this);
    }

    private void retrieveIntentData() {
        preferenceHelper = new PreferenceHelper("UserTB", this);
        userKey = String.valueOf(preferenceHelper.getPK());//TODO 내 키
        otherUserName = getIntent().getStringExtra("otherUserName");
        chatRoomId = getIntent().getStringExtra("ChatRoomId");
        otherUserKey = getIntent().getStringExtra("otherUserKey"); //TODO 상대 키
        Log.d(TAG, "onCreate: otherUserKey " + otherUserKey);
    }

    private void setupRecyclerView() {
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messageList, preferenceHelper.getUser_NM(), otherUserName);
        messageRecyclerView.setAdapter(messageListAdapter);
    }

    private void setupSQLiteUtil() {
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
    }

    private void setupListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    sendMessageToServer(messageText);
                    socketSingleton.SqlLiteSaveMessage(Integer.parseInt(userKey),1,messageText,Integer.parseInt(chatRoomId));
                    getAllMSG();
                    messageEditText.setText("");
                }
            }
        });
    }
    private void getMessagesFromServer() {
        setupSQLiteUtil();
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        Call<List<MSG>> call = apiService.getMSGFromServer(new pkData(Integer.parseInt(userKey)));
        call.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                if (response.isSuccessful()) {
                    List<MSG> msgList = response.body();
                    if (msgList != null) {
                        for (MSG msg : msgList) {
                            socketSingleton.SqlLiteSaveMessage(Integer.parseInt(userKey),2,msg.getMessage(),Integer.parseInt(chatRoomId));

                        }
                    }
                    getAllMSG();
                } else {
                    Log.e(TAG, "getMessagesFromServer: API 요청 실패. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                Log.e(TAG, "getMessagesFromServer: API 요청 실패: " + t.getMessage());
            }
        });
    }

    public String getChatRoomId(){
        return chatRoomId;
    }
    public void getAllMSG() {

        final CountDownLatch latch = new CountDownLatch(1);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                    List<MSG> TM = sqLiteUtil.SelectAllMSG(userKey, Integer.parseInt(chatRoomId));
                    if(TM != null) {
                        messageList.clear();
                        messageList.addAll(TM);
                        messageListAdapter.notifyDataSetChanged();
                        messageRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            updatingMSG = false;
        }
// 작업이 모두 끝난 후에 실행될 코드

    }
    public boolean getUpdatingMSG(){
        return updatingMSG;
    }
    public void setUpdatingMSG(boolean t){
        this.updatingMSG = t;
    }
    private void sendMessageToServer(String messageText) {
        try {
            JSONObject data = new JSONObject();
            data.put("myUserKey", userKey);
            data.put("otherUserKey", Integer.parseInt(otherUserKey));
            data.put("chatRoomId", Integer.parseInt(chatRoomId));
            data.put("messageText", messageText);
            socketSingleton.sendMessage(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickmenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.chat_menu_popup, null);

                Button reportBtn = findViewById(R.id.report_btn);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                alertDialog.show();

//                reportBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Intent intent = new Intent(ChatActivity.this,ReportActivity.class)
//                     intent.putExtra("otherUserName",user.getUserNM());
//                    intent.putExtra("otherUserKey",user.getOtherUserKey());
//                      intent.putExtra("mypk",String.valueOf(preferenceHelper.getPK()) );
//                    //TODO 사용자 신고 엑티비티로 내pk와 상대 pk 넘겨주고
//                    //TODO 체크박스 누르고 글쓰고 신고하기 하면 서버db에 저장
//                    }
//                });

            }
        });
    }

}

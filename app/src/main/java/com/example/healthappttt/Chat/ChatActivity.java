package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.CustomToast;
import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Chat.getMSGKey;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.Profile.MyProfileActivity;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity{
    private CustomToast currentToast;
    private String current_day;
    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private List<MSG> messageList;
    private PreferenceHelper preferenceHelper;
    private String userKey;
    private EditText messageEditText;
    private View sendButton;
    private ImageView menu;
    private SocketSingleton socketSingleton;
    private String otherUserName;
    private String chatRoomId;
    private String otherUserKey;
    private SQLiteUtil sqLiteUtil;
    private ServiceApi apiService;
    private boolean updatingMSG = false;
    private boolean sendUpdatingMSG  = false;
    private TextView toolbarName;
    private View tbr;
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
        messageRecyclerView.addOnScrollListener(recyclerViewScrollListener);
        SocketSingleton.getInstance(this);
// 리사이클러뷰의 LayoutManager를 가져옵니다.

    }
    @Nullable
    private ChattingFragment findChattingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        for (Fragment fragment : fragments) {
            if (fragment instanceof ChattingFragment) {
                // ChattingFragment를 찾았을 때 해당 Fragment를 반환
                return (ChattingFragment) fragment;
            }
        }

        // ChattingFragment를 찾지 못한 경우 null 반환
        return null;
    }
    private void initViews() {
        messageRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sendButton);
        menu = findViewById(R.id.menu);
        toolbarName = findViewById(R.id.toolbarName);
        tbr = findViewById(R.id.Toolbar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentToast != null)
            currentToast.cancel();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        socketSingleton.setChatActivity(null);
        if (currentToast != null)
            currentToast.cancel();
        finish();
    }

    private RecyclerView.OnScrollListener recyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            // Call getFirstRecyclerView() whenever there is a change in the visible RecyclerView
            getFirstRecyclerView();

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getFirstRecyclerView(){
        int tbrId = R.id.Toolbar; // Replace with the ID of your tbr (Toolbar) layout
        View tbrLayout = findViewById(tbrId); // Assuming this method is called within an Activity

// Get the bottom position of the tbr layout relative to the parentLayout
        int tbrBottom = tbrLayout.getBottom();
        RecyclerView.LayoutManager layoutManager = messageRecyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            // 리사이클러뷰의 레이아웃 매니저를 LinearLayoutManager로 캐스팅합니다.
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            // 첫 번째 보이는 항목의 위치를 가져옵니다.
            int firstVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

            // 최소한 하나 이상의 항목이 화면에 보이는지 확인합니다.
            if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                // 어댑터에서 해당 위치의 데이터 항목을 가져옵니다.
                String firstVisibleItemData = messageListAdapter.getItemTS(firstVisibleItemPosition);

                // 이제 firstVisibleItemData를 사용하여 원하는 값을 출력할 수 있습니다.
                // 예를 들어:
                String firstVisibleItemValue = firstVisibleItemData;

                // 값을 로그로 출력하거나 원하는 방식으로 화면에 표시합니다.
                if (currentToast != null) {
                    current_day = extractDateTime(firstVisibleItemValue);
                    currentToast.cancel();
                    currentToast = CustomToast.makeText(ChatActivity.this, extractDateTime(firstVisibleItemValue), Toast.LENGTH_SHORT);
                    try {

                        currentToast.show();

                    } finally {

                        int yOffset = tbrBottom; // This will position the toast below the tbr layout
                        currentToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, yOffset);

                    }
                }
                else {
                    current_day = extractDateTime(firstVisibleItemValue);
                    currentToast = CustomToast.makeText(ChatActivity.this, extractDateTime(firstVisibleItemValue), Toast.LENGTH_SHORT);
                    try {
                        currentToast.show();
                    }finally {
                        int yOffset = tbrBottom; // This will position the toast below the tbr layout
                        currentToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, yOffset);
                    }
                }
            }
        }
    }


    private void initSocketSingleton() {

        socketSingleton = SocketSingleton.getInstance(getBaseContext());
        socketSingleton.setChatActivity(this);

    }


    private void retrieveIntentData() {

        preferenceHelper = new PreferenceHelper("UserTB", this);
        userKey = String.valueOf(preferenceHelper.getPK());
        otherUserName = getIntent().getStringExtra("otherUserName");
        chatRoomId = getIntent().getStringExtra("ChatRoomId");
        otherUserKey = getIntent().getStringExtra("otherUserKey");
        toolbarName.setText(otherUserName);
        Log.d(TAG, "onCreate: otherUserKey " + otherUserKey);

    }

    private void setupRecyclerView() {
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messageList, preferenceHelper.getUser_NM(), otherUserName,otherUserKey,this);
        messageRecyclerView.setAdapter(messageListAdapter);
    }


    private void setupSQLiteUtil() {
        sqLiteUtil.setInitView(this, "CHAT_MSG_TB");
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
            // 날짜가 오늘이 아니면 월과 일 추출
            int month = dateTime.getMonthValue();
            int day = dateTime.getDayOfMonth();
            return String.format("%02d월 %02d일", month, day);
        }
    }


    private void setupListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    messageEditText.setText("");
                    int chatkey = -1;
                    String ts  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    try {
                        if(getApplicationContext()!=null) {
                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                            chatkey = sqLiteUtil.getLastMyMsgPK(String.valueOf(chatRoomId), userKey);
                            chatkey = chatkey + 1;
                        }
                    }finally {
                        if(getApplicationContext() !=null) {
                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                            sqLiteUtil.insert(getApplicationContext(),chatkey, Integer.parseInt(userKey), 1, messageText, Integer.parseInt(chatRoomId), 0, ts);
                            Log.d(TAG, "onClick: 보내기" + chatkey);
                            sendMessageToServer(messageText, chatkey, ts);
                        }
                    }
                }
            }
        });
        View view = findViewById(R.id.backBtn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View toolbarNameBtn = findViewById(R.id.toolbarName);
        toolbarNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("상세 프로필", "userAdapter에서 클릭처리");
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
                String adapterUserKey = otherUserKey;
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                intent.putExtra("PK", Integer.valueOf(adapterUserKey));
                intent.putExtra("dayOfWeek", dayOfWeekNumber - 1);
//                intent.putExtra("post",finalProfilefile);--?
                startActivity(intent);
            }
        });
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


    private void getMessagesFromServer() {
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        if(getApplicationContext() != null) {
            MSG m = sqLiteUtil.selectLastMsg(this,chatRoomId, userKey, 1);
            Log.d(TAG, "getMessagesFromServer: " + m.getKey());
            Call<List<MSG>> call = apiService.getMSGFromServer(new getMSGKey(Integer.parseInt(userKey), Integer.parseInt(chatRoomId), m.getKey(), m.timestampString()));
            call.enqueue(new Callback<List<MSG>>() {
                @Override
                public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                    if (response.isSuccessful()) {
                        try {
                            List<MSG> msgList = response.body();
                            if (msgList != null) {
                                for (MSG msg : msgList) {
                                    String name1 = extractName(msg.getMessage());
                                    Log.d(TAG, "onResponse: ts" + msg.timestampString());
                                    if (name1 != null) {
                                        if (getApplicationContext() != null) {
                                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                                            sqLiteUtil.insert(getApplicationContext(),msg.getKey(), Integer.parseInt(userKey), msg.getMyFlag(), name1, Integer.parseInt(chatRoomId), 1, msg.timestampString());
                                        }
                                    } else {
                                        if (getApplicationContext() != null) {
                                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                                            sqLiteUtil.insert(getApplicationContext(),msg.getKey(), Integer.parseInt(userKey), msg.getMyFlag(), msg.getMessage(), Integer.parseInt(chatRoomId), 1, msg.timestampString());
                                        }
                                    }
                                    Log.d(TAG, "onResponse: msg" + msg.getMessage());
                                }
                            }
                        }
                        finally {
                            updatingMSG = true;
                            sendUpdatingMSG = true;
                            getMSG(3);
                        }
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
    }


    public String getChatRoomId(){
        return chatRoomId;
    }
    public void getMSG(int sendOrReceive) {
        final CountDownLatch latch = new CountDownLatch(1);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(getApplicationContext() != null) {
                        List<MSG> TM = sqLiteUtil.selectAllMSG(userKey, Integer.parseInt(chatRoomId),getApplicationContext());
                        messageList.clear();
                        if (TM != null) {
                            messageList.clear();
                            messageList.addAll(TM);
                            messageListAdapter.notifyDataSetChanged();
                            messageRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
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
            if(sendOrReceive == 1)
                updatingMSG = false;
            else if (sendOrReceive ==2)
                sendUpdatingMSG = false;
            else if (sendOrReceive == 3){
                updatingMSG = false;
                sendUpdatingMSG = false;
            }
        }
// 작업이 모두 끝난 후에 실행될 코드
    }


    public boolean getUpdatingMSG(){
        return updatingMSG;
    }
    public void setUpdatingMSG(boolean t){
        this.updatingMSG = t;
    }
    public boolean getSendUpdatingMSG(){
        return sendUpdatingMSG;
    }
    public void setSendUpdatingMSG(boolean t){
        this.sendUpdatingMSG = t;
    }
    public void sendMessageToServer(String messageText, int chatPk,String ts) {
        try {
            JSONObject data = new JSONObject();
            data.put("myUserKey", userKey);
            data.put("otherUserKey", Integer.parseInt(otherUserKey));
            data.put("chatRoomId", Integer.parseInt(chatRoomId));
            data.put("messageText", messageText);
            data.put("chatPk",chatPk);
            data.put("TS",ts);
            socketSingleton.sendMessage(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // clickmenu() 메서드 추가
    public void clickmenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 바텀 시트 다이얼로그 생성
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ChatActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.chat_menu_popup, null);

                if (dialogView == null) {
                    Log.d("chat", "다이얼로그 널이야");
                    return;
                }

                bottomSheetDialog.setContentView(dialogView);

                // 신고하기 버튼 클릭 시
                View reportBtn = dialogView.findViewById(R.id.report_btn);
                reportBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, ReportActivity.class);
                        intent.putExtra("otherUserName", otherUserName);
                        intent.putExtra("otherUserKey", otherUserKey);
                        intent.putExtra("mypk", String.valueOf(userKey));
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                        finish();
                    }
                });

                View BlackBtn = dialogView.findViewById(R.id.blackBtn);
                BlackBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick:black ");
                        apiService = RetrofitClient.getClient().create(ServiceApi.class);
                        Call<String> call = apiService.insertBL(new pkData(preferenceHelper.getPK(),Integer.parseInt(otherUserKey)));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse: "+response.body());

                                    Log.d(TAG, "onResponse: 실행");
                                    try {
                                        sqLiteUtil.setInitView(getBaseContext(), "CHAT_ROOM_TB");
                                        sqLiteUtil.deleteChatRoom(preferenceHelper.getPK(), Integer.parseInt(chatRoomId));
                                    } finally {
                                        finish();
                                    }


                                } else {
                                    Log.d(TAG, "onResponse: 프레그 널");
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e(TAG, "getMessagesFromServer: API 요청 실패: " + t.getMessage());
                            }
                        });



                        setupSQLiteUtil();
                        apiService = RetrofitClient.getClient().create(ServiceApi.class);
                        Call<String> call2 = apiService.blackChatRoom(new pkData(preferenceHelper.getPK(),Integer.parseInt(chatRoomId)));
                        call2.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call2, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse: "+response.body());

                                    Log.d(TAG, "onResponse: 실행");
                                    try {
                                        sqLiteUtil.setInitView(getBaseContext(), "CHAT_ROOM_TB");
                                        sqLiteUtil.deleteChatRoom(preferenceHelper.getPK(), Integer.parseInt(chatRoomId));
                                    } finally {
                                        finish();
                                    }


                                } else {
                                    Log.d(TAG, "onResponse: 프레그 널");
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call2, Throwable t) {
                                Log.e(TAG, "getMessagesFromServer: API 요청 실패: " + t.getMessage());
                            }
                        });
                    }
                });



                View reviewBtn = dialogView.findViewById(R.id.review_btn);
                reviewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, ReviewActivity.class);
                        intent.putExtra("name", otherUserName);
                        intent.putExtra("code", otherUserKey);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                View leaveBtn = dialogView.findViewById(R.id.leave_btn);
                leaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick:leav ");
                        setupSQLiteUtil();
                        apiService = RetrofitClient.getClient().create(ServiceApi.class);
                        Call<String> call = apiService.leaveChatRoom(new pkData(preferenceHelper.getPK(),Integer.parseInt(chatRoomId)));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse: "+response.body());

                                    try {
                                        sqLiteUtil.setInitView(getBaseContext(), "CHAT_ROOM_TB");
                                        sqLiteUtil.deleteChatRoom(preferenceHelper.getPK(), Integer.parseInt(chatRoomId));
                                    } finally {
                                        finish();
                                    }

                                } else {
                                    Log.d(TAG, "onResponse: 프레그 널");
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e(TAG, "getMessagesFromServer: API 요청 실패: " + t.getMessage());
                            }
                        });
                    }
                });


                // 바텀 시트 다이얼로그 노출
                bottomSheetDialog.show();

            }
        });
    }

}







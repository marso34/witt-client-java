package com.example.healthappttt.Data.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Chat.ChattingFragment;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.AlarmRecevier;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketSingleton {
    private static volatile SocketSingleton instance;
    private Socket mSocket;
    private String serverUrl = "http://43.200.245.144:1337/";
    private boolean isConnected = false;
    private Context context;
    private SQLiteUtil sqLiteUtil;
    private ChatActivity chatActivity;
    private int chatPk = -1;
    private PreferenceHelper preferenceHelper;
    private static int alarmID = 100;
    private static int i;
    ChattingFragment chatF;
    private SocketSingleton(Context context) {
        try {
            this.context = context;
            mSocket = IO.socket(serverUrl);
            preferenceHelper = new PreferenceHelper("UserTB", context);
            setupSocketListeners();
        } catch (URISyntaxException e) {
            Log.d("SocketSingleton", "Failed to initialize Socket: " + e.getMessage());
        }
        connect();
        receiveMessage();
        returnSignal();
    }

    public static synchronized SocketSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new SocketSingleton(context);
        }
        return instance;
    }

    public void initialize() {
        sqLiteUtil = SQLiteUtil.getInstance();

    }

    private void setupSocketListeners() {
        mSocket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketSingleton", "Socket connected");
                JSONObject data = (JSONObject) args[0];
                try {
                    String socketId = data.getString("socketId");
                    Log.d("SocketSingleton", "Socket ID: " + socketId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                insertSocket();
            }
        });

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketSingleton", "Socket disconnected");
                isConnected = false;
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

    private void receiveMessage() {
        mSocket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: 문자받음");
                JSONObject data = (JSONObject) args[0];
                String message;
                String chatRoomId;
                boolean flag = false;
                try {
                    message = data.getString("message");
                    chatRoomId = data.getString("chatRoomId");
                    String TS = data.getString("timeStamp");
                    String chat_Pk = data.getString("chatKey");
                    if(extractName(message) != null) {
                        Intent receiverIntent = new Intent(context, AlarmRecevier.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i++, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
                        Calendar calendar = Calendar.getInstance();
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                        if(!chatF.chatflag) {
                            chatF.chatflag = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    chatF.getLastMSG(chatRoomId,String.valueOf(preferenceHelper.getPK()));
                                }
                            }).start();
                        }
                        flag = true;
                    }
                    if (chatRoomId != null) {
                        int CRI = Integer.parseInt(chatRoomId);
                        SqlLiteSaveMessage(Integer.parseInt(chat_Pk),preferenceHelper.getPK(), 2, message, CRI,TS);
                        mSocket.emit("completeMessage");
                            if(chatActivity != null && chatActivity.getChatRoomId().equals(chatRoomId)) {
                                if (!chatActivity.getUpdatingMSG()) {
                                    chatActivity.setUpdatingMSG(true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "run: " + chatActivity.getUpdatingMSG());
                                            chatActivity.getMSG(1);
                                        }
                                    }).start();
                                }
                            }
                        else {
                            if(flag == false) {
                                createNotificationChannel();
                                showCustomNotification(chatRoomId, message); // 채팅 메시지 알림 표시
                                if(chatF!=null && !chatF.chatflag) {
                                    chatF.chatflag = true;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatF.getLastMSG(chatRoomId,String.valueOf(preferenceHelper.getPK()));
                                        }
                                    }).start();
                                }
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMessage(JSONObject message) {
        Log.d("SocketSingleton", "sendMessage: Sending message");
        mSocket.emit("sendMessage", message);

    }

    public void returnSignal() {
        mSocket.on("receiveReturn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String signal = data.getString("signal");
                    String ts = data.getString("timeStamp");
                    String chatPk = data.getString("chatKey");
                    Log.d(TAG, "chatpk받기"+chatPk+"ㅁㅁ"+ signal + "ts" + ts);
                    sqLiteUtil.setInitView(context.getApplicationContext(), "CHAT_MSG_TB");
                    MSG m = sqLiteUtil.SelectMSG(String.valueOf(preferenceHelper.getPK()),Integer.parseInt(signal));
                    if(m != null) {
                        sqLiteUtil.setInitView(context.getApplicationContext(), "CHAT_MSG_TB");
                        sqLiteUtil.deleteMSG(Integer.parseInt(signal));
                        sqLiteUtil.setInitView(context.getApplicationContext(), "CHAT_MSG_TB");
                        sqLiteUtil.insert(Integer.parseInt(chatPk), preferenceHelper.getPK(), 1, m.getMessage(), m.getChatRoomId(), 1, ts);
                        if (chatActivity != null) {
                            if (!chatActivity.getSendUpdatingMSG()) {
                                chatActivity.setSendUpdatingMSG(true);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: " + chatActivity.getSendUpdatingMSG());
                                        chatActivity.getMSG(2);
                                    }
                                }).start();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void SqlLiteSaveMessage(int chatPk,int userKey, int myFlag, String message, int chatRoomId,String ts) {
        sqLiteUtil.setInitView(context.getApplicationContext(), "CHAT_MSG_TB");
        sqLiteUtil.insert(chatPk,userKey, myFlag, message, chatRoomId, 1,ts);
    }

    private void insertSocket() {
        JSONObject data = new JSONObject();
        try {
            data.put("userKey", preferenceHelper.getPK());
            data.put("socketId", mSocket.id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("insertSocket", data);
    }



    public Socket getSocket() {
        return mSocket;
    }

    public void connect() {
        if (mSocket != null) {
            mSocket.connect();
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
            instance.disconnect();
            instance=null;
            Log.d("SocketSingleton", "disconnect: Disconnected");
        }
    }

    public String getSocketId() {
        if (mSocket != null && isConnected) {
            return mSocket.id();
        }
        return null;
    }

    public void setChatActivity(ChatActivity chatActivity) {
        if (chatActivity == null) {
            this.chatActivity = null;
        }
        this.chatActivity = chatActivity;
    }
    public void setChatFragment(ChattingFragment chatF) {
        if (chatF == null) {
            this.chatF = null;
        }
        this.chatF = chatF;
    }
    // 알림 채널 생성
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat_channel";
            String channelName = "Chat Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

            // 알림 채널에 대한 추가 설정
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // 커스텀 알림 레이아웃을 사용하여 알림 생성
    private void showCustomNotification(String chatRoomID, String MSG) {
        // 알림 빌더 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "chat_channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(chatRoomID)
                .setContentText(MSG)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 알림 레이아웃을 커스텀한 RemoteViews 생성
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        customLayout.setTextViewText(R.id.notification_title, chatRoomID);
        customLayout.setTextViewText(R.id.notification_content, MSG);
        builder.setCustomContentView(customLayout);

        // 알림 매니저를 통해 알림 생성
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(alarmID, builder.build());
        alarmID++;
    }


    // 스와이프로 알림 제거 처리
    private void handleNotificationDismiss(int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }


}

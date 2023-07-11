package com.example.healthappttt.Data.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketSingleton {
    private static volatile SocketSingleton instance;
    public static volatile Socket mSocket;
    private String serverUrl = "http://43.200.245.144:1337/";  // 서버 URL 설정
    private boolean isConnected = false;  // 소켓 연결 여부 확인
    private int userKey;
    private static Context context;
    private PreferenceHelper prefhelper;
    private SQLiteUtil sqLiteUtil;
    private ChatActivity chatActivity = null;

    private SocketSingleton(Context context) {
        this.context = context;
        Log.d(TAG, "소캣 키는중");
        try {
            mSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Failed to initialize Socket: " + e.getMessage());
            Toast.makeText(context, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            // 접속 할 수 있게 바꿔야돼나?
        } catch (Exception e) {
            Log.d(TAG, "Exception during Socket initialization: " + e.getMessage());
            Toast.makeText(context, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            // 접속 할 수 있게 바꿔야돼나?
        }
        connectSocket();
        setupSocketListeners();
        receiveMessage();
        sqLiteUtil = SQLiteUtil.getInstance(); //sqllite 객체

    }

    private void setupSocketListeners() {
        isConnected = false;
        mSocket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "소캣 연결됨");
                JSONObject data = (JSONObject) args[0];
                try {
                    String socketId = data.getString("socketId");
                    Log.d("ClientActivity", "Socket ID: " + socketId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isConnected = true;
                insertSocket();
            }
        });

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "Socket disconnected");
                isConnected = false;
            }
        });
    }

    private void receiveMessage() {
        prefhelper = new PreferenceHelper("UserTB",context);
        mSocket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // 서버로부터 메시지를 수신했을 때의 동작 처리
                Log.d(TAG, "callaa: ");
                JSONObject data = (JSONObject) args[0];
                String message;
                String chatRoomId = null;
                try {
                    message = data.getString("message");
                    chatRoomId = data.getString("chatRoomId");
                    if(chatRoomId !=null) {
                        int CRI = Integer.parseInt(chatRoomId);
                        SqlLiteSaveMessage(prefhelper.getPK(),2,message,CRI);
                        mSocket.emit("completeMessage");
                        if(chatActivity != null && chatActivity.getChatRoomId().equals(chatRoomId))
                            chatActivity.getAllMSG();
                        Log.d(TAG, "callcall: " + chatRoomId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 받은 메시지를 처리하는 로직 작성
            }
        });
    }


    private void SqlLiteSaveMessage(int userKey,int myFlag,String message,int chatRoomId){
        sqLiteUtil.setInitView(context,"CHAT_MSG_TB");
        sqLiteUtil.insert(userKey,myFlag,message,chatRoomId,1);
        Log.d(TAG, "SqlLiteSaveMessage: 메세지 저장 완료"+message);
    }

    private void insertSocket(){
        JSONObject data = new JSONObject();
        try {
            data.put("userKey", prefhelper.getPK());//임시로 이걸로
            data.put("socketId", mSocket.id());//임시로 이걸로
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("insertSocket", data);
        Log.d(TAG, "insertSocket: "+data);
    }
    public static SocketSingleton getInstance(Context context_) {
        if (instance == null) {
            synchronized (SocketSingleton.class) {
                if (instance == null) {
                    instance = new SocketSingleton(context_);
                }
            }
        }
        return instance;
    }
    public void setUserKey(int userkey_){
        this.userKey = userkey_;
    }
    public Socket getSocket() {
            return mSocket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        setupSocketListeners();
        if (!isConnected) {
            connectSocket();
        }
    }

    private void connectSocket() {
        if (mSocket != null && !mSocket.connected()) {
            mSocket.connect();
        }
    }


    public void disconnect() {
        if (mSocket != null && isConnected) {
            mSocket.disconnect();
            isConnected = false;
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
            throw new IllegalArgumentException("chatActivity cannot be null");
        }
        this.chatActivity = chatActivity;
    }

//    private void showNotification(String message) {
//        Intent deleteIntent = new Intent(context, NotificationDeleteReceiver.class);
//        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("New Message")
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .setDeleteIntent(deletePendingIntent);
//
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Notification Channel";
//            String description = "Channel for notifications";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED);
//
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
    //알림 띄우기 는 금방이다.
    // 다른 메소드 및 이벤트 핸들러 등 추가 구현 가능
}

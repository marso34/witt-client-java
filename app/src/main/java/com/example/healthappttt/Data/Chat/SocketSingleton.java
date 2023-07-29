package com.example.healthappttt.Data.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Chat.ChattingFragment;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Home.AlarmManagerCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketSingleton {
    private static volatile SocketSingleton instance;
    private Socket mSocket;
    private String serverUrl = "http://15.165.49.106:1337/";
    private boolean isConnected = false;
    private Context context;
    private SQLiteUtil sqLiteUtil;
    private ChatActivity chatActivity;
    private int chatPk = -1;
    private PreferenceHelper preferenceHelper;
    private static int alarmID = 100;
    private static int i;
    ChattingFragment chatF;
    private AlarmManagerCustom alarmManager;
    private SocketSingleton(Context context) {
        try {
            this.context = context;
            mSocket = IO.socket(serverUrl);
            preferenceHelper = new PreferenceHelper("UserTB", context);
            setupSocketListeners();
            alarmManager = AlarmManagerCustom.getInstance(context);
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
//                        Intent receiverIntent = new Intent(context, AlarmRecevier.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i++, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
//                        Calendar calendar = Calendar.getInstance();
//                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                        //채팅방 생성알림
                        alarmManager.onAlarm("New","새로운 위트가 왔어요");
                        if(chatF!=null && !chatF.chatflag) {
                            chatF.chatflag = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    chatF.getUsersFromServer();
                                    chatF.getLastMSG(chatRoomId,String.valueOf(preferenceHelper.getPK()),context);
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
//                                createNotificationChannel();
//                                showCustomNotification(chatRoomId, message); // 채팅 메시지 알림 표시
                                sqLiteUtil.setInitView(context, "CHAT_ROOM_TB");
                                String otherUserNM = sqLiteUtil.selectOtherUserName(String.valueOf(preferenceHelper.getPK()),chatRoomId);
                                alarmManager.onAlarm(otherUserNM,message);
                                if(chatF!=null && !chatF.chatflag) {
                                    chatF.chatflag = true;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatF.getLastMSG(chatRoomId,String.valueOf(preferenceHelper.getPK()),context);
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


}

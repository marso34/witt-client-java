package com.example.healthappttt.Data.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

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
    private static SocketSingleton instance;
    private Socket mSocket;
    private String serverUrl = "http://43.200.245.144:1337/";
    private boolean isConnected = false;
    private int userKey;
    private Context context;
    private PreferenceHelper prefHelper;
    private SQLiteUtil sqLiteUtil;
    private ChatActivity chatActivity;


    private SocketSingleton() {

    }

    public static synchronized SocketSingleton getInstance() {
        if (instance == null) {
            instance = new SocketSingleton();
        }
        return instance;
    }

    public void initialize(Context context) {
        this.context = context.getApplicationContext();
        try {
            mSocket = IO.socket(serverUrl);
            setupSocketListeners();
        } catch (URISyntaxException e) {
            Log.d(TAG, "Failed to initialize Socket: " + e.getMessage());
        }
        receiveMessage();
        sqLiteUtil = SQLiteUtil.getInstance();
    }

    private void setupSocketListeners() {
        mSocket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "Socket connected");
                JSONObject data = (JSONObject) args[0];
                try {
                    String socketId = data.getString("socketId");
                    Log.d("ClientActivity", "Socket ID: " + socketId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        prefHelper = new PreferenceHelper("UserTB", context);
        mSocket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String message;
                String chatRoomId = null;
                try {
                    message = data.getString("message");
                    chatRoomId = data.getString("chatRoomId");
                    if (chatRoomId != null) {
                        int CRI = Integer.parseInt(chatRoomId);
                        SqlLiteSaveMessage(prefHelper.getPK(), 2, message, CRI);
                        mSocket.emit("completeMessage");
                        if (chatActivity != null && chatActivity.getChatRoomId().equals(chatRoomId)) {
                            chatActivity.getAllMSG();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SqlLiteSaveMessage(int userKey, int myFlag, String message, int chatRoomId) {
        sqLiteUtil.setInitView(context, "CHAT_MSG_TB");
        sqLiteUtil.insert(userKey, myFlag, message, chatRoomId, 1);
    }

    private void insertSocket() {
        JSONObject data = new JSONObject();
        try {
            data.put("userKey", prefHelper.getPK());
            data.put("socketId", mSocket.id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("insertSocket", data);
    }

    public Socket getSocket() {
        return mSocket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        isConnected = false;
        connectSocket();
    }

    private void connectSocket() {
        if (mSocket != null && !mSocket.connected()) {
            mSocket.connect();
        }
    }

    public void disconnect() {
            mSocket.disconnect();
        Log.d(TAG, "disconnect: 연결종료");
            mSocket = null;
            instance = null;
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
}

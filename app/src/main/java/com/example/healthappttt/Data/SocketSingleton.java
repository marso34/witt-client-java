package com.example.healthappttt.Data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketSingleton {
    private static volatile SocketSingleton instance;
    private Socket mSocket;
    private String serverUrl = "http://43.200.245.144:1337/";  // 서버 URL 설정
    private boolean isConnected = false;  // 소켓 연결 여부 확인
    private int userKey;
    private static Context context;
    private PreferenceHelper prefhelper;
    private String name_TB = "UserTB";
    private SocketSingleton(Context context) {
        try {
            mSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Failed to initialize Socket: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "Exception during Socket initialization: " + e.getMessage());
            e.printStackTrace();
        }
        this.context = context;
        prefhelper = new PreferenceHelper(context);
        connectSocket();
        setupSocketListeners();
        receiveMessage();
    }

    private void setupSocketListeners() {
        mSocket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
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
        mSocket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // 서버로부터 메시지를 수신했을 때의 동작 처리
                mSocket.emit("completeMessage");
                JSONObject data = (JSONObject) args[0];
                try {
                    String message = data.getString("message");
                    handleReceivedMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 받은 메시지를 처리하는 로직 작성
            }
        });
    }

    // handleReceivedMessage 메소드 구현
    private void handleReceivedMessage(String message) {
        // 받은 메시지를 처리하는 로직을 구현
        Log.d("ChatActivity", "Received message: " + message);
        // 처리 로직 작성
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

    // 다른 메소드 및 이벤트 핸들러 등 추가 구현 가능
}

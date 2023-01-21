//package com.example.healthappttt.Activity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.healthappttt.Data.PreferenceHelper;
//import com.example.healthappttt.R;
//import com.example.healthappttt.interface_.LoginInterface;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//
//public class LoginActivity extends AppCompatActivity
//{
//    private final String TAG = "LoginActivity";
//
//    private EditText etUname, etPass;
//    private Button btnlogin;
//    private TextView tvreg;
//    private ProgressBar mProgressView;
//    private PreferenceHelper preferenceHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        preferenceHelper = new PreferenceHelper(this);
//
//        etUname = (EditText) findViewById(R.id.etusername);
//        etPass = (EditText) findViewById(R.id.etpassword);
//
//        btnlogin = (Button) findViewById(R.id.btn);
//        tvreg = (TextView) findViewById(R.id.tvreg);
//
//        mProgressView = (ProgressBar) findViewById(R.id.progress);
//
//        tvreg.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent= new Intent(LoginActivity.this, signupActivity.class);
//                startActivity(intent);
//                LoginActivity.this.finish();
//            }
//        });
//
//        btnlogin.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                loginUser();
//            }
//        });
//    }
//
//    private void loginUser()
//    {
//        final String username = etUname.getText().toString().trim();
//        final String password = etPass.getText().toString().trim();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        if (password.isEmpty()) {
//            etPass.setError("비밀번호를 입력해주세요.");
//            focusView = etPass;
//            cancel = true;
//        }
//
//        if (username.isEmpty()) {
//            etUname.setError("이름을 입력해주세요.");
//            focusView = etUname;
//            cancel = true;
//        }
//
//        if (cancel) {
//            focusView.requestFocus();
//        } else {
//            Log.e(TAG, username + " " + password);
////            showProgress(true);
//            startLogIn(username, password);
//        }
//    }
//
//    private void startLogIn(String username, String password) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(LoginInterface.LOGIN_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        LoginInterface api = retrofit.create(LoginInterface.class);
//        Call<String> call = api.getUserLogin(username, password);
//        call.enqueue(new Callback<String>()
//        {
//            @Override
//            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response)
//            {
//                if (response.isSuccessful() && response.body() != null)
//                {
//                    Log.e("onSuccess", response.body());
//                    String jsonResponse = response.body();
//
//                    try {
//                        parseLoginData(jsonResponse);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t)
//            {
//                Log.e(TAG, "에러 = " + t.getMessage());
//            }
//        });
//    }
//
//    private void parseLoginData(String response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response);
//
//        if (jsonObject.getString("status").equals("true"))
//        {
//            saveInfo(response);
//            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//        }
////        showProgress(false);
//        finish();
//    }
//
//    private void saveInfo(String response)
//    {
//        preferenceHelper.putIsLogin(true);
//        try
//        {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getString("status").equals("true"))
//            {
//                JSONArray dataArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < dataArray.length(); i++)
//                {
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    preferenceHelper.putName(dataobj.getString("name"));
//                    preferenceHelper.putHobby(dataobj.getString("hobby"));
//                }
//            }
//        }
//        catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//    }
//    private void showProgress(boolean show) {
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//
//}
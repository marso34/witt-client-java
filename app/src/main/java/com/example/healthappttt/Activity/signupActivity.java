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
//import com.example.healthappttt.interface_.RegisterInterface;
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
//public class signupActivity extends AppCompatActivity
//{
//    public final String TAG = "signupActivity";
//
//    private EditText etname, ethobby, etusername, etpassword;
//    private Button btnregister;
//    private TextView tvlogin;
//    private ProgressBar mProgressView;
//    private PreferenceHelper preferenceHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        preferenceHelper = new PreferenceHelper(this);
//
//        etname = (EditText) findViewById(R.id.etname);
//        ethobby = (EditText) findViewById(R.id.ethobby);
//        etusername = (EditText) findViewById(R.id.etusername);
//        etpassword = (EditText) findViewById(R.id.etpassword);
//
//        btnregister = (Button) findViewById(R.id.btn);
//        tvlogin = (TextView) findViewById(R.id.tvlogin);
//
//        mProgressView = (ProgressBar) findViewById(R.id.progress);
//
//        tvlogin.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(signupActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        btnregister.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                registerMe();
//            }
//        });
//    }
//
//    private void registerMe()
//    {
//        final String name = etname.getText().toString();
//        final String hobby = ethobby.getText().toString();
//        final String username = etusername.getText().toString();
//        final String password = etpassword.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        if (password.isEmpty()) {
//            etpassword.setError("비밀번호를 입력해주세요.");
//            focusView = etpassword;
//            cancel = true;
//        } else if (!isPasswordValid(password)) {
//            etpassword.setError("6자 이상의 비밀번호를 입력해주세요.");
//            focusView = etpassword;
//            cancel = true;
//        }
//
//        if (username.isEmpty()) {
//            etusername.setError("이름을 입력해주세요.");
//            focusView = etusername;
//            cancel = true;
//        }
//
//        if (hobby.isEmpty()) {
//            ethobby.setError("취미를 입력해주세요.");
//            focusView = ethobby;
//            cancel = true;
//        }
//
//        if (name.isEmpty()) {
//            etname.setError("아이디를 입력해주세요.");
//            focusView = etname;
//            cancel = true;
//        } else if (!isEmailValid(name)) {
//            etname.setError("@를 포함한 유효한 이메일을 입력해주세요.");
//            focusView = etname;
//            cancel = true;
//        }
//
//        if (cancel) {
//            focusView.requestFocus();
//        } else {
////            showProgress(true);
//            startSignUp(name, hobby, username, password);
//        }
//    }
//
//    private void startSignUp(String name, String hobby, String username, String password) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(RegisterInterface.REGIST_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        RegisterInterface api = retrofit.create(RegisterInterface.class);
//        Call<String> call = api.getUserRegist(name, hobby, username, password);
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
//                        parseRegData(jsonResponse);
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
//    private void parseRegData(String response) throws JSONException
//    {
//        JSONObject jsonObject = new JSONObject(response); // String cannot be converted to JSONObject
//
//        if (jsonObject.optString("status").equals("true"))
//        {
//            saveInfo(response);
//            Toast.makeText(signupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(signupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//        }
////        showProgress(false);
//        finish();
//    }
//
//    private void saveInfo(String response)
//    {
//        preferenceHelper.putIsLogin(true);
//        try {
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
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isEmailValid(String name) {
//        return name.contains("@");
//    }
//
//    private boolean isPasswordValid(String password) {
//        return password.length() >= 6;
//    }
//
//    private void showProgress(boolean show) {
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//}
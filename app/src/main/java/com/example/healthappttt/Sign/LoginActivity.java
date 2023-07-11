package com.example.healthappttt.Sign;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.MainActivity;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.User.email;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_HINT = 1000;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSignInButton;
    private ActivityResultLauncher<Intent> startActivityResult; // startActivityForResult 대체 방법

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult data) { // 원래 onActivityResult에 있던 내용
                if (data.getResultCode() == Activity.RESULT_OK && data.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data.getData());
                    handleSignInResult(task);
                }
            }
        });  // startActivityForResult 대체 방법

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_sign_in_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set up Google Sign In Button`
        mGoogleSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
//                GoMain("291");
            }
        });

        //updateUI(GoogleSignIn.getLastSignedInAccount(this));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityResult.launch(signInIntent); // startActivityForResult 대체 방법
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//
//        }
//    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "handleSignInResult: account = " + account);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult: failed code = " + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {

        if (account != null) {
            // Get Google ID token
            String token = account.getIdToken();
            String email = account.getEmail();
            String name = account.getDisplayName();
            SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("useremail", email);
            editor.apply();

            ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
            Call<ResponseBody> call = apiService.CheckUser(new email(email));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseString = response.body().string();
                            Log.d(TAG, "onResponse!!: " + responseString);

                            if (responseString.equals("Fail")) {
                                sendData(email, name);

                            } else {
                                GoMain(responseString);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 서버로 데이터 전송 실패
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "sendTokenToServer fail");
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "sendTokenToServer error: " + t.getMessage());
                }
            });

            // Send token to server for verification
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    private void sendData(String email,String name) {
        Intent intent = new Intent(LoginActivity.this, SubActivity.class);
        intent.putExtra("email", email);
        Log.d(TAG, "adadad"+email);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }
    private void GoMain(String userKey){
        Log.d(TAG, "GoMain: "+userKey);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userKey",userKey);
        startActivity(intent);
        finish();
    }

}




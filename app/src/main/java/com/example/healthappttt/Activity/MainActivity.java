package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Data.GetUserInfo;
import com.example.healthappttt.Fragment.ChattingFragment;
import com.example.healthappttt.Fragment.HomeFragment;
import com.example.healthappttt.Fragment.ProfileFragment;
//import com.example.healthappttt.Fragment.ChattingFragment;
import com.example.healthappttt.Fragment.RoutineFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private int tempItemID;
    Button mGoogleSignOutButton;
    private LoginActivity loginActivity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_sign_in_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String useremail = sharedPref.getString("useremail", "");


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                Log.d("lllllllll", "위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도  : " + altitude);

                DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                washingtonRef
                        .update("lat", latitude)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                washingtonRef
                        .update("lon", longitude)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
            else {
                Log.d("wwwwww","qeqeqeqeqe");
            }
//            위치정보를 원하는 시간, 거리마다 갱신해준다.
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    5000,
//                    4,
//                    gpsLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    5000,
//                    4,
//                    gpsLocationListener);
                    }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tempItemID = R.id.home;
        replaceFragment(new HomeFragment());


        binding.bottomNav.setOnItemSelectedListener(item -> {
            int ItemId = item.getItemId();

            if (tempItemID != ItemId) {
                tempItemID = ItemId;

                switch (ItemId) {
                    case R.id.home:
                        binding.viewName.setText("홈");
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.routine:
                        binding.viewName.setText("루틴");
                        replaceFragment(new RoutineFragment());
                        break;
                    case R.id.chatting:
                        binding.viewName.setText("채팅");
                        replaceFragment(new ChattingFragment());
                        break;
                    case R.id.profile:
                        binding.viewName.setText("기록");
                        replaceFragment(new ProfileFragment()); // 운동 기록 프래그먼트로 나중에 수정
                        break;
                }

            }

            return true;
        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SetExerciseActivity.class);
            startActivity(intent);
        });
        binding.signOutButton.setOnClickListener(view -> {
            signOut();
        });
        binding.myInformation.setOnClickListener(view -> {
                showUserInfoPopup(useremail);
        });
    }




    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            Log.d("lllllllll", "위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Update UI after sign out
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "Google sign out failed", e);
                    }
                });
    }

    private void showUserInfoPopup(String userEmail) {

       // Toast.makeText(MainActivity.this, userEmail, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.my_popup_layout, null);

        GetUserInfo.getUserInfo(userEmail, new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    GetUserInfo userInfo = response.body();



                    TextView tvUserName = view.findViewById(R.id.tv_user_info_name);
                    TextView tvUserEmail = view.findViewById(R.id.tv_user_info_email);
                    Button btnClose = view.findViewById(R.id.btn_user_info_close);

                    String userName = userInfo.getUserNm();
                    String userEmail = userInfo.getEmail();

                    tvUserName.setText(userName);
                    tvUserEmail.setText(userEmail);

                    builder.setView(view);
                    AlertDialog dialog = builder.create();

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); // 팝업창 닫기
                        }
                    });
                    dialog.show();


                } else {
                    // API 호출이 실패한 경우 처리
                    // ...
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {;
            }
        });


    }



    private void getUserInfo(String userEmail) {
        GetUserInfo.getUserInfo(userEmail, new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    GetUserInfo userInfo = response.body();
                    int userPk = userInfo.getUserPk();
                    String email = userInfo.getEmail();
                    String userNm = userInfo.getUserNm();


                } else {
                    // API 호출이 실패한 경우 처리
                    // ...
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {;
            }
        });
    }


}
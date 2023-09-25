package com.gwnu.witt.Sign;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.User.BodyInfo;
import com.gwnu.witt.Data.User.ExPerfInfo;
import com.gwnu.witt.Data.User.LocInfo;
import com.gwnu.witt.Data.User.MannerInfo;
import com.gwnu.witt.Data.User.PhoneInfo;
import com.gwnu.witt.Data.User.UploadResponse;
import com.gwnu.witt.Data.User.UserClass;
import com.gwnu.witt.Data.User.UserData;
import com.gwnu.witt.MainActivity;
import com.gwnu.witt.R;
import com.gwnu.witt.interface_.ServiceApi;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements SUSelectGymFragment.OnFragmentInteractionListener
{
    String name, email = "jjj";
    int platform;
    private boolean isPublic;
    double lat, lon, gymLat, gymLon;
    String gymName, gymAdress;
    int height, weight, gender;
    int bench, deadlift, squat;

    private Uri userImageUri;
    private String seturi;

    private PreferenceHelper UserTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserTB = new PreferenceHelper("UserTB",this);
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            name = intent.getStringExtra("name");
            platform = intent.getIntExtra("platform", 0);
        }

        height = 170;
        weight = 60;
        gender = -1;
        bench = deadlift = squat = 100; // 데이터 초기값
        isPublic = true;

        setContentView(R.layout.activity_sign_up);

        SUInputNameFragment fragment = SUInputNameFragment.newInstance(name);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContentLayout, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentLayout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    public void goToInputName() {
        replaceFragment(SUInputNameFragment.newInstance(name));
    }

    public void goToInputBody(String name) {
        if (name != null)
            this.name = name;

        replaceFragment(SUInputBodyFragment.newInstance(height, weight, isPublic));
    }

    public void goToSelectGender(int height, int weight, boolean isPublic) {
        if (height != -1)
            this.height = height;
        if (weight != -1) {
            this.weight = weight;
            this.isPublic = isPublic;
        }

        replaceFragment(SUSelectGenderFragment.newInstance(gender));
    }

    public void goToSelectGym(int gender) {
        if (gender != -1)
            this.gender = gender;

        replaceFragment(SUSelectGymFragment.newInstance(lat, lon, gymName, false));
    }

    public void sendToServer(int squat, int bench, int deadlift) {
        this.squat = squat;
        this.bench = bench;
        this.deadlift = deadlift;

        Log.d("ㅇㅁ1ㄴ:", "ㅇㄴㅁㅇ");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                uploadImage();
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //            Log.d(TAG, "sub2f"+email);
//                sendTokenToServer(email,name);
//            String message = "Name: " + email + "\n" +
//                    "Height: " + height + " cm\n" +
//                    "Weight: " + weight + " kg\n" +
//                    "Latitude: " + latitude + "\n" +
//                    "Longitude: " + longitude + "\n" +
//                    "Location Name: " + loName +
//                    "Values:" + squatValue +" "+ benchValue + " " + deadliftValue;
//            // Display the message
//            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

//
    private UserData getUserDT() {
        return new UserData(email, platform, name,seturi);
    }

    private PhoneInfo getPhoneInfo() {
        return new PhoneInfo("00000000000", "phoneModel", "serialNumber");
    }

    private MannerInfo getMannerInfo() {
        return new MannerInfo(150,150,150);
    } // 매너 테이블에 디폴트 값 넣고 그냥 생성하면 되잖아?

    private LocInfo getLocInfo(){
        return new LocInfo(lat, lon, gymName, gymLat, gymLon, gymAdress);
    }

    private ExPerfInfo getExPerInfo() {
        return new ExPerfInfo(bench, squat, deadlift);
    }

    private BodyInfo getBodyInfo() {
        int temp;
        if (isPublic)   temp = 1;
        else            temp = 0;

        return new BodyInfo("1999-12-27", gender, height, weight, temp);
    }
//
private void sendTokenToServer() {
    ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);

        Call<Integer> call = apiService.sendData(new UserClass(getUserDT(), getPhoneInfo(), getMannerInfo(), getLocInfo(), getExPerInfo(), getBodyInfo()));

        Log.d(TAG, getUserDT().getImage().toString());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int userKey = response.body();
                    UserClass sharedUser = new UserClass(getUserDT(), getPhoneInfo(), getMannerInfo(), getLocInfo(), getExPerInfo(), getBodyInfo());
                    UserTB.putMembership(sharedUser);
                    Log.d("shared 로컬 저장 회원가입 pk:", String.valueOf(userKey));
                    UserTB.setPK(userKey); //pk 저장

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("userKey", userKey);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "업로드 성공");
                } else {
                    // 서버로 데이터 전송 실패
                    Log.d(TAG, "업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "이미지 업로드 에러: " + t.getMessage());
            }
        });

}

    private void uploadImage() {


        if(userImageUri != null) {
            ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
            String imagePath = getRealPathFromUri(userImageUri);
            File imageFile = new File(imagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

            Call<UploadResponse> call = apiService.uploadImage(imagePart);
            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (response.isSuccessful()) {

                        Log.d(TAG, "sendTokenToServer success");
                        UploadResponse uploadResponse = response.body();

                        String imageUrl = uploadResponse.getImageUrl();
                        seturi = imageUrl;


                        sendTokenToServer();

                    } else {
                        Log.d(TAG, "sendTokenToServer fail");
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Log.e("Upload Error", "통신 실패: " + t.getMessage());
                }
            });

        }
        else {
            Log.e("Upload Error", "이미지 없음");
            seturi = "이미지 없음";
            sendTokenToServer();
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String result;
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor == null) {
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(columnIdx);
            }
        }
        return result;
    }



    @Override
    public void onSaveLocation(double userLat, double userLon, double gymLat, double gymLon, String gymName, String gymAdress) {
        if (userLat != 0)
            this.lat = userLat;
        if (userLon != 0)
            this.lon = userLon;

        this.gymName = gymName;
        this.gymAdress = gymAdress;
        this.gymLat = gymLat;
        this.gymLon = gymLon;

        replaceFragment(SUInputPerfFragment.newInstance(bench, deadlift, squat));
    }

    public void receiveImageUri(Uri imageUri) {

        userImageUri = imageUri;


    }

    @Override
    public void onCancel() {
        goToSelectGender(-1, -1, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
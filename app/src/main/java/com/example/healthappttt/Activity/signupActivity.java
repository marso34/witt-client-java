package com.example.healthappttt.Activity;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class signupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView profileImageVIew;
    private Uri downloadUri = Uri.parse("android.resource://" + "com.example.healthappttt" + "/" + R.drawable.profile);
    private String profilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(onClickListener);
        profileImageVIew = findViewById(R.id.ProfileImg);
        profileImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 45);
            }
       });

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==45 && resultCode == RESULT_OK && data!=null && data.getData()!= null) {
            downloadUri = data.getData();
            profileImageVIew.setImageURI(downloadUri);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSignUp:
                    Log.d(TAG, "onClick: aaa");
                    signUp();
                    break;
            }
        }
    };

    private void signUp() {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();
        String uName = ((EditText)findViewById(R.id.edt_name)).getText().toString();
        String BenchPower = ((EditText)findViewById(R.id.edt_BenchPower)).getText().toString();
        String SquatPower = ((EditText)findViewById(R.id.edt_SquatPower)).getText().toString();
        String DeadPower = ((EditText)findViewById(R.id.edt_DeadPower)).getText().toString();
        String LN = ((EditText)findViewById(R.id.LN)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0){
            if(password.equals(passwordCheck)){
                final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);
                if(loaderLayout != null)
                    loaderLayout.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(loaderLayout != null)
                                    loaderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    storage.getReference().child("article/photo").child(user.getUid())
                                            .putFile(downloadUri);

//                                db.collection("users").document(user.getUid())
//                                            .set(new User(user.getUid(),uName,downloadUri.toString(), BenchPower, DeadPower,SquatPower,LN,0))
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                                    setRoutine();
//
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.w(TAG, "Error writing document", e);
//                                                }
//                                            });
                                }

                        }
            });
            }
        }
    }

    private void setRoutine() {

        for (int i = 0; i < 7; i++) {
            String dayOfWeek = "";

            switch (i) {
                case 0: dayOfWeek = "sun"; break;
                case 1: dayOfWeek = "mon"; break;
                case 2: dayOfWeek = "tue"; break;
                case 3: dayOfWeek = "wed"; break;
                case 4: dayOfWeek = "thu"; break;
                case 5: dayOfWeek = "fri"; break;
                case 6: dayOfWeek = "sat"; break;
            }

            db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + dayOfWeek).
                    set(new Routine(0)).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("루틴 생성 = >", "success");
                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("루틴 생성 => ", "failure");
                        }
                    });
        }
        finishAffinity();
        myStartActivity(MainActivity.class);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);

        startActivity(intent);
    }

}

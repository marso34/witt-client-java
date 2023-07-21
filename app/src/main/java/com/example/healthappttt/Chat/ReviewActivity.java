package com.example.healthappttt.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.ReviewData;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityReviewBinding;
import com.example.healthappttt.interface_.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    ActivityReviewBinding binding;
    private CheckedTextView[] goodCheckedTextViews; // 체크박스 모음
    private CheckedTextView[] badCheckedTextViews; // 체크박스 모음

    private static final String Backgrount_1 = "#F2F5F9";
    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Signature = "#05C78C";
    private static final String Signature_Toggle = "#D9F7EE";
    private static final String Pink = "#F257AF";
    private static final String Pink_Toggle = "#fde6f3";

    private ServiceApi service;
    private PreferenceHelper prefhelper;

    private int isGood; // > 0 good, < 0 bad, == 0 클릭 x
    private int pk, OtherPk, check_box;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goodCheckedTextViews = new CheckedTextView[] {
                binding.goodCheck1,
                binding.goodCheck2,
                binding.goodCheck3,
                binding.goodCheck4
        };

        badCheckedTextViews = new CheckedTextView[] {
                binding.badCheck1,
                binding.badCheck2,
                binding.badCheck3,
                binding.badCheck4,
                binding.badCheck5,
                binding.badCheck6,
                binding.badCheck7,
                binding.badCheck8
        };

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        OtherPk = intent.getIntExtra("code", 0);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        prefhelper = new PreferenceHelper("UserTB", this);
        pk = prefhelper.getPK();
//  ------------------------------------------------------------------------------------------------

        binding.userName.setText(name);

        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.goodCard.setOnClickListener(v -> {
            if (isGood <= 0) {
                isGood = 1;

                setGoodIcon();
                initCheckedTextViews(true);
            }
        });

        binding.badCard.setOnClickListener(v -> {
            if (isGood >= 0) {
                isGood = -1;

                setBadIcon();
                initCheckedTextViews(false);
            }
        });

        binding.addBlacklist.setOnClickListener(v -> {
            
        });

        setCheckedTextViewListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setCheckedTextViewListeners() {
        View.OnClickListener goodCheckedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckedTextView) v).setChecked(!((CheckedTextView) v).isChecked());

                if (((CheckedTextView) v).isChecked()) {
                    v.setBackground(getDrawable(R.drawable.border_background));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Signature));
                } else {
                    v.setBackground(getDrawable(R.drawable.rectangle_16dp));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
                }
            }
        };

        View.OnClickListener badCheckedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckedTextView) v).setChecked(!((CheckedTextView) v).isChecked());

                if (((CheckedTextView) v).isChecked()) {
                    v.setBackground(getDrawable(R.drawable.border_background_pink));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Pink));
                } else {
                    v.setBackground(getDrawable(R.drawable.rectangle_16dp));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
                }
            }
        };

        for (CheckedTextView checkedTextView : goodCheckedTextViews)
            checkedTextView.setOnClickListener(goodCheckedListener);

        for (CheckedTextView checkedTextView : badCheckedTextViews)
            checkedTextView.setOnClickListener(badCheckedListener);
    }

    private void setGoodIcon() {
        binding.goodImg.setAlpha(1f);
        binding.badImg.setAlpha(0.2f);

        binding.goodCard.setStrokeWidth(1);
        binding.badCard.setStrokeWidth(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.goodCard.setOutlineSpotShadowColor(Color.parseColor(Signature));
            binding.badCard.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));

            binding.gootTxtView.setOutlineSpotShadowColor(Color.parseColor(Signature));
            binding.badTxtView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
        }

        binding.goodTxt.setTextColor(Color.parseColor(Signature));
        binding.badTxt.setTextColor(Color.parseColor(Backgrount_2));
        binding.gootTxtView.setCardBackgroundColor(Color.parseColor(Signature_Toggle));
        binding.badTxtView.setCardBackgroundColor(Color.parseColor(Backgrount_1));
    }

    private void setBadIcon() {
        binding.goodImg.setAlpha(0.2f);
        binding.badImg.setAlpha(1f);

        binding.goodCard.setStrokeWidth(0);
        binding.badCard.setStrokeWidth(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.goodCard.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            binding.badCard.setOutlineSpotShadowColor(Color.parseColor(Pink));

            binding.gootTxtView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            binding.badTxtView.setOutlineSpotShadowColor(Color.parseColor(Pink));
        }

        binding.goodTxt.setTextColor(Color.parseColor(Backgrount_2));
        binding.badTxt.setTextColor(Color.parseColor(Pink));
        binding.gootTxtView.setCardBackgroundColor(Color.parseColor(Backgrount_1));
        binding.badTxtView.setCardBackgroundColor(Color.parseColor(Pink_Toggle));
    }

    private void initCheckedTextViews(boolean isGood) {
        if (isGood) {
            binding.goodCheckBox.setVisibility(View.VISIBLE);
            binding.badCheckBox.setVisibility(View.GONE);

            for (CheckedTextView checkedTextView : badCheckedTextViews) {
                checkedTextView.setChecked(false);

                checkedTextView.setBackground(getDrawable(R.drawable.rectangle_16dp));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    checkedTextView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            }
        } else {
            binding.goodCheckBox.setVisibility(View.GONE);
            binding.badCheckBox.setVisibility(View.VISIBLE);

            for (CheckedTextView checkedTextView : goodCheckedTextViews) {
                checkedTextView.setChecked(false);

                checkedTextView.setBackground(getDrawable(R.drawable.rectangle_16dp));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    checkedTextView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            }
        }
    }

    private void sendToServer() {
        service.sendReivew(new ReviewData(OtherPk, pk, text, check_box)).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d("성공", "리뷰 보내기 성공");

                    finish();
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 보내기 실패..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 실패..", Toast.LENGTH_SHORT).show();
                Log.d("서버 연결 실패", t.getMessage());
            }
        });
    }
}
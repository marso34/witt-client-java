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
    private static final String Body = "#4A5567";

    private ServiceApi service;
    private PreferenceHelper prefhelper;

    private int isGood; // > 0 good, < 0 bad, == 0 클릭 x
    private int pk, OtherPk, check_box;
    private String text = "";

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
            if (binding.addBlacklist.isChecked()) {
                binding.addBlacklist.setBackground(getDrawable(R.drawable.border_background_pink));
                binding.addBlacklist.setTextColor(Color.parseColor(Pink));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    binding.addBlacklist.setOutlineSpotShadowColor(Color.parseColor(Pink));
                }
            } else {
                binding.addBlacklist.setBackground(getDrawable(R.drawable.rectangle_16dp));
                binding.addBlacklist.setTextColor(Color.parseColor(Body));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    binding.addBlacklist.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
                }
            }
        });

        binding.send.setOnClickListener(v -> sendToServer());

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
                ((CheckedTextView) v).setTextColor(Color.parseColor(Signature));

                if (((CheckedTextView) v).isChecked()) {
                    v.setBackground(getDrawable(R.drawable.border_background));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Signature));
                } else {
                    v.setBackground(getDrawable(R.drawable.rectangle_16dp));
                    ((CheckedTextView) v).setTextColor(Color.parseColor(Body));

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
                    ((CheckedTextView) v).setTextColor(Color.parseColor(Pink));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        v.setOutlineSpotShadowColor(Color.parseColor(Pink));
                } else {
                    v.setBackground(getDrawable(R.drawable.rectangle_16dp));
                    ((CheckedTextView) v).setTextColor(Color.parseColor(Body));

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
        binding.txtLayout.setVisibility(View.VISIBLE);
        binding.tempBtn.setVisibility(View.GONE);

        if (isGood) {
            binding.goodCheckBox.setVisibility(View.VISIBLE);
            binding.badCheckBox.setVisibility(View.GONE);
            binding.addBlacklist.setChecked(false);
            binding.addBlacklist.setBackground(getDrawable(R.drawable.rectangle_16dp));
            binding.addBlacklist.setTextColor(Color.parseColor(Body));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                binding.addBlacklist.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));

            for (CheckedTextView checkedTextView : badCheckedTextViews) {
                checkedTextView.setChecked(false);
                checkedTextView.setTextColor(Color.parseColor(Body));
                checkedTextView.setBackground(getDrawable(R.drawable.rectangle_16dp));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    checkedTextView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            }

            binding.txtTitle.setText("따뜻한 후기를 보내요");
            binding.txtSubtitle.setText("남겨주신 운동 후기는 상대방의 프로필에 공개되어요");
        } else {
            binding.goodCheckBox.setVisibility(View.GONE);
            binding.badCheckBox.setVisibility(View.VISIBLE);

            for (CheckedTextView checkedTextView : goodCheckedTextViews) {
                checkedTextView.setChecked(false);
                checkedTextView.setTextColor(Color.parseColor(Body));
                checkedTextView.setBackground(getDrawable(R.drawable.rectangle_16dp));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    checkedTextView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            }

            binding.txtTitle.setText("아쉬웠던 점을 위트팀에게 알려주세요");
            binding.txtSubtitle.setText("상대방에게 전달되지 않으니 안심하세요");
        }
    }

    private void sendToServer() {
//        이 분과는 다시는 운동하고 싶지 않아요 // 이 부분은 차단 목록에 추가할까 생각중

        if (isGood > 0) {
            check_box = 0x8000; // 좋아요

            for (int i = 0; i <  goodCheckedTextViews.length; i++) {
                if (goodCheckedTextViews[i].isChecked()) {
                    switch (i) {
                        case 0: check_box |= 0x01; break; // 친절하고 매너가 좋아요
                        case 1: check_box |= 0x02; break; // 시간약속을 잘 지켜요
                        case 2: check_box |= 0x04; break; // 응답이 빨라요
                        case 3: check_box |= 0x08; break; // 운동을 잘 알려줘요
                        case 4: check_box |= 0x10; break;
                        case 5: check_box |= 0x20; break;
                        case 6: check_box |= 0x40; break;
                    } // 실제로 사용은 3까지
                }
            }
        } else if (isGood < 0) {
            check_box = 0; // 별로예요

            if (binding.addBlacklist.isChecked()) {
//                차단목록 추가
            }

            for (int i = 0; i <  badCheckedTextViews.length; i++) {
                if (badCheckedTextViews[i].isChecked()) {
                    switch (i) {
                        case 0: check_box |= 0x01; break; // 원하지 않는 운동을 계속 강요해요
                        case 1: check_box |= 0x02; break; // 시간약속을 안 지켜요
                        case 2: check_box |= 0x04; break; // 채팅 메시지를 읽고도 답장이 없어요
                        case 3: check_box |= 0x08; break; // 약속 시간을 명확하게 알려주지 않아요
                        case 4: check_box |= 0x10; break; // 약속 시간과 장소를 정한 후 운동 직전 취소했어요
                        case 5: check_box |= 0x20; break; // 약속 장소에 나타나지 않았어요
                        case 6: check_box |= 0x40; break; // 반말을 사용해요
                        case 7: check_box |= 0x80; break; // 불친절해요
                        case 8: check_box |= 0x100; break;
                        case 9: check_box |= 0x200; break;
                    } // 실제로 사용은 7까지
                }
            }
        } else {

        }

        text = String.valueOf(binding.txtEdit.getText());

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
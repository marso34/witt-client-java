package com.example.healthappttt.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;

public class ReportActivity extends AppCompatActivity {

    CheckedTextView[] checkedTextViews; //체크박스들 모음
    int selection;
    Button GO_RPT;
    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        selection = 0;

        setCheckview();
        setCheckedTextViewListeners();
        RPTclick();

    }

    public void setCheckview() {
        GO_RPT = findViewById(R.id.GO_RPT);
        txt = findViewById(R.id.RPT_txt);

        checkedTextViews = new CheckedTextView[] {
                findViewById(R.id.RPT_ck1),
                findViewById(R.id.RPT_ck2),
                findViewById(R.id.RPT_ck3),
                findViewById(R.id.RPT_ck4),
                findViewById(R.id.RPT_ck5),
                findViewById(R.id.RPT_ck8)
        };
    }

    public void setCheckedTextViewListeners() {
        View.OnClickListener checkedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.setChecked(!checkedTextView.isChecked());
                if (checkedTextView.isChecked()) {
                    checkedTextView.setTextColor(getResources().getColor(R.color.Signature));// 텍스트색
                    checkedTextView.setCheckMarkDrawable(getDrawable(R.drawable.ic_baseline_check_24));//체크표시 모양
                    checkedTextView.setBackground(getDrawable(R.drawable.border_background));//배경색


                } else {
                    checkedTextView.setCheckMarkDrawable(null);// 체크 표시 제거
                    checkedTextView.setBackground(getDrawable(R.drawable.rectangle));
                    checkedTextView.setTextAppearance(ReportActivity.this, R.style.report);


                }
            }
        };

        for (CheckedTextView checkedTextView : checkedTextViews) { //체크리스트 하나하나 onclick리스너 등록
            checkedTextView.setOnClickListener(checkedListener);
//            if(checkedTextViews[0] == checkedTextView){
//
//            }

        }
    }

    public void RPTclick() {
        GO_RPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(CheckedTextView checkedTextView : checkedTextViews){
                    if(!checkedTextView.isChecked() && txt.getText() == null) { //모두 안적었을때
                        v.setClickable(false);
                        Toast.makeText(ReportActivity.this, "신고 내용을 하나 이상 체크해주세요", Toast.LENGTH_SHORT).show(); }
//                    }else if() {
//                        //TODO 체크표시를 안하고 텍스트만 적었을때
//                        Log.d("ReportActivity : ", "체크표시를 안하고 텍스트만 적었을때");
//                    }else if(){
//                        //TODO 체크표시만하고 텍스트만 적었을때
//                        Log.d("ReportActivity : ","체크표시만하고 텍스트만 적었을때" );
//                    }else() {
//                        //TODO 둘다 적었을때
//                        Log.d("ReportActivity : ","둘다 적었을때");
//                    }
                }

//                setCheckBit(checkedTextViews);
//                Log.d("ReportActivity 10진수: ", String.valueOf(selection));
//
//                if(txt.getText() != null){
//                    //TODO 서버 db에 체크박스 저장
//                    Intent email = new Intent(Intent.ACTION_SEND);
//                    email.setType("plane/text");
//                    String[] address = {"hwstar1204@gmail.com"};
//                    email.putExtra(Intent.EXTRA_EMAIL,address);
//                    email.putExtra(Intent.EXTRA_SUBJECT,"Witt 신고");
//                    email.putExtra(Intent.EXTRA_TEXT,txt.getText());
//                    startActivity(email);
//                    finish();
//                }else {
//                    //TODO 서버 db에 체크박스 저장
//                }



            }
        });
    }
    /** 체크된 박스들 비트연산  */
    public void setCheckBit(CheckedTextView[] array) {
        int Num = 0;
        for (CheckedTextView checkedTextView : array) {
            if(checkedTextView.isChecked()){
                selection |= (1 << Num+1);
                Num++;
            }else {
                selection &= ~(1 << Num+1);
                Num++;
            }
        }
        selection >>= 1;
    }
//10진수 풀어서 1001 이면 true false false true 나오도록  -> 신고 내역에서 활용할것
//                tes(selection);
    public void tes(int num) {
        int length = (int) (Math.log(num) / Math.log(2)) + 1;
        for (int i = length - 1; i >= 0; i--) {
            int digit = (num >> i) & 1;
            boolean result = (digit == 1);
            System.out.println(digit + " -> " + result);
        }
    }


}

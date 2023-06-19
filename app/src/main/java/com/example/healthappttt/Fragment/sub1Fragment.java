package com.example.healthappttt.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthappttt.Activity.SubActivity;
import com.example.healthappttt.R;

public class sub1Fragment extends Fragment {
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";

    private double latitude;
    private double longitude;
    private String loName;
    private String email;
    private Integer rtH = -1;
    private Integer rtW = -1;;
    private EditText Name;
    private Editable NameV;
    public static sub1Fragment newInstance(String email, double latitude, double longitude, String LoName) {
        sub1Fragment fragment = new sub1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putString(ARG_NAME, LoName);
        fragment.setArguments(args);
        return new sub1Fragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getArguments().getString(ARG_EMAIL);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            loName = getArguments().getString(ARG_NAME);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {

        View view = inflater.inflate(R.layout.fragment_sub1, null);     // Fragment로 불러올 xml파일을 view로 가져옵니다.
        Button button1 = (Button)view.findViewById(R.id.study_button);   // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        Name = (EditText)view.findViewById(R.id.name);
        EditText H = (EditText) view.findViewById(R.id.hight);
        H.setInputType(InputType.TYPE_CLASS_NUMBER);

        EditText W = (EditText) view.findViewById(R.id.weight);
        W.setInputType(InputType.TYPE_CLASS_NUMBER);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtH = Integer.parseInt(String.valueOf(H.getText()));
                rtW = Integer.parseInt(String.valueOf(W.getText()));
                NameV = Name.getText();
                if (getActivity() instanceof SubActivity && rtH > 0 && rtW > 0 && NameV != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("height", rtH);
                    bundle.putInt("weight", rtW);
                    bundle.putString("name", Name.getText().toString());
                    ((SubActivity) getActivity()).replaceFragment(sub2Fragment.newInstance(email,latitude, longitude, loName, rtH,rtW, Name.getText().toString()));
                    }// 새로 불러올 Fragment의 Instance를 Main으로 전달
                else
                    Toast.makeText(getActivity(), "빈칸이 있습니다", '1').show();
            }
        });

        return view;
    }
}
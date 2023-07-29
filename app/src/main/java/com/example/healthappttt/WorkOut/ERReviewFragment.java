package com.example.healthappttt.WorkOut;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Chat.ReviewActivity;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentErReviewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERReviewFragment extends Fragment {
    FragmentErReviewBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_CODE = "code";

    // TODO: Rename and change types of parameters
    private String name;
    private int code;

    public ERReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Parameter 1.
     * @param code Parameter 2.
     * @return A new instance of fragment ERReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERReviewFragment newInstance(String name, int code) {
        ERReviewFragment fragment = new ERReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            code = getArguments().getInt(ARG_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErReviewBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.name.setText(name + "님과");

        binding.skipBtn.setOnClickListener(v -> requireActivity().finish());

        binding.nextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReviewActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("code", code);

            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
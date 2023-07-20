package com.example.healthappttt.WorkOut;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;
import com.example.healthappttt.Routine.RoutineAdapter;
import com.example.healthappttt.User.UserGenAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERSelectUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERSelectUserFragment extends Fragment {
    private CardView NextBtn;

    private RecyclerView recyclerView;
    private UserGenAdapter adapter;

    ArrayList<UserChat> test;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSelectUser();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public ERSelectUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ERSelectUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERSelectUserFragment newInstance(String param1, String param2) {
        ERSelectUserFragment fragment = new ERSelectUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_er_select_user, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        NextBtn = view.findViewById(R.id.nextBtn);

        NextBtn.setOnClickListener(v -> mListener.onSelectUser());

        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        test = new ArrayList<>();
        test.add(new UserChat("3ddddddddddddddd", "3", ""));
        test.add(new UserChat("3ddd", "3", ""));
        test.add(new UserChat("3ddddddddd", "3", ""));
        test.add(new UserChat("3ddddddddddddddddddd", "3", ""));


        adapter = new UserGenAdapter(getContext(), test, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
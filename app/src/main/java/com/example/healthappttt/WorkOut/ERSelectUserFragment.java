package com.example.healthappttt.WorkOut;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentErSelectUserBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERSelectUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERSelectUserFragment extends Fragment {
    FragmentErSelectUserBinding binding;
    private WittUserAdapter adapter;

    private ArrayList<UserChat> test;

    private int OUser_PK;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSelectUser(int OUser_PK);
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

    public static ERSelectUserFragment newInstance() {
        ERSelectUserFragment fragment = new ERSelectUserFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        OUser_PK = 0; // 임시값
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErSelectUserBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backBtn.setOnClickListener(v -> mListener.onSelectUser(-1));

        binding.nextBtn.setOnClickListener(v -> mListener.onSelectUser(OUser_PK));

        setRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        test = new ArrayList<>();

        adapter = new WittUserAdapter();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private class WittUserAdapter extends RecyclerView.Adapter<WittUserAdapter.MainViewHolder> {


        class MainViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout routineLayout;
            public TextView Name, GymName, GymAdress;
            public ImageView MapIcon, CheckIcon;


            public MainViewHolder(@androidx.annotation.NonNull View view) {
                super(view);

                this.routineLayout = view.findViewById(R.id.routine);
                this.Name =  view.findViewById(R.id.UNE);
                this.GymName = view.findViewById(R.id.gymName);
                this.GymAdress = view.findViewById(R.id.gymAdress);

                this.MapIcon = view.findViewById(R.id.mapIcon);
                this.CheckIcon = view.findViewById(R.id.check);
            }
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
            final MainViewHolder mainViewHolder = new MainViewHolder(view);

            return mainViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            holder.routineLayout.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
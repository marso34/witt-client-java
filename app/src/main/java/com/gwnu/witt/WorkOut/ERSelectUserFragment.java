package com.gwnu.witt.WorkOut;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwnu.witt.Data.Chat.UserChat;
import com.gwnu.witt.R;
import com.google.android.material.card.MaterialCardView;
import com.gwnu.witt.databinding.FragmentErSelectUserBinding;

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

    private static final String Body = "#4A5567";
    private static final String Signature = "#05C78C";
    private static final String White = "#ffffff";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";

    private int OUser_PK;
    private String name;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERS = "users";

    // TODO: Rename and change types of parameters

    private ArrayList<UserChat> users;
    private ArrayList<UserChat> searchUsers;


    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSelectUser(int OUser_PK, String name);
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

    public static ERSelectUserFragment newInstance(ArrayList<UserChat> users) {
        ERSelectUserFragment fragment = new ERSelectUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            users = (ArrayList<UserChat>) getArguments().getSerializable(ARG_USERS);
            searchUsers = (ArrayList<UserChat>) users.clone();
        }
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

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mListener.onSelectUser(-1, "");
            }
        });

        binding.backBtn.setOnClickListener(v -> mListener.onSelectUser(-1, ""));

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUser(newText);
                return false;
            }
        });

        binding.nextBtn.setOnClickListener(v -> mListener.onSelectUser(OUser_PK, name));

        setRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        adapter = new WittUserAdapter();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void searchUser(String searchTxt) {
        searchUsers.clear();

        for (UserChat user : users) {
            if (user.getUserNM().contains(searchTxt))
                searchUsers.add(user);
        }

        adapter.notifyDataSetChanged();
    }

    private class WittUserAdapter extends RecyclerView.Adapter<WittUserAdapter.MainViewHolder> {
        class MainViewHolder extends RecyclerView.ViewHolder {
            public MaterialCardView CardView, NameCardView;
            public LinearLayout routineLayout;
            public TextView Name, GymName, GymAdress;
            public ImageView MapIcon, CheckIcon;

            public MainViewHolder(@androidx.annotation.NonNull View view) {
                super(view);

                this.CardView = view.findViewById(R.id.cardView);
                this.NameCardView = view.findViewById(R.id.nameCardView);
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

            mainViewHolder.CardView.setOnClickListener(v -> {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                if (OUser_PK != searchUsers.get(position).getOtherUserKey()) {
                    binding.nextBtn.setTextColor(Color.parseColor(White));
                    binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
                    binding.nextBtn.setText("선택하기");

                    name = searchUsers.get(position).getUserNM();
                    OUser_PK = searchUsers.get(position).getOtherUserKey();
                } else {
                    binding.nextBtn.setTextColor(Color.parseColor(Body));
                    binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_20dp));
                    binding.nextBtn.setText("건너뛰기");

                    name = "";
                    OUser_PK = 0;
                }

                adapter.notifyDataSetChanged();
            });

            return mainViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            String gymName = searchUsers.get(position).getGNM();

            holder.routineLayout.setVisibility(View.GONE);
            holder.Name.setText(searchUsers.get(position).getUserNM());

            if (gymName.equals("") || gymName == null) {
                holder.MapIcon.setColorFilter(Color.parseColor(Background_2));
                holder.GymName.setTextColor(Color.parseColor(Background_2));
                holder.GymName.setText("선택된 헬스장이 없어요");
                holder.GymAdress.setText("");
            } else {
                holder.MapIcon.setColorFilter(Color.parseColor(Signature));
                holder.GymName.setTextColor(Color.parseColor(Body));
                holder.GymName.setText(gymName);
                holder.GymAdress.setText(searchUsers.get(position).getGADS());
            }

            if (OUser_PK == searchUsers.get(position).getOtherUserKey()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    holder.CardView.setOutlineSpotShadowColor(Color.parseColor(Signature));
                holder.CardView.setStrokeWidth(2);
                holder.NameCardView.setStrokeWidth(2);
                holder.CheckIcon.setVisibility(View.VISIBLE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    holder.CardView.setOutlineSpotShadowColor(Color.parseColor(Background_3));
                holder.CardView.setStrokeWidth(0);
                holder.NameCardView.setStrokeWidth(0);
                holder.CheckIcon.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return searchUsers.size();
        }
    }
}
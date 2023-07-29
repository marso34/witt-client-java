package com.example.healthappttt.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthappttt.R;
import com.example.healthappttt.Sign.SUSelectGymFragment;
import com.example.healthappttt.databinding.FragmentEditGymBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGymFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGymFragment extends Fragment {

    FragmentEditGymBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String MyName;
    private String MyGym;


    private OnFragmentInteractionListener mListner;

    public interface OnFragmentInteractionListener {
        void EditGymInfo();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListner = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public EditGymFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param MyName Parameter 1.
     * @param MyGym Parameter 2.
     * @return A new instance of fragment EditGymFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditGymFragment newInstance(String MyName, String MyGym) {
        EditGymFragment fragment = new EditGymFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, MyName);
        args.putString(ARG_PARAM2, MyGym);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MyName = getArguments().getString(ARG_PARAM1);
            MyGym = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditGymBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        binding.MyName.setText(MyName+"님의");
        binding.Gym.setText(MyGym);
        binding.locDetail.setText(""); //헬스장 주소

        binding.cancelEditgym.setOnClickListener(v -> requireActivity().finish());

        binding.editGym.setOnClickListener(v -> mListner.EditGymInfo());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}
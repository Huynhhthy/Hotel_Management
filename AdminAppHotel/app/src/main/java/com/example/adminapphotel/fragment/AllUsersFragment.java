package com.example.adminapphotel.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminapphotel.R;
import com.example.adminapphotel.adapter.RcvAllUserAdapter;
import com.example.adminapphotel.model.Firebase;
import com.example.adminapphotel.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllUsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllUsersFragment newInstance(String param1, String param2) {
        AllUsersFragment fragment = new AllUsersFragment();
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
    private RecyclerView rcvAllUsers;
    private RcvAllUserAdapter adapter;
    private ArrayList<User> userList;
    private View view;
    private Firebase mfirebase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_users, container, false);
        Anhxa();
        getDataAllUser();
        return view;
    }

    private void Anhxa() {
        rcvAllUsers = view.findViewById(R.id.rcv_allusers);
        userList = new ArrayList<>();
        mfirebase = new Firebase(getContext());
    }
    private void getDataAllUser(){
        mfirebase.getAllUsers(new Firebase.getAllUsersCallback() {
            @Override
            public void onCallback(boolean isSuccess, ArrayList<User> userList) {
                if(isSuccess) {
                    rcvAllUsers.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new RcvAllUserAdapter(userList);
                    rcvAllUsers.setAdapter(adapter);
                } else {

                }
            }
        });
    }
}
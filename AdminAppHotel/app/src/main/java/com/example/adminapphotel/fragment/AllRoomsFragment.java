package com.example.adminapphotel.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.adminapphotel.R;
import com.example.adminapphotel.activity.AddRoomActivity;
import com.example.adminapphotel.adapter.RcvAllRoomsAdapter;
import com.example.adminapphotel.model.Firebase;
import com.example.adminapphotel.model.Room;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRoomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRoomsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllRoomsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRoomsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRoomsFragment newInstance(String param1, String param2) {
        AllRoomsFragment fragment = new AllRoomsFragment();
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
    private RecyclerView rcvAllRooms;
    private RcvAllRoomsAdapter adapter;
    private ArrayList<Room> roomList;
    private Firebase mfirebase;
    private View view;
    private Button btn_add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_rooms, container, false);
        Anhxa();
        getDataAllCar();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddRoomActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataAllCar();
    }

    private void Anhxa() {
        btn_add = view.findViewById(R.id.btn_addroom);
        rcvAllRooms = view.findViewById(R.id.rcv_allrooms);
        roomList = new ArrayList<>();
        mfirebase = new Firebase(getContext());
    }

    private void getDataAllCar(){
        mfirebase.getAllCars(new Firebase.getAllRoomsCallback() {
            @Override
            public void onCallback(boolean isSuccess, ArrayList<Room> roomList) {
                if(isSuccess) {
                    rcvAllRooms.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new RcvAllRoomsAdapter(getContext(),roomList);
                    rcvAllRooms.setAdapter(adapter);
                } else {
                }
            }
        });
    }
}
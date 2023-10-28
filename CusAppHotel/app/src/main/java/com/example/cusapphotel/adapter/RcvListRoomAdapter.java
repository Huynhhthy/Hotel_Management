package com.example.cusapphotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cusapphotel.R;
import com.example.cusapphotel.activity.DeitailRoomActivity;
import com.example.cusapphotel.model.Room;

import java.util.ArrayList;

public class RcvListRoomAdapter extends RecyclerView.Adapter<RcvListRoomAdapter.ViewHolder>{
    private ArrayList<Room> roomList;
    private Context context;
    public RcvListRoomAdapter(Context context, ArrayList<Room> roomList) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public RcvListRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcvlistroom, parent, false);
        return new RcvListRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcvListRoomAdapter.ViewHolder holder, int position) {
        Room currentRoom = roomList.get(position);
        holder.nameroom.setText(currentRoom.getNameroom());
        holder.priceroom.setText(String.format("%,d", Math.round(currentRoom.getPriceroom())) + " VNĐ/Ngày");
        Glide.with(context).load(currentRoom.getImage1()).into(holder.imagecar);
        holder.imagecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeitailRoomActivity.class);
                intent.putExtra("currentRoom", currentRoom);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameroom, priceroom;
        ImageView imagecar;
        public ViewHolder(View itemView) {
            super(itemView);
            nameroom = itemView.findViewById(R.id.tv_nameroom);
            priceroom = itemView.findViewById(R.id.tv_priceroom);
            imagecar = itemView.findViewById(R.id.imagecar1);
        }
    }
}

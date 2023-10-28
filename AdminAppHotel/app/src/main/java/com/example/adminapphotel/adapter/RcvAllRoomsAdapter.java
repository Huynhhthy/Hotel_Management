package com.example.adminapphotel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapphotel.R;
import com.example.adminapphotel.activity.DetailRoomActivity;
import com.example.adminapphotel.activity.EditRoomActivity;
import com.example.adminapphotel.model.Firebase;
import com.example.adminapphotel.model.Room;

import java.util.ArrayList;

public class RcvAllRoomsAdapter extends RecyclerView.Adapter<RcvAllRoomsAdapter.ViewHolder>{
    private ArrayList<Room> roomList;
    private Context context;
    private Firebase mfirebase;
    public RcvAllRoomsAdapter(Context context, ArrayList<Room> roomList) {
        this.roomList = roomList;
        this.context = context;
        mfirebase = new Firebase(context);
    }

    @Override
    public RcvAllRoomsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_allrooms, parent, false);
        return new RcvAllRoomsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcvAllRoomsAdapter.ViewHolder holder, int position) {
        Room currentRoom = roomList.get(position);
        holder.nameroom.setText(currentRoom.getNameroom());
        holder.priceroom.setText(String.format("%,d", Math.round(currentRoom.getPriceroom())) + " VNĐ/Ngày");
        holder.statusroom.setText(currentRoom.getStatusroom());
        Glide.with(context).load(currentRoom.getImage1()).into(holder.imageroom);
        holder.btn_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRoomActivity.class);
                intent.putExtra("currentRoom", currentRoom);
                context.startActivity(intent);
            }
        });
        holder.btn_editroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditRoomActivity.class);
                intent.putExtra("currentRoom", currentRoom);
                context.startActivity(intent);
            }
        });
        holder.btn_deleteroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(position, currentRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameroom, priceroom, statusroom;
        Button btn_viewdetails, btn_editroom, btn_deleteroom;
        ImageView imageroom;
        public ViewHolder(View itemView) {
            super(itemView);
            nameroom = itemView.findViewById(R.id.tv_nameroom);
            priceroom = itemView.findViewById(R.id.tv_priceroom);
            statusroom = itemView.findViewById(R.id.tv_statusroom);
            btn_viewdetails = itemView.findViewById(R.id.btn_viewdetails);
            btn_editroom = itemView.findViewById(R.id.btn_editroom);
            btn_deleteroom = itemView.findViewById(R.id.btn_deleteroom);
            imageroom = itemView.findViewById(R.id.imageroom1);
        }
    }

    private void showDeleteDialog(final int position, Room currentRoom){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this room?");
        builder.setPositiveButton("Delete", (dialog, id) -> {
            // Call the deleteCar method
            mfirebase.deleteCar(currentRoom, new Firebase.DeleteRoomCallback() {
                @Override
                public void onCallback(boolean isSuccess) {
                    if (isSuccess) {
                        Toast.makeText(context, "Room deleted successfully", Toast.LENGTH_SHORT).show();
                        roomList.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Failed to delete room", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }
}

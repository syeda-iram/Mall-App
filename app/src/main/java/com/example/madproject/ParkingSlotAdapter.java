package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {

    private List<ParkingLane> parkingLanes;
    private OnSlotClickListener slotClickListener;

    public interface OnSlotClickListener {
        void onSlotClick(ParkingSlot slot);
    }

    public ParkingSlotAdapter(List<ParkingLane> parkingLanes, OnSlotClickListener listener) {
        this.parkingLanes = parkingLanes;
        this.slotClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingLane lane = parkingLanes.get(position);

        holder.tvLaneHeader.setText("LANE " + lane.getLaneName());

        // Clear previous slots
        holder.slotsGrid.removeAllViews();

        // Add slots to grid
        for (ParkingSlot slot : lane.getSlots()) {
            View slotView = LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_single_slot, holder.slotsGrid, false);

            TextView tvSlotNumber = slotView.findViewById(R.id.tvSlotNumber);
            Button btnBook = slotView.findViewById(R.id.btnBook);
            ImageView ivCar = slotView.findViewById(R.id.car);

            tvSlotNumber.setText(slot.getSlotId());

            if (slot.isAvailable()) {
                // Available slot
                slotView.setBackgroundResource(R.drawable.bg_slot_free);
                tvSlotNumber.setTextColor(holder.itemView.getResources().getColor(android.R.color.holo_green_dark));
                btnBook.setVisibility(View.VISIBLE);
                ivCar.setVisibility(View.GONE);

                btnBook.setOnClickListener(v -> {
                    if (slotClickListener != null) {
                        slotClickListener.onSlotClick(slot);
                    }
                });
            } else {
                // Occupied slot
                slotView.setBackgroundResource(R.drawable.bg_slot_booked);
                tvSlotNumber.setTextColor(holder.itemView.getResources().getColor(android.R.color.darker_gray));
                btnBook.setVisibility(View.GONE);
                ivCar.setVisibility(View.VISIBLE);

                slotView.setOnClickListener(v -> {
                    String message = "Slot " + slot.getSlotId() + " is already occupied";
                    Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
                });
            }

            // Add click listener to the entire slot
            slotView.setOnClickListener(v -> {
                if (slot.isAvailable() && slotClickListener != null) {
                    slotClickListener.onSlotClick(slot);
                }
            });

            holder.slotsGrid.addView(slotView);
        }
    }

    @Override
    public int getItemCount() {
        return parkingLanes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLaneHeader;
        GridLayout slotsGrid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLaneHeader = itemView.findViewById(R.id.tv_lane_header);
            slotsGrid = itemView.findViewById(R.id.slots_grid);
        }
    }
}
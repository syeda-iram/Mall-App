package com.example.madproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ParkingActivity extends AppCompatActivity implements ParkingSlotAdapter.OnSlotClickListener {

    private RecyclerView parkingRecyclerView;
    private LinearLayout bookingInfo;
    private TextView tvSelectedSlot, tvBookingTime, tvBookingCost;
    private Button btnConfirmBooking;

    private ParkingSlotAdapter adapter;
    private List<ParkingLane> parkingLanes = new ArrayList<>();
    private ParkingSlot selectedSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        initViews();
        setupParkingData();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        parkingRecyclerView = findViewById(R.id.parking_recycler_view);
        bookingInfo = findViewById(R.id.booking_info);
        tvSelectedSlot = findViewById(R.id.tv_selected_slot);
        tvBookingTime = findViewById(R.id.tv_booking_time);
        tvBookingCost = findViewById(R.id.tv_booking_cost);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
    }

    private void setupParkingData() {
        parkingLanes.clear();

        // Lane A
        List<ParkingSlot> laneASlots = new ArrayList<>();
        laneASlots.add(new ParkingSlot("A", 1, true));
        laneASlots.add(new ParkingSlot("A", 2, false));
        laneASlots.add(new ParkingSlot("A", 3, true));
        laneASlots.add(new ParkingSlot("A", 4, false));
        laneASlots.add(new ParkingSlot("A", 5, true));
        laneASlots.add(new ParkingSlot("A", 6, false));
        parkingLanes.add(new ParkingLane("A", laneASlots));

        // Lane B
        List<ParkingSlot> laneBSlots = new ArrayList<>();
        laneBSlots.add(new ParkingSlot("B", 1, true));
        laneBSlots.add(new ParkingSlot("B", 2, false));
        laneBSlots.add(new ParkingSlot("B", 3, true));
        laneBSlots.add(new ParkingSlot("B", 4, true));
        laneBSlots.add(new ParkingSlot("B", 5, true));
        laneBSlots.add(new ParkingSlot("B", 6, false));
        parkingLanes.add(new ParkingLane("B", laneBSlots));

        // Lane C
        List<ParkingSlot> laneCSlots = new ArrayList<>();
        laneCSlots.add(new ParkingSlot("C", 1, false));
        laneCSlots.add(new ParkingSlot("C", 2, false));
        laneCSlots.add(new ParkingSlot("C", 3, true));
        laneCSlots.add(new ParkingSlot("C", 4, false));
        laneCSlots.add(new ParkingSlot("C", 5, true));
        laneCSlots.add(new ParkingSlot("C", 6, true));
        parkingLanes.add(new ParkingLane("C", laneCSlots));
    }

    private void setupRecyclerView() {
        // Use GridLayoutManager with 1 column (vertical list)
        parkingRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new ParkingSlotAdapter(parkingLanes, this);
        parkingRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnConfirmBooking.setOnClickListener(v -> {
            if (selectedSlot != null) {
                confirmBooking();
            } else {
                Toast.makeText(this, "Please select a parking slot first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSlotClick(ParkingSlot slot) {
        if (slot.isAvailable()) {
            selectedSlot = slot;
            showBookingInfo(slot);
        } else {
            Toast.makeText(this, "Slot " + slot.getSlotId() + " is already occupied", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBookingInfo(ParkingSlot slot) {
        tvSelectedSlot.setText("Selected: " + slot.getSlotId());
        tvBookingTime.setText("Duration: 2 hours");
        tvBookingCost.setText("Cost: $5.00");
        bookingInfo.setVisibility(View.VISIBLE);

        // Scroll to bottom to show booking info
        parkingRecyclerView.post(() -> parkingRecyclerView.scrollToPosition(parkingLanes.size() - 1));
    }

    private void confirmBooking() {
        if (selectedSlot != null) {
            // Update slot status
            selectedSlot.setAvailable(false);

            // Update UI
            bookingInfo.setVisibility(View.GONE);
            selectedSlot = null;

            // Refresh adapter
            adapter.notifyDataSetChanged();

            // Show success message
            Toast.makeText(this, "Parking slot booked successfully!", Toast.LENGTH_LONG).show();

            // In real app, you would save this to database
        }
    }
}
package com.example.madproject;

public class ParkingSlot {
    private String id;
    private String lane;
    private int number;
    private boolean isAvailable;
    private String slotId; // e.g., "A1", "B2", etc.

    public ParkingSlot() {}

    public ParkingSlot(String lane, int number, boolean isAvailable) {
        this.lane = lane;
        this.number = number;
        this.isAvailable = isAvailable;
        this.slotId = lane + number;
        this.id = lane + "_" + number;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLane() { return lane; }
    public void setLane(String lane) { this.lane = lane; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
}
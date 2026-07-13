package com.example.madproject;

import java.util.List;

public class ParkingLane {
    private String laneName;
    private List<ParkingSlot> slots;

    public ParkingLane() {}

    public ParkingLane(String laneName, List<ParkingSlot> slots) {
        this.laneName = laneName;
        this.slots = slots;
    }

    public String getLaneName() { return laneName; }
    public void setLaneName(String laneName) { this.laneName = laneName; }

    public List<ParkingSlot> getSlots() { return slots; }
    public void setSlots(List<ParkingSlot> slots) { this.slots = slots; }
}
package com.alchemistdigital.buxa.model;

/**
 * Created by user on 9/17/2016.
 */
public class CustomClearanceModel {
    private int customeClearanceId;
    private int serverId;
    private String bookingId;
    private int shipmentType;
    private String stuffingType;
    private String stuffingAddress;
    private int availOption ;
    private int status ;
    private String  createdAt ;

    public CustomClearanceModel() {
    }

    public CustomClearanceModel(
            int serverId,
            String bookingId,
            int shipmentType,
            String stuffingType,
            String stuffingAddress,
            int availOption,
            int status,
            String createdAt) {
        this.serverId = serverId;
        this.bookingId = bookingId;
        this.shipmentType = shipmentType;
        this.stuffingType = stuffingType;
        this.stuffingAddress = stuffingAddress;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getCustomeClearanceId() {
        return customeClearanceId;
    }

    public void setCustomeClearanceId(int customeClearanceId) {
        this.customeClearanceId = customeClearanceId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(int shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getStuffingType() {
        return stuffingType;
    }

    public void setStuffingType(String stuffingType) {
        this.stuffingType = stuffingType;
    }

    public String getStuffingAddress() {
        return stuffingAddress;
    }

    public void setStuffingAddress(String stuffingAddress) {
        this.stuffingAddress = stuffingAddress;
    }

    public int getAvailOption() {
        return availOption;
    }

    public void setAvailOption(int availOption) {
        this.availOption = availOption;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

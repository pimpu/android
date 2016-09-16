package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class TransportationModel {
    private int  transportationId ;
    private int serverId ;
    private int commodityServerId ;
    private int dimenLength ;
    private int dimenHeight ;
    private int dimenWeight ;
    private int shipmentType ;
    private int noOfPack ;
    private int packType ;
    private String  pickUp ;
    private String drop ;
    private String lrCopy ;
    private int availOption ;
    private int status ;
    private String  createdAt ;
    private String bookingId;
    private int measurement;
    private float grossWeight;

    public TransportationModel() {
    }

    public TransportationModel(int serverId, int commodityServerId, int dimenLength,
                               int dimenHeight, int dimenWeight, int shipmentType, int noOfPack,
                               int packType, String pickUp, String drop, String lrCopy,
                               int availOption, int status, String createdAt, String bookingId,
                               int measurement, float grossWeight) {
        this.serverId = serverId;
        this.commodityServerId = commodityServerId;
        this.dimenLength = dimenLength;
        this.dimenHeight = dimenHeight;
        this.dimenWeight = dimenWeight;
        this.shipmentType = shipmentType;
        this.noOfPack = noOfPack;
        this.packType = packType;
        this.pickUp = pickUp;
        this.drop = drop;
        this.lrCopy = lrCopy;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.bookingId = bookingId;
        this.measurement = measurement;
        this.grossWeight = grossWeight;
    }

    public int getTransportationId() {
        return transportationId;
    }

    public void setTransportationId(int transportationId) {
        this.transportationId = transportationId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCommodityServerId() {
        return commodityServerId;
    }

    public void setCommodityServerId(int commodityServerId) {
        this.commodityServerId = commodityServerId;
    }

    public int getDimenLength() {
        return dimenLength;
    }

    public void setDimenLength(int dimenLength) {
        this.dimenLength = dimenLength;
    }

    public int getDimenHeight() {
        return dimenHeight;
    }

    public void setDimenHeight(int dimenHeight) {
        this.dimenHeight = dimenHeight;
    }

    public int getDimenWeight() {
        return dimenWeight;
    }

    public void setDimenWeight(int dimenWeight) {
        this.dimenWeight = dimenWeight;
    }

    public int getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(int shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public float getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(float grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getNoOfPack() {
        return noOfPack;
    }

    public void setNoOfPack(int noOfPack) {
        this.noOfPack = noOfPack;
    }

    public int getPackType() {
        return packType;
    }

    public void setPackType(int packType) {
        this.packType = packType;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getLrCopy() {
        return lrCopy;
    }

    public void setLrCopy(String lrCopy) {
        this.lrCopy = lrCopy;
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

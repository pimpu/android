package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class TransportationModel implements Parcelable {
    private int userId ;
    private int transportationId ;
    private int serverId ;
    private int commodityServerId ;
    private String strCommodity;
    private int dimenLength ;
    private int dimenHeight ;
    private int dimenWidth;
    private int shipmentType ;
    private int noOfPack ;
    private int packType ;
    private String strPackType ;
    private String  pickUp ;
    private String drop ;
    private String lrCopy ;
    private int availOption ;
    private int status ;
    private String  createdAt ;
    private String bookingId;
    private String measurement;
    private float grossWeight;
    private String strShipmentType;

    public TransportationModel() {
    }

    public TransportationModel(int serverId, int commodityServerId, int dimenLength,
                               int dimenHeight, int dimenWidth, int shipmentType, int noOfPack,
                               int packType, String pickUp, String drop, String lrCopy,
                               int availOption, int status, String createdAt, String bookingId,
                               String measurement, float grossWeight) {
        this.serverId = serverId;
        this.commodityServerId = commodityServerId;
        this.dimenLength = dimenLength;
        this.dimenHeight = dimenHeight;
        this.dimenWidth = dimenWidth;
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

    public TransportationModel(
            String strCommodity,
            int dimenLength,
            int dimenHeight,
            int dimenWidth,
            int noOfPack,
            String strPackType,
            String pickUp,
            String drop,
            int availOption,
            int status,
            String createdAt,
            String bookingId,
            String measurement,
            float grossWeight,
            String strShipmentType,
            int shipmentType,
            int userId) {
        this.strCommodity = strCommodity;
        this.dimenLength = dimenLength;
        this.dimenHeight = dimenHeight;
        this.dimenWidth = dimenWidth;
        this.noOfPack = noOfPack;
        this.strPackType = strPackType;
        this.pickUp = pickUp;
        this.drop = drop;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.bookingId = bookingId;
        this.measurement = measurement;
        this.grossWeight = grossWeight;
        this.strShipmentType = strShipmentType;
        this.shipmentType = shipmentType;
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "{" +
                "\"commodityServerId\":\"" +commodityServerId + '\"' +
                ",\"dimenLength\":\"" + dimenLength + '\"' +
                ",\"dimenHeight\":\"" + dimenHeight + '\"' +
                ",\"dimenWidth\":\"" + dimenWidth + '\"' +
                ",\"noOfPack\":\"" + noOfPack + '\"' +
                ",\"packType\":\"" + packType + '\"' +
                ",\"pickUp\":\"" + pickUp + '\"' +
                ",\"drop\":\"" + drop + '\"' +
                ",\"availOption\":\"" + availOption + '\"' +
                ",\"status\":\"" + status + '\"' +
                ",\"createdAt\":\"" + createdAt + '\"' +
                ",\"bookingId\":\"" + bookingId + '\"' +
                ",\"measurement\":\"" + measurement + '\"' +
                ",\"grossWeight\":\"" + grossWeight + '\"' +
                ",\"strShipmentType\":\"" + strShipmentType + '\"' +
                ",\"shipmentType\":\"" + shipmentType + '\"' +
                ",\"userId\":\"" + userId + '\"' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getDimenWidth() {
        return dimenWidth;
    }

    public void setDimenWidth(int dimenWidth) {
        this.dimenWidth = dimenWidth;
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

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
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

    public String getStrShipmentType() {
        return strShipmentType;
    }

    public void setStrShipmentType(String strShipmentType) {
        this.strShipmentType = strShipmentType;
    }

    public String getStrCommodity() {
        return strCommodity;
    }

    public void setStrCommodity(String strCommodity) {
        this.strCommodity = strCommodity;
    }

    public String getStrPackType() {
        return strPackType;
    }

    public void setStrPackType(String strPackType) {
        this.strPackType = strPackType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(serverId);
        dest.writeString(strCommodity);
        dest.writeInt(dimenLength);
        dest.writeInt(dimenHeight);
        dest.writeInt(dimenWidth);
        dest.writeInt(shipmentType);
        dest.writeInt(noOfPack);
        dest.writeString(strPackType);
        dest.writeString(pickUp);
        dest.writeString(drop);
        dest.writeString(lrCopy);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeString(bookingId);
        dest.writeString(measurement);
        dest.writeFloat(grossWeight);
        dest.writeString(strShipmentType);
    }

    private TransportationModel(Parcel in) {
        userId = in.readInt();
        serverId = in.readInt();
        strCommodity = in.readString();
        dimenLength = in.readInt();
        dimenHeight = in.readInt();
        dimenWidth = in.readInt();
        shipmentType = in.readInt();
        noOfPack = in.readInt();
        strPackType = in.readString();
        pickUp = in.readString();
        drop = in.readString();
        lrCopy = in.readString();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        bookingId = in.readString();
        measurement = in.readString();
        grossWeight = in.readFloat();
        strShipmentType = in.readString();
    }

    public static final Parcelable.Creator<TransportationModel> CREATOR = new Parcelable.Creator<TransportationModel>() {

        @Override
        public TransportationModel createFromParcel(Parcel source) {
            return new TransportationModel(source);
        }

        @Override
        public TransportationModel[] newArray(int size) {
            return new TransportationModel[size];
        }
    };

}

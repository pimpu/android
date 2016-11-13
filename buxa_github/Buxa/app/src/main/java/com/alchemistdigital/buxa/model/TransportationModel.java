package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alchemistdigital.buxa.utilities.DateHelper;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class TransportationModel implements Parcelable {
    private int userId ;
    private int transportationId ;
    private int serverId ;
    private String bookingId;
    private String  pickUp ;
    private String drop ;
    private String strShipmentType;
    private int shipmentType ;
    private String measurement;
    private float grossWeight;
    private int packType ;
    private String strPackType ;
    private int noOfPack ;
    private int commodityServerId ;
    private String strCommodity;
    private int dimenLength ;
    private int dimenHeight ;
    private int dimenWidth;
    private String lrCopy ;
    private int availOption ;
    private int status ;
    private String  createdAt ;

    public TransportationModel() {
    }

    // this constructure is used for passing of object between activity
    public TransportationModel(
            String bookingId,
            String pickUp,
            String drop,
            String strShipmentType,
            String measurement,
            float grossWeight,
            String strPackType,
            int noOfPack,
            String strCommodity,
            int dimenLength,
            int dimenHeight,
            int dimenWidth,
            int availOption,
            int status,
            String createdAt,
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
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"bookingId\":\"" + bookingId + '\"' +
                ",\"pickUp\":\"" + pickUp + '\"' +
                ",\"drop\":\"" + drop + '\"' +
                ",\"shipmentType\":\"" + shipmentType + '\"' +
                ",\"measurement\":\"" + measurement + '\"' +
                ",\"grossWeight\":\"" + grossWeight + '\"' +
                ",\"packType\":\"" + packType + '\"' +
                ",\"noOfPack\":\"" + noOfPack + '\"' +
                ",\"commodityServerId\":\"" +commodityServerId + '\"' +
                ",\"dimenLength\":\"" + dimenLength + '\"' +
                ",\"dimenHeight\":\"" + dimenHeight + '\"' +
                ",\"dimenWidth\":\"" + dimenWidth + '\"' +
                ",\"availOption\":\"" + availOption + '\"' +
                ",\"status\":\"" + status + '\"' +
                ",\"createdAt\":\"" + createdAt + '\"' +
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
        dest.writeString(bookingId);
        dest.writeString(pickUp);
        dest.writeString(drop);
        dest.writeString(strShipmentType);
        dest.writeString(measurement);
        dest.writeFloat(grossWeight);
        dest.writeString(strPackType);
        dest.writeInt(packType);
        dest.writeInt(noOfPack);
        dest.writeString(strCommodity);
        dest.writeInt(commodityServerId);
        dest.writeInt(dimenLength);
        dest.writeInt(dimenHeight);
        dest.writeInt(dimenWidth);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeInt(userId);
        dest.writeInt(serverId);
        dest.writeInt(shipmentType);
        dest.writeString(lrCopy);
    }

    private TransportationModel(Parcel in) {
        bookingId = in.readString();
        pickUp = in.readString();
        drop = in.readString();
        strShipmentType = in.readString();
        measurement = in.readString();
        grossWeight = in.readFloat();
        strPackType = in.readString();
        packType = in.readInt();
        noOfPack = in.readInt();
        strCommodity = in.readString();
        commodityServerId = in.readInt();
        dimenLength = in.readInt();
        dimenHeight = in.readInt();
        dimenWidth = in.readInt();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        userId = in.readInt();
        serverId = in.readInt();
        shipmentType = in.readInt();
        lrCopy = in.readString();
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

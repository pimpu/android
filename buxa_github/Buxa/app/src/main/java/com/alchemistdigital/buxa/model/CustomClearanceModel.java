package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 9/17/2016.
 */
public class CustomClearanceModel implements Parcelable {
    private int userId ;
    private int serverId;
    private String bookingId;
    private int iShipmentType;
    private String stuffingType;
    private String stuffingAddress;
    private int availOption ;
    private int status ;
    private String  createdAt ;
    private String strShipmentType;

    public CustomClearanceModel() {
    }

    public CustomClearanceModel(
            int serverId,
            String bookingId,
            int iShipmentType,
            String stuffingType,
            String stuffingAddress,
            int availOption,
            int status,
            String createdAt) {
        this.serverId = serverId;
        this.bookingId = bookingId;
        this.iShipmentType = iShipmentType;
        this.stuffingType = stuffingType;
        this.stuffingAddress = stuffingAddress;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
    }

    public CustomClearanceModel(
            String bookingId,
            String stuffingType,
            String stuffingAddress,
            int availOption,
            int status,
            String createdAt,
            String strShipmentType,
            int userId,
            int iShipmentType) {
        this.bookingId = bookingId;
        this.stuffingType = stuffingType;
        this.stuffingAddress = stuffingAddress;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.strShipmentType = strShipmentType;
        this.userId = userId;
        this.iShipmentType = iShipmentType;
    }

    @Override
    public String toString() {
        return "{" +
                "\"bookingId\":\""+bookingId + '\"' +
                ",\"stuffingType\":\""+stuffingType + '\"' +
                ",\"stuffingAddress\":\""+stuffingAddress + '\"' +
                ",\"availOption\":\""+availOption + '\"' +
                ",\"status\":\""+status + '\"' +
                ",\"createdAt\":\""+createdAt + '\"' +
                ",\"strShipmentType\":\""+strShipmentType + '\"' +
                ",\"userId\":\"" + userId + '\"' +
                ",\"iShipmentType\":\""+iShipmentType + '\"' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getiShipmentType() {
        return iShipmentType;
    }

    public void setiShipmentType(int iShipmentType) {
        this.iShipmentType = iShipmentType;
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

    public String getStrShipmentType() {
        return strShipmentType;
    }

    public void setStrShipmentType(String strShipmentType) {
        this.strShipmentType = strShipmentType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(serverId);
        dest.writeString(bookingId);
        dest.writeInt(iShipmentType);
        dest.writeString(stuffingType);
        dest.writeString(stuffingAddress);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeString(strShipmentType);
    }

    private CustomClearanceModel(Parcel in) {
        userId = in.readInt();
        serverId = in.readInt();
        bookingId = in.readString();
        iShipmentType = in.readInt();
        stuffingType = in.readString();
        stuffingAddress = in.readString();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        strShipmentType = in.readString();
    }

    public static final Parcelable.Creator<CustomClearanceModel> CREATOR = new Parcelable.Creator<CustomClearanceModel>() {

        @Override
        public CustomClearanceModel createFromParcel(Parcel source) {
            return new CustomClearanceModel(source);
        }

        @Override
        public CustomClearanceModel[] newArray(int size) {
            return new CustomClearanceModel[size];
        }
    };
}

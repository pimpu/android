package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 9/17/2016.
 */
public class CustomClearanceModel implements Parcelable {
    private int userId ;
    private int CCId;
    private int serverId;
    private String bookingId;
    private String CCType;
    private int commodityServerId ;
    private String strCommodity;
    private float grossWeight;
    private int HSCode;
    private int iShipmentType;
    private String strShipmentType;
    private String stuffingType;
    private String stuffingAddress;
    private int availOption ;
    private int status ;
    private String  createdAt ;

    public CustomClearanceModel() {
    }

    // this constructure is used for passing of object between activity
    public CustomClearanceModel(
            String bookingId,
            String CCType,
            String strCommodity,
            float grossWeight,
            int HSCode,
            String strShipmentType,
            String stuffingType,
            String stuffingAddress,
            int availOption,
            int status,
            String createdAt,
            int userId) {
        this.bookingId = bookingId;
        this.stuffingType = stuffingType;
        this.stuffingAddress = stuffingAddress;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.strShipmentType = strShipmentType;
        this.userId = userId;
        this.CCType = CCType;
        this.strCommodity = strCommodity;
        this.grossWeight = grossWeight;
        this.HSCode = HSCode;
    }

    @Override
    public String toString() {
        return "{" +
                "\"bookingId\":\""+bookingId + '\"' +
                ",\"CCType\":\""+CCType + '\"' +
                ",\"commodityServerId\":\""+commodityServerId + '\"' +
                ",\"grossWeight\":\""+grossWeight + '\"' +
                ",\"HSCode\":\""+HSCode + '\"' +
                ",\"iShipmentType\":\""+iShipmentType + '\"' +
                ",\"stuffingType\":\""+stuffingType + '\"' +
                ",\"stuffingAddress\":\""+stuffingAddress + '\"' +
                ",\"availOption\":\""+availOption + '\"' +
                ",\"status\":\""+status + '\"' +
                ",\"createdAt\":\""+createdAt + '\"' +
                ",\"userId\":\"" + userId + '\"' +
                '}';
    }

    public int getCCId() {
        return CCId;
    }

    public void setCCId(int CCId) {
        this.CCId = CCId;
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

    public String getCCType() {
        return CCType;
    }

    public void setCCType(String CCType) {
        this.CCType = CCType;
    }

    public int getCommodityServerId() {
        return commodityServerId;
    }

    public void setCommodityServerId(int commodityServerId) {
        this.commodityServerId = commodityServerId;
    }

    public String getStrCommodity() {
        return strCommodity;
    }

    public void setStrCommodity(String strCommodity) {
        this.strCommodity = strCommodity;
    }

    public float getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(float grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getHSCode() {
        return HSCode;
    }

    public void setHSCode(int HSCode) {
        this.HSCode = HSCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookingId);
        dest.writeString(CCType);
        dest.writeString(strCommodity);
        dest.writeInt(commodityServerId);
        dest.writeFloat(grossWeight);
        dest.writeInt(HSCode);
        dest.writeString(strShipmentType);
        dest.writeString(stuffingType);
        dest.writeString(stuffingAddress);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeInt(userId);
        dest.writeInt(serverId);
        dest.writeInt(iShipmentType);
    }

    private CustomClearanceModel(Parcel in) {
        bookingId = in.readString();
        CCType = in.readString();
        strCommodity = in.readString();
        commodityServerId = in.readInt();
        grossWeight = in.readFloat();
        HSCode = in.readInt();
        strShipmentType = in.readString();
        stuffingType = in.readString();
        stuffingAddress = in.readString();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        userId = in.readInt();
        serverId = in.readInt();
        iShipmentType = in.readInt();
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

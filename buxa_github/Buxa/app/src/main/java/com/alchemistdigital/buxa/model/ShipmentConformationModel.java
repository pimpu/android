package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pimpu on 11/15/2016.
 */

public class ShipmentConformationModel implements Parcelable{

    private int keyId;
    private String bookingId;
    private int enquiryStatus;
    private int rates;
    private int isTrans;
    private int isCC;
    private int isFF;
    private int status;
    private String createdAt;
    private String quotaion;
    private String shipArea;

    public ShipmentConformationModel() {
    }

    public String getQuotaion() {
        return quotaion;
    }

    public void setQuotaion(String quotaion) {
        this.quotaion = quotaion;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getEnquiryStatus() {
        return enquiryStatus;
    }

    public void setEnquiryStatus(int enquiryStatus) {
        this.enquiryStatus = enquiryStatus;
    }

    public int getRates() {
        return rates;
    }

    public void setRates(int rates) {
        this.rates = rates;
    }

    public int getIsTrans() {
        return isTrans;
    }

    public void setIsTrans(int isTrans) {
        this.isTrans = isTrans;
    }

    public int getIsCC() {
        return isCC;
    }

    public void setIsCC(int isCC) {
        this.isCC = isCC;
    }

    public int getIsFF() {
        return isFF;
    }

    public void setIsFF(int isFF) {
        this.isFF = isFF;
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

    public String getShipArea() {
        return shipArea;
    }

    public void setShipArea(String shipArea) {
        this.shipArea = shipArea;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeInt(keyId);
        dest.writeString(bookingId);
        dest.writeInt(enquiryStatus);
        dest.writeInt(rates);
        dest.writeInt(isTrans);
        dest.writeInt(isCC);
        dest.writeInt(isFF);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeString(shipArea);
    }

    private ShipmentConformationModel(Parcel in) {
        keyId = in.readInt();
        bookingId = in.readString();
        enquiryStatus = in.readInt();
        rates = in.readInt();
        isTrans = in.readInt();
        isCC = in.readInt();
        isFF = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        shipArea = in.readString();
    }

    public static final Parcelable.Creator<ShipmentConformationModel> CREATOR = new Parcelable.Creator<ShipmentConformationModel>() {

        @Override
        public ShipmentConformationModel createFromParcel(Parcel source) {
            return new ShipmentConformationModel(source);
        }

        @Override
        public ShipmentConformationModel[] newArray(int size) {
            return new ShipmentConformationModel[size];
        }
    };
}

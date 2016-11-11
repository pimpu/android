package com.alchemistdigital.buxa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 9/19/2016.
 */
public class FreightForwardingModel implements Parcelable {
    private int userId;
    private int freightForwardingId;
    private int serverId;
    private String bookingId;
    private String portOfLoading;
    private String portOfCountry;
    private String portOfDestination;
    private String strIncoterm;
    private String strDestinatioDeliveryAdr;
    private int shipmentType;
    private String strShipmentType;
    private String measurement;
    private float grossWeight;
    private int packType ;
    private String strPackType ;
    private int noOfPack ;
    private int commodityServerId ;
    private String strCommodity;
    private int availOption ;
    private int status ;
    private String  createdAt ;

    public FreightForwardingModel() {
    }

    public FreightForwardingModel(
            String bookingId,
            int shipmentType,
            String portOfLoading,
            String portOfDestination,
            int availOption,
            int status,
            String createdAt,
            int serverId) {
        this.bookingId = bookingId;
        this.shipmentType = shipmentType;
        this.portOfLoading = portOfLoading;
        this.portOfDestination = portOfDestination;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.serverId = serverId;
    }

    // this constructure is used for passing of object between activity
    public FreightForwardingModel(
            String bookingId,
            String portOfLoading,
            String portOfCountry,
            String portOfDestination,
            String strIncoterm,
            String strDestinatioDeliveryAdr,
            String strShipmentType,
            String measurement,
            float grossWeight,
            String strPackType,
            int noOfPack,
            String strCommodity,
            int availOption,
            int status,
            String createdAt,
            int userId) {
        this.bookingId = bookingId;
        this.portOfLoading = portOfLoading;
        this.portOfCountry = portOfCountry;
        this.portOfDestination = portOfDestination;
        this.strIncoterm = strIncoterm;
        this.strDestinatioDeliveryAdr = strDestinatioDeliveryAdr;
        this.strShipmentType = strShipmentType;
        this.measurement = measurement;
        this.grossWeight = grossWeight;
        this.strPackType = strPackType;
        this.noOfPack = noOfPack;
        this.strCommodity = strCommodity;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":\""+ userId + '\"' +
                ",\"bookingId\":\"" + bookingId + '\"' +
                ",\"shipmentType\":\"" + shipmentType + '\"' +
                ",\"portOfLoading\":\"" + portOfLoading + '\"' +
                ",\"portOfDestination\":\"" + portOfDestination + '\"' +
                ",\"availOption\":\"" + availOption + '\"' +
                ",\"status\":\"" + status + '\"' +
                ",\"createdAt\":\"" + createdAt + '\"' +
                ",\"strShipmentType\":\"" + strShipmentType + '\"' +
                '}';
    }

    public int getFreightForwardingId() {
        return freightForwardingId;
    }

    public void setFreightForwardingId(int freightForwardingId) {
        this.freightForwardingId = freightForwardingId;
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

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public String getPortOfDestination() {
        return portOfDestination;
    }

    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPortOfCountry() {
        return portOfCountry;
    }

    public void setPortOfCountry(String portOfCountry) {
        this.portOfCountry = portOfCountry;
    }

    public String getStrIncoterm() {
        return strIncoterm;
    }

    public void setStrIncoterm(String strIncoterm) {
        this.strIncoterm = strIncoterm;
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

    public int getPackType() {
        return packType;
    }

    public void setPackType(int packType) {
        this.packType = packType;
    }

    public String getStrPackType() {
        return strPackType;
    }

    public void setStrPackType(String strPackType) {
        this.strPackType = strPackType;
    }

    public int getNoOfPack() {
        return noOfPack;
    }

    public void setNoOfPack(int noOfPack) {
        this.noOfPack = noOfPack;
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

    public String getStrDestinatioDeliveryAdr() {
        return strDestinatioDeliveryAdr;
    }

    public void setStrDestinatioDeliveryAdr(String strDestinatioDeliveryAdr) {
        this.strDestinatioDeliveryAdr = strDestinatioDeliveryAdr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookingId);
        dest.writeString(portOfLoading);
        dest.writeString(portOfCountry);
        dest.writeString(portOfDestination);
        dest.writeString(strIncoterm);
        dest.writeString(strShipmentType);
        dest.writeString(strDestinatioDeliveryAdr);
        dest.writeString(measurement);
        dest.writeFloat(grossWeight);
        dest.writeString(strPackType);
        dest.writeInt(packType);
        dest.writeInt(noOfPack);
        dest.writeString(strCommodity);
        dest.writeInt(commodityServerId);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeInt(userId);
        dest.writeInt(serverId);
    }

    protected FreightForwardingModel(Parcel in) {
        bookingId = in.readString();
        portOfLoading = in.readString();
        portOfCountry = in.readString();
        portOfDestination = in.readString();
        strIncoterm = in.readString();
        strDestinatioDeliveryAdr = in.readString();
        strShipmentType = in.readString();
        measurement = in.readString();
        grossWeight = in.readFloat();
        strPackType = in.readString();
        packType = in.readInt();
        noOfPack = in.readInt();
        strCommodity = in.readString();
        commodityServerId = in.readInt();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        userId = in.readInt();
        serverId = in.readInt();
    }

    public static final Creator<FreightForwardingModel> CREATOR = new Creator<FreightForwardingModel>() {
        @Override
        public FreightForwardingModel createFromParcel(Parcel in) {
            return new FreightForwardingModel(in);
        }

        @Override
        public FreightForwardingModel[] newArray(int size) {
            return new FreightForwardingModel[size];
        }
    };

}

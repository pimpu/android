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
    private int shipmentType;
    private String portOfLoading;
    private String portOfDestination;
    private int availOption ;
    private int status ;
    private String  createdAt ;
    private String strShipmentType;

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

    public FreightForwardingModel(
            String bookingId,
            String portOfLoading,
            String portOfDestination,
            int availOption,
            int status,
            String createdAt,
            String strShipmentType,
            int userId,
            int shipmentType) {
        this.bookingId = bookingId;
        this.portOfLoading = portOfLoading;
        this.portOfDestination = portOfDestination;
        this.availOption = availOption;
        this.status = status;
        this.createdAt = createdAt;
        this.strShipmentType = strShipmentType;
        this.userId = userId;
        this.shipmentType = shipmentType;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serverId);
        dest.writeString(bookingId);
        dest.writeInt(shipmentType);
        dest.writeString(portOfLoading);
        dest.writeString(portOfDestination);
        dest.writeInt(availOption);
        dest.writeInt(status);
        dest.writeString(createdAt);
        dest.writeString(strShipmentType);
    }

    protected FreightForwardingModel(Parcel in) {
        serverId = in.readInt();
        bookingId = in.readString();
        shipmentType = in.readInt();
        portOfLoading = in.readString();
        portOfDestination = in.readString();
        availOption = in.readInt();
        status = in.readInt();
        createdAt = in.readString();
        strShipmentType = in.readString();
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

package com.alchemistdigital.kissan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 5/20/2016.
 */
public class Vendor implements Parcelable{
    public int id;
    public int serverId;
    public int vendor_status;
    private String creted_at;
    public String vendor_name;
    public String vendor_contact;
    public String vendor_Address;
    public String vendor_email;
    private String vendor_offline_action;

    public Vendor() {
    }

    public Vendor(int serverId, int vendor_status, String vendor_name, String vendor_contact, String vendor_Address, String vendor_email) {
        this.serverId = serverId;
        this.vendor_status = vendor_status;
        this.vendor_name = vendor_name;
        this.vendor_contact = vendor_contact;
        this.vendor_Address = vendor_Address;
        this.vendor_email = vendor_email;
    }

    @Override
    public String toString() {
        return "{" +
                " \"id\":\"" + id + '\"' +
                ", \"serverId\":\"" + serverId + '\"' +
                ", \"vendor_status\":\"" + vendor_status + '\"' +
                ", \"vendor_name\":\"" + vendor_name + '\"' +
                ", \"vendor_contact\":\"" + vendor_contact + '\"' +
                ", \"vendor_Address\":\"" + vendor_Address + '\"' +
                ", \"vendor_email\":\"" + vendor_email + '\"' +
                ", \"vendor_offline_action\":\"" + vendor_offline_action + '\"' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }

    public int getVendor_status() {
        return vendor_status;
    }

    public void setVendor_status(int vendor_status) {
        this.vendor_status = vendor_status;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_contact() {
        return vendor_contact;
    }

    public void setVendor_contact(String vendor_contact) {
        this.vendor_contact = vendor_contact;
    }

    public String getVendor_Address() {
        return vendor_Address;
    }

    public void setVendor_Address(String vendor_Address) {
        this.vendor_Address = vendor_Address;
    }

    public String getVendor_email() {
        return vendor_email;
    }

    public void setVendor_email(String vendor_email) {
        this.vendor_email = vendor_email;
    }

    public String getVendor_offline_action() {
        return vendor_offline_action;
    }

    public void setVendor_offline_action(String vendor_offline_action) {
        this.vendor_offline_action = vendor_offline_action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendor_name);
        dest.writeString(vendor_email);
        dest.writeString(vendor_contact);
        dest.writeString(vendor_Address);
        dest.writeInt(id);
        dest.writeInt(serverId);
        dest.writeInt(vendor_status);
    }

    /**
     * Retrieving Vendor data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     */
    private Vendor(Parcel in) {
        vendor_name = in.readString();
        vendor_email = in.readString();
        vendor_contact = in.readString();
        vendor_Address = in.readString();
        id = in.readInt();
        serverId = in.readInt();
        vendor_status = in.readInt();
    }

    public static final Parcelable.Creator<Vendor> CREATOR = new Parcelable.Creator<Vendor>() {

        @Override
        public Vendor createFromParcel(Parcel source) {
            return new Vendor(source);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };
}

package com.alchemistdigital.kissan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 4/5/2016.
 */
public class OBP implements Parcelable {
    public int obp_id;
    public int userID_serverId; // server id
    public String obp_name;
    public String obp_store_name;
    public String obp_email_id;
    public String obp_email_passowrd;
    public String obp_contact_number;
    public String obp_address;
    public int obp_pincode;
    public String obp_city;
    public String obp_state;
    public String obp_country;
    public int obp_status;
    public String obp_offline_action;

    public OBP() {
    }

    public OBP(int userID_serverId,
               String obp_name,
               String obp_store_name,
               String obp_email_id,
               String obp_email_passowrd,
               String obp_contact_number,
               String obp_address,
               int obp_pincode,
               String obp_city,
               String obp_state,
               String obp_country,
               int obp_status) {
        this.userID_serverId = userID_serverId;
        this.obp_name = obp_name;
        this.obp_store_name = obp_store_name;
        this.obp_email_id = obp_email_id;
        this.obp_email_passowrd = obp_email_passowrd;
        this.obp_contact_number = obp_contact_number;
        this.obp_address = obp_address;
        this.obp_pincode = obp_pincode;
        this.obp_city = obp_city;
        this.obp_state = obp_state;
        this.obp_country = obp_country;
        this.obp_status = obp_status;
    }

    @Override
    public String toString() {
        return "{" +
                " \"obp_id\":\"" + obp_id + '\"' +
                ", \"userID_serverId\":\"" + userID_serverId + '\"' +
                ", \"obp_name\":\"" + obp_name + '\"' +
                ", \"obp_store_name\":\"" + obp_store_name + '\"' +
                ", \"obp_email_id\":\"" + obp_email_id + '\"' +
                ", \"obp_email_passowrd\":\"" + obp_email_passowrd + '\"' +
                ", \"obp_contact_number\":\"" + obp_contact_number + '\"' +
                ", \"obp_address\":\"" + obp_address + '\"' +
                ", \"obp_pincode\":\"" + obp_pincode + '\"' +
                ", \"obp_city\":\"" + obp_city + '\"' +
                ", \"obp_state\":\"" + obp_state + '\"' +
                ", \"obp_country\":\"" + obp_country + '\"' +
                ", \"obp_status\":\"" + obp_status + '\"' +
                ", \"obp_offline_action\":\"" + obp_offline_action + '\"' +
                '}';
    }

    public int getObp_id() {
        return obp_id;
    }

    public void setObp_id(int obp_id) {
        this.obp_id = obp_id;
    }

    public int getUserID_serverId() {
        return userID_serverId;
    }

    public void setUserID_serverId(int userID_serverId) {
        this.userID_serverId = userID_serverId;
    }

    public String getObp_name() {
        return obp_name;
    }

    public void setObp_name(String obp_name) {
        this.obp_name = obp_name;
    }

    public String getObp_store_name() {
        return obp_store_name;
    }

    public void setObp_store_name(String obp_store_name) {
        this.obp_store_name = obp_store_name;
    }

    public String getObp_email_id() {
        return obp_email_id;
    }

    public void setObp_email_id(String obp_email_id) {
        this.obp_email_id = obp_email_id;
    }

    public String getObp_email_passowrd() {
        return obp_email_passowrd;
    }

    public void setObp_email_passowrd(String obp_email_passowrd) {
        this.obp_email_passowrd = obp_email_passowrd;
    }

    public String getObp_contact_number() {
        return obp_contact_number;
    }

    public void setObp_contact_number(String obp_contact_number) {
        this.obp_contact_number = obp_contact_number;
    }

    public String getObp_address() {
        return obp_address;
    }

    public void setObp_address(String obp_address) {
        this.obp_address = obp_address;
    }

    public int getObp_pincode() {
        return obp_pincode;
    }

    public void setObp_pincode(int obp_pincode) {
        this.obp_pincode = obp_pincode;
    }

    public String getObp_city() {
        return obp_city;
    }

    public void setObp_city(String obp_city) {
        this.obp_city = obp_city;
    }

    public String getObp_state() {
        return obp_state;
    }

    public void setObp_state(String obp_state) {
        this.obp_state = obp_state;
    }

    public String getObp_country() {
        return obp_country;
    }

    public void setObp_country(String obp_country) {
        this.obp_country = obp_country;
    }

    public int getObp_status() {
        return obp_status;
    }

    public void setObp_status(int obp_status) {
        this.obp_status = obp_status;
    }

    public String getObp_offline_action() {
        return obp_offline_action;
    }

    public void setObp_offline_action(String obp_offline_action) {
        this.obp_offline_action = obp_offline_action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(obp_id);
        dest.writeInt(userID_serverId);
        dest.writeString(obp_name);
        dest.writeString(obp_store_name);
        dest.writeString(obp_email_id);
        dest.writeString(obp_email_passowrd);
        dest.writeString(obp_contact_number);
        dest.writeString(obp_address);
        dest.writeInt(obp_pincode);
        dest.writeString(obp_city);
        dest.writeString(obp_state);
        dest.writeString(obp_country);
        dest.writeInt(obp_status);
        dest.writeString(obp_offline_action);
    }

    /**
     * Retrieving OBP data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private OBP(Parcel in) {
        obp_id = in.readInt();
        userID_serverId = in.readInt();
        obp_name = in.readString();
        obp_store_name = in.readString();
        obp_email_id = in.readString();
        obp_email_passowrd = in.readString();
        obp_contact_number = in.readString();
        obp_address = in.readString();
        obp_pincode = in.readInt();
        obp_city = in.readString();
        obp_state = in.readString();
        obp_country = in.readString();
        obp_status = in.readInt();
        obp_offline_action = in.readString();
    }

    public static final Parcelable.Creator<OBP> CREATOR = new Parcelable.Creator<OBP>() {

        @Override
        public OBP createFromParcel(Parcel source) {
            return new OBP(source);
        }

        @Override
        public OBP[] newArray(int size) {
            return new OBP[size];
        }
    };

}

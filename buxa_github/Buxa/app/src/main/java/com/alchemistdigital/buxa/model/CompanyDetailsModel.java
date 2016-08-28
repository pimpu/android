package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class CompanyDetailsModel {
    private int id ;
    private int serverId;
    private int referenceNo;
    private String userDesignation;
    private String name;
    private int contact;
    private String address;
    private String city;
    private String landmark;
    private String state;
    private String pan;
    private String tin;
    private int status ;
    private String createdAt;

    public CompanyDetailsModel() {
    }

    public CompanyDetailsModel(int serverId, int referenceNo, String userDesignation,
                               String name, int contact, String address, String city,
                               String landmark, String state, String pan,
                               String tin, int status, String createdAt) {
        this.serverId = serverId;
        this.referenceNo = referenceNo;
        this.userDesignation = userDesignation;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.city = city;
        this.landmark = landmark;
        this.state = state;
        this.pan = pan;
        this.tin = tin;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(int referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

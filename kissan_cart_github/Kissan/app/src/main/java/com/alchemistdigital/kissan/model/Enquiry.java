package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/7/2016.
 */
public class Enquiry {
    private int enquiry_id;
    private int enquiry_server_id;
    private String enquiry_reference;
    private int enquiry_userId;
    private int enquiry_groupId;
    private int enquiry_replyTo;
    private int enquiry_replied;
    private int enquiry_societyId;
    private int enquiry_subCategoryId;
    private int enquiry_productId;
    private String enquiry_quantity;
    private int  enquiry_status;
    private String creted_at;
    private String enquiry_offline_action;

    public Enquiry() {
    }

    public Enquiry(int enquiry_server_id,
                   String creted_at,
                   String enquiry_reference,
                   int enquiry_userId,
                   int enquiry_groupId,
                   int enquiry_replyTo,
                   int enquiry_replied,
                   int enquiry_societyId,
                   int enquiry_subCategoryId,
                   int enquiry_productId,
                   String enquiry_quantity,
                   int enquiry_status) {

        this.enquiry_server_id = enquiry_server_id;
        this.creted_at = creted_at;
        this.enquiry_reference = enquiry_reference;
        this.enquiry_userId = enquiry_userId;
        this.enquiry_groupId = enquiry_groupId;
        this.enquiry_replyTo = enquiry_replyTo;
        this.enquiry_replied = enquiry_replied;
        this.enquiry_societyId = enquiry_societyId;
        this.enquiry_subCategoryId = enquiry_subCategoryId;
        this.enquiry_productId = enquiry_productId;
        this.enquiry_quantity = enquiry_quantity;
        this.enquiry_status  = enquiry_status;
    }

    @Override
    public String toString() {
        return "{" +
                " \"enquiry_id\":\"" + enquiry_id + '\"' +
                ", \"enquiry_server_id\":\"" + enquiry_server_id + '\"' +
                ", \"enquiry_reference\":\"" + enquiry_reference + '\"' +
                ", \"enquiry_userId\":\"" + enquiry_userId + '\"' +
                ", \"enquiry_groupId\":\"" + enquiry_groupId + '\"' +
                ", \"enquiry_replyTo\":\"" + enquiry_replyTo + '\"' +
                ", \"enquiry_replied\":\"" + enquiry_replied + '\"' +
                ", \"enquiry_societyId\":\"" + enquiry_societyId + '\"' +
                ", \"enquiry_subCategoryId\":\"" + enquiry_subCategoryId + '\"' +
                ", \"enquiry_productId\":\"" + enquiry_productId + '\"' +
                ", \"enquiry_quantity\":\"" + enquiry_quantity + '\"' +
                ", \"enquiry_creted_at\":\"" + creted_at + '\"' +
                ", \"enquiry_offline_action\":\"" + enquiry_offline_action + '\"' +
                '}';
    }

    // Getters
    public int getEnquiry_id() {
        return enquiry_id;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public String getEnquiry_reference() {
        return enquiry_reference;
    }

    public int getEnquiry_userId() {
        return enquiry_userId;
    }

    public int getEnquiry_groupId() {
        return enquiry_groupId;
    }

    public int getEnquiry_replyTo() {
        return enquiry_replyTo;
    }

    public int getEnquiry_replied() {
        return enquiry_replied;
    }

    public int getEnquiry_status() {
        return enquiry_status;
    }

    public int getEnquiry_server_id() {
        return enquiry_server_id;
    }

    public String getEnquiry_offline_action() {
        return enquiry_offline_action;
    }

    public int getEnquiry_societyId() {
        return enquiry_societyId;
    }

    public int getEnquiry_subCategoryId() {
        return enquiry_subCategoryId;
    }

    public int getEnquiry_productId() {
        return enquiry_productId;
    }

    public String getEnquiry_quantity() {
        return enquiry_quantity;
    }

    // Setters
    public void setEnquiry_id(int enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }

    public void setEnquiry_reference(String enquiry_reference) {
        this.enquiry_reference = enquiry_reference;
    }

    public void setEnquiry_userId(int enquiry_userId) {
        this.enquiry_userId = enquiry_userId;
    }

    public void setEnquiry_groupId(int enquiry_groupId) {
        this.enquiry_groupId = enquiry_groupId;
    }

    public void setEnquiry_replyTo(int enquiry_replyTo) {
        this.enquiry_replyTo = enquiry_replyTo;
    }

    public void setEnquiry_replied(int enquiry_replied) {
        this.enquiry_replied = enquiry_replied;
    }

    public void setEnquiry_status(int enquiry_status) {
        this.enquiry_status = enquiry_status;
    }

    public void setEnquiry_server_id(int enquiry_server_id) {
        this.enquiry_server_id = enquiry_server_id;
    }

    public void setEnquiry_offline_action(String enquiry_offline_action) {
        this.enquiry_offline_action = enquiry_offline_action;
    }

    public void setEnquiry_societyId(int enquiry_societyId) {
        this.enquiry_societyId = enquiry_societyId;
    }

    public void setEnquiry_subCategoryId(int enquiry_subCategoryId) {
        this.enquiry_subCategoryId = enquiry_subCategoryId;
    }

    public void setEnquiry_productId(int enquiry_productId) {
        this.enquiry_productId = enquiry_productId;
    }

    public void setEnquiry_quantity(String enquiry_quantity) {
        this.enquiry_quantity = enquiry_quantity;
    }
}

package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/7/2016.
 */
public class Enquiry {
    private int enquiry_id;
    private int enquiry_server_id;
    private String creted_at;
    private String enquiry_reference;
    private int enquiry_userId;
    private int enquiry_groupId;
    private int enquiry_replyTo;
    private int enquiry_replied;
    private String enquiry_message;
    private String enquiry_society;
    private String enquiry_society_address;
    private String enquiry_society_contact;
    private String enquiry_society_email;
    private String enquiry_document;
    private int  enquiry_status;
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
                   String enquiry_message,
                   String enquiry_society,
                   String enquiry_society_address,
                   String enquiry_society_contact,
                   String enquiry_society_email,
                   String enquiry_document,
                   int enquiry_status) {

        this.enquiry_server_id = enquiry_server_id;
        this.creted_at = creted_at;
        this.enquiry_reference = enquiry_reference;
        this.enquiry_userId = enquiry_userId;
        this.enquiry_groupId = enquiry_groupId;
        this.enquiry_replyTo = enquiry_replyTo;
        this.enquiry_replied = enquiry_replied;
        this.enquiry_message = enquiry_message;
        this.enquiry_society = enquiry_society;
        this.enquiry_society_address = enquiry_society_address;
        this.enquiry_society_contact = enquiry_society_contact;
        this.enquiry_society_email = enquiry_society_email;
        this.enquiry_document = enquiry_document;
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
                ", \"enquiry_message\":\"" + enquiry_message + '\"' +
                ", \"enquiry_society\":\"" + enquiry_society + '\"' +
                ", \"enquiry_society_address\":\"" + enquiry_society_address + '\"' +
                ", \"enquiry_society_contact\":\"" + enquiry_society_contact + '\"' +
                ", \"enquiry_society_email\":\"" + enquiry_society_email + '\"' +
                ", \"enquiry_document\":\"" + enquiry_document + '\"' +
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

    public String getEnquiry_message() {
        return enquiry_message;
    }

    public String getEnquiry_society() {
        return enquiry_society;
    }

    public String getEnquiry_society_address() {
        return enquiry_society_address;
    }

    public String getEnquiry_society_email() {
        return enquiry_society_email;
    }

    public String getEnquiry_society_contact() {
        return enquiry_society_contact;
    }

    public String getEnquiry_document() {
        return enquiry_document;
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

    public void setEnquiry_message(String enquiry_message) {
        this.enquiry_message = enquiry_message;
    }

    public void setEnquiry_society(String enquiry_society) {
        this.enquiry_society = enquiry_society;
    }

    public void setEnquiry_society_address(String enquiry_society_address) {
        this.enquiry_society_address = enquiry_society_address;
    }

    public void setEnquiry_society_contact(String enquiry_society_contact) {
        this.enquiry_society_contact = enquiry_society_contact;
    }

    public void setEnquiry_society_email(String enquiry_society_email) {
        this.enquiry_society_email = enquiry_society_email;
    }

    public void setEnquiry_document(String enquiry_document) {
        this.enquiry_document = enquiry_document;
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
}

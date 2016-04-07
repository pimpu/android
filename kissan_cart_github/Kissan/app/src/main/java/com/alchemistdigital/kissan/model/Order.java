package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/10/2016.
 */
public class Order {
    private int order_id;
    private int userId;
    private String order_reference;
    private String order_utr;
   /* private String order_item;
    private int order_quantity;
    private String order_price;
    private String order_total_amount;*/
    private int order_status;
    private String order_creted_at;
    private String order_items;
    private String order_offline_action;

    public Order() {
    }

    public Order(int userId, String order_reference, String order_utr, String order_creted_at,
                 int order_status) {
        this.userId = userId;
        this.order_id = order_id;
        this.order_reference = order_reference;
        this.order_utr = order_utr;
       /* this.order_item = order_item;
        this.order_quantity = order_quantity;
        this.order_price = order_price;
        this.order_total_amount = order_total_amount;*/
        this.order_status = order_status;
        this.order_creted_at = order_creted_at;
    }

    @Override
    public String toString() {
        return "{" +
                " \"order_id\":\"" + order_id + '\"' +
                ", \"userId\":\"" + userId + '\"' +
                ", \"order_reference\":\"" + order_reference + '\"' +
                ", \"order_utr\":\"" + order_utr + '\"' +
                ", \"order_status\":\"" + order_status + '\"' +
                ", \"order_creted_at\":\"" + order_creted_at + '\"' +
                ", \"order_items\":\"" + order_items + '\"' +
                ", \"order_offline_action\":\"" + order_offline_action + '\"' +
                '}';
    }

    // Setters

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setOrder_reference(String order_reference) {
        this.order_reference = order_reference;
    }

    public void setOrder_utr(String order_utr) {
        this.order_utr = order_utr;
    }

    /*public void setOrder_item(String order_item) {
        this.order_item = order_item;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public void setOrder_total_amount(String order_total_amount) {
        this.order_total_amount = order_total_amount;
    }*/

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public void setOrder_creted_at(String order_creted_at) {
        this.order_creted_at = order_creted_at;
    }

    public void setOrder_items(String order_items) {
        this.order_items = order_items;
    }

    public void setOrder_offline_action(String order_offline_action) {
        this.order_offline_action = order_offline_action;
    }

    // Getters

    public int getUserId() {
        return userId;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getOrder_reference() {
        return order_reference;
    }

    public String getOrder_utr() {
        return order_utr;
    }

    /*public String getOrder_item() {
        return order_item;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public String getOrder_price() {
        return order_price;
    }

    public String getOrder_total_amount() {
        return order_total_amount;
    }*/

    public int getOrder_status() {
        return order_status;
    }

    public String getOrder_creted_at() {
        return order_creted_at;
    }

    public String getOrder_items() {
        return order_items;
    }

    public String getOrder_offline_action() {
        return order_offline_action;
    }

}

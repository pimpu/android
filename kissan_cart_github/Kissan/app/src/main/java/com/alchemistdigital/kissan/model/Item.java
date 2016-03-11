package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/10/2016.
 */
public class Item {
    private int itemId;
    private String referenceNo;
    private String itemName;
    private int itemQuantity;
    private String itemPrice;
    private String itemTotalAmount;

    public Item() {
    }

    public Item(String referenceNo, String itemName, int itemQuantity, String itemPrice, String itemTotalAmount) {
        this.referenceNo = referenceNo;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemTotalAmount = itemTotalAmount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(String itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public String getItemCreationTime() {
        return itemCreationTime;
    }

    public void setItemCreationTime(String itemCreationTime) {
        this.itemCreationTime = itemCreationTime;
    }

    private String itemCreationTime;
}

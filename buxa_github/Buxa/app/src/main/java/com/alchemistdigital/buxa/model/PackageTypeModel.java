package com.alchemistdigital.buxa.model;

/**
 * Created by user on 9/10/2016.
 */
public class PackageTypeModel {

    private int packageTypeId ;
    private int serverId ;
    private String name ;
    private int status ;
    private String createdAt ;

    @Override
    public String toString() {
        return this.name ;
    }

    public PackageTypeModel() {
    }

    public PackageTypeModel(int serverId, String name, int status) {
        this.serverId = serverId;
        this.name = name;
        this.status = status;
    }

    public int getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(int packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

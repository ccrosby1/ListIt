package com.gcu.model;

import java.util.Date;

public class SharedCatalogModel {

    private int id;
    private int catalogId;
    private int sharedUserId;
    private String permission; // "view" or "edit"
    private Date createdDate;

    private String sharedUsername;

    public SharedCatalogModel() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCatalogId() { return catalogId; }
    public void setCatalogId(int catalogId) { this.catalogId = catalogId; }

    public int getSharedUserId() { return sharedUserId; }
    public void setSharedUserId(int sharedUserId) { this.sharedUserId = sharedUserId; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public String getSharedUsername() { return sharedUsername; }
    public void setSharedUsername(String sharedUsername) { this.sharedUsername = sharedUsername; }
}
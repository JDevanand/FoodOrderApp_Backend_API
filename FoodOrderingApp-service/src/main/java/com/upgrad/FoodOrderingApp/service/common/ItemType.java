package com.upgrad.FoodOrderingApp.service.common;

public enum ItemType {
    VEG("0"),NON_VEG("1");

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    private String itemType;

    ItemType(String s) {
        this.itemType =s;
    }


}

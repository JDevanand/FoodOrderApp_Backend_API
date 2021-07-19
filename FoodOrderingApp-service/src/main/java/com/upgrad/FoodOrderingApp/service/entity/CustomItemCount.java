package com.upgrad.FoodOrderingApp.service.entity;

public class CustomItemCount {

    private ItemEntity item;
    private int count;

    public CustomItemCount(ItemEntity item, int count) {
        this.item = item;
        this.count = count;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}

package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.service.common.ItemType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="item")
@NamedQueries(
        {
                @NamedQuery(name="getItembyUuid", query = "select p from ItemEntity p where p.uuid =:uuid")
        }
)
public class ItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    private String uuid;

    @Column(name ="item_name")
    @Size(max=30)
    private String itemName;

    @Column(name ="price")
    private Integer price;

    @Enumerated(EnumType.ORDINAL)
    @Column(name ="type")
    @Size(max=10)
    private ItemType itemType;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<CategoryItemEntity> categoryItemEntities = new HashSet<>();

    public Set<CategoryItemEntity> getCategoryItemEntities() {
        return categoryItemEntities;
    }

    public void setCategoryItemEntities(Set<CategoryItemEntity> categoryItemEntities) {
        this.categoryItemEntities = categoryItemEntities;
    }

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<OrderItemEntity> orderItemEntities = new HashSet<>();

    public Set<OrderItemEntity> getOrderItemEntities() {
        return orderItemEntities;
    }

    public void setOrderItemEntities(Set<OrderItemEntity> orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
    }

    @OneToMany(mappedBy = "itemEntity", cascade = CascadeType.ALL)
    private Set<RestaurantItemEntity> restaurantItemEntities = new HashSet<>();

    public Set<RestaurantItemEntity> getRestaurantItemEntities() {
        return restaurantItemEntities;
    }

    public void setRestaurantItemEntities(Set<RestaurantItemEntity> restaurantItemEntities) {
        this.restaurantItemEntities = restaurantItemEntities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }


    /*
    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    */


}

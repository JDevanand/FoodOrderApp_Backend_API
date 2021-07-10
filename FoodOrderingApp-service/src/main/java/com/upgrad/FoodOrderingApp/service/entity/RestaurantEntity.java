package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="restaurant")
@NamedQueries(
        {
                @NamedQuery(name = "byRestaurantUuid", query = "select s from RestaurantEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "getAllRestaurant", query = "select s from RestaurantEntity s")
        }
)
public class RestaurantEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    private String uuid;

    @Column(name ="restaurant_name")
    @Size(max=50)
    private String restaurantName;

    @Column(name ="photo_url")
    @Size(max=255)
    private String photoUrl;

    @Column(name ="customer_rating")
    private float customer_rating;

    @Column(name ="average_price_for_two")
    private Integer averagePriceForTwo;

    @Column(name ="number_of_customers_rated")
    private Integer noOfCustomersRated;

    @Column(name ="address_id")
    private Integer addressId;

    /*
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="restaurant_category",
            joinColumns = @JoinColumn(name="restaurant_id",referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name="category_id", referencedColumnName = "id",
                    nullable = false, updatable = false)
    )
    private List<CategoryEntity> categoryList;

    public List<CategoryEntity> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryEntity> categoryList) {
        this.categoryList = categoryList;
    }

    */

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(float customer_rating) {
        this.customer_rating = customer_rating;
    }

    public Integer getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(Integer averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public Integer getNoOfCustomersRated() {
        return noOfCustomersRated;
    }

    public void setNoOfCustomersRated(Integer noOfCustomersRated) {
        this.noOfCustomersRated = noOfCustomersRated;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

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

}

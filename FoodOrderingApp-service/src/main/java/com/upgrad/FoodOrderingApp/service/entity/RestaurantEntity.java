package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="restaurant")
@NamedQueries(
        {
                @NamedQuery(name = "byRestaurantUuid", query = "select s from RestaurantEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "getAllRestaurant", query = "select s from RestaurantEntity s"),
                @NamedQuery(name = "bySearchString", query = "select s from RestaurantEntity s where upper(s.restaurantName) LIKE :pattern")
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


    @Column(name ="customer_rating", scale=2)
    private double customerRating;

    @Column(name ="average_price_for_two")
    private Integer avgPrice;

    @Column(name ="number_of_customers_rated")
    private Integer numberCustomersRated;

    @OneToOne
    @JoinColumn(name ="address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.ALL)
    private Set<RestaurantItemEntity> restaurantItemEntities = new HashSet<>();

    public Set<RestaurantItemEntity> getRestaurantItemEntities() {
        return restaurantItemEntities;
    }

    public void setRestaurantItemEntities(Set<RestaurantItemEntity> restaurantItemEntities) {
        this.restaurantItemEntities = restaurantItemEntities;
    }

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.ALL)
    private Set<RestaurantCategoryEntity> restaurantCategoryEntities = new HashSet<>();

    public Set<RestaurantCategoryEntity> getRestaurantCategoryEntities() {
        return restaurantCategoryEntities;
    }

    public void setRestaurantCategoryEntities(Set<RestaurantCategoryEntity> restaurantCategoryEntities) {
        this.restaurantCategoryEntities = restaurantCategoryEntities;
    }

    public double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(double customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(Integer numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }
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


    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
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

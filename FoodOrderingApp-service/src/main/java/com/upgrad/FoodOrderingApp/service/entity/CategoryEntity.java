package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="category")
@NamedQueries(
        {
                @NamedQuery(name = "byCategoryUuid", query = "select s from CategoryEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "getAllCategories", query = "select s from CategoryEntity s")
        }
)
public class CategoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    private String uuid;

    @Column(name ="category_name")
    @Size(max=255)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<CategoryItemEntity> categoryItemEntities = new HashSet<>();

    public Set<CategoryItemEntity> getCategoryItemEntities() {
        return categoryItemEntities;
    }

    public void setCategoryItemEntities(Set<CategoryItemEntity> categoryItemEntities) {
        this.categoryItemEntities = categoryItemEntities;
    }

    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL)
    private Set<RestaurantCategoryEntity> restaurantCategoryEntities = new HashSet<>();

    public Set<RestaurantCategoryEntity> getRestaurantCategoryEntities() {
        return restaurantCategoryEntities;
    }

    public void setRestaurantCategoryEntities(Set<RestaurantCategoryEntity> restaurantCategoryEntities) {
        this.restaurantCategoryEntities = restaurantCategoryEntities;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

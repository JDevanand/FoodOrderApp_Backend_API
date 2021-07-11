package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="category_item")

@NamedQueries(
        {
                @NamedQuery(name = "byCategoryId", query = "select s from CategoryItemEntity s where s.category = :category"),
                @NamedQuery(name = "getAllCategoryItem", query = "select s from CategoryItemEntity s")
        }
)

public class CategoryItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name="item_id")
    private ItemEntity item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }


/*
     public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryItemEntity)) return false;
        CategoryItemEntity that = (CategoryItemEntity) o;
        return Objects.equals(category.getId(), that.category.getId()) &&
                Objects.equals(item.getId(), that.item.getId());
    }

    @Override
    public int hashCode() {
        //return new HashCodeBuilder().append(this).hashCode();
        return Objects.hash();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
*/

}

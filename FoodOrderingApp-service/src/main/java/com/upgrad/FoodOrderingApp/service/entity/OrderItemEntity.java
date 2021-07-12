package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="order_item")

@NamedQueries(
        {
                @NamedQuery(name="getAllbyOrderId", query = "select p from OrderItemEntity p where p.order =:order")
        }
)
public class OrderItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name="price")
    private Integer price;

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name="order_id")
    private OrderEntity order;

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}

package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="order_item")

@NamedQueries(
        {
                //@NamedQuery(name="getOrderItemByOrderId", query = "select com.upgrad.FoodOrderingApp.service.entity.CustomItemCount(p.item,SUM(p.quantity)) from OrderItemEntity p where p.order =:order group by p.item order by p.quantity DESC")
                @NamedQuery(name="getorderitembyOrder", query="select oie.item from OrderItemEntity oie where oie.order IN :orderentitylist group by oie.item.id order by sum(oie.quantity) desc")
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
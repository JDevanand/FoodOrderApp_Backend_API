package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order")
@NamedQueries(
        {
                @NamedQuery(name="getOrderByCustomerId", query = "select o from OrderEntity o where o.customer=:customer")
        }
)
public class OrderEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    @NotNull
    private String uuid;

    @Column(name="BILL")
    @NotNull
    private Number bill;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @Column(name="DISCOUNT")
    private Number discount;

    @Column(name = "date")
    @NotNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @NotNull
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItemEntity> orderItemEntities = new HashSet<>();

    public Set<OrderItemEntity> getOrderItemEntities() {
        return orderItemEntities;
    }

    public void setOrderItemEntities(Set<OrderItemEntity> orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
    }

    public  OrderEntity(){

    }

    public OrderEntity(String orderId, double v, CouponEntity couponEntity, double v1, Date orderDate, PaymentEntity paymentEntity, CustomerEntity customerEntity, AddressEntity addressEntity, RestaurantEntity restaurantEntity) {
            this.uuid = orderId;
            this.coupon = couponEntity;
            this.date = orderDate;
            this.payment = paymentEntity;
            this.address = addressEntity;
            this.customer = customerEntity;
            this.restaurant = restaurantEntity;
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

    public Number getBill() {
        return bill;
    }

    public void setBill(Number bill) {
        this.bill = bill;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public Number getDiscount() {
        return discount;
    }

    public void setDiscount(Number discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }
}

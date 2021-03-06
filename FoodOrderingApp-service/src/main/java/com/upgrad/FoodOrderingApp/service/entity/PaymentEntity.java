package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="payment")
@NamedQueries(
        {
                @NamedQuery(name="getpaymentmode", query = "select p from PaymentEntity p"),
                @NamedQuery(name="getpaymentbyUuid", query = "select p from PaymentEntity p where p.uuid =:uuid")
        }
)

public class PaymentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    private String uuid;

    @Column(name ="payment_name")
    @Size(max=255)
    private String paymentName;

    public PaymentEntity(){

    }

    public PaymentEntity(String paymentId, String spmePayment) {
        this.uuid = paymentId;
        this.paymentName = spmePayment;
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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
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

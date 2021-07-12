package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="customer")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from CustomerEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByContactNumber", query = "select u from CustomerEntity u where u.contactNumber =:contactNumber"),

        }
)

public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @Size(max = 30)
    @NotNull
    private String firstName;

    @Column(name = "LASTNAME")
    @Size(max = 30)
    private String lastName;

    @Column(name = "EMAIL")
    @Size(max = 50)
    @NotNull
    private String email;

    @Column(name = "CONTACT_NUMBER")
    @Size(max = 30)
    @NotNull
    private String contactNumber;

    @Column(name = "PASSWORD")
    @Size(max = 255)
    @NotNull
    private String password;

    @Column(name = "SALT")
    @Size(max = 200)
    private String salt;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private Set<CustomerAddressEntity> customerAddressEntities = new HashSet<>();

    public Set<CustomerAddressEntity> getCustomerAddressEntities() {
        return customerAddressEntities;
    }

    public void setCustomerAddressEntities(Set<CustomerAddressEntity> customerAddressEntities) {
        this.customerAddressEntities = customerAddressEntities;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
/*
    public List<AddressEntity> getAddressEntities() {
        return addressEntities;
    }

    public void setAddressEntities(List<AddressEntity> addressEntities) {
        this.addressEntities = addressEntities;
    }
*/
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

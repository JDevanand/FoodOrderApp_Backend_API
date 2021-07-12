package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="address")
@NamedQueries(
        {
                @NamedQuery(name="getAllAddresses", query = "select q from AddressEntity q"),
                @NamedQuery(name="getAddressbyUuid", query = "select q from AddressEntity q where q.uuid =:uuid")
        }
)

public class AddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="UUID")
    @Size(max=200)
    private String uuid;

    @Column(name ="flat_buil_number")
    @Size(max=255)
    private String flatBuildingNumber;

    @Column(name ="locality")
    @Size(max=255)
    private String locality;

    @Column(name ="city")
    @Size(max=30)
    private String city;

    @Column(name ="pincode")
    @Size(max=30)
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StateEntity state;

    @Column(name ="active")
    private int active;

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


    public String getFlatBuildingNumber() {
        return flatBuildingNumber;
    }

    public void setFlatBuildingNumber(String flatBuildingNumber) {
        this.flatBuildingNumber = flatBuildingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    /*
    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
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

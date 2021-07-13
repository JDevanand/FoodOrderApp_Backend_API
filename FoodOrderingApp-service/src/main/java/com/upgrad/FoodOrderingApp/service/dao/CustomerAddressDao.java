package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerAddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get list of addressed saved by a customer
    public List<CustomerAddressEntity> getAddressOfCustomer(CustomerEntity customerEntity) {
        try{
            return entityManager.createNamedQuery("getByCustomer",CustomerAddressEntity.class).setParameter("customer",customerEntity)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    //Save address and customer whenever a customer creates a new address
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddress (CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }
}
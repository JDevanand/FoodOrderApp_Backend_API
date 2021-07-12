package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Create User
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity createUser(CustomerEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    //Get user by contact number
    public CustomerEntity getUserByContactNumber(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("userByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Get customer by uuid
    public CustomerEntity getUserByUuid(final String customerUuid){
        try{
            return entityManager.createNamedQuery("userByUuid", CustomerEntity.class).setParameter("uuid", customerUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }

    }

    //update Customer
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity){
        try{
            entityManager.merge(customerEntity);
            return customerEntity;
        } catch (NoResultException nre){
            return null;
        }
    }

}

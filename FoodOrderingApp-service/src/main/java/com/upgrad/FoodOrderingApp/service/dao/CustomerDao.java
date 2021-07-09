package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    public CustomerEntity userByContactNumber(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("userByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}

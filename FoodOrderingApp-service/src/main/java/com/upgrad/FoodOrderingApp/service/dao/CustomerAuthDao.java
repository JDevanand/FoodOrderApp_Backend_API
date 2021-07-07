package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get access token entity using access token string provided by user
    public CustomerAuthEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",
                    CustomerAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    //Create access token when signing in
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity createAuthToken(final CustomerAuthEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    //User sign out update
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity userSignOut(final CustomerAuthEntity userAuthTokenEntity){
        entityManager.merge(userAuthTokenEntity);
        return  userAuthTokenEntity;
    }
}

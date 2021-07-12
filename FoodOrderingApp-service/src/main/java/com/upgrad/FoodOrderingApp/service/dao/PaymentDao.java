package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get all payment modes
    public List<PaymentEntity> getPaymentModes() {
        try {
            return entityManager.createNamedQuery("getpaymentmode", PaymentEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Fetch payment by Uuid
    public PaymentEntity getPaymentByUuid(final String paymentUuid){
        try {
            return entityManager.createNamedQuery("getpaymentbyUuid", PaymentEntity.class).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}

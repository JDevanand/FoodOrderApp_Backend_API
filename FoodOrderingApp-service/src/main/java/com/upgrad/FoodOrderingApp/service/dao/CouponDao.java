package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get coupon by coupon name
    public CouponEntity getCouponByName(final String couponName) {
        try {
            return entityManager.createNamedQuery("byCouponName", CouponEntity.class).setParameter("couponName", couponName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}

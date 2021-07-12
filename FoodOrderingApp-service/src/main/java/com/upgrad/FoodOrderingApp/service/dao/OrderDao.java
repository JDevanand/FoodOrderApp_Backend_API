package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Save order
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity newOrder){
        entityManager.persist(newOrder);
        return newOrder;
    }


    public List<OrderEntity> getCustomerOrders(CustomerEntity customer){
        try {
            return entityManager.createNamedQuery("getOrderByCustomerId", OrderEntity.class).setParameter("customer", customer)
                    .getResultList();
        } catch (NoResultException nre){
            return null;
        }

    }

}

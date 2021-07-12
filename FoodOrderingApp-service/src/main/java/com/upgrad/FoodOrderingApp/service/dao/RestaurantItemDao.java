package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantItemEntity> getRestaurantItemsById(final String restaurantUuid){
        return entityManager.createNamedQuery("byRestaurantId",RestaurantItemEntity.class).setParameter("restaurant",restaurantUuid)
                .getResultList();
    }
}

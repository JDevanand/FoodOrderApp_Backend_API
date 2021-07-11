package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Fetch all restaurant
    public List<RestaurantEntity> getAllRestaurants(){
        //establish many to many things
        return entityManager.createNamedQuery("getAllRestaurant",RestaurantEntity.class).getResultList();
    }

    //Fetch restaurant by UUID
    public RestaurantEntity getRestaurantByUuid(final String restaurantUuid){
        try {
            return entityManager.createNamedQuery("byRestaurantUuid", RestaurantEntity.class).setParameter("uuid", restaurantUuid)
                    .getSingleResult();
        } catch (NoResultException nre){
            return  null;
        }
    }

    //Merge restaurant
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurant(RestaurantEntity updatedEntity){
        entityManager.merge(updatedEntity);
        return  updatedEntity;
    }

}

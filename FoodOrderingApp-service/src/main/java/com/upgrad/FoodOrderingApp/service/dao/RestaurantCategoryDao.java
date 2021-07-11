package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategoryEntity> getRestaurantandCategory(RestaurantEntity restaurantEntity){
        try {
            return entityManager.createNamedQuery("byRestaurant", RestaurantCategoryEntity.class).setParameter("restaurantEntity", restaurantEntity)
                    .getResultList();
        } catch (NoResultException nre){
            return null;
        }
    }


    public List<RestaurantCategoryEntity> getRCByCategory(CategoryEntity categoryEntity){
        try {
            return entityManager.createNamedQuery("byCategory", RestaurantCategoryEntity.class).setParameter("categoryEntity", categoryEntity)
                    .getResultList();
        } catch (NoResultException nre){
            return null;
        }
    }

}

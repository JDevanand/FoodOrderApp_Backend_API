package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get Item by Item uuid
    public ItemEntity getItembyUuid(final String itemUuid){
        try{
            return entityManager.createNamedQuery("getItembyUuid",ItemEntity.class).setParameter("uuid",itemUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    //Get Item by restaurant and category
    //<<<<<<>>>>>>>//
    public List<ItemEntity> getItemsByCategoryAndRestaurant(final RestaurantEntity restaurant, final CategoryEntity category){
        try{
            Query query = entityManager.createQuery("");
            return  query.getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}

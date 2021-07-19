package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

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
    public List<ItemEntity>  getItemsByCategoryAndRestaurant(final RestaurantEntity restaurant, final CategoryEntity category){
        try{
            Query query = entityManager.createQuery("select itm from ItemEntity itm join fetch RestaurantItemEntity ritm on ritm.itemEntity = itm join fetch CategoryItemEntity citm on citm.item = itm  where ritm.restaurantEntity=:restaurant and citm.category=:category");
            query.setParameter("restaurant",restaurant);
            query.setParameter("category", category);
            return  query.getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}

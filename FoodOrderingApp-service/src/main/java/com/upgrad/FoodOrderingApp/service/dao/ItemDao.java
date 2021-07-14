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
            Query query = entityManager.createQuery("select itm from ItemEntity itm inner join RestaurantItemEntity ritm on ritm.itemEntity = itm inner join CategoryItemEntity citm on citm.item = itm where ritm=:restaurant and citm=:category");
            query.setParameter("restaurant",restaurant);
            query.setParameter("category", category);
            return  query.getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }


    public List<ItemEntity> getTop5Item(final RestaurantEntity restaurant){
        try{

            //SELECT t FROM Track t where t.Trackid IN (SELECT pt.Tracks_trackid FROM Playlist_Track pt WHERE pt.Playlist_playlistid = :WhicheverIdWasEntered)
            Query query = entityManager.createQuery("select itm from ItemEntity itm where itm.id IN (select itm from OrderItemEntity oie where oie.id IN (select id from Orders order where RestaurantItemEntity ritm=:restaurant))");
            //query = entityManager.createQuery("select item_id from OrderItemEntity oie where order_id=3");
            query.setParameter("restaurant",restaurant);
            return  query.getResultList();
        } catch (NoResultException nre){
            return  null;
        }
    }



}

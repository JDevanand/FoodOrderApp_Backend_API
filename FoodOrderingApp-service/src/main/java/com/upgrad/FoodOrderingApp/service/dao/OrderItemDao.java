package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomItemCount;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Save order & items
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(final OrderItemEntity orderAndItems){
        entityManager.persist(orderAndItems);
        return orderAndItems;
    }

    //
    public List<ItemEntity> getOrderItemByOrderId(final List<OrderEntity> orderEntityList){
        try {
            //return entityManager.createNamedQuery("getOrderItemByOrderId", OrderItemEntity.class).setParameter("order", orderEntity)
                    //.getResultList();

            /*
            Query query = entityManager.createQuery("select com.upgrad.FoodOrderingApp.service.entity.CustomItemCount(p.item,SUM(p.quantity)) from OrderItemEntity p where p.order =:order group by p.item order by quantity DESC");
            query.setParameter("order",orderEntity);
            query.setMaxResults(5);
            return query.getResultList();
            */
            /*
            Query query = entityManager.createNativeQuery("select com.upgrad.FoodOrderingApp.service.entity.CustomItemCount(p.item,SUM(p.quantity) as quantity) from OrderItemEntity as p where p.order =:order group by p.item order by quantity DESC");
            query.setParameter("order",orderEntity);
            query.setMaxResults(5);
            return query.getResultList();

             */
            //List<ItemEntity> orderEntities = entityManager.createNamedQuery("getorderitembyOrder", ItemEntity.class).setParameter("orderentitylist",orderEntityList).setMaxResults(5).getResultList();

            Query query1= entityManager.createQuery("select oie.item.id from OrderItemEntity oie where oie.order IN :orderEntites group by oie.item.id order by sum(oie.quantity) desc");

            //Query query = entityManager.createQuery("select itm from ItemEntity itm join fetch RestaurantItemEntity ritm on ritm.itemEntity = itm join fetch CategoryItemEntity citm on citm.item = itm  where ritm.restaurantEntity=:restaurant and citm.category=:category");
            query1.setParameter("orderEntites",orderEntityList);
            query1.setMaxResults(5);
            return  query1.getResultList();

        } catch (NoResultException nre){
            return null;
        }
    }

}

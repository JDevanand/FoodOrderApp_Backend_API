package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get
    public List<CategoryItemEntity> getCatItemById(CategoryEntity category){
        return entityManager.createNamedQuery("byCategoryId",CategoryItemEntity.class).setParameter("category",category)
                .getResultList();
    }
}

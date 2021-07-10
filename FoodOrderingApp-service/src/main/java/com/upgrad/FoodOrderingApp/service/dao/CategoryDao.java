package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get all categories
    public List<CategoryEntity> getAllCategory(){
        return entityManager.createNamedQuery("getAllCategories",CategoryEntity.class).getResultList();
    }

    //Get category by UUID
    public CategoryEntity getCategoryByUuid(final String categoryUuid){
        try{
            return entityManager.createNamedQuery("byCategoryUuid",CategoryEntity.class).setParameter("uuid",categoryUuid)
                    .getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

}

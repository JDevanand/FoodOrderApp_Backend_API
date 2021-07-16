package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get state by state uuid
    public StateEntity getStateByUuid(final String stateUuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class).setParameter("uuid", stateUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Get all state
    public List<StateEntity> getAllStates(){
        try {
            return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

}

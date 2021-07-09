package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Create Address
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity createAddress(AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    //Get address by uuid
    public AddressEntity getAddressByUuid(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("getAddressbyUuid", AddressEntity.class).setParameter("uuid", addressUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Get all addresses
    public List<AddressEntity> getAllAddresses() {
        return entityManager.createNamedQuery("getAllAddresses",AddressEntity.class).getResultList();
    }

    //Delete an address
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        try {
            entityManager.merge(addressEntity);
            return addressEntity;
        } catch (NoResultException nre){
            return null;
        }
    }

}

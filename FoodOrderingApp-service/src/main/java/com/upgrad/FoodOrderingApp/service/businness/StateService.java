package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateDao stateDao;

    //Get state by uuid
    public StateEntity getStateByUuid(String stateUuid) throws AddressNotFoundException {

        StateEntity receivedStateEntity = stateDao.getStateByUuid(stateUuid);
        if(receivedStateEntity ==null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }

        return receivedStateEntity;
    }

    //Get all states (authentication not required)
    public List<StateEntity> getAllStates (){
        return stateDao.getAllStates();
    }
}

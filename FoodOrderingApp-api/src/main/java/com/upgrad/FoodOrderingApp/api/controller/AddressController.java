package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    //Save new address
    @CrossOrigin
    @RequestMapping(path ="/address",method= RequestMethod.POST, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {

        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        AddressEntity receivedAddress = new AddressEntity();
        receivedAddress.setUuid(UUID.randomUUID().toString());
        receivedAddress.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        receivedAddress.setLocality(saveAddressRequest.getLocality());
        receivedAddress.setCity(saveAddressRequest.getCity());
        receivedAddress.setPincode(saveAddressRequest.getPincode());
        receivedAddress.setActive(1);

        StateEntity state = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        receivedAddress.setState(state);

        AddressEntity savedAddress = addressService.saveAddress(loggedCustomer, receivedAddress);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse();
        saveAddressResponse.setId(savedAddress.getUuid());
        saveAddressResponse.setStatus("ADDRESS SUCCESSFULLY REGISTERED");

        return  new ResponseEntity<>(saveAddressResponse, HttpStatus.CREATED);
    }

    //Get all saved address
    @CrossOrigin
    @RequestMapping(path="/address/customer",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        List<AddressEntity> addressEntityList = addressService.getAllAddress(loggedCustomer);

        AddressListResponse addressListResponse = new AddressListResponse();
        if(addressEntityList==null){
            addressListResponse = null;
            return new ResponseEntity<>(addressListResponse, HttpStatus.OK);
        }

        List<AddressList> addressLists = new ArrayList<>();

        for (AddressEntity addressEntity : addressEntityList) {
            AddressList adr = new AddressList();
            adr.setId(UUID.fromString(addressEntity.getUuid()));
            adr.setFlatBuildingName(addressEntity.getFlatBuilNo());
            adr.setLocality(addressEntity.getLocality());
            adr.setCity(addressEntity.getCity());
            adr.setPincode(addressEntity.getPincode());

            AddressListState statesList = new AddressListState();
            String stateUuid = addressEntity.getState().getUuid();
            statesList.setId(UUID.fromString(stateUuid));
            statesList.setStateName(addressEntity.getState().getStateName());
            adr.setState(statesList);
            addressLists.add(adr);
        }

        return new ResponseEntity<>(addressListResponse.addresses(addressLists), HttpStatus.OK);

    }

    //Delete address endpoint
    @CrossOrigin
    @RequestMapping(path="/address/{address_id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader("authorization") final String authorization, @PathVariable("address_id") final String addressUuid) throws AuthorizationFailedException, AddressNotFoundException {

        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        AddressEntity addressToBeDeleted = addressService.getAddressByUUID(addressUuid, loggedCustomer);

        AddressEntity deletedAddress = addressService.deleteAddress(addressToBeDeleted);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse();
        deleteAddressResponse.setId(UUID.fromString(deletedAddress.getUuid()));
        deleteAddressResponse.setStatus("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<>(deleteAddressResponse, HttpStatus.OK);
    }

    //Get all states
    @CrossOrigin
    @RequestMapping(path="/states",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates(){

        List<StateEntity> stateEntityList = addressService.getAllStates();
        StatesListResponse statesListResponse = new StatesListResponse();

        if(stateEntityList==null){
            statesListResponse.setStates(null);
            return new ResponseEntity<>(statesListResponse, HttpStatus.OK);
        }

        List<StatesList> statesLists = new ArrayList<>();

        if(stateEntityList==null){
            statesLists = null;
        } else {
            for (StateEntity stateEntity : stateEntityList) {
                StatesList states = new StatesList();
                states.setId(UUID.fromString(stateEntity.getUuid()));
                states.setStateName(stateEntity.getStateName());
                statesLists.add(states);
            }
        }


        return new ResponseEntity<>(statesListResponse.states(statesLists), HttpStatus.OK);
    }

}

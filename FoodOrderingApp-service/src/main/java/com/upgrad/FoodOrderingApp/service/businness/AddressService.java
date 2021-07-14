package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.ZonedDateTime.now;

@Service
public class AddressService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Autowired
    private StateDao stateDao;

    //Get an address by UUid
    public AddressEntity getAddressByUUID(final String addressUuid, final CustomerEntity customerEntity) throws AddressNotFoundException, AuthorizationFailedException {

        if(addressUuid.isEmpty()){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        AddressEntity foundAddress = addressDao.getAddressByUuid(addressUuid);
        if(foundAddress==null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        //Check if the addressed provided by user belongs to user
        List<CustomerAddressEntity> fetchedAddressList = customerAddressDao.getAddressOfCustomer(customerEntity);
        for(CustomerAddressEntity cad : fetchedAddressList){
            if(cad.getAddress().getUuid().equals(foundAddress.getUuid())){
                return foundAddress;
            }else{
                throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
            }
        }

        return foundAddress;
    }

    //Save address service
    public AddressEntity saveAddress(String authorization, AddressEntity addressDetails) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        CustomerEntity loggedCustomerEntity = customerService.getCustomer(authorization);

           //Check if any field is empty
            if(addressDetails.getPincode().isEmpty() || addressDetails.getCity().isEmpty()
            || addressDetails.getLocality().isEmpty()|| addressDetails.getFlatBuilNo().isEmpty()
            ||addressDetails.getState().getUuid().isEmpty()){
                throw new SaveAddressException("SAR-001","No field can be empty");
            }

            //Pincode format checker
            if(!pincodeFormatChecker(addressDetails.getPincode())){
                throw new SaveAddressException("SAR-002","Invalid pincode");
            }

            //fetch statedetail from state entity based on State uuid
            //This throws exception if state uuid is not in db
            addressDetails.setState(getStateByUUID(addressDetails.getState().getUuid()));

            //Save address details in address db
            AddressEntity createdAddress = addressDao.createAddress(addressDetails);


            //Save address and customer relation in customer_address db
            CustomerAddressEntity createCAEntity = new CustomerAddressEntity();
            createCAEntity.setCustomer(loggedCustomerEntity);
            createCAEntity.setAddress(createdAddress);
            customerAddressDao.saveCustomerAddress(createCAEntity);

            return createdAddress;
    }

    //Get all address of a customer
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) throws AuthorizationFailedException {

            List<CustomerAddressEntity> fetchedCAEntity = customerAddressDao.getAddressOfCustomer(customerEntity);
            List<AddressEntity> addressEntities = new ArrayList<>();

            for(CustomerAddressEntity cae : fetchedCAEntity){

                addressEntities.add(cae.getAddress());
            }

            Collections.sort(addressEntities,AddressService.AddressIdComparator);
            return addressEntities;

    }

    //Delete an address of signed in user only
    public AddressEntity deleteAddress(AddressEntity addressEntity) throws AuthorizationFailedException, AddressNotFoundException {

            addressEntity.setActive(0);
            return addressDao.deleteAddress(addressEntity);

    }

    //Get state by uuid
    public StateEntity getStateByUUID(String stateUuid) throws AddressNotFoundException {

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

    //contact number format checker
    public static boolean pincodeFormatChecker(String pincode){

        try {
            double d = Double.parseDouble(pincode);
        } catch (NumberFormatException nfe) {
            return false;
        }

        if(pincode.length()!=6){
            return false;
        }
        return true;
    }

    //Comparator to sort category
    public static Comparator<AddressEntity> AddressIdComparator = (s1, s2) -> {
        Integer address1 = s1.getId();
        Integer address2 = s2.getId();

        // descending order of address
        return address2-address1;
    };
}

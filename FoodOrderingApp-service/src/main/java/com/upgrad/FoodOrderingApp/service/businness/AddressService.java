package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.ZonedDateTime.now;

@Service
public class AddressService {

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private StateService stateService;

    //Get an address by UUid
    public AddressEntity getAddressByUUID(final String addressUuid) throws AddressNotFoundException {

        AddressEntity foundAddress = addressDao.getAddressByUuid(addressUuid);
        if(foundAddress==null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        return foundAddress;
    }

    //Save address service
    public AddressEntity saveAddress(String accessToken, AddressEntity addressDetails) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }

        if(loggedUserAuthTokenEntity.getLogoutAt() !=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }

        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {

           //Check if any field is empty
            if(addressDetails.getPincode()==null || addressDetails.getCity()==null
            || addressDetails.getLocality()==null || addressDetails.getFlatBuildingNumber()==null
            ||addressDetails.getState()==null){
                throw new SaveAddressException("SAR-001","No field can be empty");
            }

            //Pincode format checker
            if(!pincodeFormatChecker(addressDetails.getPincode())){
                throw new SaveAddressException("SAR-002","Invalid pincode");
            }

            //fetch statedetail from state entity based on State uuid
            //This throws exception if state uuid is not in db
            addressDetails.setState(stateService.getStateByUuid(addressDetails.getState().getUuid()));

            return addressDao.createAddress(addressDetails);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

    }

    //Get all address of a customer
    public List<AddressEntity> getAllAddresses(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(loggedUserAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {
            List<AddressEntity> addressEntities = addressDao.getAllAddresses();
            Collections.sort(addressEntities,AddressService.AddressIdComparator);
            return addressEntities;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
    }

    //Delete an address of signed in user only
    public AddressEntity deleteAddress(String accessToken, String addressUuid) throws AuthorizationFailedException, AddressNotFoundException {

        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }

        if(loggedUserAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }

        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {

            if (addressUuid == null) {
                throw new AddressNotFoundException("ANF-005", "Address id cannot be empty");
            }

            AddressEntity foundAddress = addressDao.getAddressByUuid(addressUuid);
            if (foundAddress == null) {
                throw new AddressNotFoundException("ANF-003", "No address by this id");
            }


            /*
            List<AddressEntity> addressList =new ArrayList<>;
            for(AddressEntity address: foundAddress.getCustomerEntities()
              if(loggedUserAuthTokenEntity.getCustomer().getUuid() == foundAddress){

                foundAddress.setActive(0);
                AddressEntity inactiveAddress = addressDao.deleteAddress(foundAddress);

                return inactiveAddress;
            }
            */
            return new AddressEntity(); //this needs to be deleted
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

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

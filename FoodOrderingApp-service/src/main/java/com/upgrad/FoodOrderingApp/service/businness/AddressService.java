package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZonedDateTime.now;

@Service
public class AddressService {

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private AddressDao addressDao;

    //Save address service
    public AddressEntity saveAddress(String accessToken, AddressEntity addressDetails) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }

        if(loggedUserAuthTokenEntity.getLogoutAt() !=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }

        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {
           //SaveAddressException chek to be performed here & exception

            //invallid pincode chek to be performed here & exception

            //state uuid chek to be performed here & exception


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
            return addressDao.getAllAddresses();
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

}

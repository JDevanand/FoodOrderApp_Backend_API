package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Autowired
    private AuthenticationService authenticationService;

    //Create a new user
    //Throws exception if user name or email id is already registered.
    public CustomerEntity saveCustomer(CustomerEntity userEntity) throws SignUpRestrictedException {

        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        /*
        //If the contact number provided by the user already exists in the current database,
        // throw ‘SignUpRestrictedException’ with the
        // message code -'SGR-001' and message -'This contact number is already registered! Try other contact number.'.
        if(customerDao.userByContactNumber(userEntity.getContactNumber())!=null){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        }
        */

        return customerDao.createUser(userEntity);
    }

    //User sign in after Basic authentication
    public CustomerAuthEntity userSignIn(final String authorization) throws AuthenticationFailedException {

        return authenticationService.authenticate(authorization);
    }

    //User sign out after bearer authentication using access token
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }

        if(loggedUserAuthTokenEntity.getLogoutAt() !=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }


        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {
            loggedUserAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
            return customerAuthDao.userSignOut(loggedUserAuthTokenEntity);
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

    }

}

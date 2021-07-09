package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private CustomerDao userDao;

    @Autowired
    private CustomerAuthDao userAuthDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    //Authenticate user based on the basic authorization provided by user
    //and return AccessToken if authorization is successful
    //if not successful, throw AuthenticationFailedException.
    public CustomerAuthEntity authenticate(final String authorization) throws AuthenticationFailedException {

        //Basic dXNlcjFAZW1haWwuY29tOnVzZXIxIQ==
        //above is a sample encoded text where the username is "username" and
        // password is "password" seperated by a ":"
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        String userName= decodedArray[0]; //username is user contact_number
        String password = decodedArray[1];

        CustomerEntity userEntity = userDao.userByContactNumber(userName);//username is user contact_number

        if(userEntity==null){
            throw new AuthenticationFailedException("ATH-001","This contact number has not been registered!");
        }
        //encrypt the user provided password
        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        //check the user provided pwd with user-entity pwd from db
        if(encryptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity userAuthToken = new CustomerAuthEntity();
            userAuthToken.setCustomer(userEntity);
            userAuthToken.setUuid((UUID.randomUUID().toString()));

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);

            userAuthDao.createAuthToken(userAuthToken);

            return userAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }
}

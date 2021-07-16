package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    //Create a new user
    //Throws exception if user name or email id is already registered.
    public CustomerEntity saveCustomer(CustomerEntity userEntity) throws SignUpRestrictedException {


        if(userEntity.getFirstName().isEmpty() || userEntity.getContactNumber().isEmpty() ||userEntity.getPassword().isEmpty() || userEntity.getEmail().isEmpty()){
            throw new SignUpRestrictedException("SGR-005","Except lastname all fields should be filled");
        }

        if(!passwordChecker(userEntity.getPassword())){
            throw new SignUpRestrictedException("SGR-004","Weak password!");
        }
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        if(!contactNumberFormatChecker(userEntity.getContactNumber())){
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }

        if(customerDao.getUserByContactNumber(userEntity.getContactNumber())!=null){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        }
        if(!isValidEmailFormat(userEntity.getEmail())){
            throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        }

        return customerDao.createUser(userEntity);
    }

    //Authenticate user
    public CustomerAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {

        CustomerEntity loggedCustomer = customerDao.getUserByContactNumber(username);

        if(loggedCustomer ==null){
            throw new AuthenticationFailedException("ATHR-001","This contact number has not been registered!");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, loggedCustomer.getSalt());
        if(encryptedPassword.equals(loggedCustomer.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity userAuthToken = new CustomerAuthEntity();
            userAuthToken.setCustomer(loggedCustomer);
            userAuthToken.setUuid((UUID.randomUUID().toString()));

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(loggedCustomer.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);

            customerAuthDao.createAuthToken(userAuthToken);
            return userAuthToken;
        }else{
            throw new AuthenticationFailedException("ATH-002","Invalid Credentials");
        }

    }

    //User sign out after bearer authentication using access token
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUserAuthTokenEntity = bearerAuthentication(accessToken);
        loggedUserAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
        return customerAuthDao.userSignOut(loggedUserAuthTokenEntity);

    }

    //Get customer entity based on signed-in user
    public CustomerEntity getCustomer(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUserAuthTokenEntity = bearerAuthentication(accessToken);
        return loggedUserAuthTokenEntity.getCustomer();
    }

    //Customer name update
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) throws UpdateCustomerException {

            if(customerEntity.getFirstName().isEmpty()){
                throw new UpdateCustomerException("UCR-002","First name field should not be empty");
            }

            return customerDao.updateCustomer(customerEntity);

    }

    //Customer password update
    public CustomerEntity updateCustomerPassword(final String oldPassword,final String newPassword, final CustomerEntity customerEntity) throws AuthorizationFailedException, UpdateCustomerException {

            //Check password strength
            if(!passwordChecker(newPassword)){
                throw new UpdateCustomerException("UCR-001","Weak password!");
            }

            //Check old password matches
            final String encryptedPassword = cryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
            if(!encryptedPassword.equals(customerEntity.getPassword())){
                throw new UpdateCustomerException("UCR-004","Incorrect old password!");
            }

            //update new password
            String[] encryptedText = cryptographyProvider.encrypt(newPassword);
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);

            return customerDao.updateCustomer(customerEntity);
    }

    public CustomerAuthEntity bearerAuthentication(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }

        if(loggedUserAuthTokenEntity.getLogoutAt() !=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }

        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) < 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        return loggedUserAuthTokenEntity;
    }

    //Password Strength checker
    public static boolean passwordChecker(String input)    {

        int n = input.length();
        boolean hasUpper = false,
                hasDigit = false, specialChar = false;

        Set<Character> set = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*'));
        for (char i : input.toCharArray())
        {
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                specialChar = true;
        }

        if (hasDigit && hasUpper && specialChar && (n >= 8))
            return true;
        else
            return false;
    }

    //contact number format checker
    public static boolean contactNumberFormatChecker(String contactNumber){

        try {
            double d = Double.parseDouble(contactNumber);
        } catch (NumberFormatException nfe) {
            return false;
        }

        if(contactNumber.length()!=10){
            return false;
        }
        return true;
    }

    //Email format checker
    public static boolean isValidEmailFormat(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

}

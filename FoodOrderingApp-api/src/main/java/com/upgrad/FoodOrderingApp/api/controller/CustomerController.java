package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Signup user endpoint
    @CrossOrigin
    @RequestMapping(path = "/customer/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        final CustomerEntity userEntity = new CustomerEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupCustomerRequest.getFirstName());
        userEntity.setLastName(signupCustomerRequest.getLastName());
        userEntity.setEmail(signupCustomerRequest.getEmailAddress());
        userEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        userEntity.setPassword(signupCustomerRequest.getPassword());

        final CustomerEntity createdUserEntity = customerService.saveCustomer(userEntity);


        SignupCustomerResponse signupUserResponse = new SignupCustomerResponse();
        signupUserResponse.setId(createdUserEntity.getUuid());
        signupUserResponse.setStatus("CUSTOMER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(signupUserResponse, HttpStatus.CREATED);
    }

    //Customer SIGN IN endpoint
    @CrossOrigin
    @RequestMapping(path = "/customer/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        String userName;
        String password;
        try {
            byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);
            String[] decodedArray = decodedText.split(":");

            userName = decodedArray[0]; //username is user contact_number
            password = decodedArray[1];
        } catch(Exception e){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }

        CustomerAuthEntity userAuthEntity = customerService.authenticate(userName,password);

        LoginResponse signinResponse = new LoginResponse();
        signinResponse.setId(userAuthEntity.getCustomer().getUuid());
        signinResponse.setMessage("SIGNED IN SUCCESSFULLY");
        signinResponse.setContactNumber(userAuthEntity.getCustomer().getContactNumber());
        signinResponse.setFirstName(userAuthEntity.getCustomer().getFirstName());
        signinResponse.setLastName(userAuthEntity.getCustomer().getLastName());
        signinResponse.setEmailAddress(userAuthEntity.getCustomer().getEmail());

        //Access token to be sent in header which will be used in request from frontend
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token",userAuthEntity.getAccessToken());

        return new ResponseEntity<>(signinResponse,headers,HttpStatus.OK);
    }

    //Customer sign out endpoint
    @CrossOrigin
    @RequestMapping(path = "/customer/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String accessToken = new String();
        String[] splitString = authorization.split(" ");
        accessToken = splitString[1];

        CustomerAuthEntity loggedUsersAuthToken = customerService.logout(accessToken);

        LogoutResponse signoutResponse = new LogoutResponse();
        signoutResponse.setId(loggedUsersAuthToken.getCustomer().getUuid());
        signoutResponse.setMessage("LOGGED OUT SUCCESSFULLY");

        return  new ResponseEntity<>(signoutResponse,HttpStatus.OK);
    }

    //Update customer endpoint
    @CrossOrigin
    @RequestMapping(path="/customer",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String authorization, final UpdateCustomerRequest updateCustomerRequest) throws AuthorizationFailedException, UpdateCustomerException {

       CustomerEntity tobeUpdatedCustomer = new CustomerEntity();
        tobeUpdatedCustomer.setFirstName(updateCustomerRequest.getFirstName());
        tobeUpdatedCustomer.setLastName(updateCustomerRequest.getLastName());

        String accessToken = new String();
        String[] splitString = authorization.split(" ");
        accessToken = splitString[1];
        CustomerEntity updatedCustomer = customerService.updateCustomer(accessToken,tobeUpdatedCustomer);

        UpdateCustomerResponse updatedCustomerResponse = new UpdateCustomerResponse();
        updatedCustomerResponse.setId(updatedCustomer.getUuid());
        updatedCustomerResponse.setStatus("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        updatedCustomerResponse.setFirstName(updatedCustomer.getFirstName());
        updatedCustomerResponse.setLastName(updatedCustomer.getLastName());

        return new ResponseEntity<>(updatedCustomerResponse,HttpStatus.OK);
    }

    //Update customer password
    @CrossOrigin
    @RequestMapping(path="/customer/password",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse > updateCustomerPassword(@RequestHeader("authorization") final String authorization, final UpdatePasswordRequest updatePasswordRequest) throws AuthorizationFailedException, UpdateCustomerException {


        String accessToken = new String();
        String[] splitString = authorization.split(" ");
        accessToken = splitString[1];
        CustomerEntity ce = customerService.updateCustomerPassword(accessToken,updatePasswordRequest.getOldPassword(),updatePasswordRequest.getNewPassword());

        UpdatePasswordResponse upr = new UpdatePasswordResponse();
        upr.setId(ce.getUuid());
        upr.setStatus("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

        return new ResponseEntity<>(upr,HttpStatus.OK);
    }
}

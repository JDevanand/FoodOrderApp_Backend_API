package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Signup user endpoint
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
    @RequestMapping(path = "/customer/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        CustomerAuthEntity userAuthEntity = customerService.userSignIn(authorization);

        LoginResponse signinResponse = new LoginResponse();
        signinResponse.setId(userAuthEntity.getUser().getUuid());
        signinResponse.setMessage("SIGNED IN SUCCESSFULLY");


        //Access token to be sent in header which will be used in request from client
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token",userAuthEntity.getAccessToken());

        return new ResponseEntity<>(signinResponse,headers,HttpStatus.OK);
    }

    //Customer sign out endpoint
    @RequestMapping(path = "/customer/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity loggedUsersAuthToken = customerService.userSignOut(accessToken);

        LogoutResponse signoutResponse = new LogoutResponse();
        signoutResponse.setId(loggedUsersAuthToken.getUser().getUuid());
        signoutResponse.setMessage("LOGGED OUT SUCCESSFULLY");

        return  new ResponseEntity<>(signoutResponse,HttpStatus.OK);
    }
}

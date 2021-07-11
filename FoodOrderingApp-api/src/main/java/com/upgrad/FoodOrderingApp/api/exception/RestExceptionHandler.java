package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exe, WebRequest request) {
        /*
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(exe.getCode());
        errorResponse.setMessage(exe.getErrorMessage());
        */
        return new ResponseEntity<>(new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exe, WebRequest request){

        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe, WebRequest request){

        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundException(AddressNotFoundException exe, WebRequest request){

        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> customerUpdateException(UpdateCustomerException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNotFoundException(RestaurantNotFoundException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> invalidRatingException(InvalidRatingException exe, WebRequest webRequest){
        return new ResponseEntity<>(
                new ErrorResponse().code(exe.getCode()).message((exe.getErrorMessage())), HttpStatus.BAD_REQUEST
        );
    }

}

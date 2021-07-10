package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    //private final RestaurantCategoryDao restaurantCategoryDao;
        //Get all restaurant
    public List<RestaurantEntity> getAllRestaurant(){
        return restaurantDao.getAllRestaurants();
    }

    //Get restaurant details by UUID
    public RestaurantEntity restaurantByUUID(final String restaurantUuid){

        //RestaurantCategoryEntity rce = RestaurantCategoryDao.find()
        return restaurantDao.getRestaurantByUuid(restaurantUuid);
    }
}

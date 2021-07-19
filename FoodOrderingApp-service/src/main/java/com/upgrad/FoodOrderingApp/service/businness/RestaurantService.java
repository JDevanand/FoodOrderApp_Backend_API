package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.math3.util.Precision;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    //Get all restaurant
    public List<RestaurantEntity> restaurantsByRating(){

        List<RestaurantEntity> fetchedRestaurantList= restaurantDao.getAllRestaurants();

        Collections.sort(fetchedRestaurantList,RestaurantService.RatingComparator);
        return fetchedRestaurantList;
    }

    //Get restaurant details by UUID
    public RestaurantEntity restaurantByUUID(final String restaurantUuid) throws RestaurantNotFoundException {

       if(restaurantUuid.isEmpty()){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }

        RestaurantEntity fetchedRestaurant = restaurantDao.getRestaurantByUuid(restaurantUuid);
        if(fetchedRestaurant==null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }

        return fetchedRestaurant;
    }

    //update restaurant by customer rating
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurant, double customerRating) throws InvalidRatingException {

        if(customerRating<1 || customerRating>5){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }

        double currentRating = restaurant.getCustomerRating();
        int customerCount = restaurant.getNumberCustomersRated();

        double updatedRating = ((currentRating * customerCount) + customerRating)/(customerCount+1);
        restaurant.setCustomerRating(Precision.round(updatedRating,2));
        restaurant.setNumberCustomersRated(customerCount +1);

        //merge this updated data in db
        return restaurantDao.updateRestaurant(restaurant);
    }

    //Get restaurant based on category
    public List<RestaurantEntity> restaurantByCategory(final String categoryId) throws CategoryNotFoundException {

        if(categoryId==null){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        CategoryEntity fetchedCategory = categoryDao.getCategoryByUuid(categoryId);

        if(fetchedCategory==null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRCByCategory(fetchedCategory);
        if(restaurantCategoryEntities == null){
            return null;
        }

        List<RestaurantEntity> fetchedRestaurantList = new ArrayList<>();
        for(RestaurantCategoryEntity rce : restaurantCategoryEntities){
            RestaurantEntity re = rce.getRestaurantEntity();
            fetchedRestaurantList.add(re);
        }
        //Sort restaurant by name
        Collections.sort(fetchedRestaurantList,RestaurantService.NameComparator);
        return fetchedRestaurantList;
    }

    //Get restaurants by name
    public List<RestaurantEntity> restaurantsByName(final String searchName) throws RestaurantNotFoundException {

        if(searchName == null){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        String pattern = "%"+searchName.toUpperCase()+"%";
        List<RestaurantEntity> fetchedRestaurantList = restaurantDao.getRestaurantByName(pattern);

        if(fetchedRestaurantList==null){
            return null;
        }

        //Sort restaurant by name
        Collections.sort(fetchedRestaurantList,RestaurantService.NameComparator);
        return fetchedRestaurantList;
    }

    //Comparator to sort category
    public static Comparator<RestaurantEntity> RatingComparator = (s1, s2) -> {
        double rating1
                = s1.getCustomerRating();
        double rating2
                = s2.getCustomerRating();

        // ascending order
        return (int)(rating1 - rating2);
    };

    //Comparator to sort restaurant by name
    public static Comparator<RestaurantEntity> NameComparator = (s1, s2) -> {
        String name1
                = s1.getRestaurantName();
        String name2
                = s2.getRestaurantName();

        // ascending order
        return name1.compareTo(name2);
    };
}

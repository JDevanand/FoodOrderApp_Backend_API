package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryItemDao categoryItemDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private RestaurantService restaurantService;

    //Get list of all categories
    public List<CategoryEntity> getAllCategoriesOrderedByName (){

        List<CategoryEntity> categoryEntityList = categoryDao.getAllCategory();
        if(categoryEntityList==null) {
            return null;
        }
        Collections.sort(categoryEntityList, CategoryService.CatNameComparator);
        return categoryEntityList;
    }

    //Get category by its uuid
    public CategoryEntity getCategoryById(final String categoryUuid) throws CategoryNotFoundException {
        if(categoryUuid.isEmpty()){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        CategoryEntity fetchedCategory = categoryDao.getCategoryByUuid(categoryUuid);
        if(fetchedCategory==null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return fetchedCategory;
    }

    public List<ItemEntity> getItemsbyCategoryId(final CategoryEntity category){
        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.getCatItemById(category);
        if(categoryItemEntities == null){
            return null;
        }

        List<ItemEntity> itemEntityList = new ArrayList<>();

        for(CategoryItemEntity catItem : categoryItemEntities){
            if(catItem.getCategory().getId() == category.getId()){
                itemEntityList.add(catItem.getItem());
            }
        }
        return itemEntityList;
    }

    //to be  completed for restaurant service
    public List<CategoryEntity> getCategoriesByRestaurant(final String restaurantUuid) throws RestaurantNotFoundException {

        RestaurantEntity fetchedRestaurant = restaurantService.restaurantByUUID(restaurantUuid);
        if(fetchedRestaurant==null){
            return null;
        }
        List<RestaurantCategoryEntity> fetchedRCEntity= restaurantCategoryDao.getRestaurantandCategory(fetchedRestaurant);

        if(fetchedRCEntity==null){
            return null;
        }
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        for(RestaurantCategoryEntity rce : fetchedRCEntity){
            categoryEntityList.add(rce.getCategoryEntity());
        }
        Collections.sort(categoryEntityList,CategoryService.CatNameComparator);
        return  categoryEntityList;
    }


    //Comparator to sort category
    public static Comparator<CategoryEntity> CatNameComparator = (s1, s2) -> {
        String CategoryName1
                = s1.getCategoryName().toUpperCase();
        String CategoryName2
                = s2.getCategoryName().toUpperCase();

        // ascending order
        return CategoryName1.compareTo(
                CategoryName2);
    };

}

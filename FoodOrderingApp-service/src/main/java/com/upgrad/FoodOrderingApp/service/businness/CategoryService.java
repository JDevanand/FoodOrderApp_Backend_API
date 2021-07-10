package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    //Get list of all categories
    public List<CategoryEntity> getAllCategoriesOrderedByName (){

        List<CategoryEntity> categoryEntityList = categoryDao.getAllCategory();
        Collections.sort(categoryEntityList,CategoryService.CatNameComparator);
        return categoryEntityList;
    }

    //Get category by its uuid
    public CategoryEntity getCategoryById(final String categoryUuid){
        return categoryDao.getCategoryByUuid(categoryUuid);
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

/**
package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Get all category
    @CrossOrigin
    @RequestMapping(path="/category",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories(){

        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        List<CategoryListResponse> categoryListResponses = new ArrayList<>();

        for(CategoryEntity category: categoryEntities){
            CategoryListResponse clr = new CategoryListResponse();
            clr.setId(UUID.fromString(category.getUuid()));
            clr.setCategoryName(category.getCategoryName());
            categoryListResponses.add(clr);
        }

        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
        return new ResponseEntity<>(categoriesListResponse.categories(categoryListResponses), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path="/category/{category_id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryByUuid(@PathVariable("category_id")final String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity fetchedCategory = categoryService.getCategoryById(categoryUuid);

        //
        System.out.println(fetchedCategory);

        CategoryDetailsResponse categoriesDetailsResponse = new CategoryDetailsResponse();
        categoriesDetailsResponse.setId(UUID.fromString(fetchedCategory.getUuid()));
        categoriesDetailsResponse.setCategoryName(fetchedCategory.getCategoryName());

        //List<CategoryItemEntity> itemsTaggedToCategory = categoryService.getItemsbyCategoryId(fetchedCategory);

        List<ItemEntity> itemsTaggedToCategory = categoryService.getItemsbyCategoryId(fetchedCategory);
        List<ItemList> itemLists = new ArrayList<>();
        for(ItemEntity items: itemsTaggedToCategory){
            ItemList itm = new ItemList();
            itm.setId(UUID.fromString(items.getUuid()));
            itm.setItemName(items.getItemName());
            itm.setItemType(items.getType());
            itm.setPrice(items.getPrice());
            itemLists.add(itm);
        }
        categoriesDetailsResponse.setItemList(itemLists);

        return new ResponseEntity<>(categoriesDetailsResponse, HttpStatus.OK);
    }

}
**/
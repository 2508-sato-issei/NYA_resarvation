package com.example.NYA_reservation.converter;

import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantConverter {

    public RestaurantForm toRestaurantForm(Restaurant restaurant) {
        RestaurantForm restaurantForm = new RestaurantForm();

            restaurant.setId(restaurant.getId());
            restaurant.setName(restaurant.getName());
            restaurant.setTelephone(restaurant.getTelephone());
            restaurant.setAddress(restaurant.getAddress());
            restaurant.setArea(restaurant.getArea());
            restaurant.setGenre(restaurant.getGenre());
            restaurant.setExplanation(restaurant.getExplanation());
            restaurant.setStartBusiness(restaurant.getStartBusiness());
            restaurant.setEndBusiness(restaurant.getEndBusiness());
            restaurant.setCapacity(restaurant.getCapacity());
            restaurant.setMinAmount(restaurant.getMinAmount());
            restaurant.setMaxAmount(restaurant.getMaxAmount());
            restaurant.setCreatedDate(restaurant.getCreatedDate());
            restaurant.setUpdatedDate(restaurant.getUpdatedDate());
            return restaurantForm;
    }

    //DBから取得したレストラン情報をEntityからFormに詰め替え
    public List<RestaurantForm> toRestaurantFormList(List<Restaurant> results) {
        List<RestaurantForm> restaurants = new ArrayList<>();

        for (Restaurant result : results) {
            RestaurantForm restaurant = new RestaurantForm();
            restaurant.setId(result.getId());
            restaurant.setName(result.getName());
            restaurant.setTelephone(result.getTelephone());
            restaurant.setAddress(result.getAddress());
            restaurant.setArea(result.getArea());
            restaurant.setGenre(result.getGenre());
            restaurant.setExplanation(result.getExplanation());
            restaurant.setStartBusiness(result.getStartBusiness());
            restaurant.setEndBusiness(result.getEndBusiness());
            restaurant.setCapacity(result.getCapacity());
            restaurant.setMinAmount(result.getMinAmount());
            restaurant.setMaxAmount(result.getMaxAmount());
            restaurant.setCreatedDate(result.getCreatedDate());
            restaurant.setUpdatedDate(result.getUpdatedDate());
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    //Formの値をEntityに詰め替え
    public Restaurant toRestaurantEntity(RestaurantForm restaurantForm){
        Restaurant restaurant = new Restaurant();

        restaurant.setName(restaurantForm.getName());
        restaurant.setTelephone(restaurantForm.getTelephone());
        restaurant.setAddress(restaurantForm.getAddress());
        restaurant.setArea(restaurantForm.getArea());
        restaurant.setGenre(restaurantForm.getGenre());
        restaurant.setExplanation(restaurantForm.getExplanation());
        restaurant.setStartBusiness(restaurantForm.getStartBusiness());
        restaurant.setEndBusiness(restaurantForm.getEndBusiness());
        restaurant.setCapacity(restaurantForm.getCapacity());
        restaurant.setMinAmount(restaurantForm.getMinAmount());
        restaurant.setMaxAmount(restaurantForm.getMaxAmount());

        if(restaurantForm.getId() != null){
            restaurant.setId(restaurantForm.getId());
            restaurant.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return restaurant;
    }

}

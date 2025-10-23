package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantSpecifications;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    //全件取得
    public List<RestaurantForm> findAllRestaurants(){
        List<Restaurant> results = restaurantRepository.findAll();
        return setRestaurantForm(results);
    }

    // 検索結果取得
    public Page<Restaurant> searchRestaurants(SearchForm searchForm, Pageable pageable) {
        String area = searchForm.getArea();
        String genre = searchForm.getGenre();
        Integer headcount = searchForm.getHeadcount();

        Specification<Restaurant> spec = RestaurantSpecifications.searchByCriteria(area, genre, headcount);

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedDate")
        );

        return restaurantRepository.findAll(spec, sortedPageable);
    }

    // レストラン情報を取得（店舗詳細画面用）
    public Restaurant findById(Integer id) {
        return restaurantRepository.findById(id).orElse(null);
    }


    //IDでレストラン情報を取得(予約用)
    public RestaurantForm findRestaurantById(Integer id){
        Restaurant result = restaurantRepository.findById(id).orElse(null);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(result);
        List<RestaurantForm> restaurant = setRestaurantForm(restaurants);
        return restaurant.get(0);
    }

    //店舗数取得
    public Long countRestaurants(){
        return restaurantRepository.count();
    }


    //店舗登録・更新処理
    public RestaurantForm addRestaurant(RestaurantForm restaurantForm){
        Restaurant restaurant = setRestaurantEntity(restaurantForm);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        List<Restaurant> savedRestaurants = new ArrayList<>();
        savedRestaurants.add(savedRestaurant);
        return setRestaurantForm(savedRestaurants).get(0);
    }

    //店舗情報削除
    public void deleteRestaurant(Integer id){
        restaurantRepository.deleteById(id);
    }

    //DBから取得したレストラン情報をEntityからFormに詰め替え
    private List<RestaurantForm> setRestaurantForm(List<Restaurant> results) {
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
    private Restaurant setRestaurantEntity(RestaurantForm restaurantForm){
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

    // 人気エリア取得
    public List<AreaReservationCountDto> selectTopAreasByReservationCount() {
        return restaurantRepository.findTopAreasByReservationCount(PageRequest.of(0, 5));
    }

    // 人気ジャンル取得
    public List<GenreReservationCountDto> selectTopGenresByReservationCount() {
        return restaurantRepository.findTopGenresByReservationCount(PageRequest.of(0, 5));
    }

    // 人気店舗取得
    public List<RestaurantReservationCountDto> selectTopRestaurantsByReservationCount() {
        return restaurantRepository.findTopRestaurantsByReservationCount(PageRequest.of(0, 5));
    }

}

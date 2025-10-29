package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.error.RecordNotFoundException;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.converter.RestaurantConverter;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.NYA_reservation.validation.ErrorMessage.E0011;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantConverter restaurantConverter;

    //全件取得
    public List<RestaurantForm> findAllRestaurants(){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Restaurant> results = restaurantRepository.findAll(sort);
        return restaurantConverter.toRestaurantFormList(results);
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
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0011));
    }

    //IDでレストラン情報を取得(予約用)
    public RestaurantForm findRestaurantById(Integer id){
        Restaurant result = restaurantRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0011));
        return restaurantConverter.toRestaurantForm(result);
    }

    //IDでレストランの画像ファイル名を取得
    public String getImageFileName(Integer id){
        Restaurant result = restaurantRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0011));
        return result.getMainImage();
    }

    //店舗数取得
    public Long countRestaurants(){
        return restaurantRepository.count();
    }


    //店舗登録
    public RestaurantForm addRestaurant(RestaurantForm restaurantForm){
        MultipartFile imageFile = restaurantForm.getMainImage();
        String mainImage = "";
        if(!imageFile.isEmpty()){
            mainImage = imageFile.getOriginalFilename();
            Path filePath = Paths.get("src/main/resources/static/storage/restaurant/" + mainImage);
            copyImageFile(imageFile, filePath);
        }

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurantConverter.toRestaurantEntity(restaurantForm, mainImage));
        return restaurantConverter.toRestaurantForm(savedRestaurant);
    }

    //店舗更新処理
    public RestaurantForm updateRestaurant(RestaurantForm restaurantForm, String existingImage) {
        MultipartFile imageFile = restaurantForm.getMainImage();
        String mainImage = existingImage;

        if (imageFile != null && !imageFile.isEmpty()) {
            mainImage = imageFile.getOriginalFilename();
            Path filePath = Paths.get("src/main/resources/static/storage/restaurant/" + mainImage);
            copyImageFile(imageFile, filePath);
        }

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurantConverter.toRestaurantEntity(restaurantForm, mainImage));
        return restaurantConverter.toRestaurantForm(savedRestaurant);
    }

    //画像ファイルを指定したファイルにコピー
    public void copyImageFile(MultipartFile imageFile, Path filePath){
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //店舗情報削除
    public void deleteRestaurant(Integer id){
        restaurantRepository.deleteById(id);
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

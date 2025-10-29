package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.error.RecordNotFoundException;
import com.example.NYA_reservation.controller.form.ReviewForm;
import com.example.NYA_reservation.converter.ReviewConverter;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.ReviewRepository;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.repository.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.NYA_reservation.validation.ErrorMessage.E0011;

@Service
public class ReviewService {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewConverter reviewConverter;

    public List<Review> getReviewsByRestaurant(Integer restaurantId) {
        return reviewRepository.findByRestaurantIdOrderByCreatedDateDesc(restaurantId);
    }

    public void addReview(Integer restaurantId, ReviewForm reviewForm) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RecordNotFoundException(E0011));
        reviewForm.setRestaurant(restaurant);
        reviewRepository.save(reviewConverter.toReviewEntity(reviewForm));
    }

}

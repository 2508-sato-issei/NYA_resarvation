package com.example.NYA_reservation.converter;

import com.example.NYA_reservation.controller.form.ReviewForm;
import com.example.NYA_reservation.repository.entity.Review;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewConverter {

    // EntityからFormに詰め替え
    public ReviewForm toReviewForm(Review review) {
        ReviewForm reviewForm = new ReviewForm();

        reviewForm.setId(review.getId());
        reviewForm.setRestaurant(review.getRestaurant());
        reviewForm.setUser(review.getUser());
        reviewForm.setTitle(review.getTitle());
        reviewForm.setRating(review.getRating());
        reviewForm.setComment(review.getComment());
        reviewForm.setCreatedDate(review.getCreatedDate());
        reviewForm.setUpdatedDate(review.getUpdatedDate());
        return reviewForm;
    }

    // EntityからFormのListに詰め替え
    public List<ReviewForm> toReviewFormList(List<Review> results) {
        List<ReviewForm> reviews = new ArrayList<>();

        for (Review result : results) {
            ReviewForm reviewForm = new ReviewForm();
            reviewForm.setId(result.getId());
            reviewForm.setRestaurant(result.getRestaurant());
            reviewForm.setUser(result.getUser());
            reviewForm.setTitle(result.getTitle());
            reviewForm.setRating(result.getRating());
            reviewForm.setComment(result.getComment());
            reviewForm.setCreatedDate(result.getCreatedDate());
            reviewForm.setUpdatedDate(result.getUpdatedDate());
            reviews.add(reviewForm);
        }
        return reviews;
    }

    //Formの値をEntityに詰め替え
    public Review toReviewEntity(ReviewForm reviewForm) {
        Review review = new Review();

        review.setRestaurant(reviewForm.getRestaurant());
        review.setUser(reviewForm.getUser());
        review.setTitle(reviewForm.getTitle());
        review.setRating(reviewForm.getRating());
        review.setComment(reviewForm.getComment());

        if (reviewForm.getId() != null) {
            review.setId(reviewForm.getId());
            review.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return review;
    }

}

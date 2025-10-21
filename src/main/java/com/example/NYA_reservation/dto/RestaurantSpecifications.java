package com.example.NYA_reservation.dto;

import com.example.NYA_reservation.repository.entity.Restaurant;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecifications {

    // 検索条件を加工
    public static Specification<Restaurant> searchByCriteria(String area, String genre, Integer headcount) {
        return (root, query, builder) -> {
            Predicate p = builder.conjunction();

            if (area != null && !area.isEmpty()) {
                p = builder.and(p, builder.equal(root.get("area"), area));
            }

            if (genre != null && !genre.isEmpty()) {
                p = builder.and(p, builder.equal(root.get("genre"), genre));
            }

            if (headcount != null) {
                // 収容人数(capacity)が指定人数以上のレストランを検索
                p = builder.and(p, builder.greaterThanOrEqualTo(root.get("capacity"), headcount));
            }

            return p;
        };
    }

}

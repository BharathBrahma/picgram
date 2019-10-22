package com.brahma.gallery.picturegram.repository;

import com.brahma.gallery.picturegram.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ReviewsRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT t.image_id as imageId, avg(t.review_rating) as averageRating FROM Review t group by t.image_id",  nativeQuery = true)
    Collection<AvgReviewResponse> findAverageRatingsForAllImages();


    @Query(value = "SELECT * from Review r where r.image_id =?1 and r.user_id=?2",nativeQuery = true)
    Review getExistingReviewForUserAndImage(Long imageId, Long userId);


    //Using this interface to implement Spring Data Projections for aggregate functions
    public static interface AvgReviewResponse {
        Integer getImageId();
        Double getAverageRating();
    }

}

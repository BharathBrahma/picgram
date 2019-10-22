package com.brahma.gallery.picturegram.service;

import com.brahma.gallery.picturegram.exceptions.ImageNotFoundException;
import com.brahma.gallery.picturegram.exceptions.IncorrectRatingException;
import com.brahma.gallery.picturegram.exceptions.UserNotFoundException;
import com.brahma.gallery.picturegram.models.ImagePost;
import com.brahma.gallery.picturegram.models.Review;
import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.repository.ImagePostRepository;
import com.brahma.gallery.picturegram.repository.ReviewsRepository;
import com.brahma.gallery.picturegram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReviewsService {

    @Autowired
    ReviewsRepository reviewsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImagePostRepository imagePostRepository;

    public void saveReviewForImage(String reviewRating, String imageId, String userId) throws Exception {
        int userRating = Integer.parseInt(reviewRating);
        if(userRating < 0 || userRating > 5) {
            throw new IncorrectRatingException("Invalid Rating!! Valid rating is between 0 and 5, inclusive.");
        }
        Review review = null;
        Review existingReview = reviewsRepository.getExistingReviewForUserAndImage(Long.parseLong(imageId), Long.parseLong(userId));

        //Create the review object or get the existing review obj
        if(existingReview == null) {
            review = new Review();

            Optional<ImagePost> imgPost = imagePostRepository.findById(Long.parseLong(imageId));
            imgPost.orElseThrow(() -> new ImageNotFoundException("Image not found for ID" + imageId));

            ImagePost imageToBeReviewed = imgPost.get();
            User uploadedUser = imageToBeReviewed.getUploadedBy();

            isUserReviewingHisOwnImage(uploadedUser, userId);

            //get the user obj
            Optional<User> loggedInUserOptional = userRepository.findById(Long.parseLong(userId));
            loggedInUserOptional.orElseThrow(() -> new UserNotFoundException("User not found!" + userId));

            //set the image and user for the review
            review.setReviewedBy(loggedInUserOptional.get());
            review.setReviewForImage(imageToBeReviewed);

        } else {
            review = existingReview;

            ImagePost uploadedImge = review.getReviewForImage();

            User uploadedUser = uploadedImge.getUploadedBy();

            isUserReviewingHisOwnImage(uploadedUser, userId);

        }

        //save or update the user rating
        review.setReviewRating(userRating);

        reviewsRepository.save(review);
    }
    //check if the user is reviewing his own image
    private void isUserReviewingHisOwnImage(User uploadedUser, String loggedInUserId) throws Exception {
        if(uploadedUser != null && String.valueOf(uploadedUser.getUserId()).equals(loggedInUserId)) {
            throw new Exception("You are not authorized to review your own images!");
        }
    }

    public ArrayList<ReviewsRepository.AvgReviewResponse> getAverageReviewsForAllImages() {
        return new ArrayList<>(reviewsRepository.findAverageRatingsForAllImages());
    }

}

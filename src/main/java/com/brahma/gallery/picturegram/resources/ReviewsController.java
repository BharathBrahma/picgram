package com.brahma.gallery.picturegram.resources;

import com.brahma.gallery.picturegram.commons.StaticConstants;
import com.brahma.gallery.picturegram.exceptions.ImageNotFoundException;
import com.brahma.gallery.picturegram.models.ImagePost;
import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.repository.ReviewsRepository;
import com.brahma.gallery.picturegram.service.ImagePostService;
import com.brahma.gallery.picturegram.service.ReviewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private static Logger logger = LoggerFactory.getLogger(ReviewsController.class);

    @Autowired
    ReviewsService reviewsService;

    @Autowired
    ImagePostService imagePostService;

    @PostMapping(value = "/api/v1/reviewPost")
    public ResponseEntity<?> reviewAnImage( @RequestParam(name = "rating",required = true) String reviewRating,
                                            @RequestParam(name = "imageId", required = true) String imageId,
                                            @RequestAttribute(StaticConstants.REQUEST_ATTR_USER_ID) String authenticatedUserId) {
        logger.debug("Invoking /api/v1/reviewPost :: ReviewsController");
        if(reviewRating == null) {
            return new ResponseEntity<>("Review cannot be blank or null", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {

            reviewsService.saveReviewForImage(reviewRating, imageId ,authenticatedUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Review saved successfully!!", HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/averageImageReviews")
    public ResponseEntity<?> getAverageReviewsForAllImages() {
        logger.debug("Getting average reviews for all images");
        ArrayList<ReviewsRepository.AvgReviewResponse> response = null;
        try {
             response = reviewsService.getAverageReviewsForAllImages();
        } catch (Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while getting the Avg. reviews", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

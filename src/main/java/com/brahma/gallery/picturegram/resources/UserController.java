package com.brahma.gallery.picturegram.resources;
import com.brahma.gallery.picturegram.commons.StaticConstants;
import com.brahma.gallery.picturegram.exceptions.ImageNotFoundException;
import com.brahma.gallery.picturegram.exceptions.UnauthorizedUserException;
import com.brahma.gallery.picturegram.models.ImagePost;
import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.service.ImagePostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    ImagePostService imagePostService;

    @PostMapping(value = "/api/v1/uploadImageByUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImageByAnUser(@RequestParam(name = "imageFile", required = true) MultipartFile file,
                                                 @RequestAttribute(StaticConstants.REQUEST_ATTR_USER_ID) String authenticatedUserId){
        logger.debug("Invoking uploadImageByAnUser :: UserController");
        //Check if the userId is same as the logged in user using the token
        //if its the same user, then uplaod
        try {
            imagePostService.uploadImage(file,authenticatedUserId);
            logger.debug("Image uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Image not uploaded!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/deleteUserImage")
    public ResponseEntity<?> deleteImageOfAnUser(@RequestParam (name = "image_id", required = true) String imageId,
                                                 @RequestAttribute(StaticConstants.REQUEST_ATTR_USER_ID) String authenticatedUserId){
        logger.debug("Deleting image for Id : " + imageId);
        //Check if the userId is same as the logged in user using the token
        //if its the same user, then delete
        try {
            //check if the user is reviewing his own image
            imagePostService.deleteImage(imageId, authenticatedUserId);

        } catch (UnauthorizedUserException ue) {
            return new ResponseEntity<String>(ue.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }  catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error deleting image, please contact admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Image successfully deleted!", HttpStatus.OK);
    }
}

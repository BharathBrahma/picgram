package com.brahma.gallery.picturegram.service;

import com.brahma.gallery.picturegram.exceptions.ImageNotFoundException;
import com.brahma.gallery.picturegram.exceptions.UnauthorizedUserException;
import com.brahma.gallery.picturegram.models.ImagePost;
import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.repository.ImagePostRepository;
import com.brahma.gallery.picturegram.repository.UserRepository;
import com.brahma.gallery.picturegram.resources.PicturegramController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagePostService {
    private static Logger logger = LoggerFactory.getLogger(ImagePostService.class);

    @Autowired
    ImagePostRepository imagePostRepository;

    @Autowired
    UserRepository userRepository;

    public void uploadImage (MultipartFile imageFile, String userId) throws Exception {
        //get the file in bytes
        byte[] fileInBytes = imageFile.getBytes();

        //set the properties for the post
        ImagePost imagePost = new ImagePost();
        imagePost.setImage(fileInBytes);

        //find the user for user Id
        Optional<User> currentUser = userRepository.findById(Long.parseLong(userId));
        currentUser.orElseThrow(() -> new Exception("User not found!!!"));

        currentUser.ifPresent(retrievedUser -> imagePost.setUploadedBy(retrievedUser));


        imagePostRepository.save(imagePost);
    }

    public Optional<ImagePost> findById(String imageId) {
        return imagePostRepository.findById(Long.parseLong(imageId));
    }

    public void deleteImage(String imageId, String userId) throws UnauthorizedUserException{
        Optional<ImagePost> imgPost = imagePostRepository.findById(Long.parseLong(imageId));
        imgPost.orElseThrow(() -> new ImageNotFoundException("Image not found for ID" + imageId));

        ImagePost imageToBeReviewed = imgPost.get();
        User uploadedUser = imageToBeReviewed.getUploadedBy();
        //delete the image only if the user of the image is the logged in user
        if(uploadedUser != null && String.valueOf(uploadedUser.getUserId()).equals(userId)) {
            imagePostRepository.deleteById(Long.parseLong(imageId));
        } else {
            throw new UnauthorizedUserException("You are not authorized to delete this image!!");
        }
    }
}

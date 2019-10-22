package com.brahma.gallery.picturegram.service;

import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.repository.ImagePostRepository;
import com.brahma.gallery.picturegram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImagePostRepository imagePostRepository;

    public User registerUser(User user) throws Exception {
        User createdUser = null;
        try {
            //Password should not be stored in clear text, we can use
            //a strong encryption algorithm to encrypt and decrypt with a secret key.
            User alreadyRegUser = userRepository.findByEmailId(user.getEmailId());
            if(alreadyRegUser != null) {
                throw new RuntimeException("User already registered, please try a different email");
            }
            createdUser = userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("User registration failed!!");
        }
        return createdUser;
    }

    public void deleteUser(String userId, String imageId){
        imagePostRepository.deleteById(Long.parseLong(imageId));
    }

    public User findUserByEmailId(String email) throws Exception {
        return  userRepository.findByEmailId(email);
    }
}

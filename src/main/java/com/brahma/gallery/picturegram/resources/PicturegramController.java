package com.brahma.gallery.picturegram.resources;

import com.brahma.gallery.picturegram.exceptions.BlankCredentialsException;
import com.brahma.gallery.picturegram.exceptions.InvalidCredentialsException;
import com.brahma.gallery.picturegram.commons.JwtUtil;
import com.brahma.gallery.picturegram.models.User;
import com.brahma.gallery.picturegram.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PicturegramController {

    private static Logger logger = LoggerFactory.getLogger(PicturegramController.class);

    @Autowired
    UserService userService;


    @PostMapping("/api/v1/login")
    public ResponseEntity<?> login(@RequestBody User authUser) throws Exception {
        logger.debug("Logging in user :: PicturegramController");

         String jwtToken = "";
         if(authUser.getEmailId() == null || authUser.getPassword() == null) {
            throw new BlankCredentialsException("Please provide username/password");
         }

         String userEmail = authUser.getEmailId();
         String userPassword = authUser.getPassword();
        logger.debug("user in email :: PicturegramController" + userEmail);

         User loggedInUser = userService.findUserByEmailId(userEmail);

         if(loggedInUser == null || (!userPassword.equals(loggedInUser.getPassword())) ){
             throw new InvalidCredentialsException("Username/password is invalid!");
         }
        logger.debug("User has supplied valid credentials for :: " + userEmail);

         jwtToken = JwtUtil.generateJwtToken(loggedInUser.getUserId());
         return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser){
        logger.debug("Register user endpoint invoked :: PicturegramController");
        User createdUser = null;
        try{
            if(newUser != null)
                createdUser = userService.registerUser(newUser);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("Registered user successfully " + newUser.getEmailId());
        return new ResponseEntity<>("User registered user successfully!", HttpStatus.CREATED);
    }

}

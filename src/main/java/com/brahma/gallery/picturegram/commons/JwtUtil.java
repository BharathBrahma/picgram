package com.brahma.gallery.picturegram.commons;

import com.brahma.gallery.picturegram.exceptions.InvalidAuthTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    //Ideally store this secret key in a config server or using an environment variable on the
    //server or the cloud environment. For this use case ONLY, I am putting it in application.properties.
    private static String jwtSecretkeyPhrase;

    private static int TEN_MINUTES_IN_MILLISECONDS = 600000;

    @Value("${jwt.secret.key}")
    public void setJwtSecretkeyPhrase(String secretkeyPhrase) {
        JwtUtil.jwtSecretkeyPhrase = secretkeyPhrase;
    }

    //generate the token and set the expiration time
    public static String generateJwtToken(Long userId){
        logger.debug("Generating token for UserId: " + userId);
        return Jwts
                .builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEN_MINUTES_IN_MILLISECONDS))
                .signWith(SignatureAlgorithm.HS256, jwtSecretkeyPhrase)
                .compact();
    }

    //Verify the token sent by the UI on each request header for authenticity
    public static String verifyJwtToken(String authToken){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecretkeyPhrase)
                    .parseClaimsJws(authToken)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidAuthTokenException("Invalid token in the request!");
        }
        //Get the email Id from the JWT token to validate the login
        return claims.getSubject();
    }

}

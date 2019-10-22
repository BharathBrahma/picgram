package com.brahma.gallery.picturegram.config;

import com.brahma.gallery.picturegram.exceptions.InvalidAuthorizationHeaderException;
import com.brahma.gallery.picturegram.commons.JwtUtil;
import com.brahma.gallery.picturegram.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class AuthFilter extends GenericFilterBean {
    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    UserService userService;

    //Filter each request to see if we have the required JWT token
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader(AUTHORIZATION);

        //Check for AUTH headers
        if(authHeader == null || !authHeader.startsWith(BEARER)){
            throw new InvalidAuthorizationHeaderException("Missing or Invalid Authorization Header!");
        }

         //Get the substring from starting from the next character of BEARER defined above
        final String authToken = authHeader.substring(7);

        String userId = JwtUtil.verifyJwtToken(authToken);
        logger.info("Setting user ID :: " + userId + " :: to the request attributes");
        request.setAttribute("userId", userId);

        filterChain.doFilter(request, response);
    }
}

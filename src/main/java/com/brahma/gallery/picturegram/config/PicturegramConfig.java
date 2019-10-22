package com.brahma.gallery.picturegram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@DependsOn("authFilter")
@Configuration
public class PicturegramConfig {
    @Autowired
    AuthFilter authFilter;

    //filter the requests and make the requests coming to defined url patterns
    //need to go through auth filter for checking authentication
    @Bean
    public FilterRegistrationBean requestFilter(){
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.addUrlPatterns("/reviews/*");
        return registrationBean;
    }
}

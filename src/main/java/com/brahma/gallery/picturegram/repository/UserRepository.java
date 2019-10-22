package com.brahma.gallery.picturegram.repository;

import com.brahma.gallery.picturegram.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmailId(String emailId) throws Exception;
}

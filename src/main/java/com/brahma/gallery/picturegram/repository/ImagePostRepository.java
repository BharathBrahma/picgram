package com.brahma.gallery.picturegram.repository;

import com.brahma.gallery.picturegram.models.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagePostRepository extends JpaRepository<ImagePost,Long> {
}

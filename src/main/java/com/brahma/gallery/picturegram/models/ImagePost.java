package com.brahma.gallery.picturegram.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImagePost {
    @Id
    @GeneratedValue
    private Long imageId;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User uploadedBy;

    @OneToMany(mappedBy = "reviewForImage", cascade = CascadeType.ALL)
    private Set<Review> imageReviews;
}

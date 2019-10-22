package com.brahma.gallery.picturegram.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue
    private int reviewId;

    private double reviewRating;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private ImagePost reviewForImage;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reviewedBy;
}

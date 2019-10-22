package com.brahma.gallery.picturegram.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PicgramUser")
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    private String password;
    private String emailId;


    @OneToMany(mappedBy = "uploadedBy")
    private Set<ImagePost> uploads;

    @OneToMany(mappedBy = "reviewedBy")
    private Set<Review> userReviews;
}

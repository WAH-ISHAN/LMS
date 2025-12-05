package com.Backend.SecureAuth.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Course course;

    private String title;

    @Column(length = 2000)
    private String description;

    private String type;       // e.g. "LECTURE", "ASSIGNMENT"
    private String contentUrl; // file URL or external link

    private Instant createdAt;
    private Instant updatedAt;
}

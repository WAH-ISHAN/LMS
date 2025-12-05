package com.Backend.SecureAuth.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"lecturer"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;
    private String description;

    // LECTURER MUST BE USER, NOT STRING
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;
}

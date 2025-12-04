package com.Backend.SecureAuth.controller;

import com.Backend.SecureAuth.model.*;
import com.Backend.SecureAuth.UserRepository.CourseRepository;
import com.Backend.SecureAuth.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseContent> getCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestBody Course course,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        if (current.getRole() != Role.ADMIN &&
                current.getRole() != Role.LECTURER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admin or lecturer can create courses");
        }

        if (course.getCode() == null || course.getCode().isBlank()) {
            return ResponseEntity.badRequest().body("Course code required");
        }

        // set lecturer as current user if not admin
        if (current.getRole() == Role.LECTURER) {
            course.setLecturer(String.valueOf(current));
        } else if (course.getLecturer() == null) {
            course.setLecturer(String.valueOf(current)); // admin as lecturer if not set
        }

        Course saved = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody Course updated,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        return courseRepository.findById(id)
                .map(course -> {
                    boolean isOwner = course.getLecturer() != null &&
                            course.getLecturer().getEmail()
                                    .equals(current.getEmail());
                    if (!isOwner && current.getRole() != Role.ADMIN) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Not allowed");
                    }
                    course.setTitle(updated.getTitle());
                    course.setDescription(updated.getDescription());
                    Course saved = courseRepository.save(course).getCourse();
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        return courseRepository.findById(id)
                .map(course -> {
                    boolean isOwner = course.getLecturer() != null &&
                            course.getLecturer().getEmail()
                                    .equals(current.getEmail());
                    if (!isOwner && current.getRole() != Role.ADMIN) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Not allowed");
                    }
                    courseRepository.delete(course);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

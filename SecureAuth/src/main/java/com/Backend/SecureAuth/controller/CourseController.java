package com.Backend.SecureAuth.controller;

import com.Backend.SecureAuth.model.Course;
import com.Backend.SecureAuth.model.Role;
import com.Backend.SecureAuth.model.User;
import com.Backend.SecureAuth.UserRepository.CourseRepository;
import com.Backend.SecureAuth.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    // GET /api/courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // GET /api/courses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)            // ResponseEntity<Course>
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/courses
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

        // If lecturer is not explicitly set, use current user
        if (current.getRole() == Role.LECTURER) {
            course.setLecturer(current);                    // setLecturer(User)
        } else if (course.getLecturer() == null) {
            course.setLecturer(current);                    // Admin as lecturer if not set
        }

        Course saved = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/courses/{id}
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
                            course.getLecturer().getEmail()     // lecturer is User, has getEmail()
                                    .equals(current.getEmail());

                    if (!isOwner && current.getRole() != Role.ADMIN) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Not allowed");
                    }

                    course.setTitle(updated.getTitle());
                    course.setDescription(updated.getDescription());
                    Course saved = courseRepository.save(course);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/courses/{id}
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
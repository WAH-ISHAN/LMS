package com.Backend.SecureAuth.controller;
import com.Backend.SecureAuth.model.*;
import com.Backend.SecureAuth.UserRepository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/contents")
@RequiredArgsConstructor
public class ContentController {
    private final CourseRepository courseRepository;
    private final CourseContentRepository contentRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CourseContent>> getContents(
            @PathVariable Long courseId
    ) {
        Course course = courseRepository.findById(courseId)
                .orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contentRepository.findByCourse(course));
    }

    @PostMapping
    public ResponseEntity<?> addContent(
            @PathVariable Long courseId,
            @RequestBody CourseContent content,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        Course course = courseRepository.findById(courseId)
                .orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isOwner = course.getLecturer() != null &&
                course.getLecturer().getEmail().equals(current.getEmail());

        if (!isOwner && current.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not allowed");
        }

        content.setId(null);
        content.setCourse(course);
        content.setCreatedAt(Instant.now());
        content.setUpdatedAt(Instant.now());

        CourseContent saved = contentRepository.save(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<?> updateContent(
            @PathVariable Long courseId,
            @PathVariable Long contentId,
            @RequestBody CourseContent updated,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        CourseContent content = contentRepository.findById(contentId)
                .orElse(null);
        if (content == null ||
                !content.getCourse().getId().equals(courseId)) {
            return ResponseEntity.notFound().build();
        }

        Course course = content.getCourse();
        boolean isOwner = course.getLecturer() != null &&
                course.getLecturer().getEmail().equals(current.getEmail());

        if (!isOwner && current.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not allowed");
        }

        content.setTitle(updated.getTitle());
        content.setDescription(updated.getDescription());
        content.setType(updated.getType());
        content.setContentUrl(updated.getContentUrl());
        content.setUpdatedAt(Instant.now());

        CourseContent saved = contentRepository.save(content);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteContent(
            @PathVariable Long courseId,
            @PathVariable Long contentId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        CourseContent content = contentRepository.findById(contentId)
                .orElse(null);
        if (content == null ||
                !content.getCourse().getId().equals(courseId)) {
            return ResponseEntity.notFound().build();
        }

        Course course = content.getCourse();
        boolean isOwner = course.getLecturer() != null &&
                course.getLecturer().getEmail().equals(current.getEmail());

        if (!isOwner && current.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not allowed");
        }

        contentRepository.delete(content);
        return ResponseEntity.noContent().build();
    }
}

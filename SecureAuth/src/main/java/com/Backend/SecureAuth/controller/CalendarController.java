package com.Backend.SecureAuth.controller;
import com.Backend.SecureAuth.model.*;
import com.Backend.SecureAuth.UserRepository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/events")
@RequiredArgsConstructor
public class CalendarController {
    private final CourseRepository courseRepository;
    private final CalendarEventRepository eventRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CalendarEvent>> getEvents(
            @PathVariable Long courseId
    ) {
        Course course = courseRepository.findById(courseId)
                .orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventRepository.findByCourse(course));
    }

    @PostMapping
    public ResponseEntity<?> addEvent(
            @PathVariable Long courseId,
            @RequestBody CalendarEvent event,
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

        event.setId(null);
        event.setCourse(course);
        CalendarEvent saved = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long courseId,
            @PathVariable Long eventId,
            @RequestBody CalendarEvent updated,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        CalendarEvent event = eventRepository.findById(eventId)
                .orElse(null);
        if (event == null ||
                !event.getCourse().getId().equals(courseId)) {
            return ResponseEntity.notFound().build();
        }

        Course course = event.getCourse();
        boolean isOwner = course.getLecturer() != null &&
                course.getLecturer().getEmail().equals(current.getEmail());

        if (!isOwner && current.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not allowed");
        }

        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());
        event.setStartTime(updated.getStartTime());
        event.setEndTime(updated.getEndTime());
        event.setType(updated.getType());

        CalendarEvent saved = eventRepository.save(event);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long courseId,
            @PathVariable Long eventId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User current = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        CalendarEvent event = eventRepository.findById(eventId)
                .orElse(null);
        if (event == null ||
                !event.getCourse().getId().equals(courseId)) {
            return ResponseEntity.notFound().build();
        }

        Course course = event.getCourse();
        boolean isOwner = course.getLecturer() != null &&
                course.getLecturer().getEmail().equals(current.getEmail());

        if (!isOwner && current.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not allowed");
        }

        eventRepository.delete(event);
        return ResponseEntity.noContent().build();
    }
}


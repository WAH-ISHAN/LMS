package com.Backend.SecureAuth.controller;
import com.Backend.SecureAuth.model.*;
import com.Backend.SecureAuth.UserRepository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<?> enroll(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User student = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        Course course = courseRepository.findById(courseId)
                .orElse(null);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Enrollment> existing =
                enrollmentRepository.findByStudentAndCourse(student, course);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Already enrolled");
        }

        Enrollment e = Enrollment.builder()
                .student(student)
                .course(course)
                .build();
        enrollmentRepository.save(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(e);
    }

    @GetMapping("/my")
    public List<Course> myCourses(
            @AuthenticationPrincipal UserDetails principal
    ) {
        User student = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();
        List<Enrollment> list = enrollmentRepository.findByStudent(student);
        return list.stream().map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
}

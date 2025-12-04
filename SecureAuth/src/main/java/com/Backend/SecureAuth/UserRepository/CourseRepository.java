package com.Backend.SecureAuth.UserRepository;

import com.Backend.SecureAuth.model.Course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourse(Course course);
}

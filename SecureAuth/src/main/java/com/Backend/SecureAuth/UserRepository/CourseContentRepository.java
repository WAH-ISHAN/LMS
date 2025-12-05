package com.Backend.SecureAuth.UserRepository;


import com.Backend.SecureAuth.model.Course;
import com.Backend.SecureAuth.model.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByCourse(Course course);
}

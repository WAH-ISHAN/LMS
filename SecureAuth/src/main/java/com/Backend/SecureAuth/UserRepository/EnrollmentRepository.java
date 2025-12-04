package com.Backend.SecureAuth.UserRepository;

import com.Backend.SecureAuth.model.Course;
import com.Backend.SecureAuth.model.Enrollment;
import com.Backend.SecureAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long student);
    List<Enrollment> findByCourseId(Long course);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
}

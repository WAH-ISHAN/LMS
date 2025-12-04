package com.Backend.SecureAuth.UserRepository;

import com.Backend.SecureAuth.model.CourseContent;
import com.Backend.SecureAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseContentRepository extends JpaRepository<CourseContent,Long> {
    List<CourseContent> findByCourseId(User lecturer);

}

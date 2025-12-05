package com.Backend.SecureAuth.UserRepository;

import com.Backend.SecureAuth.model.Course;
import com.Backend.SecureAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Lecturer විසින් own කරන courses බලන්න නම් මේක පාවිච්චි
    List<Course> findByLecturer(User lecturer);

    // ⚠ findByCourse(...) වගේ method එක මෙහි *තොටම* නැති වෙන්න ඕන
}
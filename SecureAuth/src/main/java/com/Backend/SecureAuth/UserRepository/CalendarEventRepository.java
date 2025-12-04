package com.Backend.SecureAuth.UserRepository;

import com.Backend.SecureAuth.model.CalendarEvent;
import com.Backend.SecureAuth.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByCourse(Course course);
}

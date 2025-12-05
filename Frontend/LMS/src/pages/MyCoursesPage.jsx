import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api";

export default function MyCoursesPage() {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    api.get("/enrollments/my").then((res) => setCourses(res.data));
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <h2>My Courses</h2>
      <ul>
        {courses.map((c) => (
          <li key={c.id}>
            <Link to={`/courses/${c.id}`}>
              {c.code} - {c.title}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
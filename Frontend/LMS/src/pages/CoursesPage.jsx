// CoursesPage.jsx

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api";

export default function CoursesPage() {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    api.get("/courses").then((res) => setCourses(res.data));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold text-slate-800 mb-4">
        All Courses
      </h1>
      <p className="text-sm text-slate-500 mb-6">
        Browse all available courses and click a course to view details.
      </p>

      {courses.length === 0 ? (
        <p className="text-sm text-slate-500">No courses available yet.</p>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {courses.map((c) => (
            <Link
              key={c.id}
              to={`/courses/${c.id}`}
              className="block rounded-xl bg-white border border-slate-200 shadow-sm hover:shadow-md hover:-translate-y-0.5 transition p-4"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-semibold uppercase tracking-wide text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded-full">
                  {c.code}
                </span>
                {c.lecturer && (
                  <span className="text-xs text-slate-500">
                    by {c.lecturer.fullName || "Lecturer"}
                  </span>
                )}
              </div>
              <h2 className="text-base font-semibold text-slate-800 mb-1">
                {c.title}
              </h2>
              <p className="text-xs text-slate-500 line-clamp-2">
                {c.description || "No description provided."}
              </p>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
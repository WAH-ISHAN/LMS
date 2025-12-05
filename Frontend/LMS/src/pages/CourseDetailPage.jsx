import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api";
import { useAuth } from "../context/AuthContext";

export default function CourseDetailPage() {
  const { id } = useParams();
  const [course, setCourse] = useState(null);
  const [contents, setContents] = useState([]);
  const [events, setEvents] = useState([]);
  const [msg, setMsg] = useState("");
  const { user } = useAuth();

  const load = async () => {
    const [cRes, contRes, evRes] = await Promise.all([
      api.get(`/courses/${id}`),
      api.get(`/courses/${id}/contents`),
      api.get(`/courses/${id}/events`),
    ]);
    setCourse(cRes.data);
    setContents(contRes.data);
    setEvents(evRes.data);
  };

  useEffect(() => {
    load();
  }, [id]);

  const enroll = async () => {
    await api.post(`/enrollments/enroll/${id}`);
    setMsg("Enrolled successfully");
  };

 // CourseDetailPage.jsx (only JSX structure part)

// CourseDetailPage.jsx (only JSX structure part)

return (
  <div>
    {course && (
      <>
        <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-3">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="inline-flex items-center px-2 py-0.5 text-xs font-semibold rounded-full bg-indigo-100 text-indigo-700">
                {course.code}
              </span>
            </div>
            <h1 className="text-2xl font-semibold text-slate-800">
              {course.title}
            </h1>
            <p className="text-sm text-slate-500 mt-1 max-w-2xl">
              {course.description}
            </p>
          </div>

          <div className="flex flex-col items-end gap-2">
            {msg && (
              <span className="text-xs text-green-600 bg-green-50 border border-green-200 rounded px-2 py-1">
                {msg}
              </span>
            )}
            <button
              onClick={enroll}
              className="px-4 py-2 text-sm font-semibold rounded-md bg-indigo-600 hover:bg-indigo-700 text-white shadow-sm transition"
            >
              Enroll
            </button>
          </div>
        </div>

        <div className="grid gap-6 md:grid-cols-2">
          {/* Contents column */}
          <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-4">
            <h2 className="text-lg font-semibold text-slate-800 mb-3">
              Contents
            </h2>
            {contents.length === 0 ? (
              <p className="text-sm text-slate-500">No content yet.</p>
            ) : (
              <ul className="space-y-3">
                {contents.map((c) => (
                  <li
                    key={c.id}
                    className="border border-slate-200 rounded-lg px-3 py-2.5 bg-slate-50"
                  >
                    <div className="flex items-center justify-between mb-1">
                      <span className="font-medium text-slate-800 text-sm">
                        {c.title}
                      </span>
                      <span className="text-[11px] uppercase tracking-wide text-slate-500">
                        {c.type}
                      </span>
                    </div>
                    <p className="text-xs text-slate-600 mb-1">
                      {c.description}
                    </p>
                    <div className="flex items-center justify-between text-[11px] text-slate-500">
                      {c.updatedAt && (
                        <span>
                          Updated:{" "}
                          {new Date(c.updatedAt).toLocaleString()}
                        </span>
                      )}
                      {c.contentUrl && (
                        <a
                          href={c.contentUrl}
                          target="_blank"
                          rel="noreferrer"
                          className="text-indigo-600 hover:underline"
                        >
                          Open
                        </a>
                      )}
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>

          {/* Calendar column */}
          <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-4">
            <h2 className="text-lg font-semibold text-slate-800 mb-3">
              Course Calendar
            </h2>
            {events.length === 0 ? (
              <p className="text-sm text-slate-500">No upcoming events.</p>
            ) : (
              <ul className="space-y-3">
                {events.map((e) => (
                  <li
                    key={e.id}
                    className="border border-slate-200 rounded-lg px-3 py-2.5 bg-slate-50"
                  >
                    <div className="flex items-center justify-between mb-1">
                      <span className="font-medium text-slate-800 text-sm">
                        {e.title}
                      </span>
                      <span className="text-[11px] uppercase tracking-wide text-slate-500">
                        {e.type}
                      </span>
                    </div>
                    <p className="text-xs text-slate-600 mb-1">
                      {e.description}
                    </p>
                    <p className="text-[11px] text-slate-500">
                      {e.startTime} – {e.endTime}
                    </p>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </>
    )}
  </div>
);
}
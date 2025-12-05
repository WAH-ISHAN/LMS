// src/App.jsx
import { Routes, Route, Link } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import CoursesPage from "./pages/CoursesPage.jsx";
import MyCoursesPage from "./pages/MyCoursesPage.jsx";
import CourseDetailPage from "./pages/CourseDetailPage.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import { useAuth } from "./context/AuthContext.jsx";

function App() {
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-slate-100">
      {/* Top Navbar */}
      <nav className="bg-indigo-600 text-white shadow">
        <div className="mx-auto max-w-6xl px-4 py-3 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <span className="text-xl font-bold tracking-tight">
              LMS
            </span>
            {user && (
              <>
                <Link
                  to="/"
                  className="text-sm font-medium hover:text-indigo-200 transition"
                >
                  Courses
                </Link>
                <Link
                  to="/my"
                  className="text-sm font-medium hover:text-indigo-200 transition"
                >
                  My Courses
                </Link>
              </>
            )}
          </div>

          <div className="text-sm">
            {user ? (
              <div className="flex items-center space-x-3">
                <span className="hidden sm:inline text-indigo-100">
                  {user.fullName}{" "}
                  <span className="text-xs bg-indigo-500/60 px-2 py-0.5 rounded-full ml-1">
                    {user.role}
                  </span>
                </span>
                <button
                  onClick={logout}
                  className="px-3 py-1 text-xs font-semibold bg-white text-indigo-600 rounded-md shadow-sm hover:bg-indigo-50 transition"
                >
                  Logout
                </button>
              </div>
            ) : (
              <div className="space-x-3">
                <Link
                  to="/login"
                  className="text-sm font-medium hover:text-indigo-200 transition"
                >
                  Login
                </Link>
                <Link
                  to="/register"
                  className="text-sm font-medium hover:text-indigo-200 transition"
                >
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      </nav>

      {/* Page container */}
      <main className="mx-auto max-w-6xl px-4 py-6">
        <Routes>
          {/* Protected routes */}
          <Route
            path="/"
            element={
              <PrivateRoute>
                <CoursesPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/my"
            element={
              <PrivateRoute>
                <MyCoursesPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/courses/:id"
            element={
              <PrivateRoute>
                <CourseDetailPage />
              </PrivateRoute>
            }
          />

          {/* Public routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Optional: unknown route */}
          {/* <Route path="*" element={<Navigate to="/" replace />} /> */}
        </Routes>
      </main>
    </div>
  );
}

export default App;
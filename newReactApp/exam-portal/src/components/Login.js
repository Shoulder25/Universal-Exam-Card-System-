import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import loginImage from "../assets/logo.png";
import "../App.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  
  const navigate = useNavigate();

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:5000";

const handleLogin = async () => {

  try {

    console.log("Attempting login...");

    const response = await fetch(
      `${API_URL}/api/login`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          studentId: username,
          password: password
        })
      }
    );

    console.log("RAW RESPONSE:", response);

    const data = await response.json();

    console.log("PARSED DATA:", data);

    if (response.ok && data.success) {

      localStorage.setItem(
        "studentId",
        data.student.student_id
      );

      localStorage.setItem(
        "student",
        JSON.stringify(data.student)
      );

      console.log("Navigating...");

      navigate(
        `/dashboard/${data.student.student_id}`
      );

    } else {

      alert(data.message || "Login failed");

    }

  } catch (err) {

    console.error("LOGIN ERROR:", err);

    alert("Server error");

  }
};
  return (
    <div className="container">
      <div className="login-box">
        <h2>Exam Portal</h2>

        <label>Student ID :</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <label>Password:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={handleLogin}>Login</button>
      </div>

      <div className="image-box">
        <img src={loginImage} alt="login visual" />
      </div>
    </div>
  );
}

export default Login;
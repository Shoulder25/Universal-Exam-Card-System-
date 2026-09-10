// server.js
const express = require("express");
const cors = require("cors");
const path = require("path");



const authRoutes = require("./routes/auth");
const dashboardRoutes = require("./routes/dashboard");
const invigilatorRoutes = require("./routes/invigilator");

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// Routes
app.use("/uploads", express.static(path.join(__dirname, "uploads")));
app.use("/api", authRoutes);
app.use("/api", dashboardRoutes);
app.use("/api", invigilatorRoutes);



// Test
app.get("/", (req, res) => {
  res.send("Backend running...");
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
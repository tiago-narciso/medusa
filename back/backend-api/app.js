const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

// Middleware configuration, runs before the request reaches our route
app.use(bodyParser.json()); //If client sends json automatically parse it
app.use(bodyParser.urlencoded({ extended: true })); // parses html form data
app.use(cors());

// Environment configuration
const PORT =  3000;

// API Routes 
/*
All routes are checked in order from top to down
If a route throws an error iut is handled by the error middleware 
in the end
*/

// LOGIN POST endpoint
app.post('/user/login', (req, res) => {
  const { login, password } = req.body;
  
  if (!login || !password) {
    return res.status(400).json({
      error: 'Missing required fields',
      required: ['login', 'password']
    });
  }
  
  const newUser = {
    id: Date.now(),
    login,
    email,
    createdAt: new Date().toISOString()
  };
  
  res.status(201).json({
    message: 'User created successfully',
    data: newUser
  });
});

// Health check endpoint used to check the server is running,
//  the app is responsive, the backend is alive
app.get('/health', (req, res) => {
  res.json({ status: 'ok', uptime: process.uptime() });
});

// Error handling middleware, all errors go to one place
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    error: 'Internal server error',
    message: err.message
  });
});

// 404 handler uknown route
app.use((req, res) => {
  res.status(404).json({
    error: 'Route not found',
    path: req.path
  });
});

// Start server
app.listen(PORT, () => {
  console.log(` Server running on port ${PORT}`);
  console.log(` API URL: http://localhost:${PORT}/api`);
});
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const app = express();

// register route groups
const usersRouter = require('./routes/user');

// Middleware configuration, runs before the request reaches our route
app.use(bodyParser.json()); //If client sends json automatically parse it
app.use(bodyParser.urlencoded({ extended: true })); // parses html form data
app.use(cors());

// Environment configuration
const PORT =  3000;

/*
API Routes
- Routes are checked from top to bottom in the order
they are declared.
- If a route throws an unhandled error, it is passed
to the error handling middleware defined later in
the file.
*/

// Use the routers
app.use('/user', usersRouter);

/**
 * Health check endpoint used to check the server is running,
  the app is responsive, the backend is alive
 * 
*/ 
app.get('/health', (req, res) => {
  res.json({ status: 'ok', uptime: process.uptime() });
});

// Error handling middleware, all unhandled routing get handled here
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
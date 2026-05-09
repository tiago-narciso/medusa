const express = require('express');
const router = express.Router();

/**
 * user/.. endpoint:
 * - user/login
 * - user/register
 * 
 */

// 
/**
 * LOGIN POST endpoint
 */
app.post('/login', (req, res) => {
  const { login, password } = req.body;
  
  if (!login || !password) {
    return res.status(400).json({
      error: 'Missing required fields',
      required: ['login', 'password']
    });
  }
  
  //Fake authentication: todo: to be replaced by auth using the DB
  if(login != 'admin' || password != 'admin'){
    return res.status(401).json({
      error: 'invalid credentials'
    })
  }
  // success response
  res.status(200).json({
    token: 'fake-token' // todo: to be replaced by real token
  });
});

module.exports = router;

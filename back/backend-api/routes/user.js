const express = require('express');
const app = express.Router();

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
  
  /**
   * Fake authentication:
   * @todo: to be replaced by auth using the DB
   */
  if(login != 'admin' || password != 'admin'){
    return res.status(401).json({
      error: 'Invalid credentials'
    })
  }
  /**
   *  success response
   * @todo  token to be replaced by real token
   * */
  res.status(200).json({
    token: 'fake-token' 
  });
});

/**
 * REGISTER POST endpoint
 * @todo: 
 *       - hash password
 *       - save user in DB 
 */
app.post('/register', (req, res) => {
  const { login, password } = req.body;
  
    /**
     * Validate login:
     * - max 25 chars
     * - No special chars
     */
    const loginRegex = /^[a-zA-Z0-9]{1,25}$/;
    if(!loginRegex.test(login)){
        return res.status(400).json({
            error: "Login must contain only letters and numbers and be at most 25 characters"
        })
    }
  
    /**
     * Validate password:
     * - min 8 chars
     * - max 100 chars
     * - min 1 uppercase letter and one number
     */
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,100}$/;
        if(!passwordRegex.test(password)){
        return res.status(400).json({
            error: "Password must contain at least 8 characters, one uppercase letter and one number"
        })
    }

    /**
     * Fake conflict check:
     * - check if login is already taken
     * @todo replace with database lookup
     */
      if(login == 'admin'){
        return res.status(409).json({
        error: 'Login already taken'
        })
    }

    /**
     * Account successfully created
     */
    res.status(201).json({
        /**
         * @todo  token to be replaced by real token
         * */
        token: 'fake-token' 
    });
});


module.exports = app;

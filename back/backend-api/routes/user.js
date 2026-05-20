const express = require('express');
const app = express.Router();
const pool = require('../db');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const JWT_SECRET = process.env.JWT_SECRET;
const SALT_ROUNDS = 10;

app.post('/login', async (req, res) => {
  const { login, password } = req.body;
  
  try {
    const userResult = await pool.query(
      'SELECT * FROM users WHERE login = $1',
      [login]
    );

    if (userResult.rows.length === 0) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    const user = userResult.rows[0];

    const isPasswordValid = await bcrypt.compare(password, user.hashed_password);

    if (!isPasswordValid) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    const token = jwt.sign(
      { id: user.id, login: user.login }, 
      JWT_SECRET,
      { expiresIn: '2h' }
    );

    res.status(200).json({ token });

  } catch (error) {
    console.error('Login error:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

app.post('/register', async (req, res) => {
  const { login, password } = req.body;
  
  const loginRegex = /^[a-zA-Z0-9]{1,25}$/;
  if(!loginRegex.test(login)){
      return res.status(400).json({
          error: "Login must contain only letters and numbers and be at most 25 characters"
      })
  }

  const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,100}$/;
  if(!passwordRegex.test(password)){
      return res.status(400).json({
          error: "Password must contain at least 8 characters, one uppercase letter and one number"
      })
  }

  try {
    const existingUser = await pool.query(
      'SELECT login FROM users WHERE login = $1',
      [login]
    );

    if (existingUser.rows.length > 0) {
      return res.status(409).json({ error: 'Login already taken' });
    }

    const hashedPassword = await bcrypt.hash(password, SALT_ROUNDS);

    const insertResult = await pool.query(
      `INSERT INTO users (login, hashed_password, discoverable_until) 
       VALUES ($1, $2, NOW()) 
       RETURNING id, login`,
      [login, hashedPassword]
    );

    const newUser = insertResult.rows[0];

    const token = jwt.sign(
      { id: newUser.id, login: newUser.login },
      JWT_SECRET,
      { expiresIn: '2h' }
    );

    res.status(201).json({ token });

  } catch (error) {
    console.error('Registration error:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

const verifyToken = (req, res, next) => {
  const authHeader = req.headers.authorization;
  
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Access denied. No token provided.' });
  }

  const token = authHeader.split(' ')[1];

  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded; 
    next(); 
  } catch (error) {
    return res.status(401).json({ error: 'Invalid or expired token.' });
  }
};

app.post('/near', verifyToken, async (req, res) => {
  const userId = req.user.id; 
  const { lat, lon } = req.body;
  const distanceInMeter = 150;

  const request = `
    SELECT card.id AS uniqueId, 
           ST_X(card.position::geometry) AS long,
           ST_Y(card.position::geometry) AS lat,
           wikidata_id AS wikidataId,
           owner_id IS NOT DISTINCT FROM $1 AS owned,
           locked.locked_until AS lockedUntil 
    FROM card
    LEFT JOIN locked ON locked.card_id = card.id AND locked.user_id = $1
    INNER JOIN place ON card.place_id = place.id
    INNER JOIN personality ON place.personality_id = personality.id
    WHERE ST_DWithin(
        card.position::geography,
        ST_SetSRID(ST_MakePoint($2, $3), 4326)::geography,
        $4
    )
    AND available_at < NOW()`;

  try {
    const result = await pool.query(request, [userId, lat, lon, distanceInMeter]);
    res.json(result.rows); 
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server Error');
  }
});

/**
 * GET user/cards
 * return all cards owned by user
 * @TODO SHOULD authenticate user first?
 */
app.get('/cards', (req, res) => {
   res.status(200).json({
    token: 'fake-token' 
  });
});


module.exports = app;

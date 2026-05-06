-- Enable PostGIS extension for spatial data
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create ENUM types for restricted string values
CREATE TYPE acquisition_enum AS ENUM ('captured', 'trade');
CREATE TYPE trade_status_enum AS ENUM ('pending', 'accepted', 'refused');

-- 1. Users Table
-- Named 'users' to avoid conflict with the reserved 'user' keyword
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login VARCHAR(255) UNIQUE NOT NULL,
    hashed_password TEXT NOT NULL,
    discoverable_until TIMESTAMPTZ
);

-- 2. Personality Table
CREATE TABLE personality (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wikidata_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    place_of_birth VARCHAR(255)
);

-- 3. Place Table
CREATE TABLE place (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    is_zone BOOLEAN NOT NULL DEFAULT FALSE,
    personality_id UUID REFERENCES personality(id) ON DELETE SET NULL,
    position GEOGRAPHY(Point, 4326) NOT NULL
);

-- 4. Card Table
CREATE TABLE card (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    place_id UUID REFERENCES place(id) ON DELETE CASCADE,
    power INT NOT NULL DEFAULT 0,
    position GEOGRAPHY(Point, 4326),
    owner_id UUID REFERENCES users(id) ON DELETE SET NULL,
    available_at TIMESTAMPTZ
);

-- 5. Locked Table
CREATE TABLE locked (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    card_id UUID REFERENCES card(id) ON DELETE CASCADE,
    locked_until TIMESTAMPTZ NOT NULL,
    attempt_count INT NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, card_id)
);

-- 6. Ownership History Table
CREATE TABLE ownership_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    card_id UUID REFERENCES card(id) ON DELETE CASCADE,
    acquisition_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    acquisition_type acquisition_enum NOT NULL
);

-- 7. Trade Table
CREATE TABLE trade (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    receiver_user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    sender_card_id UUID REFERENCES card(id) ON DELETE CASCADE,
    receiver_card_id UUID REFERENCES card(id) ON DELETE CASCADE,
    status trade_status_enum NOT NULL DEFAULT 'pending'
    CHECK (sender_user_id != receiver_user_id),
    CHECK (sender_card_id != receiver_card_id)
);

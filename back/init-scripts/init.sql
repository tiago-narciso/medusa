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
    discoverable_until TIMESTAMPTZ NOT NULL
);

-- 2. Personality Table
CREATE TABLE personality (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wikidata_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    place_of_birth VARCHAR(255)
);

CREATE TABLE place (
    -- 3. Place Table
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    is_zone BOOLEAN NOT NULL DEFAULT FALSE,
    personality_id UUID REFERENCES personality(id) ON DELETE CASCADE,
    position GEOGRAPHY(Point, 4326) NOT NULL
);
CREATE INDEX idx_place_position ON place USING GIST (position);

-- 4. Card Table
CREATE TABLE card (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    place_id UUID NOT NULL REFERENCES place(id) ON DELETE CASCADE,
    power INT,
    position GEOGRAPHY(Point, 4326),
    owner_id UUID REFERENCES users(id) ON DELETE SET NULL,
    available_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_card_position ON card USING GIST (position);

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
    card_id UUID NOT NULL REFERENCES card(id) ON DELETE CASCADE,
    acquisition_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    acquisition_type acquisition_enum NOT NULL
);

-- 7. Trade Table
CREATE TABLE trade (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    sender_card_id UUID NOT NULL REFERENCES card(id) ON DELETE CASCADE,
    receiver_card_id UUID NOT NULL REFERENCES card(id) ON DELETE CASCADE,
    status trade_status_enum NOT NULL DEFAULT 'pending'
    CHECK (sender_user_id != receiver_user_id),
    CHECK (sender_card_id != receiver_card_id)
);

-- Triggers

CREATE OR REPLACE FUNCTION get_random_card_position(base_pos GEOGRAPHY, is_zone BOOLEAN)
RETURNS GEOGRAPHY AS $$
BEGIN
    RETURN ST_Project(
        base_pos, 
        CASE WHEN is_zone THEN random() * 20000 ELSE random() * 500 END,
        random() * 360
    )::geography;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION create_card_for_place()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO card (place_id, position)
    VALUES (NEW.id, get_random_card_position(NEW.position, NEW.is_zone));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION create_new_card_after_claim()
RETURNS TRIGGER AS $$
DECLARE
    parent_place RECORD;
BEGIN
    SELECT id, position, is_zone INTO parent_place 
    FROM place 
    WHERE id = NEW.place_id;

    IF FOUND THEN
        INSERT INTO card (place_id, position, available_at)
        VALUES (
            parent_place.id,
            get_random_card_position(parent_place.position, parent_place.is_zone),
            current_timestamp + interval '1 minute'
        );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_after_place_insert
AFTER INSERT ON place
FOR EACH ROW EXECUTE FUNCTION create_card_for_place();

DROP TRIGGER IF EXISTS trigger_on_card_claimed ON card;
CREATE TRIGGER trigger_on_card_claimed
AFTER UPDATE ON card
FOR EACH ROW
WHEN (OLD.owner_id IS NULL AND NEW.owner_id IS NOT NULL)
EXECUTE FUNCTION create_new_card_after_claim();

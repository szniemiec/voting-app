CREATE TABLE IF NOT EXISTS voters (
    id BIGSERIAL PRIMARY KEY,
    pesel_number VARCHAR(11) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    blocked BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS elections (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS election_options (
    id BIGSERIAL PRIMARY KEY,
    election_id BIGINT NOT NULL REFERENCES elections(id) ON DELETE CASCADE,
    label VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS votes (
    id BIGSERIAL PRIMARY KEY,
    voter_id BIGINT NOT NULL REFERENCES voters(id) ON DELETE CASCADE,
    election_id BIGINT NOT NULL REFERENCES elections(id) ON DELETE CASCADE,
    election_option_id BIGINT NOT NULL REFERENCES election_options(id) ON DELETE CASCADE,
    voted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_voter_election UNIQUE (voter_id, election_id)
);

CREATE INDEX idx_voters_pesel ON voters(pesel_number);
CREATE INDEX idx_voters_blocked ON voters(blocked);
CREATE INDEX idx_elections_status ON elections(status);
CREATE INDEX idx_elections_dates ON elections(start_date, end_date);
CREATE INDEX idx_votes_voter ON votes(voter_id);
CREATE INDEX idx_votes_election ON votes(election_id);
CREATE INDEX idx_votes_option ON votes(election_option_id);
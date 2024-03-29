CREATE TABLE IF NOT EXISTS users
(

    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL

);


CREATE TABLE IF NOT EXISTS requests
(

    id           SERIAL PRIMARY KEY,
    description  VARCHAR(255)                NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    requester_id INT REFERENCES users (id)

);

CREATE TABLE IF NOT EXISTS items
(

    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    is_available BOOLEAN      NOT NULL,
    owner_id     INT REFERENCES users (id),
    request_id   INT REFERENCES requests (id)

);


CREATE TABLE IF NOT EXISTS comments
(

    id        SERIAL PRIMARY KEY,
    text      VARCHAR(255) NOT NULL,
    item_id   INT REFERENCES items (id),
    author_id INT REFERENCES users (id),
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL

);

CREATE TABLE IF NOT EXISTS bookings
(
    id         SERIAL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    INT REFERENCES items (id),
    booker_id  INT REFERENCES users (id),
    state_id   INT

);


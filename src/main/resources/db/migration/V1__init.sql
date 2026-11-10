CREATE TABLE users
(
    id         bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       varchar(255) NOT NULL,
    email      varchar(255) NOT NULL UNIQUE,
    role       varchar(16)  NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    created_at timestamptz  NOT NULL DEFAULT now()
);

CREATE TABLE resources
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code        varchar(16)   NOT NULL UNIQUE,
    name        varchar(255)  NOT NULL,
    type        varchar(32)   NOT NULL CHECK (type IN ('MEETING_ROOM', 'DESK', 'EQUIPMENT', 'COURT')),
    capacity    integer       NOT NULL CHECK (capacity > 0),
    hourly_rate numeric(12, 2) NOT NULL CHECK (hourly_rate >= 0),
    currency    varchar(3)    NOT NULL CHECK (currency IN ('USD', 'EUR', 'RUB')),
    status      varchar(16)   NOT NULL CHECK (status IN ('ACTIVE', 'MAINTENANCE', 'RETIRED')),
    created_at  timestamptz   NOT NULL DEFAULT now()
);

CREATE TABLE bookings
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     bigint         NOT NULL REFERENCES users (id),
    resource_id bigint         NOT NULL REFERENCES resources (id),
    starts_at   timestamptz    NOT NULL,
    ends_at     timestamptz    NOT NULL,
    price       numeric(12, 2) NOT NULL CHECK (price >= 0),
    currency    varchar(3)     NOT NULL CHECK (currency IN ('USD', 'EUR', 'RUB')),
    status      varchar(16)    NOT NULL CHECK (status IN ('CONFIRMED', 'CANCELLED', 'COMPLETED')),
    created_at  timestamptz    NOT NULL DEFAULT now(),
    CONSTRAINT bookings_time_valid CHECK (starts_at < ends_at)
);
CREATE EXTENSION IF NOT EXISTS pgcrypto;
create extension if not exists "uuid-ossp";
create type active_type as enum ('AUTHORIZED', 'NOT_CONFIRMED', 'DELETED', 'FROZEN');

CREATE PROCEDURE insert_data_prc(username text, nickname text, password text, email text, session_id text
, inout user_id integer default null, inout session_token text default null)
    LANGUAGE plpgsql
AS
$$
DECLARE
    user_id_d       integer;
    session_token_d text;
BEGIN
    INSERT INTO users (username, nickname, email)
    VALUES ($1, $2, $4);

    SELECT id into user_id_d FROM users where users.nickname = $2;

    INSERT INTO d_users (user_id, password)
    VALUES (user_id_d, $3);

    INSERT INTO users_sessions (user_id, session_id)
    VALUES (user_id_d, $5);
    SELECT token into session_token_d FROM users_sessions where users_sessions.user_id = user_id_d;

    user_id := user_id_d;
    session_token := session_token_d;
    RETURN;
END
$$;

create table users
(
    id          serial      not null primary key,
    username    text        not null,
    nickname    text        not null unique,
    email       text                 default '',
    active_type active_type not null default 'NOT_CONFIRMED',
    created_at  timestamp   not null default now()
);

create table d_users
(
    id         serial    not null primary key,
    user_id    int       not null references users (id),
    password   text      not null,
    updated_at timestamp not null default now()
);

create table users_sessions
(
    id         serial    not null primary key,
    user_id    int       not null unique references users (id),
    token      text      not null default (concat(uuid_generate_v5(uuid_ns_x500(), now()::text),
                                                  gen_random_uuid(), gen_random_uuid())),
    is_valid   boolean   not null default true,
    session_id text      not null,
    updated_at timestamp not null default now()
);

create view view_d_users_sessions (user_id, password, token, session_id, is_valid_session, updated_at)
as
select du.user_id,
       du.password,
       us.token,
       us.session_id,
       us.is_valid as is_valid_session,
       du.updated_at
from d_users du
         join users_sessions us on du.user_id = us.user_id
where is_valid = true;

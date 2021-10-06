create type active_type as enum ('AUTHORIZED', 'NOT_CONFIRMED', 'DELETED', 'FROZEN');
CREATE EXTENSION IF NOT EXISTS pgcrypto;
create extension if not exists "uuid-ossp";


create table users
(
    id          serial primary key,
    user_name   text not null,
    nickname    text not null unique,
    active_type active_type,
    created_at  timestamp default now()
);

create table d_users
(
    id         serial primary key,
    user_id    int not null references users (id),
    password   text,
    updated_at timestamp default now()
);


create table users_sessions
(
    id         serial primary key,
    user_id    int not null unique references users (id),
    token      text       not null default (concat(uuid_generate_v5(uuid_ns_x500(), now()::text),
                                                   gen_random_uuid(), gen_random_uuid())),
    is_valid   boolean    not null default true,
    updated_at timestamp           default now()
);

create view view_d_users_sessions (user_id, password, token, is_valid_session, updated_at)
as
select du.user_id,
       du.password,
       us.token,
       us.is_valid as is_valid_session,
       du.updated_at
from d_users du
         join users_sessions us on du.user_id = us.user_id
where is_valid = true;



create table users_info
(
    id         serial primary key,
    user_id    int not null references users (id),
    email      text      default '',
    phone      text      default '',
    city       text      default '',
    country    text      default '',
    about      text      default '',
    updated_at timestamp default now()
);

create view view_users (user_id, user_name, nickname, active_type, email, phone, city, country)
as
select ui.user_id,
       u.user_name,
       u.nickname,
       u.active_type,
       ui.email,
       ui.phone,
       ui.city,
       ui.country
from users u
         join users_info ui on u.id = ui.user_id;
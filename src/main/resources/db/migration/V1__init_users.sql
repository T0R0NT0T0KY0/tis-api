create type active_type as enum ('AUTHORIZED', 'NOT_CONFIRMED', 'DELETED', 'FROZEN');

create table users
(
    id serial primary key,
    user_name text not null,
    nickname text not null unique,
    active_type active_type,
    created_at timestamp default now()
);


create table d_users
(
    user_id int not null references users(id),
    password text,
    updated_at timestamp default now()
);

create table users_sessions
(
    user_id int not null references users(id),
    session_key text not null,
    is_valid boolean not null default true,
    updated_at timestamp default now()
);

create table users_info
(
    user_id int not null references users(id),
    email text default '',
    phone text default '',
    city text default '',
    country text default '',
    about text default '',
    updated_at timestamp default now()
)
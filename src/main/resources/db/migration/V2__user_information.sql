
create table users_info
(
    id         serial    not null primary key,
    user_id    int       not null references users (id),
    phone      text               ,
    city       text               ,
    country    text               ,
    about      text               ,
    updated_at timestamp not null default now()
);
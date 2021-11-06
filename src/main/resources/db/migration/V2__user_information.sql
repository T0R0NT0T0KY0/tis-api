create table users_info
(
    id           serial    not null primary key,
    user_id      int       not null references users (id),
    phone        text,
    living_place text,
    about        text,
    updated_at   timestamp not null default now()
);

create table users_avatars
(
    id         serial    not null primary key,
    user_id    int       not null references users (id),
    image_link text      not null,
    updated_at timestamp not null default now()
);

create table teams
(
    name       text unique not null primary key,
    image_link text        not null,
    about      text        not null default '',
    created_at timestamp            default now()
);

create table users_teams_fans
(
    id      serial not null primary key,
    user_id integer references users (id),
    team    text references teams (name)
);

-- add +-
create table users_experience_predictions
(
    id         serial    not null primary key,
    user_id    integer references users (id),
    experience integer   not null default 0,
    created_at timestamp not null default now()
);

create table users_birthdays
(
    id      serial not null primary key,
    user_id integer references users (id),
    date    timestamp
);

create view view_users_information
            (user_id, username, nickname, living_place, birthday, email, team, team_image_link, about)
as
select u.id                                                                    as user_id,
       username,
       nickname,
       ui.living_place                                                         as living_place,
       ub.date                                                                 as birthday,
       email,
       utf.team                                                                as team,
       (select image_link as team_image_link from teams where name = utf.team) as team_image_link,
       ui.about                                                                as about
from users u
         left join users_info ui on u.id = ui.user_id
         left join users_birthdays ub on u.id = ub.user_id
         left join users_experience_predictions exp on u.id = exp.user_id
         left join users_teams_fans utf on u.id = utf.user_id;
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

create table users_living_places
(
    id      serial not null primary key,
    user_id integer references users (id),
    place   text   not null
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
    id        serial not null primary key,
    user_id   integer references users (id),
    day_month text   not null,
    year      integer
);

create view users_information (user_id, place, birthday, email, predictions_experience, team_name, team_image_link)
as
select u.id                         as user_id,
       place,
       concat(day_month, ' ', year) as birthday,
       email,
       experience,
       utf.team as team_name,
       (select image_link as team_image_link from teams where name = utf.team) as team_image_link
from users u
         left join users_living_places ulp on u.id = ulp.user_id
         left join users_birthdays ub on u.id = ub.user_id
         left join users_experience_predictions exp on u.id = exp.user_id
         left join users_teams_fans utf on u.id = utf.user_id

drop table if exists comments;
drop table if exists bookings;
drop table if exists items;
drop table if exists users;

CREATE TABLE IF NOT EXISTS users
(
    id bigint generated by default as identity not null
    , name varchar(255) NOT NULL
    , email varchar(512) NOT NULL
    , primary key (id)
    , constraint UQ_USER_EMAIL unique (email)
);
create table if not exists items
(
    id bigint generated by default as identity not null
    , name varchar(255) not null
    , description varchar(512) not null
    , available bool not null default true
    , owner_id bigint not null
    , foreign key (owner_id) references users(id) on delete cascade
    , primary key (id)
);
create type if not exists booking_status as enum('WAITING', 'APPROVED', 'REJECTED');
create table if not exists bookings
(
    id bigint generated by default as identity not null
    , item_id bigint not null
    , pb datetime not null
    , pe datetime not null
    , status booking_status not null
    , user_id bigint not null
    , foreign key (item_id) references items(id) on delete cascade
    , foreign key (user_id) references users(id) on delete cascade
    , primary key (id)
);
create table if not exists comments
(
    id bigint generated by default as identity not null
    , booking_id bigint not null
    , comment varchar(1024) not null
    , foreign key (booking_id) references bookings(id) on delete cascade
    , primary key (id)
);
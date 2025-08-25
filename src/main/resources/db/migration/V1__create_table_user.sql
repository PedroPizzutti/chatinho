create table user (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    login varchar(60) not null unique,
    password varchar(100) not null,
    nickname varchar(100) not null
);
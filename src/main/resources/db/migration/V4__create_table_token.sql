create table token (
    id bigint(20) not null auto_increment primary key,
    jwt varchar(1000) not null
);
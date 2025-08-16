create table token (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    jwt varchar(1000) not null,
    id_user bigint(20) not null,
    foreign key (id_user) references user(id) on delete cascade
);
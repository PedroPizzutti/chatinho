create table room (
    id bigint(20) not null auto_increment primary key,
    name varchar(30) not null,
    created_at datetime not null default current_timestamp,
    id_user bigint(20) not null,
    foreign key (id_user) references user(id) on delete cascade
);

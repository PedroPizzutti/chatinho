create table message (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    content TEXT not null,
    type varchar(20) not null,
    room bigint(20) not null,
    user bigint(20) not null,
    foreign key (user) references user(id) on delete cascade
);
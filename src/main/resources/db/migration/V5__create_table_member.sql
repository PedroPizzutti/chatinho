create table member (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    user bigint(20) not null,
    room bigint(20) not null,
    foreign key (user) references user(id),
    foreign key (room) references room(id) on delete cascade
);

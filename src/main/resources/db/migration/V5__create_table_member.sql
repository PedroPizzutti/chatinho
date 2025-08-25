create table member (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    id_user bigint(20) not null,
    id_room bigint(20) not null,
    foreign key (id_user) references user(id),
    foreign key (id_room) references room(id) on delete cascade
);

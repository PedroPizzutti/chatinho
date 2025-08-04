create table invite (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    status varchar(50) not null,
    id_user_from bigint(20) not null,
    id_user_to bigint(20) not null,
    id_room bigint(20) not null,
    foreign key (id_user_from) references user(id),
    foreign key (id_user_to) references user(id),
    foreign key (id_room) references room(id) on delete cascade
)
create table message (
    id bigint(20) not null auto_increment primary key,
    content TEXT not null,
    created_at datetime not null default current_timestamp,
    user_id bigint(20) not null,
    foreign key (user_id) references user(id) on delete cascade
);
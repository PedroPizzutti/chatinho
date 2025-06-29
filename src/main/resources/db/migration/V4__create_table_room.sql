create table room (
    id bigint(20) not null auto_increment primary key,
    guid varchar(36) not null,
    created_at datetime not null default current_timestamp,
    owner bigint(20) not null,
    foreign key (owner) references user(id) on delete cascade
);

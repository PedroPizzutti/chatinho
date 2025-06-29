create table totem (
    id bigint(20) not null auto_increment primary key,
    created_at datetime not null default current_timestamp,
    expires_in int default 72,
    guid char(36),
    is_used boolean not null default false
);
create table if not exists chat
(
    id              bigint generated always as identity,
    created_at      timestamp with time zone not null,

    primary key (id)
)

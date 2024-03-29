create table if not exists chat
(
    id              bigint generated always as identity,
    created_at      timestamp with time zone not null,

    primary key (id)
);

create table if not exists link
(
    id              bigint generated always as identity,
    url             text                     not null,
    created_at      timestamp with time zone not null,

    primary key (id),
    unique (url)
);

create table if not exists link_to_chat
(
    chat_id bigint not null references chat (id),
    link_id bigint not null references link (id),

    primary key (chat_id, link_id)
)

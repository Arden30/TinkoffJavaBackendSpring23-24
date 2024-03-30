create table if not exists chat
(
    id              bigint,
    created_at      timestamp with time zone not null,

    unique (id),
    primary key (id)
);

create table if not exists link
(
    id              bigint generated always as identity,
    url             text                     not null,
    created_at      timestamp with time zone not null,
    updated_at      timestamp with time zone,

    primary key (id),
    unique (url)
);

create table if not exists link_to_chat
(
    chat_id bigint not null references chat (id) on delete cascade,
    link_id bigint not null references link (id) on delete cascade,

    primary key (chat_id, link_id)
)

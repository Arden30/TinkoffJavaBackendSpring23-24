create table if not exists link_to_chat
(
    chat_id bigint not null references chat (id),
    link_id bigint not null references link (id),

    primary key (chat_id, link_id)
)

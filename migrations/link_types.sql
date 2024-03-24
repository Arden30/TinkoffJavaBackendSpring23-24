create table if not exists github_repos
(
    link_id         bigint not null references link (id) on delete cascade,
    stars           bigint not null,
    issues          bigint not null,

    unique (link_id),
    primary key (link_id)
);

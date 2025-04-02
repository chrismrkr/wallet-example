use app;

drop table if exists balance_entity;
drop table if exists balance_event_outbox_entity;
drop table if exists member_entity;

create table balance_entity (
    member_id bigint,
    amount varchar(255),
    balance_id varchar(255) not null,
    primary key (balance_id)
);

create table balance_event_outbox_entity (
   event_id bigint not null,
   receiver_id bigint,
   sender_id bigint,
   amount varchar(255),
   status enum ('FAIL','STARTED','SUCCESS'),
   primary key (event_id)
);

create table member_entity (
   member_id bigint not null,
   name varchar(255),
   primary key (member_id)
);

drop table if exists frequency;
drop table if exists event;
drop table if exists notebook;
create table event
(
    id          bigint       not null
        primary key
        unique auto_increment,
    label       varchar(255) not null,
    data_base   datetime     not null,
    type        varchar(255),
    active      tinyint,
    notebook_id bigint
);

create table frequency
(
    id       bigint       not null
        primary key
        unique auto_increment,
    times    mediumint    not null,
    subject  varchar(255) not null,
    event_id bigint       not null
);

ALTER TABLE frequency
    ADD FOREIGN KEY (event_id) REFERENCES event (id);


create table notebook
(
    id    bigint       not null
        primary key
        unique auto_increment,
    label varchar(255) not null
);

ALTER TABLE event
    ADD FOREIGN KEY (notebook_id) REFERENCES notebook (id);

insert into notebook (id, label) values (1, 'default');
insert into notebook (id, label) values (2, 'lista diaria');
insert into notebook (id, label) values (3, 'lista em atraso');

insert into event (id, label, data_base, type, active, notebook_id)
values (1, 'Checkup', DATE('2022-12-06 07:00:00'), 'HEALTH', 0, 1);
insert into frequency (id, times, subject, event_id)
values (1, 1, 'YEAR', 1);

insert into event (id, label, data_base, type, active, notebook_id)
values (2, 'Dar comida', DATE('2022-11-06 07:00:00'), 'PET', 0, 1);
insert into frequency (id, times, subject, event_id)
values (2, 1, 'DAY', 2);

select
    e.*,
    n.label
from event e inner join notebook n on e.notebook_id = n.id;




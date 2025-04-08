drop table if exists people;

create table people (
    id identity not null primary key,
    name varchar(255) not null,
    age int not null
);

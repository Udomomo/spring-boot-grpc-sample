drop table if exists people;

create table people (
    id int auto_increment primary key,
    people_name varchar(255) not null,
    age int not null
);

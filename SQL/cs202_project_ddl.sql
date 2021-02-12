drop table if exists section;
drop table if exists penalty_list;
drop table if exists hold_request;
drop table if exists borrow;
drop table if exists book;
drop table if exists topic;
drop table if exists genre;
drop table if exists author;
drop table if exists users;
drop table if exists type;

create table type(
	user_type enum ('1','2','3'),
    user_type_name varchar(15),
    primary key(user_type)
);

create table users(
	id char(8) not null,
    name varchar(40) not null,
    password varchar(34) not null,
	user_type enum('1','2','3') not null,
    primary key (id),
    foreign key(user_type) references type(user_type)
);

create table author (
	author_id int not null auto_increment,
	author_name varchar(40) not null,
    UNIQUE(author_name),
    primary key(author_id)
);

create table genre(
	genre_id int not null auto_increment,
    genre_name varchar(40) not null,
    UNIQUE(genre_name),
    primary key (genre_id)
);

create table topic(
	topic_family_id int not null auto_increment,
    topic_name varchar(40) not null,
    primary key (topic_family_id,topic_name)
);

create table book (
	book_id int not null auto_increment,
    title varchar(60) not null,
	author_id int not null,
    genre_id int not null,
    topic_family_id int not null,
    publisher_id char(8) not null,
    publish_date date not null,
    page_number int not null,
    availability boolean,
    request int,
    UNIQUE(title,author_id),
    primary key(book_id),
    foreign key(author_id) references author(author_id),
    foreign key(genre_id) references genre(genre_id),
    foreign key(publisher_id) references users(id),
    foreign key(topic_family_id) references topic(topic_family_id)
);


create table borrow(
	borrow_id int not null auto_increment,
    regular_user_id char(8) not null,
    book_id int not null,
    borrow_date date not null,
    return_date date,
    overdue boolean,
    primary key (borrow_id),
    foreign key (regular_user_id) references users(id),
    foreign key (book_id) references book(book_id) on delete cascade
);
    
create table hold_request(
	hold_id int not null auto_increment,
    regular_user_id char(8) not null,
    book_id int not null,
    request_date date not null,
	primary key (hold_id),
    foreign key (regular_user_id) references users(id),
    foreign key (book_id) references book(book_id)
);
    
create table penalty_list (
    regular_user_id char(8) not null,
    debt int,
    primary key(regular_user_id),
    foreign key (regular_user_id) references users(id)
);

create table section(
	genre_id int not null,
    floor_number int not null,
    part_name char(1) not null,
    primary key(genre_id),
    foreign key(genre_id) references genre(genre_id)
);
    

                    
use elib_db;

create table elib_db.AUTHOR (
ID int not null auto_increment,
NAME varchar(25),
SURNAME varchar(25),

primary key (ID)
);

create table elib_db.BOOK (
ID int not null auto_increment,
NAME varchar(30),
PUBLISH_YEAR date,
IS_BOOKED boolean,
AUTHOR_ID int,

primary key(ID),
foreign key(AUTHOR_ID) references elib_db.AUTHOR(ID)
);

create table elib_db.USER (
ID int not null auto_increment,
LOGIN varchar(30),
REGISTRATION_DATE date,

primary key(ID)
);

create table elib_db.RESERVATION (
ID int not null auto_increment,
USER_ID int,
BOOKED_DATE datetime,
BOOK_ID int,

primary key(ID),
foreign key(USER_ID) references elib_db.USER(ID),
foreign key(BOOK_ID) references elib_db.BOOK(ID)
);






# --- !Ups

CREATE TABLE users (
    id int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL,
    password varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE users;
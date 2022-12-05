/* root 계정으로 접속, LIBRARY 데이터베이스 생성, LIBRARY 계정 생성 */
DROP database IF EXISTS library;
DROP USER IF EXISTS library@'%';

CREATE database library;
CREATE USER library@localhost IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON library.* TO library@localhost WITH GRANT OPTION;
COMMIT;

/* library 테이블 생성 */
USE library;

CREATE TABLE member
(
    member_id           INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(40) NOT NULL,
    password            VARCHAR(40) NOT NULL,
    suspend_finish_date date,
    job                 VARCHAR(40) NOT NULL
);

CREATE TABLE admin
(
    admin_id INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(40) NOT NULL
);

CREATE TABLE book_info
(
    book_number  INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(40) NOT NULL,
    author       VARCHAR(20) NOT NULL,
    publisher    VARCHAR(20) NOT NULL,
    publish_year INTEGER     NOT NULL
);

CREATE TABLE book
(
    serial_number INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_number   INTEGER NOT NULL,
    FOREIGN KEY (book_number) REFERENCES Book_info (book_number)
);

CREATE TABLE interested_in
(
    member_id   INTEGER NOT NULL,
    book_number INTEGER NOT NULL,
    FOREIGN KEY (member_id) REFERENCES Member (member_id),
    FOREIGN KEY (book_number) REFERENCES Book_info (book_number),
    PRIMARY KEY (member_id, book_number)
);

CREATE TABLE borrow
(
    id                          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id                   INTEGER NOT NULL,
    serial_number               INTEGER NOT NULL,
    borrow_start_date           DATE    NOT NULL,
    count_of_due_date_extension INTEGER NOT NULL DEFAULT 0,
    return_date                 DATE,
    FOREIGN KEY (member_id) REFERENCES Member (member_id),
    FOREIGN KEY (serial_number) REFERENCES Book (serial_number)
);

CREATE TABLE book_reservation
(
    member_id        INTEGER   NOT NULL,
    book_number      INTEGER   NOT NULL,
    reservation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (member_id) REFERENCES Member (member_id),
    FOREIGN KEY (book_number) REFERENCES Book_info (book_number)
);

CREATE TABLE damaged_book
(
    member_id     INTEGER NOT NULL,
    serial_number INTEGER NOT NULL,
    FOREIGN KEY (member_id) REFERENCES Member (member_id),
    FOREIGN KEY (serial_number) REFERENCES Book (book_number)
);
COMMIT;

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE books (
  id VARCHAR(250)  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  page_count INT NOT NULL,
  author_id VARCHAR(250) NOT NULL
);

CREATE TABLE authors (
  id VARCHAR(250) PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL
);

INSERT INTO authors (id, first_name, last_name) VALUES
  ('author-1', 'Joanne', 'Rowling'),
  ('author-2', 'Herman', 'Melville'),
  ('author-3', 'Anne', 'Rice');

INSERT INTO books (id, name, page_count, author_id) VALUES
  ('book-1', 'Harry Potter and the Philosopher''s Stone', 223, 'author-1'),
  ('book-2', 'Moby Dick', 635, 'author-2'),
  ('book-3', 'Interview with the vampire', 371, 'author-3');
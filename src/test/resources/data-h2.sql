DELETE FROM take_examinations_answers;
DELETE FROM take_examinations_questions;
DELETE FROM take_examinations;
DELETE FROM questions_auth_users;
DELETE FROM questions_auth_public;
DELETE FROM questions_answers;
DELETE FROM questions;
DELETE FROM examinations_auth_users;
DELETE FROM examinations_auth_public;
DELETE FROM examinations_categories;
DELETE FROM examinations;
DELETE FROM question_categories;
DELETE FROM authorities;
DELETE FROM users;

INSERT INTO users(username, password, enabled) VALUES('user1', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user2', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user3', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user4', 'password', true);

INSERT INTO authorities(username, authority) VALUES('user1', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user2', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user3', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user4', 'ROLE_ADMIN');

INSERT INTO question_categories(id, name) VALUES(1, 'category1');
INSERT INTO question_categories(id, name) VALUES(2, 'category2');

INSERT INTO examinations(id, text, question_count) VALUES(1, 'examinationPublic', 10);
INSERT INTO examinations(id, text, question_count) VALUES(2, 'examinationForUser1', 20);
INSERT INTO examinations(id, text, question_count) VALUES(3, 'examinationForUser2Category1', 10);
INSERT INTO examinations(id, text, question_count) VALUES(4, 'examinationForUser2Category2', 10);
INSERT INTO examinations(id, text, question_count) VALUES(5, 'examinationForUser2Category1&2', 10);

INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(1, 3, 1);
INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(2, 4, 2);
INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(3, 5, 1);
INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(4, 5, 2);

INSERT INTO examinations_auth_public(id, examination_id) VALUES(1, 1);

INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(1, 2, 'user1');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(2, 3, 'user3');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(3, 4, 'user3');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(4, 5, 'user3');

INSERT INTO questions(id, question_type, text, question_category_id) VALUES(1, 'SINGLE_CHOICE', 'question1', 1);
INSERT INTO questions(id, question_type, text, question_category_id) VALUES(2, 'SINGLE_CHOICE', 'question2', 2);
INSERT INTO questions(id, question_type, text, question_category_id) VALUES(3, 'SINGLE_CHOICE', 'question3', 1);
INSERT INTO questions(id, question_type, text, question_category_id) VALUES(4, 'SINGLE_CHOICE', 'question4', 2);

INSERT INTO questions_auth_public(id, question_id) VALUES(1, 1);
INSERT INTO questions_auth_public(id, question_id) VALUES(2, 2);

INSERT INTO questions_auth_users(id, question_id, user_username) VALUES(1, 3, 'user3');
INSERT INTO questions_auth_users(id, question_id, user_username) VALUES(2, 4, 'user3');

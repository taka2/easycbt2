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
DELETE FROM question_categories_auth_users;
DELETE FROM question_categories_auth_public;
DELETE FROM question_categories;
DELETE FROM authorities;
DELETE FROM users;

INSERT INTO users(username, password, enabled) VALUES('user1', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user2', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user3', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user4', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user5', 'password', true);
INSERT INTO users(username, password, enabled) VALUES('user6', 'password', true);

INSERT INTO authorities(username, authority) VALUES('user1', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user2', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user3', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user4', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES('user6', 'ROLE_USER');

INSERT INTO question_categories(id, name) VALUES(1, 'category1');
INSERT INTO question_categories(id, name) VALUES(2, 'category2');

INSERT INTO question_categories_auth_public(id, question_category_id) VALUES(1, 1);
INSERT INTO question_categories_auth_public(id, question_category_id) VALUES(2, 2);

INSERT INTO examinations(id, text, question_count, enabled) VALUES(1, 'examinationPublic', 10, true);
INSERT INTO examinations(id, text, question_count, enabled) VALUES(2, 'examinationForUser1', 20, true);
INSERT INTO examinations(id, text, question_count, enabled) VALUES(3, 'examinationForUser2Category1', 10, true);
INSERT INTO examinations(id, text, question_count, enabled) VALUES(4, 'examinationForUser2Category2', 10, true);
INSERT INTO examinations(id, text, question_count, enabled) VALUES(5, 'examinationForUser2Category1&2', 10, true);

INSERT INTO examinations_categories(id, examination_id, question_category_id, enabled) VALUES(1, 3, 1, true);
INSERT INTO examinations_categories(id, examination_id, question_category_id, enabled) VALUES(2, 4, 2, true);
INSERT INTO examinations_categories(id, examination_id, question_category_id, enabled) VALUES(3, 5, 1, true);
INSERT INTO examinations_categories(id, examination_id, question_category_id, enabled) VALUES(4, 5, 2, true);

INSERT INTO examinations_auth_public(id, examination_id) VALUES(1, 1);

INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(1, 2, 'user1');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(2, 3, 'user3');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(3, 4, 'user3');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(4, 5, 'user3');
INSERT INTO examinations_auth_users(id, examination_id, user_username) VALUES(5, 1, 'user4');

INSERT INTO questions(id, question_type, text, question_category_id, enabled) VALUES(1, 'SINGLE_CHOICE', 'question1', 1, true);
INSERT INTO questions(id, question_type, text, question_category_id, enabled) VALUES(2, 'SINGLE_CHOICE', 'question2', 2, true);
INSERT INTO questions(id, question_type, text, question_category_id, enabled) VALUES(3, 'SINGLE_CHOICE', 'question3', 1, true);
INSERT INTO questions(id, question_type, text, question_category_id, enabled) VALUES(4, 'SINGLE_CHOICE', 'question4', 2, true);

INSERT INTO questions_auth_public(id, question_id) VALUES(1, 1);
INSERT INTO questions_auth_public(id, question_id) VALUES(2, 2);

INSERT INTO questions_auth_users(id, question_id, user_username) VALUES(1, 3, 'user3');
INSERT INTO questions_auth_users(id, question_id, user_username) VALUES(2, 4, 'user3');
INSERT INTO questions_auth_users(id, question_id, user_username) VALUES(3, 1, 'user3');

INSERT INTO take_examinations(id, user_username, examination_id, elapsed_time) VALUES(1, 'user6', 1, 1000);
INSERT INTO take_examinations(id, user_username, examination_id, elapsed_time) VALUES(2, 'user6', 1, 800);

INSERT INTO take_examinations_questions(id, take_examination_id, question_id, elapsed_time) VALUES(1, 1, 1, 500);
INSERT INTO take_examinations_questions(id, take_examination_id, question_id, elapsed_time) VALUES(2, 1, 2, 500);
INSERT INTO take_examinations_questions(id, take_examination_id, question_id, elapsed_time) VALUES(3, 2, 1, 400);
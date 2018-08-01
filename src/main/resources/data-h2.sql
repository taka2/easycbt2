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

INSERT INTO users(username, password, enabled) VALUES('user1', '$2a$10$BOv2SDD3xL3/GHH2cCW6cuWdbkigOYdxPza5jpKWVz5aCXtdnG/vW', true);
INSERT INTO users(username, password, enabled) VALUES('user2', '$2a$10$BOv2SDD3xL3/GHH2cCW6cuWdbkigOYdxPza5jpKWVz5aCXtdnG/vW', true);

INSERT INTO authorities(username, authority) VALUES('user1', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES('user2', 'ROLE_USER');

INSERT INTO question_categories(name, enabled) VALUES('AWS-Certified-Solutions-Architect-Associate-JP', true);
INSERT INTO question_categories(name, enabled) VALUES('Instant english writing training', true);

INSERT INTO examinations(text, question_count, enabled) VALUES('AWS認定ソリューションアーキテクトアソシエイト(JP) 10問', 10, true);
INSERT INTO examinations(text, question_count, enabled) VALUES('Instant english writing training', 10, true);

INSERT INTO examinations_categories(examination_id, question_category_id, enabled) VALUES((select id from examinations where text = 'AWS認定ソリューションアーキテクトアソシエイト(JP) 10問'), (select id from question_categories where name = 'AWS-Certified-Solutions-Architect-Associate-JP'), true);
INSERT INTO examinations_categories(examination_id, question_category_id, enabled) VALUES((select id from examinations where text = 'Instant english writing training'), (select id from question_categories where name = 'Instant english writing training'), true);

INSERT INTO examinations_auth_public(examination_id) VALUES((select id from examinations where text = 'AWS認定ソリューションアーキテクトアソシエイト(JP) 10問'));
INSERT INTO examinations_auth_public(examination_id) VALUES((select id from examinations where text = 'Instant english writing training'));

-- INSERT INTO examinations_auth_users(examination_id, user_username) VALUES((select id from examinations where text = 'Instant english writing training'), 'user1');

INSERT INTO questions(question_type, text, question_category_id, enabled) VALUES('SINGLE_CHOICE', '次の構成のうち、最も可用性が高くなる構成はどれか？', (select id from question_categories where name = 'AWS-Certified-Solutions-Architect-Associate-JP'), true);
INSERT INTO questions(question_type, text, question_category_id, enabled) VALUES('MULTIPLE_CHOICE', '次のうち、利用者の責任で実施しなければいけないセキュリティ対策はどれか？2つ選べ。', (select id from question_categories where name = 'AWS-Certified-Solutions-Architect-Associate-JP'), true);
INSERT INTO questions(question_type, text, question_category_id, enabled) VALUES('TEXT', 'DNSを提供するサービスの名前は？', (select id from question_categories where name = 'AWS-Certified-Solutions-Architect-Associate-JP'), true);

INSERT INTO questions_auth_public(question_id) VALUES((select id from questions where question_type = 'SINGLE_CHOICE'));
INSERT INTO questions_auth_public(question_id) VALUES((select id from questions where question_type = 'MULTIPLE_CHOICE'));
INSERT INTO questions_auth_public(question_id) VALUES((select id from questions where question_type = 'TEXT'));

INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'SINGLE_CHOICE'), '1つのリージョン内の1つのAZに4台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', false, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'SINGLE_CHOICE'), '1つのリージョン内の2つのAZに各2台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', true, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'SINGLE_CHOICE'), '2つのリージョン内の各1つのAZに2台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', false, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'SINGLE_CHOICE'), '2つのリージョン内の各2つのAZに1台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', false, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'MULTIPLE_CHOICE'), 'EC2インスタンスの物理ホスト上のハイパーバイザのセキュリティパッチの適用', false, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'MULTIPLE_CHOICE'), 'S3上のデータの暗号化', true, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'MULTIPLE_CHOICE'), '物理ディスクの適切な廃棄', false, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'MULTIPLE_CHOICE'), 'EC2インスタンス上のOSのセキュリティパッチの適用', true, true);
INSERT INTO questions_answers(question_id, text, is_correct, enabled) VALUES((select id from questions where question_type = 'TEXT'), 'Route53', true, true);

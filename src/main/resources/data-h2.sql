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

INSERT INTO authorities(username, authority) VALUES('user1', 'ROLE_USER');

INSERT INTO question_categories(id, name) VALUES(1, 'AWS-Certified-Solutions-Architect-Associate-JP');

INSERT INTO examinations(id, text, question_count) VALUES(1, 'AWS認定ソリューションアーキテクトアソシエイト(JP) 10問', 1);

INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(1, 1, 1);

INSERT INTO examinations_auth_public(id, examination_id) VALUES(1, 1);

INSERT INTO questions(id, question_type, text, question_category_id) VALUES(1, 'SINGLE_CHOICE', '次の構成のうち、最も可用性が高くなる構成はどれか？', 1);
INSERT INTO questions(id, question_type, text, question_category_id) VALUES(2, 'MULTIPLE_CHOICE', '次のうち、利用者の責任で実施しなければいけないセキュリティ対策はどれか？2つ選べ。', 1);
INSERT INTO questions(id, question_type, text, question_category_id) VALUES(3, 'TEXT', 'DNSを提供するサービスの名前は？', 1);

INSERT INTO questions_auth_public(id, question_id) VALUES(1, 1);
INSERT INTO questions_auth_public(id, question_id) VALUES(2, 2);
INSERT INTO questions_auth_public(id, question_id) VALUES(3, 3);

INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(1, 1, '1つのリージョン内の1つのAZに4台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(2, 1, '1つのリージョン内の2つのAZに各2台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', TRUE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(3, 1, '2つのリージョン内の各1つのAZに2台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(4, 1, '2つのリージョン内の各2つのAZに1台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(5, 2, 'EC2インスタンスの物理ホスト上のハイパーバイザのセキュリティパッチの適用', FALSE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(6, 2, 'S3上のデータの暗号化', TRUE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(7, 2, '物理ディスクの適切な廃棄', FALSE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(8, 2, 'EC2インスタンス上のOSのセキュリティパッチの適用', TRUE);
INSERT INTO questions_answers(id, question_id, text, is_correct) VALUES(9, 3, 'Route53', TRUE);

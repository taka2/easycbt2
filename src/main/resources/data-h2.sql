INSERT INTO users(username, password, enabled) VALUES('user1', 'password', true);

INSERT INTO authorities(username, authority) VALUES('user1', 'ROLE_USER');

INSERT INTO question_categories(id, name) VALUES(1, 'AWS-Certified-Solutions-Architect-Associate-JP');

INSERT INTO examinations(id, text, question_count) VALUES(1, 'AWS認定ソリューションアーキテクトアソシエイト(JP) 10問', 1);

INSERT INTO examinations_categories(id, examination_id, question_category_id) VALUES(1, 1, 1);

INSERT INTO examinations_auth_public(id, examination_id) VALUES(1, 1);

INSERT INTO questions(id, question_type, text, question_category_id) VALUES(1, 'SINGLE_CHOICE', '次の構成のうち、最も可用性が高くなる構成はどれか？', 1);

INSERT INTO questions_auth_public(id, question_id) VALUES(1, 1);

INSERT INTO question_answers(id, question_id, text, is_correct) VALUES(1, 1, '1つのリージョン内の1つのAZに4台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);
INSERT INTO question_answers(id, question_id, text, is_correct) VALUES(2, 1, '1つのリージョン内の2つのAZに各2台のWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', TRUE);
INSERT INTO question_answers(id, question_id, text, is_correct) VALUES(3, 1, '2つのリージョン内の各1つのAZに2台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);
INSERT INTO question_answers(id, question_id, text, is_correct) VALUES(4, 1, '2つのリージョン内の各2つのAZに1台ずつWebサーバ(EC2)を配置し、ELBを用いて負荷分散する', FALSE);

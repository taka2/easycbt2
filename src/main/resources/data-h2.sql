DELETE FROM users;
INSERT INTO users(username, password, enabled) VALUES('admin', 'password', true);

DELETE FROM authorities;
INSERT INTO authorities(username, authority) VALUES('admin', 'ROLE_ADMIN');

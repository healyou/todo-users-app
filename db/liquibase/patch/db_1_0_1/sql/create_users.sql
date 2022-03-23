INSERT INTO users(username, password_hash)
	VALUES ('admin', '$2a$10$BuCZRCI7MgncH6Z66FqQX.Y.xHkg6mFkweVI/wwmQQAVfFsAef0Fu');
INSERT INTO user_roles(user_id, role_id)
	VALUES (
	    (select id from users where username = 'admin'),
	    (select id from roles where code = 'ROLE_ADMIN')
	);

INSERT INTO users(username, password_hash)
	VALUES ('user', '$2a$10$BaBxprqVH5QPRfK2q1QgcuvY0r0foJa51fgyAPQ1geBwcMA3nksJm');
INSERT INTO user_roles(user_id, role_id)
	VALUES (
	    (select id from users where username = 'user'),
	    (select id from roles where code = 'ROLE_USER')
	);
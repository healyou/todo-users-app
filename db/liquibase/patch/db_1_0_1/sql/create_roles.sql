INSERT INTO roles(code, description)
	VALUES ('ROLE_ADMIN', 'Администратор системы');
INSERT INTO role_privileges(role_id, privilege_id)
	VALUES (
	    (select id from roles where code = 'ROLE_ADMIN'),
	    (select id from privileges where code = 'CREATE_NOTE')
	);
INSERT INTO role_privileges(role_id, privilege_id)
	VALUES (
	    (select id from roles where code = 'ROLE_ADMIN'),
	    (select id from privileges where code = 'VIEW_NOTE_VERSION_HISTORY')
	);
INSERT INTO role_privileges(role_id, privilege_id)
	VALUES (
	    (select id from roles where code = 'ROLE_ADMIN'),
	    (select id from privileges where code = 'CHANGE_NOTE_VERSION')
	);

INSERT INTO roles(code, description)
	VALUES ('ROLE_USER', 'Пользователь системы без истории версии заметки');
INSERT INTO role_privileges(role_id, privilege_id)
	VALUES (
	    (select id from roles where code = 'ROLE_USER'),
	    (select id from privileges where code = 'CREATE_NOTE')
	);
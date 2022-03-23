CREATE TABLE user_privileges (
	id serial PRIMARY KEY,
	user_id INT NOT NULL,
	privilege_id INT NOT NULL,
	create_date TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_up_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_up_privileges FOREIGN KEY(privilege_id) REFERENCES privileges(id),
    UNIQUE (user_id, privilege_id)
);
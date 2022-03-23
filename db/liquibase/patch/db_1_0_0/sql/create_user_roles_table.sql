CREATE TABLE user_roles (
	id serial PRIMARY KEY,
	user_id INT NOT NULL,
	role_id INT NOT NULL,
	create_date TIMESTAMP NOT NULL DEFAULT NOW(),

	CONSTRAINT fk_ur_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_ur_roles FOREIGN KEY(role_id) REFERENCES roles(id),
    UNIQUE (user_id, role_id)
);
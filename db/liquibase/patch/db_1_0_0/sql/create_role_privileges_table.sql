CREATE TABLE role_privileges (
	id serial PRIMARY KEY,
	role_id INT NOT NULL,
	privilege_id INT NOT NULL,
	create_date TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_rp_roles FOREIGN KEY(role_id) REFERENCES roles(id),
    CONSTRAINT fk_rp_privileges FOREIGN KEY(privilege_id) REFERENCES privileges(id),
    UNIQUE (role_id, privilege_id)
);
CREATE TABLE users (
	id serial PRIMARY KEY,
	username VARCHAR (50) UNIQUE NOT NULL,
	password_hash VARCHAR (50) NOT NULL,
	create_date TIMESTAMP NOT NULL DEFAULT NOW()
);
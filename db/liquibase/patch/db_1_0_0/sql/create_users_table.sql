CREATE TABLE users (
	id serial PRIMARY KEY,
	username VARCHAR (50) UNIQUE NOT NULL,
    --	72 - max bcrypt length
	password_hash VARCHAR (72) NOT NULL,
	create_date TIMESTAMP NOT NULL DEFAULT NOW()
);
---------------------------------------------------------------------------------------------
-- SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
---------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Create customers DB table (used to store reservation holders' information).
---------------------------------------------------------------------------------------------
CREATE TABLE customers (
	id LONG SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(1000) NOT NULL,
	email VARCHAR(1000) NOT NULL UNIQUE,
	created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

---------------------------------------------------------------------------------------------
-- Create reservations DB table.
---------------------------------------------------------------------------------------------
CREATE TABLE reservations (
	id LONG SERIAL PRIMARY KEY NOT NULL,
	uuid UUID NOT NULL DEFAULT RANDOM_UUID(),
	customer_id LONG NOT NULL,
	check_in DATE NOT NULL,
	check_out DATE NOT NULL,
	created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

	FOREIGN KEY(customer_id) REFERENCES customers(id)
);

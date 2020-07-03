---------------------------------------------------------------------------------------------
-- SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
---------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Seed customers data.
---------------------------------------------------------------------------------------------
INSERT INTO customers (name, email) VALUES ('Yan Avery', 'yan_avery@yopmail.com');
INSERT INTO customers (name, email) VALUES ('John Parker', 'john_parker@yopmail.com');
INSERT INTO customers (name, email) VALUES ('Peter Hasbro', 'peter_hasbro@yopmail.com');

---------------------------------------------------------------------------------------------
-- Seed reservations data.
---------------------------------------------------------------------------------------------
-- 3 nights reservation
INSERT INTO reservations (customer_id, check_in, check_out)
    VALUES ((SELECT id FROM customers WHERE email = 'yan_avery@yopmail.com'), '2020-07-02', '2020-07-05');

-- 3 nights reservation
INSERT INTO reservations (customer_id, check_in, check_out)
    VALUES ((SELECT id FROM customers WHERE email = 'john_parker@yopmail.com'), '2020-07-05', '2020-07-08');

-- 2 nights reservation
INSERT INTO reservations (customer_id, check_in, check_out)
    VALUES ((SELECT id FROM customers WHERE email = 'peter_hasbro@yopmail.com'), '2020-07-08', '2020-07-10');

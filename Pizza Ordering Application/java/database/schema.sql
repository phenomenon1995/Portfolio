BEGIN TRANSACTION;

DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS invoice CASCADE;
DROP TABLE IF EXISTS pizza CASCADE;
DROP TABLE IF EXISTS pizza_product CASCADE;
DROP TABLE IF EXISTS invoice_product CASCADE;
DROP TABLE IF EXISTS product_category CASCADE;

CREATE TABLE product (
	product_id SERIAL,
	product_category_id int NOT NULL, 
	price numeric(4,2),
	description varchar(500),
	quantity int NOT NULL DEFAULT 10, 
	CONSTRAINT PK_product_id PRIMARY KEY (product_id)
);

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user_id PRIMARY KEY (user_id)
);

CREATE TABLE customer(
	customer_id SERIAL,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	street_address varchar(50),
	city varchar(50),
	zip_code numeric(5,0),
	state_abbreviation char(2),
	phone_number numeric(10,0) NOT NULL,
	email varchar(50) NOT NULL,
	user_id int UNIQUE, --NOT NULL--
	CONSTRAINT PK_customer_id  PRIMARY KEY (customer_id)
);

CREATE TABLE invoice (
	invoice_id SERIAL,
	user_id int NOT NULL, 
	total numeric(5,2) NOT NULL,
	is_delivery boolean NOT NULL,
	status varchar(50) NOT NULL,
	timestamp timestamp DEFAULT current_timestamp, 
	CONSTRAINT PK_invoice_id PRIMARY KEY (invoice_id)
);

CREATE TABLE pizza (
	pizza_id SERIAL,
	invoice_id int NOT NULL,
	pizza_name varchar(20),
	total numeric (4,2) NOT NULL, --UPDATE to SUM(*join product table using pizza_product to get price*)
	additional_instructions varchar(200), 
	CONSTRAINT PK_pizza_id PRIMARY KEY (pizza_id)
);
ALTER SEQUENCE pizza_pizza_id_seq RESTART WITH 100;

CREATE TABLE invoice_product(
	invoice_id int NOT NULL,
	product_id int NOT NULL 
);


CREATE TABLE pizza_product (
	pizza_id int NOT NULL,
	product_id int NOT NULL,
	CONSTRAINT FK_pizza_product_pizza_id FOREIGN KEY (pizza_id) REFERENCES pizza(pizza_id) ON DELETE CASCADE,
	CONSTRAINT FK_pizza_product_product_id FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE

);

CREATE TABLE product_category (
	product_category_id SERIAL,
	product_category_description varchar (50) NOT NULL,
	CONSTRAINT PK_product_catgory_id PRIMARY KEY (product_category_id)
);

ALTER TABLE pizza 
	ADD CONSTRAINT FK_pizza_invoice FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE
	
;

AlTER TABLE invoice 
	ADD CONSTRAINT FK_invoice_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
;

ALTER TABLE product
	ADD CONSTRAINT FK_product_category_id FOREIGN KEY (product_category_id) REFERENCES product_category(product_category_id)
;

ALTER TABLE invoice_product
	ADD CONSTRAINT FK_invoice_id FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE,
	ADD CONSTRAINT FK_product_id FOREIGN KEY (product_id) REFERENCES product (product_id) ON DELETE CASCADE
;

ALTER TABLE customer
	ADD CONSTRAINT FK_customer_user_userid FOREIGN KEY (user_id) REFERENCES users(user_id)
;
END TRANSACTION;

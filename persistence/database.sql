--<ScriptOptions statementTerminator=";"/>

ALTER TABLE address DROP CONSTRAINT address_person_fk;

ALTER TABLE person DROP CONSTRAINT person_pk;

ALTER TABLE address DROP CONSTRAINT address_pk;

DROP INDEX address_pk;

DROP INDEX person_pk;

DROP TABLE address;

DROP TABLE person;

CREATE TABLE address (
		id INT8 NOT NULL,
		person_id INT8,
		street TEXT(2147483647),
		city TEXT(2147483647),
		postcode INT2
	);

CREATE TABLE person (
		id INT8 NOT NULL,
		name TEXT(2147483647),
		dob DATE
	);

CREATE UNIQUE INDEX address_pk ON address (id ASC);

CREATE UNIQUE INDEX person_pk ON person (id ASC);

ALTER TABLE person ADD CONSTRAINT person_pk PRIMARY KEY (id);

ALTER TABLE address ADD CONSTRAINT address_pk PRIMARY KEY (id);

ALTER TABLE address ADD CONSTRAINT address_person_fk FOREIGN KEY (person_id)
	REFERENCES person (id);


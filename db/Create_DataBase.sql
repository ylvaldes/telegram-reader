CREATE USER "facturauser" WITH
        LOGIN
        CREATEDB
        CREATEROLE
        INHERIT
        NOREPLICATION
        CONNECTION LIMIT -1
        PASSWORD 'f@ctura';

		
CREATE DATABASE "facturauy" WITH OWNER = "facturauser";
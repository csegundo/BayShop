
-- 
-- El contenido de este fichero se cargará al arrancar la aplicación, suponiendo que uses
-- 		application-default ó application-externaldb en modo 'create'
--

-- Usuario de ejemplo con username = b y contraseña = aa  
INSERT INTO user(enabled,username,password,roles,first_name,last_name, email, baypoints, dinero) VALUES (
	1, 'a', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER,ADMIN,MODERATOR',
	'Alvaro', 'Lopez', 'a@gmail.com', 100, 100
);

-- Otro usuario de ejemplo con username = b y contraseña = aa  
INSERT INTO user(enabled,username,password,roles,first_name,last_name, email, baypoints, dinero) VALUES (
	1, 'b', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER,ADMIN',
	'Berta', 'Martinez', 'b@gmail.com', 250, 30
);

INSERT INTO user (enabled,username,password,roles,first_name,last_name, email, baypoints, dinero) VALUES (
	1,'c', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER,MODERATOR',
	'Carlos', 'Garcia', 'c@gmail.com', 100, 0
);


INSERT INTO user (enabled,username,password,roles,first_name,last_name, email, baypoints, dinero) VALUES (
	1,'d', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER',
	'David', 'Alejo', 'd@gmail.com', 100, 203
);
INSERT INTO user (enabled,username,password,roles,first_name,last_name, email, baypoints, dinero) VALUES (
	1,'e', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER',
	'Eustquio', 'Marin', 'e@gmail.com', 100, 100
);


-- Unos pocos auto-mensajes de prueba
INSERT INTO MESSAGE VALUES(1,NULL,'2020-03-23 10:48:11.074000','probando 1',3,1);
INSERT INTO MESSAGE VALUES(2,NULL,'2020-03-23 10:48:15.149000','probando 2',3,1);
INSERT INTO MESSAGE VALUES(3,NULL,'2020-03-23 10:48:18.005000','probando 3',1,3);
INSERT INTO MESSAGE VALUES(4,NULL,'2020-03-23 10:48:20.971000','probando 4',1,3);
INSERT INTO MESSAGE VALUES(5,NULL,'2020-03-23 10:48:22.926000','probando 5',1,3);


-- INSERT INTO SALE VALUES(1,'2020-06-23 10:48:11.074000',2,1, NULL);
-- INSERT INTO SALE VALUES(2,'2020-06-23 10:48:11.074000',2,1, NULL);


--Productos de ejemplo
INSERT INTO PRODUCT (enabled, user_id, name, description, creation_date, price, size, brand, status, categories, sale_id) VALUES 
	(1, 2, 'Camiseta chula', 'La vendo porque me queda grande', '2020-03-23 10:48:22.926000', 23.45, 'XL', 'Adidas', 0, 'Camiseta', NULL),
	(1, 2, 'Camiseta corta', 'La vendo porque me queda grande', '2020-03-24 10:48:22.926000', 15.25, 'XL', 'Reebok', 0, 'Camiseta', NULL),
	(1, 3, 'Camiseta fina', 'La vendo porque me queda pequeña', '2020-03-25 10:48:22.926000', 18.25, 'XS', 'Adidas', 0, 'Camiseta', NULL),
	(1, 3, 'Vaquero azul', 'Lo vendo porque me queda grande', '2020-03-26 10:48:22.926000', 24.50, 'XL', 'Adidas', 1, 'Albornoz', NULL),
	(1, 4, 'Abrigo Calentito', 'Lo vendo porque me queda grande', '2020-03-27 10:48:22.926000', 48.50, 'XL', 'Nike', 1, 'Abrigo', NULL),
	(1, 4, 'Chandal gris', 'Lo vendo porque me queda grande', '2020-03-29 10:48:22.926000', 34.50, 'XL', 'Adidas', 1, 'Chandal', NULL),
	(1, 5, 'Gorra plana', 'La vendo porque me queda pequeña', '2020-03-29 10:48:22.926000', 14.50, 'XL', 'P&B', 1, 'Gorra', NULL),
	(1, 5, 'Gorra azul', 'La vendo, es bonita per me queda grande', '2020-03-29 10:48:22.926000', 16.50, 'XL', 'Nike', 1, 'Gorra',NULL);



-- 
-- El contenido de este fichero se cargará al arrancar la aplicación, suponiendo que uses
-- 		application-default ó application-externaldb en modo 'create'
--

-- Usuario de ejemplo con username = b y contraseña = aa  
INSERT INTO user(enabled,username,password,roles,first_name,last_name, email, baypoints) VALUES (
	1, 'a', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'ADMIN',
	'Abundio', 'Ejémplez', 'abundio@gmail.com', 1
);

-- Otro usuario de ejemplo con username = b y contraseña = aa  
INSERT INTO user(enabled,username,password,roles,first_name,last_name, email, baypoints) VALUES (
	1, 'b', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER',
	'Berta', 'Muéstrez', 'berta@gmail.com', 2
);

INSERT INTO user (enabled,username,password,roles,first_name,last_name, email, baypoints) VALUES (
	1,'peri', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'MODERATOR,ADMIN,USER',
	'Pedro', 'Garcia', 'pericoLoko@hotmail.com', 3
);

-- Unos pocos auto-mensajes de prueba
INSERT INTO MESSAGE VALUES(1,NULL,'2020-03-23 10:48:11.074000','probando 1',1,1);
INSERT INTO MESSAGE VALUES(2,NULL,'2020-03-23 10:48:15.149000','probando 2',1,1);
INSERT INTO MESSAGE VALUES(3,NULL,'2020-03-23 10:48:18.005000','probando 3',1,1);
INSERT INTO MESSAGE VALUES(4,NULL,'2020-03-23 10:48:20.971000','probando 4',1,1);
INSERT INTO MESSAGE VALUES(5,NULL,'2020-03-23 10:48:22.926000','probando 5',1,1);


--Productos de ejemplo
INSERT INTO PRODUCT (enabled, user_id, name, description, creation_date, price, size, brand, status, categories) VALUES 
	(1, 2, 'Camiseta chula', 'La vendo porque me queda grande', '2020-03-23 10:48:22.926000', 23.45, 'XL', 'Adidas', 1, 'Camiseta'),
	(1, 2, 'Camiseta corta', 'La vendo porque me queda grande', '2020-03-24 10:48:22.926000', 15.25, 'XL', 'Reebok', 1, 'Camiseta'),
	(1, 2, 'Camiseta fina', 'La vendo porque me queda pequeña', '2020-03-25 10:48:22.926000', 18.25, 'XS', 'Adidas', 1, 'Camiseta'),
	(1, 2, 'Albornoz azul', 'Lo vendo porque me queda grande', '2020-03-26 10:48:22.926000', 24.50, 'XL', 'Adidas', 1, 'Albornoz'),
	(1, 2, 'Abrigo Calentito', 'Lo vendo porque me queda grande', '2020-03-27 10:48:22.926000', 48.50, 'XL', 'Nike', 1, 'Abrigo'),
	(1, 2, 'Chandal gris', 'Lo vendo porque me queda grande', '2020-03-29 10:48:22.926000', 34.50, 'XL', 'Adidas', 1, 'Chandal'),
	(1, 2, 'Gorra plana', 'La vendo porque me queda pequeña', '2020-03-29 10:48:22.926000', 14.50, 'XL', 'P&B', 1, 'Gorra');
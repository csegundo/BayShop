
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
	1,'ElPerico', 
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'USER',
	'Pedro', 'Garcia', 'pericoLoko@hotmail.com', 3
);

-- Unos pocos auto-mensajes de prueba
INSERT INTO MESSAGE VALUES(1,NULL,'2020-03-23 10:48:11.074000','probando 1',1,1);
INSERT INTO MESSAGE VALUES(2,NULL,'2020-03-23 10:48:15.149000','probando 2',1,1);
INSERT INTO MESSAGE VALUES(3,NULL,'2020-03-23 10:48:18.005000','probando 3',1,1);
INSERT INTO MESSAGE VALUES(4,NULL,'2020-03-23 10:48:20.971000','probando 4',1,1);
INSERT INTO MESSAGE VALUES(5,NULL,'2020-03-23 10:48:22.926000','probando 5',1,1);


--Productos de ejemplo
INSERT INTO PRODUCT (name, description, creation_date, price, size, brand, status, categories, enabled)
VALUES ( 'Camiseta chula', 'camiseta roja, me queda grande', '2012-09-17 18:47:52.69', 21.3, 'XL', 'NIKE', 0, 'SHIRT', '1');
Integrantes: 
Sebastian Facuse
Lucas Canales

El proyecto consiste en un sistema estilo "steam" como plataforma de videojuegos y comunidad.

Primero se deben levantar las bases de datos desde heidi, en mysql puerto 3306, con el siguiente codigo de consulta;

CREATE DATABASE db_usuarios;
CREATE DATABASE db_tienda;
CREATE DATABASE db_resenas;
CREATE DATABASE db_pagos;
CREATE DATABASE db_desarrolladores;
CREATE DATABASE db_categorias;
CREATE DATABASE db_amigos;
CREATE DATABASE db_logros;
CREATE DATABASE db_juegos;
CREATE DATABASE db_biblioteca;

El órden de encendido de los microservicios es:

1. ms-usuario
2. ms-juegos
3. ms-biblioteca
4. ms-tienda
5. ms-resenas
6. ms-logros
7. ms-amigos
8. ms-pagos
9. ms-categorias
10. ms-desarrolladores

ms-usuario

Formato raw json:
{
        "activo": true,
        "apellido": "Soto",
        "email": "gamer@example.com",
        "fechaRegistro": "2024-01-15",
        "id": 1,
        "nombre": "Carlos",
        "pais": "Chile",
        "saldo": 50000.00,
        "username": "gamer_pro",
        "password" : "1111111"
    }

GET http://localhost:8081/api/v1/usuarios -> para listar todos los usuarios

GET http://localhost:8081/api/v1/usuarios/{id} -> para buscar un usuario por su id

POST http://localhost:8081/api/v1/usuarios -> para crear un usuario

PUT http://localhost:8081/api/v1/usuarios/{id} -> para modificar un usuario

DELETE http://localhost:8081/api/v1/usuarios/{id} -> para eliminar un usuario

ms-juegos

{
        "activo": true,
        "desarrollador": "DarkStudio",
        "descripcion": "RPG de mundo abierto con más de 100 horas de contenido",
        "fechaLanzamiento": "2023-05-01",
        "genero": "RPG",
        "id": 1,
        "precio": 29990.00,
        "stock": 100,
        "titulo": "Shadow Realm"
    }

GET http://localhost:8082/api/v1/juegos -> listar todos los juegos

GET http://localhost:8082/api/v1/juegos/{id} -> para buscar por id

POST http://localhost:8082/api/v1/juegos -> para crear un juego

PUT http://localhost:8082/api/v1/juegos{id} -> para modificar un juego

DELETE http://localhost:8082/api/v1/juegos{id} -> para eliminar un juego

PATCH http://localhost:8082/api/v1/juegos/{id}/stock -> para modificar el stock de un juego

ms-biblioteca

{
        "activo": true,
        "desarrolladorJuego": "DarkStudio",
        "fechaAdquisicion": "2024-02-01",
        "generoJuego": "RPG",
        "horasJugadas": 120,
        "id": 1,
        "juegoId": 1,
        "nombreUsuario": "Carlos Soto",
        "precioJuego": 29990.00,
        "tituloJuego": "Shadow Realm",
        "usernameUsuario": "gamer_pro",
        "usuarioId": 1
    }

GET http://localhost:8083/api/v1/biblioteca -> muestra la biblioteca de juegos de todos los usuarios

GET http://localhost:8083/api/v1/biblioteca/{id} -> para mostrar segun el id de la biblioteca

GET http://localhost:8083/api/v1/biblioteca/usuario/{usuarioid} -> para mostrar todos los juegos en la biblioteca de **1** usuario

POST http://localhost:8083/api/v1/biblioteca -> agrega una nueva biblioteca

DELETE http://localhost:8083/api/v1/biblioteca{id} -> para eliminar una biblioteca

ms-tienda

{
        "desarrolladorJuego": "DarkStudio",
        "estado": "COMPLETADA",
        "fechaCompra": "2024-02-01T10:30:00",
        "generoJuego": "RPG",
        "id": 1,
        "juegoId": 1,
        "metodoPago": "SALDO",
        "nombreUsuario": "Carlos Soto",
        "precioPagado": 29990.00,
        "tituloJuego": "Shadow Realm",
        "usernameUsuario": "gamer_pro",
        "usuarioId": 1
    }

GET http://localhost:8084/api/v1/compras -> para listar todas las compras dentro de la plataforma

GET http://localhost:8084/api/v1/compras/{id] -> para mostrar segun el id de la compra

POST http://localhost:8084/api/v1/compras -> para realizar una compra

ms-resenas

{
        "activo": true,
        "contenido": "Shadow Realm me atrapó desde el primer minuto. Historia profunda y combate fluido.",
        "desarrolladorJuego": "DarkStudio",
        "fecha": "2024-02-10T12:00:00",
        "generoJuego": "RPG",
        "id": 1,
        "juegoId": 1,
        "nombreUsuario": "Carlos Soto",
        "puntuacion": 9,
        "titulo": "Increíble experiencia",
        "tituloJuego": "Shadow Realm",
        "usernameUsuario": "gamer_pro",
        "usuarioId": 1
    }

GET http://localhost:8085/api/v1/resenas -> para mostrar todas las reseñas a juegos

GET http://localhost:8085/api/v1/resenas/{id} -> para mostrar una reseña segun su id

POST http://localhost:8085/api/v1/resenas -> para crear una reseña

PUT http://localhost:8085/api/v1/resenas/{id} -> para modificar una reseña

DELETE http://localhost:8085/api/v1/resenas/{id} -> para borrar una reseña segun su id

ms-logros

{
        "condicion": "TENER_JUEGO",
        "descripcion": "Comienza tu aventura en Shadow Realm",
        "generoJuego": "RPG",
        "id": 1,
        "juegoId": 1,
        "nombre": "Primer Paso",
        "tituloJuego": "Shadow Realm"
    }

GET http://localhost:8086/api/v1/logros -> para mostrar todos los logros en la plataforma

GET http://localhost:8086/api/v1/logros/{id} -> para buscar un logro por su id

POST http://localhost:8086/api/v1/logros -> para crear un nuevo logro asociado a un juego

DELETE http://localhost:8086/api/v1/logros/{id} -> para eliminar un logro según su id

ms-amigos

GET http://localhost:8087/api/v1/amigos/usuario/{id} -> par buscar la lista de amigos de un usuario

GET http://localhost:8087/api/v1/amigos/{id} -> para buscar una amistad según su id

POST http://localhost:8087/api/v1/amigos/usuario/solicitud -> para enviar una solicitud de amigo

DELETe http://localhost:8087/api/v1/amigos/{id} -> para eliminar una amistad

ms-pagos

{
        "descripcion": "Recarga de saldo inicial",
        "estado": "COMPLETADO",
        "fechaPago": "2024-01-20T10:00:00",
        "id": 1,
        "metodoPago": "TARJETA",
        "monto": 50000.00,
        "nombreUsuario": "Carlos Soto",
        "usernameUsuario": "gamer_pro",
        "usuarioId": 1
    }

GET http://localhost:8088/api/v1/pagos -> para listar todos los pagos existentes

GET http://localhost:8088/api/v1/pagos/{id} -> para mostrar un pago según su id

POST http://localhost:8088/api/v1/pagos -> para procesar un pago

PUT http://localhost:8088/api/v1/pagos/{id} -> para  modificar un pago según su id

DELETE http://localhost:8088/api/v1/pagos/{id} -> para eliminar un pago según su id

ms-categorias 

{
        "activo": true,
        "descripcion": "Juegos de rol con historia profunda y desarrollo de personajes",
        "id": 1,
        "juegos": null,
        "nombre": "RPG"
    }

GET http://localhost:8089/api/v1/categorias -> para listar todas las categorias

GET http://localhost:8089/api/v1/categorias/{id} -> para buscar categoría según su id

POST http://localhost:8089/api/v1/categorias -> para crear una nueva categoría

PUT http://localhost:8089/api/v1/categorias/{id} -> para modificar una categoría

DELETE http://localhost:8089/api/v1/categorias/{id} -> para elimar una categoría según su id

ms-desarrolladores

{
        "activo": true,
        "descripcion": "Estudio indie especializado en RPGs oscuros",
        "id": 1,
        "juegos": null,
        "nombre": "DarkStudio",
        "pais": "Chile",
        "sitioWeb": "www.darkstudio.cl"
    }

GET http://localhost:8090/api/v1/desarrolladores -> para mostrar todos los desarrolladores

GET http://localhost:8090/api/v1/desarrolladores/{id} -> para mostrar un desarrollador según su id

POST http://localhost:8090/api/v1/desarrolladores -> para registrar un nuevo desarrollador

PUT http://localhost:8090/api/v1/desarrolladores/{id} -> para modificar un desarrollador por su id

DELETE http://localhost:8090/api/v1/desarrolladores/{id} -> para eliminar un desarrollador según su id

Eureka
http://localhost:8761

Swagger
ms-usuarios        http://localhost:8081/doc/swagger-ui/index.html
ms-juegos          http://localhost:8082/doc/swagger-ui/index.html
ms-biblioteca      http://localhost:8083/doc/swagger-ui/index.html
ms-tienda          http://localhost:8084/doc/swagger-ui/index.html
ms-resenas         http://localhost:8085/doc/swagger-ui/index.html
ms-logros          http://localhost:8086/doc/swagger-ui/index.html
ms-amigos          http://localhost:8087/doc/swagger-ui/index.html
ms-pagos           http://localhost:8088/doc/swagger-ui/index.html
ms-categorias      http://localhost:8089/doc/swagger-ui/index.html
ms-desarrolladores http://localhost:8090/doc/swagger-ui/index.html

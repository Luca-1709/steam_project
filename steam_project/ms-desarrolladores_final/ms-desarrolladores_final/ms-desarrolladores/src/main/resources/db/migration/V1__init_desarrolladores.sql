CREATE TABLE desarrollador (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL UNIQUE,
    pais        VARCHAR(60)  NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    sitio_web   VARCHAR(150),
    activo      BOOLEAN      NOT NULL DEFAULT TRUE
);
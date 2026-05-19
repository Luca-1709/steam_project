CREATE TABLE categoria (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(80)  NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    activo      BOOLEAN      NOT NULL DEFAULT TRUE
);
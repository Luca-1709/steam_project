CREATE TABLE logro (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    juego_id    INT          NOT NULL,
    condicion   VARCHAR(100) NOT NULL
);

CREATE TABLE usuario_logro (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id   INT      NOT NULL,
    logro_id     INT      NOT NULL,
    fecha_logro  DATETIME NOT NULL,
    CONSTRAINT uq_usuario_logro UNIQUE (usuario_id, logro_id)
);
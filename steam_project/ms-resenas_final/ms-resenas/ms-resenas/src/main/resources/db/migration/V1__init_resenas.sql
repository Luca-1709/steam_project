CREATE TABLE resena (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id   INT       NOT NULL,
    juego_id     INT       NOT NULL,
    puntuacion   INT          NOT NULL,
    titulo       VARCHAR(150) NOT NULL,
    contenido    TEXT         NOT NULL,
    fecha        DATETIME     NOT NULL,
    activo       BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_usuario_juego UNIQUE (usuario_id, juego_id),
    CONSTRAINT chk_puntuacion   CHECK  (puntuacion BETWEEN 1 AND 10)
);
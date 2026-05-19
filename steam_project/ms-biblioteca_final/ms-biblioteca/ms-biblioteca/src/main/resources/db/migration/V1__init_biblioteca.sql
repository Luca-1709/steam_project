CREATE TABLE entrada_biblioteca (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT         NOT NULL,
    juego_id         INT         NOT NULL,
    fecha_adquisicion DATE          NOT NULL,
    horas_jugadas    INT            NOT NULL DEFAULT 0,
    activo           BOOLEAN        NOT NULL DEFAULT TRUE,

    -- Un usuario no puede tener el mismo juego dos veces
    CONSTRAINT uq_usuario_juego UNIQUE (usuario_id, juego_id)
);
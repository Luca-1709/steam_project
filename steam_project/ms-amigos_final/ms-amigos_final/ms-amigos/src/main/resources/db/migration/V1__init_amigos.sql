CREATE TABLE amistad (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT         NOT NULL,
    amigo_id         INT         NOT NULL,
    fecha_amistad    DATETIME    NOT NULL,
    estado           VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT uq_amistad UNIQUE (usuario_id, amigo_id),
    CONSTRAINT chk_no_auto_amistad CHECK (usuario_id <> amigo_id)
);
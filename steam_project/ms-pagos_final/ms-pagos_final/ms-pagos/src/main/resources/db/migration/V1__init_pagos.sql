CREATE TABLE pago (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id      INT            NOT NULL,
    monto           DECIMAL(10,2)  NOT NULL,
    metodo_pago     VARCHAR(50)    NOT NULL,
    estado          VARCHAR(20)    NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago      DATETIME       NOT NULL,
    descripcion     VARCHAR(255)
);
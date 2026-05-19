CREATE TABLE compra (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT         NOT NULL,
    juego_id         INT         NOT NULL,
    precio_pagado    DECIMAL(10,2)  NOT NULL,
    fecha_compra     DATETIME       NOT NULL,
    estado           VARCHAR(30)    NOT NULL DEFAULT 'COMPLETADA',
    metodo_pago      VARCHAR(50)    NOT NULL
);
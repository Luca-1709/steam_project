CREATE TABLE juego (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    titulo              VARCHAR(150) NOT NULL,
    descripcion         TEXT,
    precio              DECIMAL(10,2) NOT NULL,
    fecha_lanzamiento   DATE,
    desarrollador       VARCHAR(100),
    genero              VARCHAR(80),
    imagen_url          VARCHAR(255),
    stock               INT          NOT NULL DEFAULT 0,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE
);
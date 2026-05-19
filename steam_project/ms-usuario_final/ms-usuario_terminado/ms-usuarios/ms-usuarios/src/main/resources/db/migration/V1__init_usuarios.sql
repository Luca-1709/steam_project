CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    pais VARCHAR(50),
    saldo DECIMAL(10,2),
    fecha_registro DATE,
    activo BOOLEAN
);

INSERT INTO usuario (username, email, password, nombre, apellido, pais, saldo, fecha_registro, activo)
VALUES 
('gamer_pro', 'gamer@example.com', '1234', 'Carlos', 'Soto', 'Chile', 50000.00, '2024-01-15', true),
('nitro_x', 'nitro@example.com', '1234', 'Valentina', 'Mora', 'Argentina', 30000.00, '2024-02-20', true),
('darkwolf', 'dark@example.com', '1234', 'Pedro', 'Nuñez', 'Colombia', 10000.00, '2024-03-10', true);
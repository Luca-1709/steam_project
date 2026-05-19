INSERT INTO logro (nombre, descripcion, juego_id, condicion)
VALUES
('Primer Paso',    'Comienza tu aventura en Shadow Realm',     1, 'TENER_JUEGO'),
('Veterano',       'Acumula 100 horas en Shadow Realm',        1, 'HORAS_100'),
('Coleccionista',  'Ten 3 o mas juegos en tu biblioteca',      1, 'JUEGOS_3'),
('Piloto Novato',  'Comienza tu carrera en Turbo Racer X',     2, 'TENER_JUEGO'),
('Dragon Supremo', 'Comienza tu aventura en Dragon Quest Z',   5, 'TENER_JUEGO');

INSERT INTO usuario_logro (usuario_id, logro_id, fecha_logro)
VALUES
(1, 1, '2024-02-01 12:00:00'),
(1, 2, '2024-04-01 18:00:00'),
(1, 3, '2024-04-10 10:00:00'),
(2, 4, '2024-04-01 17:00:00'),
(3, 5, '2024-06-01 20:00:00');
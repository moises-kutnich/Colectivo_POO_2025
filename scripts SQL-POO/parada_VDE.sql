-- =============================
-- Tabla y datos: PARADA (Viedma)
-- =============================
DROP TABLE IF EXISTS parada CASCADE;

CREATE TABLE parada (
  codigo INTEGER PRIMARY KEY,
  direccion TEXT NOT NULL,
  latitud DOUBLE PRECISION NOT NULL,
  longitud DOUBLE PRECISION NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Datos
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (1, 'Terminal de Ómnibus de Viedma', -40.82052, -63.0043, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (2, 'Avenida Juan Domingo Perón, 100', -40.8235, -63.0062, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (3, 'Álvaro Barros, 200', -40.8118, -62.9861, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (4, 'México, 100', -40.8182, -62.9995, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (5, 'Colón, 100', -40.8176, -63.0011, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (6, 'Zatti, Plaza Primera Junta', -40.81347, -62.99575, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (7, 'Zatti entre Guido y Alberdi', -40.8129, -62.9978, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (8, 'Zatti entre Suipacha y Rawson', -40.8121, -62.9938, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (9, 'Avenida Leloir, Rotonda', -40.82988, -62.97363, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (10, 'Rotonda Cooperación', -40.8297, -62.9739, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (11, 'Ruta Provincial 1, Rotonda acceso', -40.8306, -62.9802, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (12, 'San Martín, 223', -40.8093, -62.9962, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (13, '25 de Mayo, 521', -40.8086, -62.9945, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (14, 'Sarmiento, 280', -40.8105, -62.9928, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (15, 'Sarmiento y 7 de Marzo', -40.8112, -62.9899, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (16, 'Gallardo y San Luis', -40.8155, -62.9992, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (17, 'Gallardo e Ituzaingó', -40.8164, -62.9966, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (18, 'Cagliero y Francisco de Viedma (Costanera)', -40.8069, -62.9848, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (19, 'Coirón y Don Bosco', -40.8242, -63.0095, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (20, 'Choique y Don Bosco', -40.8251, -63.0112, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (21, 'Zatti y 25 de Mayo', -40.8108, -62.9951, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (22, 'Guido y Rivadavia', -40.8078, -62.9969, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (23, 'Rivadavia y Garrone', -40.8064, -62.9994, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (24, 'Boulevard Contín y Mitre', -40.8055, -63.0022, FALSE);
INSERT INTO parada (codigo, direccion, latitud, longitud, is_deleted) VALUES (25, 'Belgrano y Álvaro Barros', -40.8109, -62.9881, FALSE);

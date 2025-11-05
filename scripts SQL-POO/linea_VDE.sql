-- ============================
-- Tabla y datos: LINEA (Viedma)
-- ============================
DROP TABLE IF EXISTS linea CASCADE;

CREATE TABLE linea (
  codigo VARCHAR(10) PRIMARY KEY,
  nombre TEXT NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Datos
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L1I','Linea 1 Ida', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L1R','Linea 1 Regreso', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L2I','Linea 2 Ida', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L2R','Linea 2 Regreso', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L3I','Linea 3 Ida (Costanera–Centro)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L3R','Linea 3 Regreso (Centro–Costanera)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L4I','Linea 4 Ida (Terminal–Rotondas)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L4R','Linea 4 Regreso (Rotondas–Terminal)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L5I','Linea 5 Ida (Barrios Sur–Centro)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L5R','Linea 5 Regreso (Centro–Barrios Sur)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L6I','Linea 6 Ida (Zatti)', FALSE);
INSERT INTO linea (codigo, nombre, is_deleted) VALUES ('L6R','Linea 6 Regreso (Zatti)', FALSE);

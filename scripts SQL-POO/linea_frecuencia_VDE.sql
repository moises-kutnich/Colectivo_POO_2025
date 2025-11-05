-- ====================================
-- Tabla y datos: LINEA_FRECUENCIA (Viedma)
-- ====================================
DROP TABLE IF EXISTS linea_frecuencia CASCADE;

CREATE TABLE linea_frecuencia (
  id SERIAL PRIMARY KEY,
  linea VARCHAR(10) NOT NULL REFERENCES linea(codigo),
  diasemana SMALLINT NOT NULL CHECK (diasemana BETWEEN 1 AND 7), -- 1=lun ... 7=dom
  hora TIME NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Datos
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 1, '07:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 1, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 1, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 1, '17:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 1, '18:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 2, '07:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 2, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 2, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 2, '17:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 2, '18:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 3, '07:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 3, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 3, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 3, '17:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 3, '18:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 4, '07:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 4, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 4, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 4, '17:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 4, '18:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 5, '07:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 5, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 5, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 5, '17:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 5, '18:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 6, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 6, '10:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 6, '12:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 7, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1I', 7, '11:00', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 1, '07:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 1, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 1, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 1, '17:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 1, '18:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 2, '07:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 2, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 2, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 2, '17:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 2, '18:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 3, '07:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 3, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 3, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 3, '17:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 3, '18:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 4, '07:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 4, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 4, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 4, '17:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 4, '18:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 5, '07:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 5, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 5, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 5, '17:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 5, '18:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 6, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 6, '10:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 6, '12:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 7, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L1R', 7, '11:30', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 1, '07:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 1, '08:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 2, '07:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 2, '08:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 3, '07:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 3, '08:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 4, '07:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 4, '08:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 5, '07:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 5, '08:15', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 6, '09:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2I', 7, '09:00', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 1, '07:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 1, '08:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 2, '07:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 2, '08:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 3, '07:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 3, '08:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 4, '07:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 4, '08:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 5, '07:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 5, '08:45', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 6, '09:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L2R', 7, '09:30', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 1, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 2, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 3, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 4, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 5, '08:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 6, '10:00', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3I', 7, '10:00', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 1, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 2, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 3, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 4, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 5, '08:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 6, '10:30', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L3R', 7, '10:30', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 1, '07:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 2, '07:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 3, '07:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 4, '07:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 5, '07:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 6, '09:05', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4I', 7, '09:05', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 1, '07:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 2, '07:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 3, '07:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 4, '07:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 5, '07:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 6, '09:35', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L4R', 7, '09:35', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 1, '07:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 2, '07:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 3, '07:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 4, '07:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 5, '07:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 6, '08:10', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5I', 7, '08:10', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 1, '07:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 2, '07:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 3, '07:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 4, '07:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 5, '07:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 6, '08:40', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L5R', 7, '08:40', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 1, '07:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 2, '07:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 3, '07:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 4, '07:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 5, '07:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 6, '09:20', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6I', 7, '09:20', FALSE);

INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 1, '07:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 2, '07:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 3, '07:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 4, '07:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 5, '07:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 6, '09:50', FALSE);
INSERT INTO linea_frecuencia (linea, diasemana, hora, is_deleted) VALUES ('L6R', 7, '09:50', FALSE);

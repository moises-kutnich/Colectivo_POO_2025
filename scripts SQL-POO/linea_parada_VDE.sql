-- ==================================
-- Tabla y datos: LINEA_PARADA (Viedma)
-- ==================================
DROP TABLE IF EXISTS linea_parada CASCADE;

CREATE TABLE linea_parada (
  linea   VARCHAR(10) NOT NULL REFERENCES linea(codigo),
  parada  INTEGER NOT NULL REFERENCES parada(codigo),
  secuencia INTEGER NOT NULL,
  PRIMARY KEY (linea, secuencia),
  UNIQUE (linea, parada)
);

-- Datos
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 1, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 2, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 3, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 4, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 5, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 6, 6);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 7, 7);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 8, 8);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 9, 9);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 10, 10);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1I', 11, 11);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 11, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 10, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 9, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 8, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 7, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 6, 6);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 5, 7);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 4, 8);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 3, 9);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 2, 10);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L1R', 1, 11);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 6, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 21, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 12, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 13, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 14, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 15, 6);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 22, 7);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 23, 8);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 24, 9);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2I', 25, 10);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 25, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 24, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 23, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 22, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 15, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 14, 6);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 13, 7);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 12, 8);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 21, 9);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L2R', 6, 10);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 18, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 21, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 12, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 14, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 15, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3I', 22, 6);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 22, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 15, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 14, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 12, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 21, 5);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L3R', 18, 6);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4I', 1, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4I', 6, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4I', 9, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4I', 10, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4I', 11, 5);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4R', 11, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4R', 10, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4R', 9, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4R', 6, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L4R', 1, 5);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5I', 19, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5I', 20, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5I', 5, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5I', 21, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5I', 12, 5);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5R', 12, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5R', 21, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5R', 5, 3);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5R', 20, 4);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L5R', 19, 5);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6I', 7, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6I', 8, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6I', 21, 3);

INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6R', 21, 1);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6R', 8, 2);
INSERT INTO linea_parada (linea, parada, secuencia) VALUES ('L6R', 7, 3);

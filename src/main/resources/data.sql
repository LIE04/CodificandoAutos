-- Datos de prueba para desarrollo local.
-- Como la BD es H2 en memoria (se recrea vacía en cada arranque, ver application.yml
-- hibernate.ddl-auto: create-drop), este script se ejecuta automáticamente después de
-- que Hibernate crea las tablas, para poder probar las pantallas sin capturar todo a mano.
--
-- Usa subconsultas para ligar las llaves foráneas (en vez de IDs fijos) para no interferir
-- con el contador interno de las columnas IDENTITY.

-- Clientes de prueba
INSERT INTO CLIENTE (nombre, telefono) VALUES ('Juan Pérez', '5512345678');
INSERT INTO CLIENTE (nombre, telefono) VALUES ('María López', '5598765432');

-- Vehículos de prueba, ligados a los clientes de arriba
INSERT INTO VEHICULO (marca, modelo, placas, anio, kilometraje, id_cliente)
  VALUES ('Nissan', 'Versa', 'ABC-123', 2022, 35000.0, (SELECT id_cliente FROM CLIENTE WHERE nombre = 'Juan Pérez'));
INSERT INTO VEHICULO (marca, modelo, placas, anio, kilometraje, id_cliente)
  VALUES ('Toyota', 'Hilux', 'XYZ-789', 2023, 12000.0, (SELECT id_cliente FROM CLIENTE WHERE nombre = 'María López'));

-- Distribuidores de prueba (HU-25)
INSERT INTO DISTRIBUIDOR (nombre, telefono, correo, direccion, tipo_refaccion)
  VALUES ('Refaccionaria Central', '5511112222', 'ventas@refaccentral.com', 'Av. Reforma 123, CDMX', 'frenos');
INSERT INTO DISTRIBUIDOR (nombre, telefono, correo, direccion, tipo_refaccion)
  VALUES ('AutoPartes del Valle', '5533334444', 'contacto@autopartesvalle.com', 'Calle Hidalgo 45, CDMX', 'motor');
INSERT INTO DISTRIBUIDOR (nombre, telefono, correo, direccion, tipo_refaccion)
  VALUES ('Distribuidora Norte', '5555556666', 'info@distnorte.com', 'Blvd. Norte 900, CDMX', 'suspensión');

-- Historial de servicio de ejemplo, ligado al vehículo con placas ABC-123 (HU-29)
INSERT INTO SERVICIO (fecha, descripcion, piezas_utilizadas, costo_mano_obra, observaciones, fecha_hora_registro, vehiculo_id_vehiculo)
  VALUES ('2026-06-01', 'Cambio de balatas delanteras', 'Balatas delanteras', 350.0, 'Cliente reportó ruido al frenar', '2026-06-01 10:30:00', (SELECT id_vehiculo FROM VEHICULO WHERE placas = 'ABC-123'));

-- Inventario de piezas de ejemplo (HU-31)
INSERT INTO PIEZA_INVENTARIO (nombre, cantidad, proveedor, costo_unitario, fecha_recepcion, fecha_hora_registro)
  VALUES ('Balatas delanteras', 20, 'Refaccionaria Central', 180.0, '2026-05-01', '2026-05-01 09:00:00');
INSERT INTO PIEZA_INVENTARIO (nombre, cantidad, proveedor, costo_unitario, fecha_recepcion, fecha_hora_registro)
  VALUES ('Filtro de aceite', 15, 'AutoPartes del Valle', 95.0, '2026-05-10', '2026-05-10 11:00:00');

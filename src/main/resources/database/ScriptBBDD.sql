	-- Crear la base de datos
DROP DATABASE IF EXISTS tienda_lol_2;
CREATE DATABASE tienda_lol_2;
USE tienda_lol_2;


	-- Crear la tabla de usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);


	-- Crear la tabla de categorías
CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

	-- Crear la tabla de productos
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(12,2) NOT NULL,  -- Cambiado para mayor precisión
    imagen_url VARCHAR(255),
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE
);




	-- Crear la tabla de pedidos
CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    estado ENUM('Pendiente', 'Enviado', 'Entregado') NOT NULL DEFAULT 'Pendiente',  -- ENUM en lugar de FK
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario (usuario_id)
);



	-- Crear la tabla intermedia para la relación N:N entre pedidos y productos
CREATE TABLE pedido_producto (
    pedido_id BIGINT,
    producto_id BIGINT,
    cantidad INT NOT NULL DEFAULT 1,
    PRIMARY KEY (pedido_id, producto_id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);



	-- Crear la tabla de detalles de pedido (1:1 con pedidos)
CREATE TABLE detalle_pedido (
    pedido_id BIGINT PRIMARY KEY,
    direccion_envio VARCHAR(255) NOT NULL,
    metodo_pago ENUM('Tarjeta', 'PayPal', 'Transferencia') NOT NULL,  -- ENUM para los métodos de pago
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);



	-- Crear la tabla de inventario
CREATE TABLE inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);
	-- Crear un trigger para actualizar el inventario después de realizar un pedido
DELIMITER $$

CREATE TRIGGER verificar_inventario
BEFORE INSERT ON pedido_producto
FOR EACH ROW
BEGIN
    DECLARE stock_disponible INT;

    -- Obtener el stock disponible
    SELECT cantidad INTO stock_disponible FROM inventario WHERE producto_id = NEW.producto_id;

    -- Verificar si hay suficiente stock
    IF stock_disponible IS NULL OR stock_disponible < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente';
    ELSE
        -- Descontar el inventario antes de insertar el pedido
        UPDATE inventario
        SET cantidad = cantidad - NEW.cantidad
        WHERE producto_id = NEW.producto_id;
    END IF;
END;


DELIMITER ;

CREATE INDEX idx_producto ON inventario (producto_id);
CREATE INDEX idx_pedido_producto ON pedido_producto (pedido_id, producto_id);










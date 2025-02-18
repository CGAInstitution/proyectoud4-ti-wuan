	-- Crear la base de datos
DROP DATABASE IF EXISTS tienda_lol;
CREATE DATABASE tienda_lol;
USE tienda_lol;


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
    administrador BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario (usuario_id)
);



	-- Crear la tabla intermedia para la relación N:N entre pedidos y productos
CREATE TABLE pedido_producto (
    pedido_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT,
    cantidad INT NOT NULL DEFAULT 1,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);



	-- Crear la tabla de detalles de pedido (1:1 con pedidos)
CREATE TABLE detalle_pedido (
    pedido_id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
CREATE INDEX idx_producto ON inventario (producto_id);
CREATE INDEX idx_pedido_producto ON pedido_producto (pedido_id, producto_id);










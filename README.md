# Tienda Online de League of Legends

## 📌 Supuesto
Este proyecto consiste en el desarrollo de una tienda online basada en la temática de *League of Legends*, donde los usuarios pueden registrarse, iniciar sesión, explorar productos y realizar compras. La aplicación maneja un carrito de compras, pedidos y sesiones de usuario.

## 📊 Diagrama ER de la Base de Datos (Crow's Foot Model)
![Diagrama ETCFM](/src/main/resources/static/img/SQL.png)

### Explicación del modelo de datos
- **Usuario**: Representa a los usuarios registrados. Contiene información como email, contraseña, nombre, rol (admin/usuario) y pedidos asociados.
- **Pedido**: Contiene la información de las compras realizadas por cada usuario.
- **DetallePedido**: Relaciona el detalle único de cada pedido con el pedido en sí. 
- **Producto**: Lista los productos disponibles con nombre, descripción, precio, imagen y a la categoría que pertenecen.
- **Categoría**: Permite clasificar los productos en tipos (Ej. Figuras, Camisetas, Teclados).
- **Inventario**: Relaciona los productos con la cantidad disponible en stock.
- **PedidoProducto**: Relaciona los productos con los pedidos y la cantidad comprada.

---

## ⚙️ Manual Técnico para Desarrolladores

### 📂 Estructura del Proyecto
```
├── src
│   ├── main
│   │   ├── java/madstodolist/controller
│   │   │   ├── CarritoController.java
│   │   │   ├── LoginController.java
│   │   │   ├── AdminController.java
│   │   │   ├── CamisetasController.java
│   │   │   ├── FigurasController.java
│   │   │   ├── PerfilController.java
│   │   │   ├── TiendaController.java
│   │   │   ├── TecladosController.java
│   │   │
│   │   ├── java/madstodolist/model
│   │   │   ├── Usuario.java
│   │   │   ├── Pedido.java
│   │   │   ├── DetallePedido.java
│   │   │   ├── Producto.java
│   │   │   ├── Categoria.java
│   │   │   ├── Inventario.java
│   │   │   ├── PedidoProducto.java
│   │   │
│   │   ├── java/madstodolist/service
│   │   │   ├── UsuarioService.java
│   │   │   ├── PedidoService.java
│   │   │   ├── CategoriaService.java
│   │   │   ├── ProductoService.java
│   │   │   ├── InitDbService.java
│   │   │
│   │   ├── java/madstodolist/repository
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── PedidoRepository.java
│   │   │   ├── CategoriaRepository.java
│   │   │   ├── InventarioRepository.java
│   │   │   ├── ProductoRepository.java
│   │   │   ├── PedidoProductoRepository.java
│   │   │   ├── DetallePedidoRepository.java
│   │   │
│   │   ├── java/madstodolist/dto
│   │   │   ├── UsuarioData.java
│   │   │   ├── PedidoData.java
│   │   │   ├── CategoriaData.java
│   │   │   ├── ProductoData.java
│   │   │   ├── LoginData.java
│   │   │   ├── RegistroData.java
```

### 🖥️ Tecnologías Utilizadas
- **Spring Boot** para la gestión del backend
- **Thymeleaf** para la renderización de vistas dinámicas en el frontend
- **Bootstrap 5** para el diseño responsivo
- **JPA/Hibernate** para la gestión de la base de datos
- **H2 Database** para la base de datos en memoria y MySQL en AWS RDS

### 🔥 Ejemplo de Código Relevante
#### Método en `PedidoService.java` para agregar productos al carrito
```java
@Transactional
public List<String> crearPedido(PedidoData pedidoData, Long usuarioId) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
    if (usuarioOpt.isEmpty()) {
        throw new RuntimeException("Usuario no encontrado.");
    }

    Usuario usuario = usuarioOpt.get();
    List<Producto> productos = pedidoData.getProductos();

    // Verificar si hay suficiente stock para todos los productos
    List<String> mensajesDeError = verificarStock(productos);
    if (!mensajesDeError.isEmpty()) {
        return mensajesDeError;
    }
    
    Pedido nuevoPedido = new Pedido();
    nuevoPedido.setFecha(pedidoData.getFecha());
    nuevoPedido.setEstado(pedidoData.getEstado());
    nuevoPedido.setTotal(productos.stream().mapToDouble(Producto::getPrecio).sum());
    nuevoPedido.setUsuario(usuario);

    DetallePedido detalle = new DetallePedido();
    detalle.setPedido(nuevoPedido);
    detalle.setDireccionEnvio(pedidoData.getDetallePedido().getDireccionEnvio());
    detalle.setMetodoPago(pedidoData.getDetallePedido().getMetodoPago());
    nuevoPedido.setDetallePedido(detalle);

    Map<Long, Long> conteoProductos = productos.stream()
            .collect(Collectors.groupingBy(Producto::getId, Collectors.counting()));

    List<PedidoProducto> pedidoProductos = conteoProductos.entrySet().stream().map(entry -> {
        Long productoId = entry.getKey();
        int cantidad = entry.getValue().intValue();

        Producto producto = productos.stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en la lista"));

        PedidoProducto pedidoProducto = new PedidoProducto();
        pedidoProducto.setPedido(nuevoPedido);
        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        pedidoProducto.setProducto(new Producto(producto.getId(), producto.getNombre(),
                producto.getDescripcion(), producto.getPrecio(), producto.getImagenUrl(),
                categoria));
        pedidoProducto.setCantidad(cantidad);
        return pedidoProducto;
    }).collect(Collectors.toList());

    nuevoPedido.setPedidoProductos(pedidoProductos);
    
    pedidoRepository.save(nuevoPedido);
    pedidoRepository.flush();

    // Reducir el stock después de guardar el pedido
    reducirStockPedido(pedidoProductos);

    return List.of("Pedido creado con éxito.");
}
```
### 📌 Explicación:

1. **Verificar Stock**: Se comprueba si hay suficiente stock para los productos seleccionados.
2. **Creación de Pedido**: Se crea un nuevo pedido con la información proporcionada.
3. **Creación de Detalle de Pedido**: Se añade la dirección de envío y método de pago.
4. **Creación de PedidoProducto**: Se relacionan los productos con el pedido y se calcula el total.
5. **Calculo de unidades de un mismo producto**: Se agrupan los productos por ID y se cuentan las unidades.
6. **Guardar en Base de Datos**: Se guardan los datos en la base de datos y se actualiza el stock.
7. **Retorno de Mensajes**: Se devuelve un mensaje de éxito o error.

---

## 📖 Manual de Usuario
### Funcionalidades Principales

#### _Vista de Usuario_
1. **Registro de Usuario**: Permite a los nuevos usuarios crear una cuenta.
2. **Inicio de Sesión**: Acceso mediante email y contraseña.
3. **Exploración de Productos**: Listado de productos organizados por categorías.
4. **Carrito de Compras**: Los usuarios pueden añadir o eliminar productos.
5. **Finalización de Compra**: Se ingresa la dirección y método de pago en una ventana modal.
6. **Historial de Pedidos**: Los usuarios pueden ver sus compras anteriores.


#### _Vista de Administrador_

1. **Gestión de Productos**: CRUD de productos.
2. **Gestión de Usuarios**: CRUD de usuarios.
3. **Gestión de Stock**: Actualización de unidades disponibles de cada producto.

### 🎬 Video Tutorial 

[Link drive](https://drive)

---

## 🛠️ Explicación de GitProject y Reparto de Issues

### 📌 Repositorio y Gestión en GitHub
El equipo utilizó *GitHub Projects* para la planificación y seguimiento de tareas.

**Distribución de Tareas:**
- **Sergio Prieto**: Desarrollo de la vista principal de la tienda, y del carrito, con toda su lógica. Corrección de errores y creación de tests con JUnit.
- **David Baquero**: Desarrollo de la vista de Administrador, con toda su lógica. Corrección de errores y creación de tests JUnit.
- **Manuel Cendón**: Desarrollo de la vista y lógica de las tres categorías de productos actuales y la gestión del stock. Corrección de errores y integración de AWS.


**Panel del proyecto en github:**

[Panel proyecto](/src/main/resources/static/img/panelGithub.)

---

## 🚀 Propuestas de Mejora
### 🔹 Funcionalidades Futuras
1. **Búsqueda y filtrado de productos** por nombre, precio y categoría.
2. **Implementación de pagos reales** con PayPal o tarjeta de crédito
3. **Sistema de notificaciones** por email sobre el estado del pedido.
4. **Mejora en la interfaz gráfica** con un menuBar mas adecuado para una tienda con atajos a los diferentes productos.


---

## 📌 Conclusiones y Opinión del Trabajo

### **✅ Logros**
- Desarrollo de una tienda funcional con gestión de usuarios y compras.
- Uso de **Spring Boot** y **Thymeleaf**, fortaleciendo el aprendizaje en backend y frontend.
- Aprendizaje de **bootstrap 5** para el diseño de la interfaz gráfica.
- Integración de **H2 Database** y **MySQL** en AWS RDS.

### **⏳ Dedicación Temporal**
| Integrante    | Horas Estimadas |
|---------------|-----------------|
| Manuel Cendón | 33h             |
| David Baquero | 31h             |
| Sergio Prieto | 30h             |

### **📊 Cualificación Estimada**
- **Backend:** 8/10 (buena implementación de servicios y controladores)
- **Frontend:** 10/10 (diseño atractivo y funcional)
- **Base de Datos:** 7/10 (modelo bien estructurado con relaciones adecuadas)
- **Pruebas:** 7/10 (algunos test podrían mejorarse)

### **📌 Conclusión Final**
Este proyecto ha sido una gran oportunidad para aprender sobre **desarrollo web con Spring Boot y Thymeleaf**, integrando un sistema funcional de compras con sesiones y base de datos. Además hemos aprendido a trabajar en equipo y a gestionar un proyecto de principio a fin como si estuviesemos en una empresa, usando los pull request y las issues de GitHub.

🚀 ¡Gracias por revisar nuestro proyecto! 🎮



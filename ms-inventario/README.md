# Microservicio de Inventario

Este microservicio maneja el inventario de productos, incluyendo stock, reservas y movimientos.

## Características

- Gestión de stock de productos
- Sistema de reservas para compras
- Control de concurrencia con optimistic locking
- Registro de movimientos de inventario
- Publicación de eventos de dominio

## Endpoints

### Inventario

- `POST /api/inventory/{productId}/restock` - Agregar stock a un producto
- `GET /api/inventory?productId={productId}` - Consultar stock de un producto

### Reservas

- `POST /api/reservations` - Crear reserva de productos
- `POST /api/reservations/{reservationId}/confirm` - Confirmar reserva
- `POST /api/reservations/{reservationId}/release` - Liberar reserva

## Tecnologías

- Java 17
- Spring Boot 2.7.14
- MongoDB
- Maven
- Lombok

## Configuración

### Base de datos
El microservicio utiliza MongoDB. Configuración por defecto:
- Host: localhost
- Puerto: 27017
- Base de datos: inventory_db

### Puerto
El servicio se ejecuta en el puerto 8081 por defecto.

## Ejecución

```bash
mvn spring-boot:run
```

## Eventos Publicados

- `StockAumentado` - Cuando se incrementa el stock
- `ReservaCreada` - Cuando se crea una reserva
- `ReservaConfirmada` - Cuando se confirma una reserva
- `ReservaRechazada` - Cuando se rechaza una reserva
- `ReservaLiberada` - Cuando se libera una reserva

## Modelo de Datos

### Stock
- productId: Identificador del producto
- onHand: Cantidad en stock
- reserved: Cantidad reservada
- version: Versión para control de concurrencia

### Movement
- productId: Identificador del producto
- type: Tipo de movimiento (RESTOCK, ADJUSTMENT, RESERVATION, CONFIRMATION, RELEASE)
- quantity: Cantidad del movimiento
- externalReference: Referencia externa
- timestamp: Fecha y hora del movimiento

### Reservation
- orderId: Identificador de la orden
- items: Lista de productos reservados
- status: Estado de la reserva (ACTIVE, CONFIRMED, RELEASED)
- createdAt/updatedAt: Timestamps

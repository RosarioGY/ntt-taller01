# ms-catalog-product

Este microservicio gestiona el catálogo de productos. Permite realizar operaciones CRUD sobre productos, actualizar stock y consultar información relevante del catálogo.

## Estructura
- `src/main/java/org/example/catalog/`: Código fuente principal
- `src/main/resources/application.yml`: Configuración de la aplicación
- `diagnostico-mongodb.js`, `insertar-productos.js`, `setup-database.js`: Scripts para inicialización y diagnóstico de la base de datos MongoDB
- `pom.xml`: Configuración de dependencias Maven

## Endpoints principales
- `/products`: Listar, crear, actualizar y eliminar productos
- `/products/{id}`: Consultar producto por ID
- `/products/{id}/stock`: Actualizar stock de producto

## Base de datos
Utiliza MongoDB para almacenar la información de productos.

## Ejecución
1. Instalar dependencias: `mvn install`
2. Ejecutar la aplicación: `mvn spring-boot:run`
3. Inicializar la base de datos con los scripts proporcionados

## Requisitos
- Java 17+
- Maven
- MongoDB

---

# ms-inventario

Este microservicio gestiona el inventario de productos. Permite consultar el stock disponible, actualizar inventario y verificar la integridad de la base de datos.

## Estructura
- `src/main/java/com/microservices/inventory/`: Código fuente principal
- `src/main/resources/application.yml`: Configuración de la aplicación
- `init-db.js`, `verify-db.js`, `create_inventory_db.py`: Scripts para inicialización y verificación de la base de datos
- `docker-compose.yml`, `Dockerfile`: Archivos para despliegue con Docker
- `pom.xml`: Configuración de dependencias Maven

## Endpoints principales
- `/inventory`: Consultar inventario
- `/inventory/{id}`: Consultar inventario por producto
- `/inventory/update`: Actualizar inventario

## Base de datos
Utiliza MongoDB para almacenar la información de inventario.

## Ejecución
1. Instalar dependencias: `mvn install`
2. Ejecutar la aplicación: `mvn spring-boot:run`
3. Inicializar la base de datos con los scripts proporcionados
4. Opcional: Desplegar con Docker usando `docker-compose up`

## Requisitos
- Java 17+
- Maven
- MongoDB
- Docker (opcional)

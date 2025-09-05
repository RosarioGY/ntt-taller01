// Script para configurar la base de datos MongoDB para el microservicio de catálogo

// Usar la base de datos 'catalog'
use('catalog');

// Crear la colección 'products' si no existe
db.createCollection('products');

// Insertar productos de ejemplo
db.products.insertMany([
  {
    name: "Laptop Gaming",
    price: NumberDecimal("1299.99"),
    stock: 10
  },
  {
    name: "Mouse Inalámbrico",
    price: NumberDecimal("29.99"),
    stock: 50
  },
  {
    name: "Teclado Mecánico",
    price: NumberDecimal("89.99"),
    stock: 25
  },
  {
    name: "Monitor 4K",
    price: NumberDecimal("399.99"),
    stock: 15
  },
  {
    name: "Auriculares Gaming",
    price: NumberDecimal("79.99"),
    stock: 30
  }
]);

// Crear índices para mejorar el rendimiento
db.products.createIndex({ "name": 1 });
db.products.createIndex({ "price": 1 });

// Mostrar los productos insertados
print("=== PRODUCTOS CREADOS ===");
db.products.find().forEach(printjson);

print("=== CONFIGURACIÓN COMPLETADA ===");
print("Base de datos: catalog");
print("Colección: products");
print("Productos insertados: " + db.products.countDocuments());

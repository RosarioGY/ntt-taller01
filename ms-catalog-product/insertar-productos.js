// Script para insertar productos en el catálogo de MongoDB
use('catalog');

// Limpiar la colección products si existe
db.products.deleteMany({});

// Insertar productos en el catálogo
print("Insertando productos en el catálogo...");

db.products.insertMany([
  {
    name: "Laptop Gaming MSI",
    price: NumberDecimal("1299.99"),
    stock: 15
  },
  {
    name: "Mouse Logitech MX Master 3",
    price: NumberDecimal("99.99"),
    stock: 45
  },
  {
    name: "Teclado Mecánico Corsair K95",
    price: NumberDecimal("189.99"),
    stock: 20
  },
  {
    name: "Monitor LG UltraWide 34 pulgadas",
    price: NumberDecimal("549.99"),
    stock: 12
  },
  {
    name: "Auriculares Sony WH-1000XM4",
    price: NumberDecimal("349.99"),
    stock: 25
  },
  {
    name: "Webcam Logitech C920",
    price: NumberDecimal("79.99"),
    stock: 30
  },
  {
    name: "SSD Samsung 970 EVO 1TB",
    price: NumberDecimal("129.99"),
    stock: 40
  },
  {
    name: "Tarjeta Gráfica RTX 4070",
    price: NumberDecimal("599.99"),
    stock: 8
  },
  {
    name: "Memoria RAM Corsair 32GB",
    price: NumberDecimal("199.99"),
    stock: 18
  },
  {
    name: "Procesador AMD Ryzen 7",
    price: NumberDecimal("399.99"),
    stock: 22
  }
]);

// Crear índices para optimizar consultas
db.products.createIndex({ "name": 1 });
db.products.createIndex({ "price": 1 });
db.products.createIndex({ "stock": 1 });

// Verificar que los productos se insertaron correctamente
var count = db.products.countDocuments();
print("Total de productos insertados: " + count);

print("=== PRODUCTOS EN EL CATÁLOGO ===");
db.products.find().forEach(function(product) {
    print("- " + product.name + " | $" + product.price + " | Stock: " + product.stock);
});

print("=== INSERCIÓN COMPLETADA ===");

// Script de diagnóstico completo para MongoDB
print("=== DIAGNÓSTICO COMPLETO DE MONGODB ===");
print("");

// 1. Información general del servidor
print("1. INFORMACIÓN DEL SERVIDOR:");
print("Versión MongoDB:", version());
print("Fecha actual:", new Date());
print("");

// 2. Listar todas las bases de datos
print("2. BASES DE DATOS DISPONIBLES:");
db.adminCommand('listDatabases').databases.forEach(function(database) {
    print("- " + database.name + " (" + (database.sizeOnDisk/1024/1024).toFixed(2) + " MB)");
});
print("");

// 3. Usar la base de datos catalog
use('catalog');
print("3. INFORMACIÓN DE LA BASE DE DATOS 'catalog':");

try {
    var stats = db.stats();
    print("- Nombre:", stats.db);
    print("- Colecciones:", stats.collections);
    print("- Documentos totales:", stats.objects);
    print("- Tamaño en disco:", (stats.dataSize/1024/1024).toFixed(2) + " MB");
    print("- Índices:", stats.indexes);
} catch(e) {
    print("Error obteniendo estadísticas:", e.message);
}
print("");

// 4. Listar colecciones
print("4. COLECCIONES EN 'catalog':");
try {
    db.getCollectionNames().forEach(function(collection) {
        print("- " + collection);
    });
} catch(e) {
    print("Error listando colecciones:", e.message);
}
print("");

// 5. Información específica de la colección 'products'
print("5. INFORMACIÓN DE LA COLECCIÓN 'products':");
try {
    var productCount = db.products.countDocuments();
    print("- Total de productos:", productCount);

    if (productCount > 0) {
        print("- Productos encontrados:");
        db.products.find().forEach(function(product) {
            print("  * ID:", product._id);
            print("    Nombre:", product.name);
            print("    Precio: $" + product.price);
            print("    Stock:", product.stock);
            print("    ---");
        });
    } else {
        print("- No hay productos en la colección");
    }
} catch(e) {
    print("Error consultando productos:", e.message);
}
print("");

// 6. Índices de la colección products
print("6. ÍNDICES EN LA COLECCIÓN 'products':");
try {
    db.products.getIndexes().forEach(function(index) {
        print("- " + index.name + ":", JSON.stringify(index.key));
    });
} catch(e) {
    print("Error consultando índices:", e.message);
}
print("");

print("=== FIN DEL DIAGNÓSTICO ===");

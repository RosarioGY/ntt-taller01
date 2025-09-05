import pymongo
from datetime import datetime
from bson import ObjectId

def create_inventory_database():
    try:
        # Conectar a MongoDB
        client = pymongo.MongoClient("mongodb://localhost:27017/")
        db = client["inventory_db"]

        print("🔗 Conectado a MongoDB")
        print("📁 Creando base de datos: inventory_db")

        # Limpiar colecciones existentes si existen
        db.stocks.drop()
        db.movements.drop()
        db.reservations.drop()
        print("🧹 Colecciones limpiadas")

        # Crear colección stocks con datos de ejemplo
        stocks_data = [
            {
                "_id": ObjectId(),
                "productId": "PROD-001",
                "onHand": 100,
                "reserved": 10,
                "version": 1,
                "_class": "com.microservices.inventory.domain.entity.Stock"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-002",
                "onHand": 250,
                "reserved": 25,
                "version": 1,
                "_class": "com.microservices.inventory.domain.entity.Stock"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-003",
                "onHand": 75,
                "reserved": 5,
                "version": 1,
                "_class": "com.microservices.inventory.domain.entity.Stock"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-004",
                "onHand": 500,
                "reserved": 50,
                "version": 1,
                "_class": "com.microservices.inventory.domain.entity.Stock"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-005",
                "onHand": 30,
                "reserved": 0,
                "version": 1,
                "_class": "com.microservices.inventory.domain.entity.Stock"
            }
        ]

        db.stocks.insert_many(stocks_data)
        print("📦 Colección 'stocks' creada con 5 productos")

        # Crear colección movements con datos de ejemplo
        movements_data = [
            {
                "_id": ObjectId(),
                "productId": "PROD-001",
                "type": "RESTOCK",
                "quantity": 100,
                "externalReference": "INITIAL_STOCK_001",
                "timestamp": datetime(2024, 1, 1, 8, 0, 0),
                "_class": "com.microservices.inventory.domain.entity.Movement"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-002",
                "type": "RESTOCK",
                "quantity": 250,
                "externalReference": "INITIAL_STOCK_002",
                "timestamp": datetime(2024, 1, 1, 8, 0, 0),
                "_class": "com.microservices.inventory.domain.entity.Movement"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-001",
                "type": "RESERVATION",
                "quantity": 10,
                "externalReference": "RES-001",
                "timestamp": datetime(2024, 1, 2, 10, 30, 0),
                "_class": "com.microservices.inventory.domain.entity.Movement"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-003",
                "type": "RESTOCK",
                "quantity": 75,
                "externalReference": "INITIAL_STOCK_003",
                "timestamp": datetime(2024, 1, 1, 8, 0, 0),
                "_class": "com.microservices.inventory.domain.entity.Movement"
            },
            {
                "_id": ObjectId(),
                "productId": "PROD-004",
                "type": "ADJUSTMENT",
                "quantity": 500,
                "externalReference": "ADJ-001",
                "timestamp": datetime(2024, 1, 3, 14, 15, 0),
                "_class": "com.microservices.inventory.domain.entity.Movement"
            }
        ]

        db.movements.insert_many(movements_data)
        print("📋 Colección 'movements' creada con 5 movimientos")

        # Crear colección reservations con datos de ejemplo
        reservations_data = [
            {
                "_id": ObjectId(),
                "orderId": "ORDER-001",
                "items": [
                    {"productId": "PROD-001", "quantity": 5},
                    {"productId": "PROD-002", "quantity": 10}
                ],
                "status": "ACTIVE",
                "createdAt": datetime(2024, 1, 2, 10, 30, 0),
                "updatedAt": datetime(2024, 1, 2, 10, 30, 0),
                "_class": "com.microservices.inventory.domain.entity.Reservation"
            },
            {
                "_id": ObjectId(),
                "orderId": "ORDER-002",
                "items": [
                    {"productId": "PROD-003", "quantity": 5}
                ],
                "status": "ACTIVE",
                "createdAt": datetime(2024, 1, 2, 11, 0, 0),
                "updatedAt": datetime(2024, 1, 2, 11, 0, 0),
                "_class": "com.microservices.inventory.domain.entity.Reservation"
            },
            {
                "_id": ObjectId(),
                "orderId": "ORDER-003",
                "items": [
                    {"productId": "PROD-004", "quantity": 20}
                ],
                "status": "CONFIRMED",
                "createdAt": datetime(2024, 1, 1, 15, 0, 0),
                "updatedAt": datetime(2024, 1, 1, 16, 0, 0),
                "_class": "com.microservices.inventory.domain.entity.Reservation"
            },
            {
                "_id": ObjectId(),
                "orderId": "ORDER-004",
                "items": [
                    {"productId": "PROD-002", "quantity": 15}
                ],
                "status": "RELEASED",
                "createdAt": datetime(2024, 1, 3, 9, 0, 0),
                "updatedAt": datetime(2024, 1, 3, 9, 30, 0),
                "_class": "com.microservices.inventory.domain.entity.Reservation"
            },
            {
                "_id": ObjectId(),
                "orderId": "ORDER-005",
                "items": [
                    {"productId": "PROD-004", "quantity": 30},
                    {"productId": "PROD-005", "quantity": 5}
                ],
                "status": "ACTIVE",
                "createdAt": datetime(2024, 1, 4, 8, 30, 0),
                "updatedAt": datetime(2024, 1, 4, 8, 30, 0),
                "_class": "com.microservices.inventory.domain.entity.Reservation"
            }
        ]

        db.reservations.insert_many(reservations_data)
        print("🎫 Colección 'reservations' creada con 5 reservas")

        # Crear índices para optimización
        db.stocks.create_index([("productId", 1)], unique=True)
        db.movements.create_index([("productId", 1)])
        db.movements.create_index([("timestamp", -1)])
        db.movements.create_index([("externalReference", 1)])
        db.reservations.create_index([("orderId", 1)], unique=True)
        db.reservations.create_index([("status", 1)])
        print("🔍 Índices creados para optimización")

        # Mostrar estadísticas
        print("\n📊 ESTADÍSTICAS DE LA BASE DE DATOS:")
        print(f"Stocks: {db.stocks.count_documents({})} documentos")
        print(f"Movements: {db.movements.count_documents({})} documentos")
        print(f"Reservations: {db.reservations.count_documents({})} documentos")

        # Mostrar algunos datos de ejemplo
        print("\n📦 PRODUCTOS EN STOCK:")
        for stock in db.stocks.find():
            available = stock['onHand'] - stock['reserved']
            print(f"  {stock['productId']}: {stock['onHand']} total, {stock['reserved']} reservado, {available} disponible")

        print("\n✅ Base de datos 'inventory_db' creada exitosamente!")
        return True

    except Exception as e:
        print(f"❌ Error al crear la base de datos: {e}")
        return False
    finally:
        if 'client' in locals():
            client.close()

if __name__ == "__main__":
    print("=== INICIALIZADOR DE BASE DE DATOS DE INVENTARIO ===")
    create_inventory_database()

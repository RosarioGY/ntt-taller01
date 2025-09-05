// MongoDB initialization script for Inventory Microservice
// This script creates collections and inserts sample data

use inventory_db;

// Create stocks collection and insert sample data
db.stocks.insertMany([
  {
    _id: ObjectId(),
    productId: "PROD-001",
    onHand: 100,
    reserved: 10,
    version: NumberLong(1),
    _class: "com.microservices.inventory.domain.entity.Stock"
  },
  {
    _id: ObjectId(),
    productId: "PROD-002",
    onHand: 250,
    reserved: 25,
    version: NumberLong(1),
    _class: "com.microservices.inventory.domain.entity.Stock"
  },
  {
    _id: ObjectId(),
    productId: "PROD-003",
    onHand: 75,
    reserved: 5,
    version: NumberLong(1),
    _class: "com.microservices.inventory.domain.entity.Stock"
  },
  {
    _id: ObjectId(),
    productId: "PROD-004",
    onHand: 500,
    reserved: 50,
    version: NumberLong(1),
    _class: "com.microservices.inventory.domain.entity.Stock"
  },
  {
    _id: ObjectId(),
    productId: "PROD-005",
    onHand: 30,
    reserved: 0,
    version: NumberLong(1),
    _class: "com.microservices.inventory.domain.entity.Stock"
  }
]);

// Create movements collection and insert sample data
db.movements.insertMany([
  {
    _id: ObjectId(),
    productId: "PROD-001",
    type: "RESTOCK",
    quantity: 100,
    externalReference: "INITIAL_STOCK_001",
    timestamp: new Date("2024-01-01T08:00:00Z"),
    _class: "com.microservices.inventory.domain.entity.Movement"
  },
  {
    _id: ObjectId(),
    productId: "PROD-002",
    type: "RESTOCK",
    quantity: 250,
    externalReference: "INITIAL_STOCK_002",
    timestamp: new Date("2024-01-01T08:00:00Z"),
    _class: "com.microservices.inventory.domain.entity.Movement"
  },
  {
    _id: ObjectId(),
    productId: "PROD-001",
    type: "RESERVATION",
    quantity: 10,
    externalReference: "RES-001",
    timestamp: new Date("2024-01-02T10:30:00Z"),
    _class: "com.microservices.inventory.domain.entity.Movement"
  },
  {
    _id: ObjectId(),
    productId: "PROD-003",
    type: "RESTOCK",
    quantity: 75,
    externalReference: "INITIAL_STOCK_003",
    timestamp: new Date("2024-01-01T08:00:00Z"),
    _class: "com.microservices.inventory.domain.entity.Movement"
  },
  {
    _id: ObjectId(),
    productId: "PROD-004",
    type: "ADJUSTMENT",
    quantity: 500,
    externalReference: "ADJ-001",
    timestamp: new Date("2024-01-03T14:15:00Z"),
    _class: "com.microservices.inventory.domain.entity.Movement"
  }
]);

// Create reservations collection and insert sample data
db.reservations.insertMany([
  {
    _id: ObjectId(),
    orderId: "ORDER-001",
    items: [
      {
        productId: "PROD-001",
        quantity: 5
      },
      {
        productId: "PROD-002",
        quantity: 10
      }
    ],
    status: "ACTIVE",
    createdAt: new Date("2024-01-02T10:30:00Z"),
    updatedAt: new Date("2024-01-02T10:30:00Z"),
    _class: "com.microservices.inventory.domain.entity.Reservation"
  },
  {
    _id: ObjectId(),
    orderId: "ORDER-002",
    items: [
      {
        productId: "PROD-003",
        quantity: 5
      }
    ],
    status: "ACTIVE",
    createdAt: new Date("2024-01-02T11:00:00Z"),
    updatedAt: new Date("2024-01-02T11:00:00Z"),
    _class: "com.microservices.inventory.domain.entity.Reservation"
  },
  {
    _id: ObjectId(),
    orderId: "ORDER-003",
    items: [
      {
        productId: "PROD-004",
        quantity: 20
      }
    ],
    status: "CONFIRMED",
    createdAt: new Date("2024-01-01T15:00:00Z"),
    updatedAt: new Date("2024-01-01T16:00:00Z"),
    _class: "com.microservices.inventory.domain.entity.Reservation"
  },
  {
    _id: ObjectId(),
    orderId: "ORDER-004",
    items: [
      {
        productId: "PROD-002",
        quantity: 15
      }
    ],
    status: "RELEASED",
    createdAt: new Date("2024-01-03T09:00:00Z"),
    updatedAt: new Date("2024-01-03T09:30:00Z"),
    _class: "com.microservices.inventory.domain.entity.Reservation"
  },
  {
    _id: ObjectId(),
    orderId: "ORDER-005",
    items: [
      {
        productId: "PROD-004",
        quantity: 30
      },
      {
        productId: "PROD-005",
        quantity: 5
      }
    ],
    status: "ACTIVE",
    createdAt: new Date("2024-01-04T08:30:00Z"),
    updatedAt: new Date("2024-01-04T08:30:00Z"),
    _class: "com.microservices.inventory.domain.entity.Reservation"
  }
]);

// Create indexes for better performance
db.stocks.createIndex({ "productId": 1 }, { unique: true });
db.movements.createIndex({ "productId": 1 });
db.movements.createIndex({ "timestamp": -1 });
db.movements.createIndex({ "externalReference": 1 });
db.reservations.createIndex({ "orderId": 1 }, { unique: true });
db.reservations.createIndex({ "status": 1 });

print("Database initialization completed successfully!");
print("Collections created: stocks, movements, reservations");
print("Sample data inserted for 5 products and related movements/reservations");
print("Indexes created for optimal performance");

// Show collection counts
print("\nCollection counts:");
print("Stocks: " + db.stocks.countDocuments());
print("Movements: " + db.movements.countDocuments());
print("Reservations: " + db.reservations.countDocuments());

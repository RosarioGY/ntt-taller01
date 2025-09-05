// Script to verify and display the data in MongoDB collections
use inventory_db;

print("=== INVENTORY DATABASE VERIFICATION ===\n");

// Display Stocks Collection
print("📦 STOCKS COLLECTION:");
print("====================");
db.stocks.find().forEach(function(doc) {
    print(`Product: ${doc.productId}`);
    print(`  - On Hand: ${doc.onHand}`);
    print(`  - Reserved: ${doc.reserved}`);
    print(`  - Available: ${doc.onHand - doc.reserved}`);
    print(`  - Version: ${doc.version}`);
    print("");
});

print("\n📋 MOVEMENTS COLLECTION:");
print("========================");
db.movements.find().sort({timestamp: -1}).forEach(function(doc) {
    print(`${doc.timestamp.toISOString()} - ${doc.productId}`);
    print(`  - Type: ${doc.type}`);
    print(`  - Quantity: ${doc.quantity}`);
    print(`  - Reference: ${doc.externalReference}`);
    print("");
});

print("\n🎫 RESERVATIONS COLLECTION:");
print("===========================");
db.reservations.find().forEach(function(doc) {
    print(`Order: ${doc.orderId} [${doc.status}]`);
    print(`  - Created: ${doc.createdAt.toISOString()}`);
    print(`  - Items:`);
    doc.items.forEach(function(item) {
        print(`    * ${item.productId}: ${item.quantity} units`);
    });
    print("");
});

print("\n📊 COLLECTION STATISTICS:");
print("=========================");
print(`Total Stocks: ${db.stocks.countDocuments()}`);
print(`Total Movements: ${db.movements.countDocuments()}`);
print(`Total Reservations: ${db.reservations.countDocuments()}`);

print("\n🔍 INDEXES CREATED:");
print("===================");
print("Stocks indexes:");
db.stocks.getIndexes().forEach(function(index) {
    print(`  - ${JSON.stringify(index.key)}`);
});

print("\nMovements indexes:");
db.movements.getIndexes().forEach(function(index) {
    print(`  - ${JSON.stringify(index.key)}`);
});

print("\nReservations indexes:");
db.reservations.getIndexes().forEach(function(index) {
    print(`  - ${JSON.stringify(index.key)}`);
});

print("\n✅ Database verification completed!");

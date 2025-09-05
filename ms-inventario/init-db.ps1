# PowerShell script to initialize MongoDB with sample data
# Run this script if you want to manually initialize the database

Write-Host "=== MongoDB Database Initialization Script ===" -ForegroundColor Green

# Check if MongoDB is running
try {
    $mongoStatus = Test-NetConnection -ComputerName localhost -Port 27017 -ErrorAction SilentlyContinue
    if (-not $mongoStatus.TcpTestSucceeded) {
        Write-Host "MongoDB is not running on localhost:27017" -ForegroundColor Red
        Write-Host "Please start MongoDB first using: docker-compose up mongodb" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "Could not connect to MongoDB. Please ensure it's running." -ForegroundColor Red
    exit 1
}

Write-Host "MongoDB is running. Initializing database..." -ForegroundColor Yellow

# Execute the MongoDB initialization script
$initScript = Get-Content "init-db.js" -Raw
$mongoCommand = "mongosh --host localhost:27017 --eval `"$initScript`""

try {
    Invoke-Expression $mongoCommand
    Write-Host "Database initialization completed successfully!" -ForegroundColor Green
} catch {
    Write-Host "Failed to initialize database. Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Make sure MongoDB shell (mongosh) is installed and accessible." -ForegroundColor Yellow
}

Write-Host "`nDatabase is ready for the Inventory Microservice!" -ForegroundColor Green

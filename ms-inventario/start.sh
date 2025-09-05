#!/bin/bash

echo "=== Inventory Microservice Startup Script ==="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "Docker is not running. Please start Docker first."
    exit 1
fi

echo "Starting MongoDB with Docker Compose..."
docker-compose up -d mongodb

echo "Waiting for MongoDB to be ready..."
sleep 10

echo "Building the application..."
mvn clean package -DskipTests

echo "Starting the inventory microservice..."
docker-compose up -d ms-inventario

echo "=== Services Status ==="
docker-compose ps

echo ""
echo "=== Application URLs ==="
echo "Inventory API: http://localhost:8081/api"
echo "Health Check: http://localhost:8081/api/actuator/health"
echo ""
echo "Use the postman-collection.json file to test the endpoints."

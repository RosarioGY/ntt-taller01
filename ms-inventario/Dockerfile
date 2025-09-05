FROM openjdk:17-jdk-slim

LABEL maintainer="microservices-team"

WORKDIR /app

COPY target/ms-inventario-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/api/actuator/health || exit 1

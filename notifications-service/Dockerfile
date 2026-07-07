# Builder
FROM gradle:9.1.0-jdk21 AS builder
WORKDIR /app

# Cache dependencies
COPY build.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon

# Build
COPY src ./src
RUN ./gradlew build --no-daemon -x test

# Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: non-root user + read-only rootfs
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -s /bin/sh -D appuser
USER appuser

WORKDIR /app

# Copy built jar
COPY --from=builder /app/build/libs/*.jar app.jar

# JVM tuning para contenedores
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

# Ejecucción
ENTRYPOINT ["java", "-jar", "app.jar"]

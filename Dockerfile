# Builder
FROM gradle:9.1.0-jdk21 AS builder
WORKDIR /app

COPY build.gradle gradlew ./
COPY gradle ./gradle

RUN ./gradlew dependencies

COPY src ./src

# Construir el jar
RUN ./gradlew build --no-daemon -x test

# Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Ejecucción
ENTRYPOINT ["java", "-jar", "app.jar"]
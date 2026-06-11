# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

# Baixa dependências em camada separada (cache eficiente)
RUN ./mvnw dependency:go-offline -q

COPY src ./src

# Compila e empacota, pulando testes
RUN ./mvnw clean package -DskipTests -q

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia apenas o JAR
COPY --from=builder /app/target/biblioteca-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# JVM otimizada para containers (Render Free: 512MB RAM)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=prod", \
  "-jar", "app.jar"]

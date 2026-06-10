# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copia apenas os arquivos de dependência primeiro (melhora o cache do Docker)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

# Baixa as dependências em camada separada (cache eficiente)
RUN ./mvnw dependency:go-offline -q

# Copia o restante do código-fonte
COPY src ./src

# Compila e empacota, pulando os testes
RUN ./mvnw clean package -DskipTests -q

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Cria usuário não-root por segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Cria o diretório de logs com as permissões corretas
RUN mkdir -p logs && chown -R appuser:appgroup /app

# Copia apenas o JAR gerado pelo stage de build
COPY --from=builder /app/target/biblioteca-0.0.1-SNAPSHOT.jar app.jar

USER appuser

EXPOSE 8080

# Configurações da JVM para container (limita uso de memória/CPU)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

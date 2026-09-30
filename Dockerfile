# Etapa 1: compilar
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Corrige quebra de linha do Windows (CRLF), que dá erro no Linux
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests -B

# Etapa 2: executar
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --uid 1001 appuser
COPY --from=build /app/target/*.jar app.jar
USER appuser

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
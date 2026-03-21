# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copia o pom.xml e baixa as dependências (otimiza o cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final leve para rodar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

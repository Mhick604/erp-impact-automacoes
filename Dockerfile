# ETAPA 1: Construção (Build) - Usa o JDK (Pesado, com compilador)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# ETAPA 2: Produção (Run) - Usa o JRE (Leve, apenas para rodar)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copia APENAS o arquivo .jar gerado na Etapa 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
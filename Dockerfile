FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Dando permissão e compilando dentro do Docker
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Comando para rodar o projeto usando curinga para evitar erro de nome de versão
CMD sh -c 'java -jar target/*.jar'
# Stage 1: Build l'application
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Build l'application (sans les tests pour aller plus vite)
RUN mvn clean package -DskipTests

# Stage 2: Créer l'image finale légère
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 9090
EXPOSE 9091

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
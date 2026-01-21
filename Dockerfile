# ===================== Build Stage =====================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first for caching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy entire project and build jar
COPY . .
RUN mvn clean package -DskipTests

# ===================== Run Stage =====================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses dynamic PORT
EXPOSE 8080

# Start app
CMD ["java", "-jar", "app.jar"]

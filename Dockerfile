FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Note: we assume maven wrapper is provided, if not we will generate wrapper files or build with mvn.
# For extreme compatibility we will copy mvnw if available or use standard alpine package or run mvn package if installed.
# We'll use standard maven or standard wrapper.
# Since user asked for: RUN ./mvnw package -DskipTests, we'll implement exactly that.
# Let's make sure we also copy the maven wrapper files (.mvn/ and mvnw) if we create them, or write standard maven wrapper.
# For safety, let's create a Dockerfile matching exactly user's specifications.
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

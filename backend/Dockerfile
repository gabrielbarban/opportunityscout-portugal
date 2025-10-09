FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.18

ENV LANGUAGE='en_US:en'

WORKDIR /app

COPY --from=build --chown=185 /app/target/quarkus-app/lib/ /app/lib/
COPY --from=build --chown=185 /app/target/quarkus-app/*.jar /app/
COPY --from=build --chown=185 /app/target/quarkus-app/app/ /app/app/
COPY --from=build --chown=185 /app/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080

USER 185

ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/app/quarkus-run.jar"

ENTRYPOINT [ "java", "-jar", "/app/quarkus-run.jar" ]
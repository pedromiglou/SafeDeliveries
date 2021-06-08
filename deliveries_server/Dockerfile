FROM maven:3.6.3-jdk-11
EXPOSE 8080
WORKDIR /app
COPY . .
RUN mvn install -DskipTests
COPY target/DeliveriesServer-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "DeliveriesServer-0.0.1-SNAPSHOT.jar" ]
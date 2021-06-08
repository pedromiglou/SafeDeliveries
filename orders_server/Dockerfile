FROM maven:3.6.3-jdk-11
EXPOSE 8080
WORKDIR /app
COPY . .
RUN mvn install -DskipTests
COPY target/OrdersServer-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "OrdersServer-0.0.1-SNAPSHOT.jar" ]
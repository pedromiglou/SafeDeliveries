FROM maven:3.6.3-jdk-11
EXPOSE 8081
WORKDIR /app
COPY . .
RUN mvn install -DskipTests
ENTRYPOINT [ "java", "-jar", "target/OrdersServer-0.0.1-SNAPSHOT.jar" ]
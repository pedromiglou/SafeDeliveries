FROM maven:3.6.3-jdk-11
EXPOSE 8080
WORKDIR /app
COPY . .
RUN export environment="prod"
RUN mvn install -DskipTests
ENTRYPOINT [ "java", "-jar", "target/OrdersServer-0.0.1-SNAPSHOT.jar" ]
FROM openjdk:17
#WORKDIR /app

#COPY /target/transactions_wallet-0.0.1-SNAPSHOT.jar app.jar
ADD /target/transactions_wallet-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

#docker-compose up --build

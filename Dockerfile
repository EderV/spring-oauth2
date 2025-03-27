FROM openjdk:21

WORKDIR /project

COPY . .

RUN ./mvnw clean package -P dev -D skipTests

RUN mkdir /app
RUN cp ./target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["/bin/bash"]
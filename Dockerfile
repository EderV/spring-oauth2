FROM openjdk:21

WORKDIR /project

COPY . .

RUN ./mvnw clean package -P dev -D skipTests

#RUN VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout | tail -n 1)
#RUN mkdir /app && cp ./target/spring-oauth2-${VERSION}.jar /app/app.jar

RUN mkdir /app && \
    VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout | tail -n 1) && \
    echo "Building version ${VERSION}" && \
    cp ./target/spring-oauth2-${VERSION}.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["/bin/bash"]
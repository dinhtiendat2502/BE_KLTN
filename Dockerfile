#run
FROM openjdk:21-jdk
EXPOSE 8080

ADD ./target/*.jar app.jar

RUN sh -c 'touch /app.jar'

CMD [ "java", "--enable-preview", "-jar", "app.jar" ]

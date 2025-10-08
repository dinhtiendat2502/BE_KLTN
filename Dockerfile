#run
FROM openjdk:21-jdk
EXPOSE 8080

ADD ./target/BE-0.0.1.jar app.jar

RUN sh -c 'touch /app.jar'

CMD [ "java", "--enable-preview", "-jar", "app.jar" ]

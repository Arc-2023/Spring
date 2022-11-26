FROM openjdk:8

VOLUME /tmp/vue-spring

ADD ./target/vuee-spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java" ,"-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
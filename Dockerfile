FROM openjdk:21

VOLUME /vue-spring

ADD target/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
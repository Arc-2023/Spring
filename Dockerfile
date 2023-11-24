FROM openjdk:18

VOLUME /vue-spring

ADD vuee-spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
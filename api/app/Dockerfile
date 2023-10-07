FROM gradle:7.2.0-jdk17 AS build

RUN mkdir /opt/app
COPY . /opt/app
WORKDIR /opt/app

RUN ./gradlew dependencies --write-locks
RUN ./gradlew build

FROM openjdk:17-oracle

RUN mkdir /opt/app
WORKDIR /opt/app
COPY --from=build /opt/app/build/libs/*.jar /opt/app/app.jar

EXPOSE 6060


ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/app.jar"]
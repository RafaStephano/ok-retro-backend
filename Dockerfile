FROM gradle:5.4.1-jdk8-alpine

ENV JAVA_PARAMETERS="-Dserver.port=8080"

WORKDIR /opt/app/

COPY build/libs/ok-retro-backend-*.jar ok-retro-backend.jar

EXPOSE 8080

CMD java ${JAVA_PARAMETERS} -jar ok-retro-backend.jar
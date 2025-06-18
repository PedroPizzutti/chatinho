FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*jar api.jar

ENTRYPOINT ["java", "-jar", "api.jar"]
FROM eclipse-temurin:17-jre-alpine

ARG SERVICE

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --chown=app:app ${SERVICE}/build/libs/${SERVICE}.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

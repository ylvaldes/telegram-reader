FROM bellsoft/liberica-runtime-container:jre-17-stream-musl
# Quien lo mantiene
LABEL maintainer="yasman05@gmail.com"
ENV PORT "8080"
# Especificar la variables de languaje, para evitar problemas de encoding
ENV LANG=en_US.utf8
ENV LC_ALL=en_US.utf8
ENV LANGUAGE=en_US.utf8

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

VOLUME /tmp
#cambio el workdir
WORKDIR /home/ylvaldes

COPY --chown=spring:spring target/*.jar app.jar
EXPOSE $PORT
CMD ["java", "-jar", "/home/ylvaldes/app.jar"]
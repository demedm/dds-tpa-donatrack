
# compilar dos servicios igual que sus propios dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY donaciones-mserv donaciones-mserv
COPY logistica-mserv logistica-mserv
COPY cliente-liviano/pom.xml cliente-liviano/pom.xml
RUN mvn -q -pl donaciones-mserv,logistica-mserv -am -Dmaven.test.skip=true \
    package dependency:copy-dependencies -DincludeScope=runtime

#imagn final java + cron
FROM eclipse-temurin:17-jre
RUN apt-get update && apt-get install -y --no-install-recommends cron tzdata \
 && rm -rf /var/lib/apt/lists/*
ENV TZ=America/Argentina/Buenos_Aires

WORKDIR /app
COPY --from=build /app/donaciones-mserv/target/classes    donaciones/classes
COPY --from=build /app/donaciones-mserv/target/dependency donaciones/lib
COPY --from=build /app/logistica-mserv/target/classes     logistica/classes
COPY --from=build /app/logistica-mserv/target/dependency  logistica/lib

COPY cron/crontab       /etc/cron.d/donatrack
COPY cron/tarea.sh       /usr/local/bin/tarea
COPY cron/entrypoint.sh      /entrypoint.sh

# escritos en Windows (CRLF): Linux necesita LF
RUN sed -i 's/\r$//' /etc/cron.d/donatrack /usr/local/bin/tarea /entrypoint.sh \
 && chmod 0644 /etc/cron.d/donatrack \
 && chmod +x /usr/local/bin/tarea /entrypoint.sh

CMD ["/entrypoint.sh"]

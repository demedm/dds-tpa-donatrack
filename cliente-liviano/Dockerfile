FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY cliente-liviano .
RUN mvn -q -Dmaven.test.skip=true package dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/classes classes
COPY --from=build /app/target/dependency lib
CMD ["java", "-cp", "classes:lib/*", "ar.edu.utn.frba.dds.server.App"]
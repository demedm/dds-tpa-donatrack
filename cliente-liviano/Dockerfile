FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY donaciones-mserv donaciones-mserv
COPY logistica-mserv logistica-mserv
COPY cliente-liviano cliente-liviano
RUN mvn -q -pl cliente-liviano -am -Dmaven.test.skip=true package dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/cliente-liviano/target/classes classes
COPY --from=build /app/cliente-liviano/target/dependency lib
CMD ["java", "-cp", "classes:lib/*", "ar.edu.utn.frba.dds.server.App"]

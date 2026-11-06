FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY donaciones-mserv donaciones-mserv
COPY logistica-mserv/pom.xml logistica-mserv/pom.xml
RUN mvn -q -pl donaciones-mserv -am -Dmaven.test.skip=true package dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/donaciones-mserv/target/classes classes
COPY --from=build /app/donaciones-mserv/target/dependency lib
CMD ["java", "-cp", "classes:lib/*", "ar.edu.utn.frba.dds.main.App_Donaciones"]
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY logistica-mserv logistica-mserv
COPY donaciones-mserv/pom.xml donaciones-mserv/pom.xml
RUN mvn -q -pl logistica-mserv -am -Dmaven.test.skip=true package dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/logistica-mserv/target/classes classes
COPY --from=build /app/logistica-mserv/target/dependency lib
CMD ["java", "-cp", "classes:lib/*", "ar.edu.utn.frba.dds.main.App_Logistica"]
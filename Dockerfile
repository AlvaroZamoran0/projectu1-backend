FROM openjdk:17
ARG JAR_FILE=target/projectu1-backend.jar
COPY ${JAR_FILE} projectu1-backend.jar
ENTRYPOINT ["java","-jar","/projectu1-backend.jar"]
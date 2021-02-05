FROM openjdk:8
ADD /target/spring-boot-browser.jar spring-boot-browser.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "spring-boot-browser.jar"]
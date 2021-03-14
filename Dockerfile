FROM adoptopenjdk/openjdk11:alpine-jre
COPY "notes-manager-web/build/libs/notes-manager-[^web]*.jar" notes-manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "notes-manager.jar"]

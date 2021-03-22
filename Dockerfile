FROM adoptopenjdk/openjdk11:alpine-jre as builder
COPY "notes-manager-web/build/libs/notes-manager-[^web]*.jar" notes-manager.jar
RUN java -Djarmode=layertools -jar notes-manager.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=builder dependencies .
COPY --from=builder snapshot-dependencies .
COPY --from=builder spring-boot-loader .
COPY --from=builder application .
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

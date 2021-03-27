FROM gradle:6.8.3-jdk as builder
WORKDIR /app
COPY build.gradle settings.gradle  ./
COPY notes-manager-web/build.gradle notes-manager-web/
RUN gradle notes-manager-web:assemble --no-daemon || return 0
COPY notes-manager-web notes-manager-web/
RUN gradle notes-manager-web:assemble --no-daemon

FROM adoptopenjdk/openjdk11:alpine-jre as extractor
WORKDIR /app
COPY --from=builder "app/notes-manager-web/build/libs/notes-manager-[^web]*.jar" notes-manager.jar
RUN java -Djarmode=layertools -jar notes-manager.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY --from=extractor /app/dependencies ./
COPY --from=extractor /app/snapshot-dependencies ./
COPY --from=extractor /app/spring-boot-loader ./
COPY --from=extractor /app/application ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

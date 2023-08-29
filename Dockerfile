FROM adoptopenjdk/openjdk11 AS builder

# 빌드 인자로 .env 파일을 전달받습니다.
ARG ENV_FILE
ENV ENV_FILE=${ENV_FILE}

# .env 파일을 복사하고 환경변수를 읽어들입니다.
COPY ${ENV_FILE} .env

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew	#gradlew 실행 권한 부여
RUN ./gradlew bootJar	#gradlew를 통해 실행 가능한 jar파일 생성

FROM adoptopenjdk/openjdk11
COPY --from=builder build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
VOLUME /tmp
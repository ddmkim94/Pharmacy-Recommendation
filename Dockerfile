FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/pharmacy.jar
# 도커 컨테이너 안에 pharmacy.jar 라는 이름으로 복사
COPY ${JAR_FILE} ./pharmacy.jar
# TimeZone을 한국 시간으로 변경
ENV TZ=Asia/Seoul
# 컨테이너가 실행될 때 실행될 때 항상 실행되어야 하는 명령어(command)
ENTRYPOINT ["java", "-jar", "./pharmacy.jar"]
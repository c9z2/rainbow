FROM maven:3.8.1-openjdk-8 AS MAVEN_BUILD_BIZ
MAINTAINER ch3ng <ch3ng57@gmail.com>
WORKDIR /build/
COPY pom.xml /build/

COPY rainbow-api/src /build/rainbow-api/src
COPY rainbow-api/pom.xml /build/rainbow-api/

COPY rainbow-biz/src /build/rainbow-biz/src
COPY rainbow-biz/pom.xml /build/rainbow-biz/

COPY rainbow-skt/pom.xml /build/rainbow-skt/

COPY rainbow-skt/rainbow-tcp/src /build/rainbow-skt/rainbow-tcp/src
COPY rainbow-skt/rainbow-tcp/pom.xml /build/rainbow-skt/rainbow-tcp/

COPY rainbow-skt/rainbow-udp/src /build/rainbow-skt/rainbow-udp/src
COPY rainbow-skt/rainbow-udp/pom.xml /build/rainbow-skt/rainbow-udp/

COPY rainbow-skt/rainbow-ws/src /build/rainbow-skt/rainbow-ws/src
COPY rainbow-skt/rainbow-ws/pom.xml /build/rainbow-skt/rainbow-ws/

RUN mvn clean package -pl rainbow-biz -am -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app
ENV mongodb.uri=$MONGODB_URI
ENV redis.host=$REDIS_HOST
ENV redis.db=$REDIS_DB
ENV redis.port=$REDIS_PORT
ENV mysql.url=$MYSQL_URL
ENV mysql.username=$MYSQL_USERNAME
ENV mysql.password=$MYSQL_PASSWORD
ENV minio.prefix=$MINIO_PREFIX
ENV minio.host=$MINIO_HOST
ENV minio.accessKey=$MINIO_ACCESS_KEY
ENV minio.secretKey=$MINIO_SECRET_KEY
ENV minio.bucket=$MINIO_BUCKET
COPY --from=MAVEN_BUILD_BIZ /build/rainbow-biz/target/rainbow-biz-emerald.1.0.0.jar /app
ENTRYPOINT ["java","-jar","/app/rainbow-biz-emerald.1.0.0.jar"]
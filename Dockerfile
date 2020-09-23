FROM ubuntu:18.04
MAINTAINER ch3ng  <ch3ng57@gmail.com>
WORKDIR /root/
# copy mysql init sql include ddl and dml
COPY rainbow.sql /root/
# change ubuntu sources.list
RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse \ndeb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse \ndeb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse \ndeb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse \ndeb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse \ndeb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse \ndeb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse \ndeb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse \ndeb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse \ndeb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" > /etc/apt/sources.list \
&& apt-get update \
##### jre #####
&& apt-get install -y openjdk-8-jre \
# create  user of mysql
&& apt-get install -y wget \
##### mysql #####
&& echo "GRANT all privileges on *.* to 'test@%' IDENTIFIED BY 'test';\nflush privileges;\nSET PASSWORD FOR 'root'@'localhost' = PASSWORD('root'); \nexit" > /root/gen.sql \
&& apt-get install -y mysql-server \
&& service mysql start \
&& mysql < rainbow.sql \
&& mysql < gen.sql \
&& rm -rf gen.sql rainbow.sql \
##### mongodb #####
&& apt-get install -y mongodb \
&& service mongodb start \
&& sleep 10 \
&& mongo --eval "printjson(db.createCollection('rainbow'))" \
##### redis #######
&& apt-get install -y redis \
&& sed -i 's/127.0.0.1 ::1/127.0.0.1/' /etc/redis/redis.conf \
#####  minio ######
&& wget https://oss-1254056673.cos.ap-beijing.myqcloud.com/minio \
&& chmod +x minio \
##### jar #####
&& mvn clean package -DskipTests \
&& mkdir ./rainbow \
&& cp ./rainbow-biz/target/rainbow-biz-1.0.0.jar /root/rainbow \
&& cp ./rainbow-skt/rainbow-ws/target/rainbow-ws-1.0.0.jar /root/rainbow \
##### start shell #####
&& echo "#!/bin/bash \nservice mysql start \nservice mongodb start \nredis-server /etc/redis/redis.conf \n./minio server /root/oss/data \nnohup java -jar ./rainbow/rainbow-biz-1.0.0.jar > /dev/null 2>&1 & \nnohup java -jar ./rainbow/rainbow-ws-1.0.0.jar > /dev/null 2>&1 & \ntail -f  /dev/null" > ./start.sh \
&& mkdir -p ./oss/data \
&& chmod +x ./start.sh
# open poort 9000  minio, 6767 websocket ,9090 for biz
EXPOSE 9000 6767 9090
VOLUME ["/root/oss/data","/var/lib/mongodb","var/lib/mysql"]
ENTRYPOINT ["/root/start.sh"]
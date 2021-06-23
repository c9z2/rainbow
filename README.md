# Rainbow Project
Rainbow期望是一个将要覆盖全平台的IM系统。 包括IOS, Android, Web, Windows,Mac和Linux。这里只有服务端!!!
## 结构
```
rainbow
├── rainbow-api: common program
│   ├── src: source code
│   └── pom: pom file
├── rainbow-biz: provide business program for clients
│   ├── src: source code
│   └── pom: pom file
├── rainbow-skt: sever program
│   ├── rainbow-tcp: tcp connection
│ 	│ 	├── src: source code
│   │ 	└── pom: pom file
│   ├── rainbow-udp: tcp connection
│ 	│ 	├── src: source code
│   │ 	└── pom: pom file
│   ├── rainbow-ws: tcp connection
│   │ 	├── src: source code
│   │   └── pom: pom file
└── pom: pom file
```

## 设计
![alt rainbow](https://ibbbb.oss-cn-beijing.aliyuncs.com/20-9/rainbow.png)
## 开始

- 目前你可以通过maven自行编译安装，依赖的中间件包括MySQL-5.7.x，Redis-5.x，MongoDB-4.x，Minio。

    注意：minio需要创建一个名叫oss的bucket，并授权read and write。
    
    ```
	# 克隆项目
    git clone https://github.com/ch3n90/rainbow.git
    
    cd rainbow 
    
    mvn clean package
    ```
- 也可以通过docker-compose安装
  ```
  cd docker-compose 
 
  cd rainbow 
  
  chmod +x build
  
  ./build
  ```
  运行
  ```shell
  sudo docker run -d \
  	-v ~/data/rainbow/oss:/root/oss/data \
  	-v ~/data/rainbow/mongodb:/var/lib/mongodb \
  	-v ~/data/rainbow/mysql:/var/lib/mysql \
  	-p 9000:9000 \
  	-p 6767:6767 \
  	-p 9090:9090 \
  	--name rainbow \
      --restart=always \
  	ch3n90/rainbow:0.0.8
  ```
- 此时你可能还需要一个前端程序，[rainbow-web](https://github.com/ch3n90/rainbow-web) 正好是rainbow的web客户端程序。
- 你也可以体验已经部署好的 [rainbow](http://web.rainbow.milchstrabe.com) 请勿压测，谢谢！
## 现在
| 项目            | 地址                                  |
| --------------- | ------------------------------------- |
| rainbow         | https://github.com/ch3n90/rainbow     |
| rainbow-web     | https://github.com/ch3n90/rainbow-web |
| rainbow-mobile  |                                       |
| rainbow-desktop |                                       |
## License
Rainbow已获得Apache 2.0许可。 有关详细信息，请查看[LICENSE](https://github.com/RainbowRW2/rainbow/blob/master/LICENSE)。


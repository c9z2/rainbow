# Rainbow Project
Rainbow期望是一个将要覆盖全平台的IM系统。 包括IOS, Android, Web, Windows,Mac和Linux。这里只有服务端!!!
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
- 也可以通过docker-compose快速安装体验
  ```
  cd docker-compose 
 
  sudo docker-compose up 
  ```
- 此时你可能还需要一个前端程序，[rainbow-web](https://github.com/ch3n90/rainbow-web) 正好是rainbow的web客户端程序。
## 现在
| 项目            | 地址                                  |
| --------------- | ------------------------------------- |
| rainbow         | https://github.com/ch3n90/rainbow     |
| rainbow-web     | https://github.com/ch3n90/rainbow-web |
| rainbow-mobile  |                                       |
| rainbow-desktop |                                       |
## License
Rainbow已获得Apache 2.0许可。 有关详细信息，请查看[LICENSE](https://github.com/RainbowRW2/rainbow/blob/master/LICENSE)。


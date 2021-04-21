<!-- TOC -->

- [系统架构](#系统架构)
- [部署规范](#部署规范)
    - [K8S 部署](#k8s-部署)
    - [docker-compose部署](#docker-compose部署)
        - [使用 image 直接启动](#使用-image-直接启动)
        - [build之后启动](#build之后启动)

<!-- /TOC -->
## 系统架构

![img](doc/images/jiagou.png)

- 10个服务
  6个后端基础服务
  1个公共本地服务
  前端
  zuul网关
  eureka服务注册中心

- 1个云存储服务
  阿里云OSS

- 1个消息中间件
  Rabbitmq

## 部署规范
两种部署方式，K8S 和  docdocker-compose
### K8S 部署

`kubectl apply -f quickstart.yml`

在 ip:30004 进行访问

### docker-compose部署

#### 使用 image 直接启动

`docker-compose -f docker-compose-image.yml up -d`

#### build之后启动
首先对后端进行打包，在跟根目录中运行 

```mvn clean package```

其次将前端进行打包成dist，在Cypberchair_frontend目录下 
```npm run build```

然后打包镜像，在根目录下运行 

```docker-compose build```

利用 docker-compose 运行 

```docker-compose -f docker-compose.yml up -d```

在 ip:1024 进行访问



# bird-java概述

bird-java是以dubbo为基础的分布式服务框架，专注于业务开发，提炼后台应用中的经典业务场景，大幅减少开发编码量。

技术选型
-----------------

 - 基础框架：spring
 - 服务调度：dubbo
 - web层：spring-mvc
 - 缓存：redis
 - 数据访问：mybatis、mybatis-plus
 - 数据库连接池：druid
 - 消息队列：kafka
 - 日志：slf4j、logback
 - 任务调度：quartz
 - 服务总线：基于Kafka自研EventBus
 - 身份认证：自研单点登录

架构图
-----------------

![架构图.png-37kB](http://static.zybuluo.com/liuxx-/cph5c8pyt2d6s0x1ubneajzg/%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

 - 接入层：即web层，服务的使用者，面向用户。多系统之间通过sso实现登录与权限的统一控制。
 - 服务层：服务的提供者，每个服务均可集群部署，服务之间可通过RPC调用，也可通过EventBus实现通信。
 - 数据层：包括数据的持久化与缓存。每个服务可对应其各自的数据库，缓存使用redis。
 - 基础设施层：为以上各层提供服务，包括日志、工具类、任务调度等。
 
项目介绍
-----------------

![项目截图.png-35kB](http://static.zybuluo.com/liuxx-/tn3wsk10mkfxa6n8jpi3tfzu/%E9%A1%B9%E7%9B%AE%E6%88%AA%E5%9B%BE.png)

- bird-core：包括各种抽象基类与工具类、缓存的实现等。
- bird-service-common：依赖bird-core，包括所有服务接口、DTO、Model、EventArg的定义。
- bird-service-xxx：bird-core、bird-service-common，服务的实现，可自由扩展。每个模块均可集群部署，可拥有自己的数据库，可单独做读写分离等。
- bird-web-admin：依赖bird-core、bird-service-common，web层，可负载均衡，调用远程服务，不依赖具体实现。
- bird-web-user：用户中心，单点登录服务器。
- bird-web-file：文件服务器。

功能特性
-----------------
bird-java提供了很多功能特性，包括：

 1. 分布式。每个服务均可集群部署，服务间可自由通信，每个服务可拥有自己的数据库，可单独做读写分离。
 2. EventBus事件总线。让服务间事件传递像本地程序一样简单。
 3. 全自动CRUD，表格增删查改、筛选、排序、分页均从框架层面解决，业务编码量不到20行。



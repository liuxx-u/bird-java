# bird-java概述

bird-java是以dubbo为基础的分布式服务框架，专注于业务开发，提炼后台应用中的经典业务场景，大幅减少开发编码量。

## 技术选型

 - 基础框架：spring
 - 服务调度：dubbo
 - web层：spring-boot
 - 缓存：redis
 - 数据访问：mybatis、mybatis-plus
 - 数据库连接池：druid
 - 消息队列：kafka
 - 日志：slf4j、logback
 - 任务调度：quartz
 - 服务总线：基于Kafka自研EventBus
 - 身份认证：自研单点登录

## 架构图

![image.png-58.5kB][1]

 - 接入层：即web层，服务的使用者，面向用户。多系统之间通过sso实现登录与权限的统一控制。
 - 服务层：服务的提供者，每个服务均可集群部署，服务之间可通过RPC调用，也可通过EventBus实现通信。
 - 数据层：包括数据的持久化与缓存。每个服务可对应其各自的数据库，缓存使用redis。
 - 基础设施层：为以上各层提供服务，包括日志、工具类、任务调度等。


## 功能特性

bird-java提供了许多功能特性，包括：

 1. 分布式。每个服务均可集群部署，服务间可自由通信，每个服务可拥有自己的数据库，可单独做读写分离。
 2. EventBus事件总线。让服务间事件传递像本地程序一样简单。
 3. 全自动CRUD，表格增删查改、筛选、排序、分页均从框架层面解决，业务编码量不到20行。
 4. 实现从db->mappper->service->controller各层代码一键生成。
 5. 为web与service项目提供不同类型的starter，按配置注入对应的组件，使编码环境更加简洁。


## 项目结构

 ![image.png-44kB][2]
 
 - service-xxx：服务，服务拆分的最小单位。
 - service-xxx-api：服务定义，包括服务接口、Model、DTO、EventArg的定义。
 - service-xxx-impl：服务的实现，包括服务实现、Mapper。服务部署的最小单位。
 - web-xxx：web层，对外提供接口，可根据业务需要拆分为不同的web层。


  [1]: http://static.zybuluo.com/liuxx-/a929cejt4qq3p11tj0jmdrhi/image.png
  [2]: http://static.zybuluo.com/liuxx-/w92q0rd8wgql1edc4yfnj2vs/image.png
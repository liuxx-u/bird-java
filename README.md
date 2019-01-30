# bird-java概述

bird-java是以dubbo为基础的分布式服务框架，专注于业务开发，提炼后台应用中的经典业务场景，大幅减少开发编码量。

## 技术选型

 - 基础框架：spring
 - PRC框架：dubbo
 - 服务发现：zookeeper
 - EventBus，自研，支持Kafka与RabbitMQ
 - 服务网关：支持Dubbo与Spring Cloud
 - web层：spring-boot
 - 缓存：redis
 - 数据访问：mybatis、mybatis-plus
 - 数据库连接池：druid
 - 日志：slf4j、logback
 - 任务调度：quartz
 - 身份认证：自研单点登录

## 功能特性

bird-java提供了许多功能特性，包括：

 1. 分布式。每个服务均可集群部署，服务间可自由通信，每个服务可拥有自己的数据库，可单独做读写分离。
 2. EventBus事件总线。让服务间事件传递像本地程序一样简单。
 3. 全自动CRUD，表格增删查改、筛选、排序、分页均从框架层面解决，业务编码量不到20行。
 4. 实现从db->mappper->service->controller各层代码一键生成。
 5. 为web与service项目提供不同类型的starter，按配置注入对应的组件，使编码环境更加简洁。
 
## 使用指南

[1、通用查询使用指南][1]

[2、数据权限使用指南][2]

[3、操作日志接入指南][3]

[4、Eventbus使用指南][4]

[5、网关使用指南][5]


  [1]: https://github.com/liuxx001/bird-java/blob/master/docs/1%E3%80%81%E9%80%9A%E7%94%A8%E6%9F%A5%E8%AF%A2%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97.md
  [2]: https://github.com/liuxx001/bird-java/blob/master/docs/2%E3%80%81%E6%95%B0%E6%8D%AE%E6%9D%83%E9%99%90%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97.md
  [3]: https://github.com/liuxx001/bird-java/blob/master/docs/3%E3%80%81%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%97%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97.md
  [4]: https://github.com/liuxx001/bird-java/blob/master/docs/4%E3%80%81Eventbus%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97.md
  [5]: https://github.com/liuxx001/bird-java/blob/master/docs/5%E3%80%81%E7%BD%91%E5%85%B3%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97.md
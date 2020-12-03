# bird-java概述

bird-java是以Spring Boot为基础的开发增强组件包。

## 技术选型

 - 基础框架：Spring Framework 5.2.6.RELEASE
 - web层：Spring Boot 2.3.0.RELEASE
 - 数据访问：Mybatis-Plus 3.4.0
 - 数据库连接池：druid
 - 身份认证：自研单点登录
 - 执行链路：执行链路抽象，Skywalking适配
 - 分布式锁：统一抽象，Redis分布式锁适配
 - Eventbus：自研Eventbus，支持RocketMQ、Kafka、RabbitMQ
 - 状态机：自研状态机，状态机内部不存储状态，支持分布式环境

## 功能特性

[参考文档](https://www.yuque.com/books/share/5ceae84e-8c45-4068-91f0-88c54ab7488d)

bird-java提供了一些简单的功能特性，包括：

 1. [Spring Boot 功能增强；](https://www.yuque.com/docs/share/701ca7d4-dc0e-46a4-9fd0-371f4eb65313)
 2. [业务层开发指南；](https://www.yuque.com/docs/share/35e51fb5-8e2b-4d92-953f-5708174baa63)
 3. [异常处理机制；](https://www.yuque.com/docs/share/d934b703-e855-4bb6-80f4-36f82ab6a700)
 4. [执行链路追踪 - Trace；](https://www.yuque.com/docs/share/3e32797a-e123-4771-9e28-1860ccdeb481)
 5. [SSO使用指南；](https://www.yuque.com/docs/share/3c246bc0-0137-47c5-b019-d93a98c0d99e)
 6. [文件上传；](https://www.yuque.com/docs/share/587090e6-f91c-41ac-acb6-7a47f7cdfd61)
 7. [状态机使用指南；](https://www.yuque.com/docs/share/0d35016a-e5aa-4107-9e93-d6ed03fdb2de)
 8. [Eventbus使用指南；](https://www.yuque.com/docs/share/3f8fef0f-bdf3-485e-bab9-6c4a78a7289e)
 9. [分布式锁使用指南;](https://www.yuque.com/docs/share/b04d3789-bcc1-4048-ac16-5607c8466f18)
 

 只做一些通用功能的封装。


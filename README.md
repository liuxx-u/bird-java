# bird-java概述

bird-java是以Spring Boot为基础的开发增强组件包。

## 技术选型

 - 基础框架：Spring Boot 2.5.5、Spring Framework 5.3.10.RELEASE
 - 数据访问：Mybatis-Plus 3.4.3.1
 - 数据库连接池：druid
 - 身份认证：自研单点登录
 - 执行链路：执行链路抽象，Skywalking适配
 - 分布式锁：统一抽象，Redis分布式锁适配
 - Eventbus：自研Eventbus，支持RocketMQ、Kafka、RabbitMQ
 - 状态机：自研状态机，状态机内部不存储状态，支持分布式环境

## 功能特性

bird-java提供了一些简单的功能特性，包括：

 1. [Spring Boot 功能增强；](https://www.yuque.com/docs/share/8d949746-6ab5-4adc-881a-f353b4a47fe7)
 2. [业务层开发指南；](https://www.yuque.com/docs/share/14674a56-de0b-4b0f-b6e7-249456a47f90)
 3. [异常处理机制；](https://www.yuque.com/docs/share/904c95ee-61ac-4c3a-9448-b2d3cd0310e6)
 4. [执行链路追踪 - Trace；](https://www.yuque.com/docs/share/24986ccd-0153-46ec-9720-6ea03ded7edb)
 5. [SSO使用指南；](https://www.yuque.com/docs/share/e9f0720a-c18a-4c26-88f6-0c3be6b99cb5)
 6. [文件上传；](https://www.yuque.com/docs/share/7bce3f28-fe1c-4f29-889e-d1dc9056be73)
 7. [状态机使用指南；](https://www.yuque.com/docs/share/4373d6b9-bd3f-49b9-93d9-d2f0f74acfbc)
 8. [Eventbus使用指南；](https://www.yuque.com/docs/share/3ddd2558-b6ea-4d55-b8cc-aa60cdd2d1ef)
 9. [分布式锁使用指南;](https://www.yuque.com/docs/share/904b1677-9308-40cc-af38-b32586eb093a)
 

 只做一些通用功能的封装。


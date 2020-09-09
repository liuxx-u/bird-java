# bird-java概述

bird-java是以Spring Boot为基础的开发增强组件包。

## 技术选型

 - 基础框架：Spring Framework 5.2.6.RELEASE
 - web层：Spring Boot 2.3.0.RELEASE
 - 数据访问：Mybatis-Plus 3.3.2
 - 数据库连接池：druid
 - 身份认证：自研单点登录

## 功能特性

bird-java提供了一些简单的功能特性，包括：

 1. Spring Boot 功能增强；
 2. 全自动CRUD，表格增删查改、筛选、排序、分页均从框架层面解决，业务编码量不到20行；
 3. 自研轻量级SSO组件，设计优雅，可扩展；
 4. 多数据源支持；
 5. Long类型主键、String类型主键支持；
 
 只做一些通用功能的封装。


# bird-gateway概述

bird-gateway灵感来自于soul，soul地址：https://github.com/Dromara/soul。

## 功能特性

 - 使用注解的方式向网关暴露服务
 - 支持身份认证插件，认证通过后向dubbo传递session信息
 - 屏蔽dubbo参数信息，前端调用和普通http请求一致
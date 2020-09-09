# Spring Boot 增强

## 1、全局JSON结果封装

服务端统一返回JSON格式默认为：

```json
{
	"code": "00000",
	"message": "success",
	"timestamp": 1599630450582,
	"data": {}
}
```
### 1.1、相关配置

```yaml
bird:
  web:
    base-packages: com.example.demo
    global-result-wrapper: true
```

- base-packages：必填，包扫描范围内@RestController标记的接口会自动对结果进行封装。
- global-result-wrapper：是否开启JSON全局结果封装，默认：true

### 1.2、正常的结果返回

Controller中接口中不需要做额外处理，框架默认会对结果进行封装。

```java
@ResponseBody
@GetMapping("/json")
public DemoBO testJson(){
  return new DemoBO();
}
```
返回结果：
```json
{
	"code": "00000",
	"message": "success",
	"timestamp": 1599630450582,
	"data": {
		"name": "",
		"count": "",
		"createTime": "2020-09-09 13:47:30"
	}
}
```
### 1.3、忽略全局结果封装

对于某些特定的接口不需要进行统一封装的接口，可使用@com.bird.web.common.advice.JsonWrapperIgnore注解进行忽略。@JsonWrapperIgnore可作用与类上或方法上。

## 2、全局异常处理
### 2.1、相关配置

```yaml
bird:
  web:
    global-exception-enable: true
```

- 是否开启全局异常处理：默认：true

如果开启了全局异常处理，服务端出现异常时返回的结果统一为JSON格式：
```json
{
	"code": "",
	"message": "error message",
	"timestamp": 1599630450582,
	"data": null
}
```
### 2.2、异常处理
#### 2.2.1、校验类异常

- **Spring的校验框架：**即使用@org.springframework.validation.annotation.Validated标注的校验，校验失败时message直接向前端返回；处理异常包括 ：
   - org.springframework.web.bind.MethodArgumentNotValidException
   - javax.validation.ConstraintViolationException；
- **IllegalArgumentException：**抛出非法参数异常及其子类异常时，默认向前端返回message为："非法参数**"**
- **UserArgumentException：**自定义的用户友好的校验参数异常，默认直接向前端返回该异常的message。

校验类异常日志打印级别：**WARN**

#### 2.2.2、运行时异常

- **UserFriendlyException：**自定义的用户友好的运行时异常，默认直接向前端返回该异常的message。日志打印级别：WARN
- **其他异常：**默认向前端返回message为："系统走神了,请稍候再试."。日志打印级别：ERROR

#### 2.2.3、系统执行异常
默认向前端返回message为："系统走神了,请稍候再试."。日志打印级别：ERROR

## 3、接口版本号
版本号注解：@com.bird.web.common.version.ApiVersion。支持在Controller及其Method上标记版本号，优先级：Method上的版本号优先级 > Controller上的版本号。

### 3.1 使用示例
```java
package com.example.demo;

import com.bird.core.exception.UserFriendlyException;
import com.bird.web.common.advice.JsonWrapperIgnore;
import com.bird.web.common.version.ApiVersion;
import com.example.demo.pojo.DemoBO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@ApiVersion("v1")
public class DemoController {

    @GetMapping("/json")
    public DemoBO testJson(){
        return new DemoBO();
    }
    
    @GetMapping(value = "/exception")
    public void testException(){
        throw new UserFriendlyException("exception error");
    }
    
    @ApiVersion("v2")
    @GetMapping(value = "/api")
    public DemoBO testApi(){
        return new DemoBO();
    }
}

```
生成的的URL如下：
```html
/v1/test/json
/v1/test/exception
/v2/test/api
```
## 4、接口幂等性
接口幂等性依赖于Redis进行实现，流程步骤如下：

- 客户端先发送获取token的请求，服务端生成一个全局唯一的ID保存在redis中，同时把这个ID返回给客户端。
- 客户端调用业务请求的时候必须携带这个token，一般放在请求头上。
- 服务端会校验这个Token（一般是执行删除操作），如果校验成功，则执行业务。
- 如果校验失败，则表示重复操作，直接返回指定的结果给客户端。

![1.png](https://cdn.nlark.com/yuque/0/2020/png/191323/1599639711166-932ee269-3a91-48d0-a6c8-ce0af4bdc0f5.png#align=left&display=inline&height=343&margin=%5Bobject%20Object%5D&name=1.png&originHeight=343&originWidth=640&size=133780&status=done&style=none&width=640)
 图片来自网络，如有侵权，请联系我进行删除
 
### 4.1、添加依赖

接口幂等性的实现依赖于Redis：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
添加Redis的配置：

```yaml
spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    password: 123456
    timeout: 30000
```
### 4.2、幂等性配置

```yaml
bird:
  web:
    idempotency:
      enable: true
      expire: 120
      header-name: bird-idempotency
      error-message: 该操作已失效，请刷新后重试
```

- enable：启动幂等性相关功能，必填，默认：false
- expire：token有效期，单位：分钟；默认：120
- header-name：前端回传token的请求头名称，默认：bird-idempotency
- error-message：token验证失败时，返回的错误消息

### 4.3、获取token

开启幂等性功能之后，会自动暴露获取token的接口，接口地址为：
```html
/vb/idempotency/token
```
### 4.4、幂等性校验

在需要进行幂等校验的接口添加@com.bird.web.common.idempotency.Idempotency注解即可

## 5、跨域支持
### 5.1、相关配置

```yaml
bird:
  web:
    cors:
      enable: true
      url-patterns: /*
      allow-origin: default
      allow-methods: POST,GET,OPTIONS,DELETE
      allow-headers: Origin,X-Requested-With,Content-Type,Accept,Sso-Token,bird-idempotency,appId,tenantId,JSESSIONID
      max-age: 3600
      allow-credentials: true
```

- enable：是否启用CORS跨域资源共享，默认：false
- allow-origin：允许的来源网站，default表示不限制
- allow-methods：允许的请求方式
- allow-headers：允许传递的请求头
- allow-credentials：是否允许跨域设置Cookie

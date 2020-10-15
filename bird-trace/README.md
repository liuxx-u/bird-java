# 执行链路追踪 - Trace

<a name="uMyzo"></a>
## 1、请求记录

<a name="euaet"></a>
### 1.1、相关配置

请求记录用于记录请求发起时的参数信息，包括Header（请求头）、QueryString（URL参数）、Body参数。配置如下：

```yaml
bird:
  trace:
    request:
      trace-type: default
```

- trace-type ：跟踪方式，default表示开启默认的请求记录跟踪

<a name="LXPBm"></a>
### 1.2、Body参数记录

Body参数的记录需要开启[Body多次读取](https://www.yuque.com/wkmvwf/bkqgfh/vu62c4#AEFqj)的功能，配置如下：

```yaml
bird:
  web:
    body-read:
      enabled: true
```

- 只有请求方式为 **POST、PUT、PATCH **时才记录Body参数
- **_MultipartHttpServletRequest _**的请求不记录Body参数

<a name="dLNQY"></a>
## 2、方法调用记录

方法调用记录用于记录方法调用时的参数信息，包括方法参数、返回值。

<a name="BAb1x"></a>
### 2.1、添加依赖

方法调用记录是通过AOP切面进行实现，需要添加依赖：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

<a name="D8N0J"></a>
### 2.2、添加注解

在需要跟踪的方法上添加@com.bird.core.trace.Traceable 注解即可记录方法的入参、返回值、耗时。每进入一个被@com.bird.core.trace.Traceable标记的方法时，都将重新创建一个子跟踪信息（Child），退出方法时再回到当前跟踪信息。

<a name="TgfJe"></a>
### 2.3、获取当前跟踪信息

在程序任何地方都能通过如下方式获取到当前的Trace信息：

```java
TraceContext.current()
```

注意：跨线程获取当前Trace信息会失效。

<a name="yqSDx"></a>
## 3、数据字段变更记录

一些关键业务数据发生变更时，如果需要记录其变化前后的数据值，可开启数据字段变更记录跟踪。

<a name="FJjGv"></a>

### 3.1、相关配置
```yaml
bird:
  trace:
    db-field:
      enabled: true
```

- enabled：是否开启数据字段变更跟踪，默认：false

<a name="d58478e7"></a>
### 3.2、标记需要跟踪的字段

根据阿里巴巴开发手册，每一个DO对应数据库的一张表，如果需要对DO的某个字段进行跟踪其变化前后的数值，只需要在该字段上添加@com.bird.service.common.trace.TraceField即可。<br />数据字段变更信息将作为摘要的形式附加到当前Trace信息中，如果当前Trace信息不存在，数据字段变更信息将被丢弃。

<a name="w9vgf"></a>
## 4、日志存储

当整个Trace结束之后，Trace信息进入调度存储阶段，框架提供了默认的处理方式，开发者也可自己实现com.bird.core.trace.dispatch.ITraceLogDispatcher来自定义Trace信息的处理方式（比如发送至Skywalking）。

<a name="z3swW"></a>
### 4.1、日志格式

示例Trace格式如下：

```json
{
	"globalTraceId":"",
	"parentTraceId":"",
	"traceId": "17e37b2d-abb0-4396-a464-44dbed51a636",
	"description":"test",
	"startTime": 1602487329546,
	"endTime": 1602487332362,
	"entrance": "GET:/trace/field/update",
	"userId":"",
	"userName":"",
	"params": [{
		"headers": {
			"sec-fetch-mode": "navigate",
			"sec-fetch-site": "none",
			"accept-language": "zh-CN,zh;q=0.9",
			"cookie": "Idea-26a157a6=f8166351-4b63-49ba-935b-d8ae9c0435da; _ga=GA1.1.1019945537.1589424036",
			"sec-fetch-user": "?1",
			"accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
			"host": "localhost:8080",
			"upgrade-insecure-requests": "1",
			"connection": "keep-alive",
			"cache-control": "max-age=0",
			"accept-encoding": "gzip, deflate, br",
			"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36",
			"sec-fetch-dest": "document"
		},
		"params":"id=1&name=test",
		"body":""
	}],
	"returnValue":"",
	"claims": {
		"fields": [{
			"fields": [{"description": "名称","name": "name"}, {"description": "日期","name": "date"}],
			"news": [["'56e1e0fe-77be-47d6-91ea-d55a4555ed53'", "'2020-10-12 15:22:09.746'"]],
			"old": [["liuxx", "2020-09-30 13:16:26.094"]],
			"operate": "UPDATE",
			"sql": "UPDATE trace_demo  SET name='56e1e0fe-77be-47d6-91ea-d55a4555ed53',\ndescription='56e1e0fe-77be-47d6-91ea-d55a4555ed53',\nnum=5,\ndate='2020-10-12 15:22:09.746',\n\nmodifiedTime='2020-10-12 15:22:09.747'  WHERE id='0kfoxt0hi-18d3ee3fb2-0002qg-001'  AND delFlag=0",
			"table": "trace_demo",
			"traceId": "17e37b2d-abb0-4396-a464-44dbed51a636"
		}]
	}
}
```

<a name="yrxNw"></a>
### 4.2、默认存储方式

日志的处理由Spring容器中的com.bird.core.trace.dispatch.ITraceLogDispatcher来处理，框架提供了一个缺省实现：com.bird.core.trace.dispatch.DefaultTraceLogDispatcher。其实现原理是将Trace信息发送到一个内存队列，当队列到达一定阈值或指定间隔时间后，触发Spring容器中的ITraceLogStore进行一次保存操作。相关配置：

```yaml
bird:
  trace:
    dispatcher:
      threshold: 100
      period: 20
      poolSize: 1
      daemon: true
```

- threshold：阈值，当队列中数量达到阈值时，触发一次保存，默认：100；
- period：指定时间间隔，每个时间间隔，触发一次保存，默认：20秒；
- poolSize：消费者线程数，保存操作在线程池中完成，poolSize可指定线程池大小，默认：1；
- daemon：消费者线程是否是守护线程，默认：是；

<a name="Ve9Wv"></a>
## 5、分布式链路追踪方案集成

社区有很多非常优秀的开源分布式链路跟踪方案，框架中对于Trace的设计不是为了重复造轮子，而是为了让我们的链路跟踪需求能更好的与开源方案进行整合与适配。

<a name="2kHYg"></a>
### 5.1、SkyWalking

SkyWalking 是观察性分析平台和应用性能管理系统。提供分布式追踪、服务网格遥测分析、度量聚合和可视化一体化解决方案。

<a name="sWXGI"></a>
#### 5.1.1、添加依赖

```xml
<dependency>
  <groupId>com.bird</groupId>
  <artifactId>bird-trace-skywalking</artifactId>
  <version>2.0.2-RELEASE</version>
</dependency>
```
<a name="iCA8N"></a>
#### 5.1.2、请求记录集成

```yaml
bird:
  trace:
    request:
      trace-type: skywalking
```

- trace-type ：跟踪方式，skywalking表示开启Skywalking的请求记录跟踪，将在我们的Trace信息中关联Skywalking的traceId。

<a name="gPwx7"></a>
#### 5.1.3、日志存储

集成Skywalking后默认的存储方式将失效，所有Trace信息将发送至Skywalking，每条Trace对应Skywalking中的Span，Trace中的字段将作为Tag在Skywalking中展示。

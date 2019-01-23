# 4、Eventbus使用指南

## 1、添加pom依赖

```
<dependency>
	<groupId>com.bird</groupId>
	<artifactId>bird-service-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```
`bird-service-starter`会根据配置按需注入`Eventbus`组件，目前支持`Kafka`与`RabbitMQ`；


## 2、Eventbus配置

消息队列安装，略。

### 2.1、Kafka

如果使用`kafka`作为Eventbus的消息队列中间件，配置如下：

```
//配置host后，默认注入Provider
eventbus.kafka.host=localhost:9092
eventbus.kafka.provider.defaultTopic = thctay-kafka-default-topic
eventbus.kafka.provider.retries = 5
eventbus.kafka.provider.batchSize = 16384
eventbus.kafka.provider.lingerms = 3
eventbus.kafka.provider.bufferMemory = 33554432

//配置消费者扫描包路径后，默认注入Consumer
eventbus.kafka.listener.basePackages = com.bird.service.zero
eventbus.kafka.listener.groupId = thctay-service-crm-group
```
`Eventbus`会开启消费者线程，项目按需求决定是否开启，不需要监听消息的服务不要开启消费者配置。

### 2.2、RabbitMQ

如果使用`RabbitMQ`作为Eventbus的消息队列中间件，配置如下：

```
//配置address后，默认注入Provider
eventbus.rabbit.address = localhost:5672
eventbus.rabbit.user = guest
eventbus.rabbit.password = guest

//配置消费者扫描包路径后，默认注入Consumer
eventbus.rabbit.listenerPackages = com.bird.service.zero
```

## 3、Eventbus使用

### 3.1、提供者Provider

 - 定义事件
继承`com.bird.eventbus.arg.EventArg`定义事件，示例：
```
@Getter
@Setter
public class TestEventArg extends EventArg{
    private String value;
}
```

  - 发送事件

```
@Autowired
private EventBus eventBus;

public void test() {
    //...业务逻辑
    eventBus.push(new TestEventArg());
}

```

### 3.2、消费者Consumer

在Spring容器的`Bean`中使用`@com.bird.eventbus.handler.EventHandler`注解标记方法来监听事件，示例：

```
@Component
public class TestService {

    @EventHandler
    public void HandleEvent(TestEventArg eventArg) {
        System.out.println("notify======");
    }
}

```
在`Kafka`中，将使用`TestEventArg`的类名作为`topic`，传递消息；
在`RabbitMQ`中，将使用`TestEventArg`的类名作为`exchange`，消费者的服务名作为`queue`，使用`fanout`模式传递消息；

注意：`@EventHandler`标记的消费者方法只能接收一个参数，即`EventArg`及其子类的参数。

## 4、Eventbus消费结果保存

框架提供了两个接口，分别是：

1、保存消息发送结果的`com.bird.eventbus.register.IEventRegisterStore`；
2、保存消息消费结果的`com.bird.eventbus.handler.IEventHandlerStore`；

业务服务实现这两个接口，并注入到Spring容器中，Eventbus会在发送消息、消费消息时调用对应组件保存记录。
# 状态机使用指南

状态机用于控制程序状态流转，特性：

- 状态流转
- 内部不存储状态，支持在多个实例上部署
- 多条件支持，条件优先级设置

<a name="2.1"></a>
## 1. 添加依赖

```xml
<dependency>
  <groupId>com.bird</groupId>
  <artifactId>bird-statemachine</artifactId>
  <version>2.0.1-RELEASE</version>
</dependency>
```

<a name="a6fb4b0d"></a>
## 2. 状态机使用

<a name="0e769d26"></a>
### 2.1. 定义状态

自定义状态，建议使用枚举作为状态，示例：

```java
enum States {
    STATE1, 
    STATE2, 
    STATE3, 
    STATE4
}
```

<a name="d792c3be"></a>
### 2.2. 定义事件

自定义事件，建议使用枚举作为事件，示例：

```java
enum Events {
    EVENT1, 
    EVENT2, 
    EVENT3, 
    EVENT4
}
```

<a name="H3yWi"></a>
### 2.3. 定义业务上下文

```java
public class Context {
    String id = "123465";
    String operator = "liuxx";
    int amount = 10000;
}
```

<a name="6f9dae8f"></a>
### 2.4. 状态流转定义

假定有如下流程，状态流转定义如下：

```java
StateMachineBuilder<States, Events, Context> builder = StateMachineBuilder.init();
builder.transition()
    .from(States.STATE1)
    .on(Events.EVENT1)
    .perform(1, ctx -> ctx.amount > 10000, ctx -> States.STATE2)
    .perform(3,	ctx -> ctx.amount > 5000, ctx -> States.STATE3)
    .perform(2, ctx -> ctx.amount > 3000, ctx -> States.STATE4);

builder.transition()
    .from(States.STATE2)
    .on(Events.EVENT2)
    .perform(ctx -> States.STATE3);

builder.transition()
    .from(States.STATE2)
    .on(Events.EVENT3)
    .perform(ctx -> States.STATE4);

builder.build(machineId);
```

<a name="NsCHU"></a>
### 2.5. 业务状态流转

```java
StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get(machineId);
States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
```

<a name="f40adc50"></a>
## 3. 注意事项

<a name="EjtHJ"></a>
### 3.1. 条件判断的优先级

perform方法中支持条件判断的优先级，数字越小优先级越高，默认：Integer.MAX_VALUE<br />

<a name="olJqe"></a>
### 3.2. 不同节点业务上下文不同

不同的事件触发时，可能需要传递的业务上下文参数各不相同。可针对状态机定义基类的Context，不同事件节点可通过Context的子类去触发事件。<br />

<a name="FHVIV"></a>
## 4. 最佳实践

<br />推荐使用方式：将状态机注册为Spring的组件，perform方法中直接调用Service中的方法。即：业务处理放在业务Service中，状态机只控制状态流转与方法调用。示例代码：

```java
@Component
public class CreditStateMachine {

    private final static String MACHINE_ID = "creditAudit";

    private final ApplyService applyService;
    private final CreditService creditService;
    private final DebtService debtService;

    public CreditStateMachine(ApplyService applyService, CreditService creditService, DebtService debtService) {
        this.applyService = applyService;
        this.creditService = creditService;
        this.debtService = debtService;
    }

    public ApplyCodeEnum fireEvent(ApplyCodeEnum source,CreditEvent event,ApplyContext context){
        StateMachine<ApplyCodeEnum, CreditEvent, ApplyContext> stateMachine = StateMachineFactory.get(MACHINE_ID);
        return stateMachine.fireEvent(source,event,context);
    }

    @PostConstruct
    public void init() {
        StateMachineBuilder<ApplyCodeEnum, CreditEvent, ApplyContext> builder = StateMachineBuilder.init();
        builder.transition()
                .from(ApplyCodeEnum.INIT)
                .on(CreditEvent.SUBMIT)
                .perform(ctx -> {
                    /* applyService.submit(ctx)*/
                    return ApplyCodeEnum.CREDIT_APPROVAL_IN_PROGRESS;
                });

        builder.transition()
                .from(ApplyCodeEnum.CREDIT_APPROVAL_IN_PROGRESS)
                .on(CreditEvent.BANK_PROVED)
                .perform(ctx -> {
                    /* creditService.approved(ctx)*/
                    return ApplyCodeEnum.CREDIT_APPROVAL;
                });

        builder.transition()
                .from(ApplyCodeEnum.GUARANTOR_CREDIT_APPROVAL)
                .on(CreditEvent.BANK_PROVED)
                .perform(1, ctx -> ((DebtContext) ctx).getApplyAmount() > 10000, ctx -> {
                    System.out.println(ctx);
                    return ApplyCodeEnum.CREDIT_APPROVAL;
                }).perform(2, ctx -> ((DebtContext) ctx).getApplyAmount() > 5000, ctx -> {
                    System.out.println(ctx);
                    System.out.println(creditService.queryCreditByApplyCode("SX15962042036730002"));
                    return ApplyCodeEnum.REPAYMENT_APPROVAL;
                });

        builder.build(MACHINE_ID);
    }
}
```

业务层开发指南

```xml
<dependency>
  <groupId>com.bird</groupId>
  <artifactId>bird-service-support</artifactId>
  <version>2.0.2-RELEASE</version>
</dependency>
```

业务层强依赖mybatis-plus，当前版本3.4.0

<a name="8cyzU"></a>
## 1、DO层
DO（Data Object）: 阿里巴巴开发手册中专指数据库表一一对应的 POJO 类<br />
<br />框架提供了抽象的DO基类，如下：

- String类型主键的基类：StringPureDO、StringDO、StringFullDO
- Long类型主键的基类：LongPureDO、LongDO、LongFullDO

![image.png](https://cdn.nlark.com/yuque/0/2020/png/191323/1602582362906-25410dcf-2f4b-4317-97b8-d18b7aa76d76.png#align=left&display=inline&height=488&margin=%5Bobject%20Object%5D&name=image.png&originHeight=488&originWidth=1078&size=45130&status=done&style=none&width=1078)
<a name="5HmyQ"></a>

### 1.1、主键生成规则

- Long类型的主键生成规则：数据库主键自增
- String类型的主键生成规则：时间戳 + 机器标识 + 进程标识 + 序列号 （36进制）。可向Spring容器中注入com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator的实现类来自定义主键生成器。

<a name="8ySEe"></a>
### 1.2、审计字段
其中delFlag、createTime、modifiedTime、creatorId、modifierId会在DO保存时（新增或编辑）自动赋值。若需要关闭自动赋值，配置如下：

```yaml
bird:
  service:
    audit-meta-object: false
```

- audit-meta-object：是否自动填充审计字段，默认：true

<a name="9hicd"></a>
## 2、Mapper层
框架提供了抽象的Mapper基类：AbstractMapper<T extends IDO>

<a name="LrNqb"></a>
### 2.1、防止全表更新/删除

```yaml
bird:
  service:
    block-attack: true
```

- block-attack：是否阻止全表更新与删除，默认：true

<a name="y5Pxx"></a>
### 2.2、动态数据源
暂无<br />

<a name="EF2BA"></a>
## 3、Service层
框架提供了抽象的Service基类，如下：

- String类型主键的基类：AbstractStringService<M extends AbstractMapper<T>,T extends IDO<String>>
- Long类型主键的基类：AbstractLongService<M extends AbstractMapper<T>,T extends IDO<Long>>

<a name="rIfra"></a>
### 3.1、表格通用查询

[查看文档](https://www.yuque.com/docs/share/982887d4-2b1b-477a-b7de-ad50a493afb3)


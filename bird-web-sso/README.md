# SSO使用指南

<a name="1nSYv"></a>
## 1、SSO概述

单点登录（后文简称：sso）的定义是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。整个流程中涉及到的角色有：

- 浏览器；
- 应用服务，即业务系统；
- SSO服务器，所有业务系统登录的核心枢纽；

<a name="FOfhc"></a>
## 2、SSO登录流程

<a name="fz9NH"></a>
### 2.1、流程介绍

1. 用户通过浏览器访问业务系统中的受保护资源；
2. 业务系统验证用户是否有`token`，如果没有，将请求重定向至SSO服务器；
3. SSO服务器验证用户是否登录；
4. 未登录则在SSO服务器进行登录，在SSO服务器创建`token`（使用Cookie存储）；已登录则跳过此步骤；
5. SSO服务器携带token重定向回业务系统；
6. 业务系统拿到`token`，存入Cookie或localStorage；
7. 业务系统获取请求中的`token`，并通过SSO服务器解析用户信息并缓存；

<a name="mGfVi"></a>

### 2.2、流程图
![](http://static.zybuluo.com/liuxx-/ulcdanjb8p1l1uchw4smgctd/image.png#align=left&display=inline&height=723&margin=%5Bobject%20Object%5D&originHeight=723&originWidth=561&status=done&style=none&width=561)

<a name="edpYH"></a>
## 3、SSO服务器部署

<a name="3n6x1"></a>
### 3.1、添加pom依赖

```xml
<dependency>
  <groupId>com.bird</groupId>
  <artifactId>bird-web-sso-server</artifactId>
  <version>2.0.1-RELEASE</version>
</dependency>
```

<a name="3tiNK"></a>
### 3.2、添加配置

SSO服务器相关配置以`bird.sso.server`开头，具体配置项包括：

```yaml
bird:
  sso:
    server:
      cookie-name: Sso-Token
      # expire: 120
      # useSessionStore: true
      # autoRefresh: true
```

- cookieName ：SSO服务器Cookie的名称，必填。
- expire：有效期，单位分，默认120。<br />
- useSessionStore：是否使用Session存储器，默认是。
- autoRefresh：是否自动刷新，开启Session存储器后才可生效，默认是。

SSO服务器默认使用`SessionStore`存储Session信息，并且提供默认的JVM缓存实现，实现`ITicketSessionStore`并注入Spring容器即可完成替换（推荐使用Redis作为Session的存储器实现）。SSO服务器建议使用Session存储器功能进行Session的保存。

当不使用Session存储器时，即服务端不保存Session信息，需要实现`ITicketProtector`并注入Spring容器，SSO服务器将不会保存Session，而是将用户信息加密成字符串当作`token`返回业务系统。

<a name="uFUmS"></a>
### 3.3、登录、注销

SSO服务器添加登录接口，完成用户登录验证，调用`SsoServer`即可完成登录与注销功能：

```java
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private SsoServer ssoServer;
    
    @GetMapping("/login")
    public OperationResult<String> getToken(String userName, String password, HttpServletResponse response) {
        //TODO: 验证用户名密码
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setUserId(1L);
        return ssoServer.login(response, ticketInfo);
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        ssoServer.logout(request, response);
        return "logout";
    }
}
```

<a name="g2Blq"></a>
## 4、应用服务接入

<a name="Y1ldQ"></a>
### 4.1、服务端接入

<a name="jJOoz"></a>
#### 4.1.1、添加pom依赖

```xml
<dependency>
    <groupId>com.bird</groupId>
    <artifactId>bird-web-sso-client</artifactId>
    <version>2.0.1-RELEASE</version>
</dependency>
```

<a name="S727Z"></a>
#### 4.1.2、添加配置

SSO服务器相关配置以`bird.sso.client`开头，具体配置项包括：

```yaml
bird:
  sso:
    client:
      cookie-name: Sso-Token
      server: https://sso.xxx.com
      host: https://client.xxx.com
      app-id: appId
      # cache: 20
```

- cookieName ：应用服务Cookie的名称（或Header头名称），必填。
- server：SSO服务器地址，必填。
- host：客户端主机地址，必填。用于SSO注销时调用。
- app-id：用作自定义用户登录信息的隔离（多租户）。
- cache：客户端缓存票据时间，单位分，默认20。

<a name="JN2nr"></a>
#### 4.1.3、获取服务端票据信息

使用`SsoClient`获取服务端票据信息，示例:

```java
@Component
public class SsoClientAuthorizeInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private SsoClient ssoClient;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        TicketInfo ticketInfo = ssoClient.getTicket(request);
        // 实现业务系统自己的验证逻辑
        return true;
    }
}
```

`ssoClient`会获取`HttpServletRequest`中的的`token`（先从`Header`读取，没有则从`Cookie`读取），根据该`token`去SSO服务器获取票据信息并缓存在客户端（客户端缓存时间可配置，默认20分钟），请求失败会默认重试两次。

<a name="MuBc3"></a>
### 4.2、权限支持

实现`com.bird.web.sso.client.permission.IUserPermissionChecker`接口并注入容器即可完成对userId的权限拦截。

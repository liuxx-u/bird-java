## 1 SSO概述

单点登录（后文简称：sso）的定义是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。整个流程中涉及到的角色有：

 - 浏览器；
 - 应用服务器，即业务系统；
 - SSO服务器，所有业务系统登录的核心枢纽；

## 2 SSO登录流程

### 2.1 流程介绍

1. 用户通过浏览器访问业务系统中的受保护资源；
2. 业务系统验证用户是否有token，如果没有，将请求重定向至SSO服务器；
3. SSO服务器验证用户是否登录；
4. 未登录则在SSO服务器进行登录，在SSO服务器创建token（使用Cookie存储）；已登录则跳过此步骤；
5. SSO服务器携带token重定向回业务系统；
6. 业务系统拿到Token，存入Cookie或localStorage；
7. 业务系统获取请求中的Token，并通过SSO服务器解析用户信息并缓存；

### 2.2 流程图

![image.png-42.8kB][1]

## 3 SSO服务器部署

- 引用`bird-web-sso-server`包：

```
<dependency>
	<groupId>com.bird</groupId>
	<artifactId>bird-web-sso-server</artifactId>
	<version>1.0.0</version>
</dependency>
```

- 在`application.properties`文件中添加配置：

```
# SSO服务器Cookie的名称，必填
bird.sso.server.cookieName = Sso-Token
# 有效期，单位分，默认360。
bird.sso.server.expire = 360
# 是否使用Session存储器，默认是。
bird.sso.server.useSessionStore = true
# 是否自动刷新，开启Session存储器后才可生效，默认是。
bird.sso.server.autoRefresh = true
```

SSO服务器默认使用`SessionStore`存储Session信息，并且提供默认的JVM缓存实现，实现`ITicketSessionStore`并注入Spring容器即可完成替换（推荐使用Redis作为Session的存储器实现）。

当不使用Session存储器时，即服务端不保存Session信息，需要实现`ITicketProtector`并注入Spring容器，SSO服务器将不会保存Session，而是将用户信息加密成字符串当作`token`返回业务系统。

- 登录、注销

SSO服务器添加登录接口，完成用户登录验证，调用`SsoServer`即可完成登录与注销功能：
```
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

## 4 应用服务器接入

### 4.1 服务端接入

- 引用`bird-web-sso-client`包：

```
<dependency>
	<groupId>com.bird</groupId>
	<artifactId>bird-web-sso-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

- 在`application.properties`文件中添加配置：

```
# 客户端cookie或header名称，必填
bird.sso.client.cookieName = Sso-Token
# sso server地址，必填
bird.sso.client.server = http://sso.xxx.com
# 当前client地址，必填
bird.sso.client.host = http://client.xxx.com
# 客户端缓存时间，单位：分 （默认30）
bird.sso.client.cache = 30
```


- 使用`SsoClient`获取服务端票据信息，示例:
```
@Component
public class SsoClientAuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SsoClient ssoClient;

    /**
     * 拦截器处理方法
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        TicketInfo ticketInfo = ssoClient.getTicket(request);

        // 实现业务系统自己的验证逻辑

        return true;
    }
}
```

`ssoClient`会获取`HttpServletRequest`中的的`token`（先从`Header`读取，没有则从`Cookie`读取），根据该`token`去SSO服务器获取票据信息并缓存在客户端（客户端缓存时间可配置，默认30分钟），请求失败会默认重试三次。

### 4.2 权限支持

SSO客户端不提供具体的权限设计，甚至不对外部系统提供默认的拦截器，只提供了`SsoClient`获取SSO服务器的票据信息，业务系统可根据自身需求实现请求的拦截与权限验证。示例参考上文的拦截器代码

[1]: http://static.zybuluo.com/liuxx-/ulcdanjb8p1l1uchw4smgctd/image.png
# authing-spring-boot-starter
## 添加依赖

在您的 Spring 项目中引入：

```xml
<dependency>
    <groupId>cn.authing</groupId>
    <artifact>authing-spring-boot-starter</artifact>
    <version>{lastest-version}</version>
</dependency>
```



> 由于 okhttp3 的版本冲突问题，请从您的 `spring-boot-starter-web`中排除 `okhttp`, 使用 `okhttp:4.8.0`版本。

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <groupId>com.squareup.okhttp3</groupId>
      <artifactId>okhttp</artifactId>
    </exclusion>
  </exclusions>
</dependency>
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>4.8.0</version>
</dependency>
```



## 配置

在 `application.yml` 配置文件中添加您的用户池与应用的相关配置：

```yml
authing:
  user-pool:
    user-pool-id: AUTHING_USERPOOL_ID
    secret: AUTHING_USERPOOL_SECRET

  app:
    app-id: APP_ID
    app-host: APP_HOST
```

- `authing.user-pool.user-pool-id`：用户池 ID。
- `authing.user-pool.secret`：用户池密钥。
- `authing.app.app-id`：应用ID。
- `authing.app.app-host`：应用域名，格式为 `https://YOUR_DOMAIN.authing.cn`

> 你可以在此[了解如何获取 UserPoolId](https://docs.authing.cn/v2/guides/faqs/get-userpool-id-and-secret.html), 在控制台的**应用**中查看自己的应用列表。



## 管理授权规则

参考这里[对用户进行权限管理](https://docs.authing.cn/v2/guides/access-control/) 创建相应的`资源`以及`授权`。

注意，资源操作请填 'read'，目前仅支持 read 操作。

![](./img/ins.png)



## 失败处理

如果您希望全局地给验证失败的请求返回 HTTP 403 异常，可在您的全局异常处理器中添加以下内容：

```java
@ControllerAdvice(basePackages = "YOUR-PACKAGE")
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthingException.class)
    public ResponseResult handleAuthingException(AuthingException e) {
        log.error("log:*token error{},exception class:{}",e.getMessage(),e.getClass());
        return ResponseVO.fail(403,"wrong token");
    }
}
```

## 开始使用

进行功能测试：

```java
@GetMapping("/test/resources")
public ResponseVO getResource() {
    return ResponseVO.success().setMessage("some resource need authorization");
}
```

携带正确的 token:

```http
GET /product/attr/info/<attrId> HTTP/1.1
Host: localhost:8080
User-Agent: apifox/1.0.0 (https://www.apifox.cn)
Authorization: YOUR TOKEN
```

响应结果：

```json
{
    "code":200,
    "message":"some resource need authorization"
}
```

不携带 token 或携带错误的 token :

```http
GET /product/attr/info/<attrId> HTTP/1.1
Host: localhost:8080
User-Agent: apifox/1.0.0 (https://www.apifox.cn)
Authorization: WRONG TOKEN
```

响应结果：

```json
{
    "code":403,
    "message":"wrong token"
}
```


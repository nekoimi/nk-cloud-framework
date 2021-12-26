# 认证服务

### 公钥生成

```shell

jwt生产证书

keytool -genkeypair -alias jwtkey -keyalg RSA -validity 365 -keypass 123456 -keystore jwtkey.jks -storepass 123456


查看证书信息

keytool -list -v -keystore jwtkey.jks -storepass 123456



查看公钥

keytool -list -rfc -keystore jwtkey.jks -storepass 123456

```

### 扩展登录认证方式

- 1 token -> 添加自定义AuthenticationToken

- 2 实现接口RequestToAuthenticationTokenConverter将请求参数封装成上述自定义token

- 3 实现接口AuthenticationSupportManager完成认证逻辑
    实现参考：org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager

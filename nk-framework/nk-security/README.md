# nk-security


### 登录接口认证filter,只作用于登录认证接口

org.springframework.security.web.server.authentication.AuthenticationWebFilter

### 资源授权保护filter,作用于所有需要权限认证的接口

org.springframework.security.web.server.authorization.AuthorizationWebFilter

### 加载授权信息filter

org.springframework.security.web.server.context.ReactorContextWebFilter

### 全局安全信息存储

org.springframework.security.web.server.context.ServerSecurityContextRepository

默认实现是基于session的，鉴于全部使用JWT，
所以需要自定义实现Redis版本的，以token为key，
认证后的AuthenticationToken为value，保存在redis中

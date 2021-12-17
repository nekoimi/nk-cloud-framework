# 认证服务

公钥生成

```shell

jwt生产证书

keytool -genkeypair -alias jwtkey -keyalg RSA -validity 365 -keypass 123456 -keystore jwtkey.jks -storepass 123456


查看证书信息

keytool -list -v -keystore jwtkey.jks -storepass 123456



查看公钥

keytool -list -rfc -keystore jwtkey.jks -storepass 123456

```

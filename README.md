# OOOO Goods Service

Hosting goods-service by OOOO.

## Test

Create a profile for testing `goodsservice/src/main/resources/application-dev.yaml`.

With something like this:

```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/oomall?serverTimezone=GMT%2B8
    username: dbuser
    password: 123456
  redis:
    host: localhost
    port: 6379
    password: 123456
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
dubbo:
  registry:
    address: nacos://localhost:8848
  protocol:
    name: dubbo
    port: 20890
  consumer:
    timeout: 4000
    check: false
goods-service:
  image-pool:
    dav-url: http://i.havent.setup/yet
    dav-username: davuser
    dav-password: 123456
    max-file-size: 2
```

And change accordingly.
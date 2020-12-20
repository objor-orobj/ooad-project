# OOOO Goods Service

Hosting goods-service by OOOO.

## Testing

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

## Routing

Run `python gen-route.py` to get the route, it should look like this:

```yaml
/comments,/comments/states,/orderItems/*/comments,/shops/*/comments/**,/skus/*/comments,/coupons/**,/couponactivities/**,/shops/*/couponactivities/**,/shops/*/couponskus/**,/flashsales/current,/shops/*/timesegments/*/flashsales,/timesegments/*/flashsales,/shops/*/flashsales/**,/groupons/**,/shops/*/groupons/**,/shops/*/spus/*/groupons,/presales/**,/shops/*/presales/**,/shops/*/skus/*/presales,/shops/*,/shops/*/newshops/**,/shops/*/onshelves,/shops/*/offshelves,/brands,/shops/*/brands,/shops/*/brands/**,/shops/*/spus/*/brands/**,/categories/**,/shops/*/categories/**,/shops/*/spus/*/categories/**,/share/*/skus/*,/shops/*/skus/*/floatPrices,/shops/*/floatPrices/*,/skus,/skus/states,/shops/*/skus/*,/shops/*/skus/*/uploadImg,/shops/*/skus/*/onshelves,/shops/*/skus/*/offshelves,/spus/*,/shops/*/spus/** 
```
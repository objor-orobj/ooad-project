# OOOO Goods Service

Hosting goods-service by OOOO.
## 
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
- id: shop-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/states
- id: shop-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops,/shops/{id},/shops/{shopId}/newshops/{id}/audit,/shops/{id}/onshelves,/shops/{id}/offshelves
  filters:
  - Auth=authorization
- id: groupon-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/groupons/states
- id: groupon-user
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/groupons
  filters:
  - Auth=authorization
- id: groupon-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{id}/groupons,/shops/{shopId}/spus/{id}/groupons,/shops/{shopId}/groupons/{id},/shops/{shopId}/groupons/{id}/onshelves,/shops/{shopId}/groupons/{id}/offshelves
  filters:
  - Auth=authorization
- id: presales-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/presales/states,/presales
- id: presales-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{shopId}/presales,/shops/{shopId}/skus/{id}/presales,/shops/{shopId}/presales/{id},/shops/{shopId}/presales/{id}/onshelves,/shops/{shopId}/presales/{id}/offshelves
  filters:
  - Auth=authorization
- id: flash-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/timesegments/{id}/flashsales,/flashsales/current
- id: flash-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{did}/timesegments/{id}/flashsales,​/shops​/{did}​/flashsales​/{id},/shops/{did}/flashsales/{id}/onshelves,/shops/{did}/flashsales/{id}/offshelves,/shops/{did}/flashsales/{id}/flashitems,/shops/{did}/flashsales/{fid}/flashitems/{id}
  filters:
  - Auth=authorization
- id: goods-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/skus/states,/skus,/skus/{id},/spus/{id}
- id: goods-user
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/share/{sid}/skus/{id}
  filters:
  - Auth=authorization
- id: goods-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{shopId}/spus/{id}/skus,/shops/{shopId}/skus/{id}/uploadImg,/shops/{shopId}/skus/{id},/shops/{id}/spus,/shops/{shopId}/spus/{id}/uploadImg,/shops/{shopId}/spus/{id},/shops/{shopId}/skus/{id}/onshelves,/shops/{shopId}/skus/{id}/offshelves,/shops/{shopId}/skus/{id}/floatPrices,/shops/{shopId}/floatPrices/{id}
  filters:
  - Auth=authorization
- id: comments-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/comments/states,/skus/{id}/comments
- id: comments-user
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/orderitems/{id}/comments,/comments
  filters:
  - Auth=authorization
- id: comments-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{did}/comments/{id}/confirm,/shops/{id}/comments/all
  filters:
  - Auth=authorization
- id: category-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/categories/{id}/subcategories,/brands
- id: category-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{shopId}/categories/{id}/subcategories,/shops/{shopId}/categories/{id},/shops/{shopId}/categories/{id},/shops/{id}/brands,/shops/{shopId}/brands/{id}/uploadImg,/shops/{shopId}/brands/{id},/shops/{shopId}/brands/{id},/shops/{shopId}/spus/{spuId}/categories/{id},/shops/{shopId}/spus/{spuId}/categories/{id},/shops/{shopId}/spus/{spuId}/brands/{id},/shops/{shopId}/spus/{spuId}/brands/{id}
  filters:
  - Auth=authorization
- id: coupon-no-login
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/coupons/states,/couponactivities,/couponactivities/{id}/skus
- id: coupon-user
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{shopId}/couponactivities/{id},/coupons,/couponactivities/{id}/usercoupons
  filters:
  - Auth=authorization
- id: coupon-admin
  uri: http://172.16.1.181:8080/
  predicates:
  - Path=/shops/{shopId}/couponactivities,/shops/{shopId}/couponactivities/{id}/uploadImg,/shops/{id}/couponactivities/invalid,/shops/{shopId}/couponactivities/{id},/shops/{shopId}/couponactivities/{id}/skus,/shops/{shopId}/couponskus/{id},/shops/{shopId}/couponactivities/{id}/onshelves,/shops/{shopId}/couponactivities/{id}/offshelves
  filters:
  - Auth=authorization
```

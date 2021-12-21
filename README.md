# springcloudalibaba
可参考官方文档搭建： https://nacos.io/zh-cn/docs/quick-start.html
本次只记录自己搭建的过程
从最新稳定版本 下载稳定版本放入到source_code中 文件夹名称为nacos_server
执行sh startup.sh -m standalone启动单机模式
访问http://127.0.0.1:8848/nacos即可进入管理页面，用户名密码皆是nacos
在idea中同一个项目启动多个端口的方式：
找出runDashboad，如没有 在


中新增一个右下方会弹出是否使用runDashboard


复制一个并这是VM options



在gateway中必须引入（注意：gateway中不引入下面dependency会导致lb://example-business会导致动态负载均衡不生效）
```java
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-loadbalancer</artifactId>
   <version>3.0.2</version>
   <type>pom</type>
   <scope>import</scope>
</dependency>
```

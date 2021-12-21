# springcloudalibaba
可参考官方文档搭建： https://nacos.io/zh-cn/docs/quick-start.html
本次只记录自己搭建的过程
从最新稳定版本 下载稳定版本放入到source_code中 文件夹名称为nacos_server
执行sh startup.sh -m standalone启动单机模式
访问http://127.0.0.1:8848/nacos即可进入管理页面，用户名密码皆是nacos
在idea中同一个项目启动多个端口的方式：
找出runDashboad，如没有 在
<img width="438" alt="2FAB0C38-1999-455B-BC3E-C4CB6E43B22A" src="https://user-images.githubusercontent.com/35331347/146879858-280ee5c2-60bd-4517-9dab-9486b074ad3b.png">


中新增一个右下方会弹出是否使用runDashboard

<img width="438" alt="DF669558-49A1-4DD2-BB3B-92B781FCC7ED" src="https://user-images.githubusercontent.com/35331347/146879864-81ef5890-7a1e-4b70-8994-e732eb9ff3d5.png">
   
复制一个并这是VM options

<img width="438" alt="8268AA95-F37E-4DEA-A741-E74D54A6ACD4" src="https://user-images.githubusercontent.com/35331347/146879878-a36d4b6c-17f1-4d99-b636-164836a41690.png">


在gateway中必须引入（注意：gateway中不引入下面dependency会导致lb://example-business会导致动态负载均衡不生效）
```xml
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-loadbalancer</artifactId>
   <version>3.0.2</version>
   <type>pom</type>
   <scope>import</scope>
</dependency>
```

<font color=#FF0000 >**注意：搭建微服务程序各种异常多数都是版本不兼容和没有正确引入包导致的问题**></font>

如果springcloud和springboot的版本对应不上可能会存在注册中心无法注册到nacos的情况
版本不一致会出现各种莫名其妙的问题

# springcloudalibaba
可参考官方文档搭建： https://nacos.io/zh-cn/docs/quick-start.html
本次只记录自己搭建的过程
- 从最新稳定版本 下载稳定版本放入到source_code中,文件夹名称为nacos_server
- 执行sh startup.sh -m standalone启动单机模式
- 访问http://127.0.0.1:8848/nacos 即可进入管理页面，用户名密码皆是nacos
 
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

**注意：搭建微服务程序各种异常多数都是版本不兼容和没有正确引入包导致的问题**

- 如果springcloud和springboot的版本对应不上可能会存在注册中心无法注册到nacos的情况
版本不一致会出现各种莫名其妙的问题

- Springcloud 阿里巴巴 && SpringBoot版本对应表
https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

 
- runDashbouad不显示请参考：
 https://github.com/wanghanchao2012/springcloudalibaba/blob/master/config-show.md

![Uploading image.png…]()

Hoxton.SR1相适应的springboot和alibabacloud的版本默认读取bootstrap.yaml的配置（试着删了用appcation.yml会报错，是强制使用）
但是Hoxton.SR12或2020.01等更高版本修改了默认读取bootstrap.yaml的规则，如果在Hoxton.SR1的基础上直接升级到2020.01会升级失败，因为2020.01后默认不再支持bootstrap.xml
如果想继续使用需要引入
```xml
   <dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-bootstrap</artifactId>
				<version>3.0.4</version>
			</dependency>
```

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

<img width="600" height="300" alt="图片1" src="https://user-images.githubusercontent.com/35331347/146888331-cfd9515b-ac8a-4d06-bd35-0bd4505dbe2e.png">

Hoxton.SR1相适应的springboot和alibabacloud的版本默认读取bootstrap.yaml的配置（试着删了用appcation.yml会报错，是强制使用）
但是Hoxton.SR12或2020.01等更高版本修改了默认读取bootstrap.yaml的规则，如果在Hoxton.SR1的基础上直接升级到2020.01会升级失败，
因为Hoxton.SR3后默认不再支持bootstrap.xml,如果想继续升级需要引入
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bootstrap</artifactId>
	<version>3.0.4</version>
</dependency>
```



sentinel在springcloudalibaba中的使用
 - 在使用前需要需要安装并运行sentinel-dashboard-1.8.1.jar，默认端口是8080 ，用户名密码都是sentinel
 - sentinel的熔断、限流、等规则需要持久化，否则重启就会都是，所以yml需要配置datasource层的相关参数，且需要在pom中引入sentinel-datasource-nacos
 - sentinel的各个模块都需要在pom中引入相应的dependency，否则无法使用
 
 pom.xml相关配置
```xml
<dependencies>
		<dependency>
			<groupId>com.alibaba.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba.csp</groupId>
			<artifactId>sentinel-transport-simple-http</artifactId>
		</dependency>
		<!--alibaba sentinel 热点限流-->
		<dependency>
			<groupId>com.alibaba.csp</groupId>
			<artifactId>sentinel-parameter-flow-control</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba.csp</groupId>
			<artifactId>sentinel-web-servlet</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba.csp</groupId>
			<artifactId>sentinel-datasource-nacos</artifactId>
		</dependency>
		<dependency>
			<groupId>com.example</groupId>
			<artifactId>example-common</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>
```
 
 sentinel持久化的配置方式：
 
 在配置列表加入${spring.application.name}的dataId，数据类型 json
 内容：
 ```json 
 [
    {
        "resource": "/testA",
        "limitApp": "default",
        "grade": 1,
        "count": 5,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
 ]
 ```
此处的/testA是的要配置的业务接口的路径 

具体参数说明：
resource：资源名称
limitApp：来源应用
grade：阀值类型，0-线程数，1-qps
count：单机阀值
strategy：流控模式，0-直接，1-关联，2-链路
controlBehavior：流控效果，0-快速失败，1-warm up，2-排队等待
clusterMode：是否集群 
 
 application.yml配置
 ```xml
 server:
  port: 8401
spring:
  application:
    name: example-sentinel
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
    sentinel:
      transport:
        dashboard: 127.0.0.1:8080
        # 应用与Sentinel控制台交互的端口，应用本地会起一个该端口占用的HttpServer
        # 默认8719端口，假如端口被占用，依次+1，直到找到未被占用端口
        port: 8719
      datasource:
        dsl:
          nacos:
            server-addr: 127.0.0.1:8848
            dataId: example-sentinel
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow
management:
  endpoints:
    web:
      exposure:
        include: '*
 ```
 
<<EOF








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

```
resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称。
resourceMode：规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route。
grade：限流指标维度，同限流规则的 grade 字段。
count：限流阈值
intervalSec：统计时间窗口，单位是秒，默认是 1 秒。
controlBehavior：流量整形的控制效果，同限流规则的 controlBehavior 字段，目前支持快速失败和匀速排队两种模式，默认是快速失败。
burst：应对突发请求时额外允许的请求数目。
maxQueueingTimeoutMs：匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效。
paramItem：参数限流配置。若不提供，则代表不针对参数进行限流，该网关规则将会被转换成普通流控规则；否则会转换成热点规则。其中的字段：
parseStrategy：从请求中提取参数的策略，目前支持提取来源 IP（PARAM_PARSE_STRATEGY_CLIENT_IP）、Host（PARAM_PARSE_STRATEGY_HOST）、任意 Header（PARAM_PARSE_STRATEGY_HEADER）和任意 URL 参数（PARAM_PARSE_STRATEGY_URL_PARAM）四种模式。
fieldName：若提取策略选择 Header 模式或 URL 参数模式，则需要指定对应的 header 名称或 URL 参数名称。
pattern：参数值的匹配模式，只有匹配该模式的请求属性值会纳入统计和流控；若为空则统计该请求属性的所有值。（1.6.2 版本开始支持）
matchStrategy：参数值的匹配策略，目前支持精确匹配（PARAM_MATCH_STRATEGY_EXACT）、子串匹配（PARAM_MATCH_STRATEGY_CONTAINS）和正则匹配（PARAM_MATCH_STRATEGY_REGEX）。（1.6.2 版本开始支持）
```
 
 application.yml配置
 ```yml
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
 
 
 spring.cloud.gateway.routes.id=httpbin_route
 ![image](https://user-images.githubusercontent.com/35331347/147173731-a5db1de2-395c-4c6a-9bfc-5736484847b2.png)
上图中的httpbin_route就是gateway的application.yml中的 spring.cloud.gateway.routes.id，用于对应唯一routeid对应的sentinel限流等规则的配置
 
<<EOF








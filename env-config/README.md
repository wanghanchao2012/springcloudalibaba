本次以Mac版的 jenkins搭建及使用流程为例:

1.`brew install jenkins`  使用brew来安装jenkins, 该命令是安装最新稳定版的jenkins ,也可指定版本

<img width="801"  height="360"  alt="image-20220916164811275" src="https://user-images.githubusercontent.com/35331347/190598804-1198d33e-4dd5-4473-9f0c-9845e14feeed.png">


2.其中我在安装的时候，有报一个错：tar: Error opening archive: Failed to open，网上说是清华源的问题，需要临时修改环境，使用export HOMEBREW_BOTTLE_DOMAIN=''

3.于是先执行`export HOMEBREW_BOTTLE_DOMAIN=''`再执行`brew install jenkins`

4.## 启动
brew services start jenkins

5.<img width="801" height="360" alt="image" src="https://user-images.githubusercontent.com/35331347/190598987-6eb0e3d6-cccf-46cc-b049-b901809ac8e6.png">
手动安装推荐的插件
    (注意: 此处有很大的坑,就mac版本来讲,如果安装的不是最新版本,那么会报各种缺失插件的错误, 手动安装插件头疼,索性干脆就用最新版的,不过从官网来看
    “开源 Devops 工具 Jenkins 在官方博客平台宣布，从2022年6 月 28 日发布的 Jenkins 2.357 和将于 9 月发布的 LTS 版本开始，Jenkins 需要 Java 11 才能使用，将放弃 Java 8。”
    未来最新版将不再支持jdk1.8,国内多数开发团队及人员在使用1.8,此时应该怎么办?  好办,下一个jdk11及以上的版本专门用于jenkins部署)



http://localhost:9090/    admin   111111    

    安装参考文章:https://blog.csdn.net/qq_35692974/article/details/125523267

------

jenkins部署配置:

1.微服务或多模块的maven工程在部署的时候往往存在相互依赖的情况,比如 business-service/business-dao依赖common ,当部署 business-service需要先 构建common再构建business-service,这种构建方式如何配置?

<img width="1112" alt="image-20220916174435650" src="https://user-images.githubusercontent.com/35331347/190613862-d919a41a-ed26-4f17-8092-981d3f42268f.png">


2.除了在pom.xml中配置repackage外 项目根目录下的pom.xml需要配置modules,这样才能在单独构建某个模块时,自动判断其所依赖的其他模块,比如 business-service依赖common,当部署business-service时会先构建common,其他用不到的模块则不会产生重新构建的动作:
<img width="1114" alt="image-20220916174756572" src="https://user-images.githubusercontent.com/35331347/190613958-9263d77f-cf48-4e9e-ad6b-b2ccf3aaa35d.png">



**<u>及repackage决定了打包时将所有依赖加入,而modules是提前为repackage所需要的模块做构建</u>**

3.项目Configuiration中的Pre Steps

```shell
cd /Users/wanghanchao/work/tech/jenkinsware/
rm -rf example-business-0.0.1-SNAPSHOT.jar
cp /Users/wanghanchao/.jenkins/workspace/example-business/example-business/target/example-business-0.0.1-SNAPSHOT.jar ./
BUILD_ID=DONTKILLME
./shell-start-server.sh stop
./shell-start-server.sh start
```

构建项目配置:

<img width="1018" alt="image-20220916175702104" src="https://user-images.githubusercontent.com/35331347/190614023-723e5c70-04d7-458c-bc3a-559dbf4d75ed.png">
<img width="1055" alt="image-20220916175649165" src="https://user-images.githubusercontent.com/35331347/190614086-d9f488e6-e6c5-48aa-b432-c8d0c64ca68d.png">

jenkins可配置多个jdk
在安装jenkins的时候,用到的jdk时11 当在全局配置中配置其他jdk时,在发布项目时会有jdk版本冲突提示,但是不影响构建结果
<img width="700" height="360" alt="image" src="https://user-images.githubusercontent.com/35331347/190947513-8bcff6e9-2bcb-419c-82be-735c1c524a79.png">
<img width="700" height="360" alt="image" src="https://user-images.githubusercontent.com/35331347/190947581-f1cdedff-d115-42ee-93d0-04eab5a6fd72.png">


Jenkins 的maven settings.xml代码

```xml
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
 
    <localRepository>/Users/wanghanchao/.m2/repository</localRepository>
    <mirrors>
         <mirror>
            <id>aliyun</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
       </mirror>
    </mirrors>

```



------

mac下安装jenkins用到的目录罗列:

- 可执行的jenkins.war包的地址 : /usr/local/Cellar/jenkins-lts/2.249.1/libexec/jenkins.war    

- jenkins.war包启动减压后的路径 /Users/wanghanchao/.jenkins/war

- 构建完成是的jar包地址  /Users/wanghanchao/.jenkins/workspace

- 实际可启动的项目jar包路径: /Users/wanghanchao/work/tech/jenkinsware

- 默认的default.json 修改为国内的可加快插件下载速度(非必须,且当手动安装插件时可配置): /Users/wanghanchao/.jenkins/updates/default.json  替换该文件内容的代码:

  ```shell
  sed -i 's/http:\/\/updates.jenkins-ci.org\/download/https:\/\/mirrors.tuna.tsinghua.edu.cn\/jenkins/g' default.json && sed -i 's/http:\/\/www.google.com/https:\/\/www.baidu.com/g' default.json
  如果依然报错执行下面命令
  sed -i 's/https:\/\/updates.jenkins.io\/download/https:\/\/mirrors.tuna.tsinghua.edu.cn\/jenkins/g' default.json && sed -i 's/http:\/\/www.google.com/https:\/\/www.baidu.com/g' default.json
  ```

- 国内插件镜像配置 /Users/wanghanchao/.jenkins/hudson.model.UpdateCenter.xml



------

安装需要注意的问题:

**版本低就升级版本,jdk低就升级jdk,按照人家报的错来**

1.需要将jenkins->系统管理->插件管理->Advanced settings->升级站点修改为(国内地址) : http://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
插件会更新的快
2.由于项目使用jdk11编译,所以在执行:
mvn clean install -pl example-business -am -amd  -Dmaven.test.skip=true 的时候必须确保 java -version 也是jdk11,否则就会报一个错误:
也就是maven所依赖的jdk版本和工程内pom设置的jdk版本不一致
<img width="801" alt="59D3BDAE-A826-44A7-83FF-837C928EEB52" src="https://user-images.githubusercontent.com/35331347/190600885-3aff19c8-985e-45cb-aa67-277dfcb3d031.png">
3.部署的shell脚本可能会存在无权限执行的情况,需要将可执行文件的读写权限放开: sudo chmod a+rwx myfile    myfile所有人可读可写可操作

4.国内版快速下载的地址:

[**https://mirrors.huaweicloud.com/jenkins/war/2.368/**](https://mirrors.huaweicloud.com/jenkins/war/2.368/)



5.启动jenkins.war和 启动部署项目通用的脚本文件:

```shell
#!/bin/bash
#这里可替换为你自己的jdk路径
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.8.jdk/Contents/Home
export JRE_HOME=/$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
#这里可替换为你自己的执行程序，后面的代码无需更改
APP_NAME=/usr/local/Cellar/jenkins-lts/2.249.1/libexec/jenkins.war


#使用说明，用来提示输入参数
usage(){
    echo "Usage: sh shell-start-server.sh [start|stop|restart|status]"
    echo `java -version`
    exit 1
}


#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
  #如果不存在返回1，存在返回0     
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}


#启动方法
start(){
  is_exist
  if [ $? -eq 0 ]; then
    echo "${APP_NAME} is already running. pid=${pid}"
  else
    nohup java -jar ${APP_NAME} --httpPort=9090 > server.log 2>&1 &
  fi
}


#停止方法
stop(){
  is_exist
  if [ $? -eq "0" ]; then
    kill -9 $pid
  else
    echo "${APP_NAME} is not running"
  fi  
}


#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}


#重启
restart(){
  stop
  sleep 5
  start
}


#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
```


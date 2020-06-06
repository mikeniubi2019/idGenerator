## 统一id生成服务

项目架构图如下：
![](https://github.com/mikeniubi2019/idGenerator/blob/master/img/id%E7%94%9F%E6%88%90%E5%99%A8%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)

#### 运行环境：
jdk1.8 
其他环境未测试

#### 项目说明：
###### 需求分析：
传统的关系型数据库随着业务的发展数据量越来越大，而分库分表能一定程度缓解这个单库的瓶颈。但是分库分表之后，主键id如何生成是一个问题。
而本项目建立的初衷是为了解决这个问题。
###### 项目特点：
1，主键id仿造snowflake算法，如下格式： 最高位无效位 - 41位时间戳 - 2位线程编号 - 20位叠加数
2,为防止数据倾斜，在启动服务时配置关闭isfast 属性，可以开启放数据倾斜保护功能
2，通信层使用netty，自定义报文协议，能提供稳定/高性能的服务
3，为了防止netty的channel线程堵塞在获取服务上，使用disruptor高性能内存队列解耦合和提高并发
#### 使用方法：
1，服务端：
```
IdGeneratorServiceBuilder idGeneratorServiceBuilder = new IdGeneratorServiceBuilder();
idGeneratorServiceBuilder.build();
```
调用如上方法即可开启服务，其中参数可配置：
```
idGeneratorServiceBuilder.option(IdGeneratorServiceBuilder.PORT,"你的端口号")
                         .option(IdGeneratorServiceBuilder.ADDRESS,"服务器地址")
                         .option(IdGeneratorServiceBuilder.IS_FAST,ture or false)
```
2，客户端：
```
IdGeneratorClient idGeneratorClient = new IdGeneratorClient();
            idGeneratorClient.option(IdGeneratorClient.ADDRESS,"服务器地址")
                             .option(IdGeneratorClient.PORT,服务器端口);
            idGeneratorClient.build();
```
使用以上代码即可生成一个客户端
```
long[] ids = idGeneratorClient.getIds(length);
```
调用客户端getIds（参数为需要批量获取几个id）方法，即可返回一个 long类型的数组
*注意：如果超时3秒钟未获取到ID列表则此方法会抛出异常。*

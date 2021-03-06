# RabbitMQ

## 目录

- [RabbitMQ概况](#RabbitMQ概况)
  - [优点](#优点)
  - [高性能原因](#高性能原因)
  - [AMQP协议](#AMQP协议)
  - [AMQP核心概念](#AMQP核心概念)
  - [RabbitMQ整体架构](#RabbitMQ整体架构)
  - [RabbitMQ消息流转图](#RabbitMQ消息流转图)
- [安装](#安装)
- [命令行与管控台-基础操作](#命令行与管控台-基础操作)
- [RabbitMQ核心概念](#RabbitMQ核心概念)
  - [Exchange](#Exchange)
  - [Binding](#Binding)
  - [Queue](#Queue)
  - [Message](#Message)
  - [Virtual host](#VirtualHost)
- [消息如何保证100%的投递成功](#消息如何保证100%的投递成功)
  - [什么是生产端的可靠性投递？](#什么是生产端的可靠性投递？)
  - [BAT/TMD互联网大厂的解决方案](#BAT/TMD互联网大厂的解决方案)
  - [消费端-幂等性保证](#消费端-幂等性保证)
  - [Confirm确认消息](#Confirm确认消息)
  - [Return消息机制](#Return消息机制)
  - [消费端自定义监听](#消费端自定义监听)
  - [消费端限流](#消费端限流)
  - [消费端ACK与重回队列](#消费端ACK与重回队列)
  - [TTL队列/消息](#TTL队列/消息)
  - [死信队列](#死信队列)
- [RabbitMQ整合SpringAMQP](#RabbitMQ整合SpringAMQP)
  - [RabbitAdmin](#RabbitAdmin)
  - [SpringAMQP声明](#SpringAMQP声明)
  - [消息模板-RabbitTemplate](#消息模板-RabbitTemplate)
  - [简单消息监听容器-SimpleMessageListenerContainer](#简单消息监听容器-SimpleMessageListenerContainer)
  - [消息监听适配器-MessageListenerAdapter](#消息监听适配器-MessageListenerAdapter)
  - [消息转换器-MessageConverter](#消息转换器-MessageConverter)
- [RabbitMQ整合SpringBoot整合配置](#RabbitMQ整合SpringBoot整合配置)
  - [生产端核心配置](#生产端核心配置)
  - [publisher-confirms](#publisher-confirms)
  - [publisher-returns](#publisher-returns)
  - [消费端核心配置](#消费端核心配置)
  - [@RabbitListener](#@RabbitListener)
- [RabbitMQ整合SpringCloudStream整合](#RabbitMQ整合SpringCloudStream整合)

### RabbitMQ概况

- RabbitMQ是一个开源的消息代理和队列服务器，用来通过普通协议在完全不同的应用之间共享数据。RabbitMQ是使用Erlang语言来编写的，并且RabbitMQ是基于AMQP协议的。

- 哪些公司在使用？滴滴、美团、头条、携程

#### 优点

1. 开源、性能优秀，稳定性保障
2. 提供可靠性消息投递模式（confirm）、返回模式（return）
3. 与SpringAMQP完美的整合，API丰富
4. 集群模式丰富，表达式匹配，HA模式，镜像队列模型
5. 保证数据不丢失的前提做到高可靠性、可用性

#### 高性能原因
- Erlang语言最初在于交换机领域的架构模式，这样使得RabbitMQ在Broker之间进行数据交互的性能是非常优秀的。Erlang的优点：Erlang有着和原生Socket一样的延迟。

#### AMQP协议
- 具有现代特征的二进制协议。是一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。

- AMQP协议模型

  ![AMQP](https://github.com/chenyaowu/rabbitmq/blob/master/image/AMQP.jpg)

#### AMQP核心概念

  - Server： 又称Broker，接收客户端的连接，实现AMQP实体服务
  - Connection：连接，应用程序与Broker的网络连接
  - Channel：网络信道，几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道。客户端可建立多个Channel，每个Channel代表一个会话任务。
  - Message：消息，服务器和应用程序之间传送的数据，有Properties和Body组成。Properties可以对消息进行修饰，比如消息的优先级、延迟等高级特性；Body则就是消息体的内容
  - Virtual host：虚拟主机，用于进行逻辑隔离，最上层的消息路由。一个Virtual Host里面可以有若干个Exchange和Queue，同一个Virtual Host里面不能有相同名称的Exchange或Queue
  - Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列。
  - Binding：Exchange和Queue之间的虚拟连接，binding中可以包含routing key
  - Routing Key：一个路由规则，虚拟机可用它来确定如何路由一个特定消息。
  - Queue：也称为Message Queue，消息队列，保存消息并将它们转发给消费者。

#### RabbitMQ整体架构

  ![RabbitMQ](https://github.com/chenyaowu/rabbitmq/blob/master/image/RabbitMQ.jpg)

#### RabbitMQ消息流转图

  ![RabbitMQ](https://github.com/chenyaowu/rabbitmq/blob/master/image/RabbitMQ2.jpg)

### 安装

```ba
下载
wget www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm
wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
wget www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm
安装
rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm
rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm
配置
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app

{loopback_users, [<<"guest">>]}, 
{loopback_users, [guest]},

启动
rabbitmq-server start &
停止
rabbitmqctl app_stop
管理插件
rabbitmq-plugins enable rabbitmq_management
访问地址
ip:15672
账号密码：guest - guest
```



### 命令行与管控台-基础操作

- rabbitmqctl stop_app 关闭应用
- rabbitmqctl start_app 启动应用
- rabbitmqctl status 节点状态
- rabbitmqctl add_user username password 添加用户
- rabbitmqctl list_users 列出所有用户
- rabbitmqctl delete_user username 删除用户
- rabbitmqctl clear_permissions -p vhostpath username 清除用户权限
- rabbitmqctl list_user_permissions username 列出用户权限
- rabbitmqctl change_password username newpassword 修改密码
- rabbitmqctl set_permissions -p vhostpath username ".*" ".*" ".*" 设置用户权限
- rabbitmqctl add_vhost vhostpath 创建虚拟主机
- rabbitmqctl list_vhosts 列出所有虚拟主机
- rabbitmqctl list_permissions -p vhostpath 列出虚拟主机上所有权限
- rabbitmqctl delete_vhost vhostpath 删除虚拟主机
- rabbitmqctl list_queues 查看所有队列信息
- rabbitmqctl -p vhostpath purge_queue blue 清除队列里的消息
- rabbitmqctl reset 移除所有数据，要在rabbitmqctl stop_app之后使用
- rabbitmqctl join_cluster <clusternode> [--ram]  组成集群命令
- rabbitmqctl cluster_status 查看集群状态
- rabbitmqctl change_cluster_node_type disc | ram 修改集群节点的储存形式
- rabbitmqctl forget_cluster_node [--offline] 忘记节点（摘除节点）
- rabbitmqctl rename_cluster_node oldnode1 newnode1 [oldnode2] [newnode2 ...] 修改节点名称

### RabbitMQ核心概念


#### Exchange

- 接收消息，并根据路由键转发消息到所绑定的队列

  ![Exchange](https://github.com/chenyaowu/rabbitmq/blob/master/image/Exchange.jpg)

- 交换机属性

  - Name：交换机名称
  - Type：交换机类型（direct、topic、fanout、headers）
  - Durability：是否需要持久化
  - Auto Delete：当最后一个绑定到Exchange上的队列删除后，自动删除该Exchange
  - Internal：当前Exchange是否用于RabbitMQ内部使用，默认为False
  - Arguments：扩展参数，用于扩展AMQP协议自制定化使用

- Direct Exchange

  - 所有发送到Direct Exchange的消息被转发到RouteKey中指定的Queue

  - Warming：Direct模式可以使用RabbitMQ自带的Exchange:default Exchange，所以不需要将Exchange进行任何绑定操作，消息传递时，RouteKey必须完全匹配才会被队列接收，否则消息会被抛弃。

    ![Direct Exchange](https://github.com/chenyaowu/rabbitmq/blob/master/image/Direct_Exchange.jpg)

- Topic Exchange

  - 所有发送到Topic Exchange的消息被转发到所有关心RouteKey中指定Topic的Queue中

  - Exchange将RouteKey和某Topic进行模糊匹配，此时队列需要绑定一个Topic

  - Warming：可以使用通配符进行模糊匹配。（“#”匹配一个或多个词 "*"匹配一个词）

    ![Topic Exchange](https://github.com/chenyaowu/rabbitmq/blob/master/image/Topic_Exchange.jpg)
  
- Fanout Exchange

  - 不处理路由键，只需要简单的讲队列绑定到交换机上

  - 发送到交换机的消息都会被转发到与该交换机绑定的所有队列上

  - Fanout交换机转发消息是最快的

    ![Fanout Exchange](https://github.com/chenyaowu/rabbitmq/blob/master/image/Fanout_Exchange.jpg)
  
#### Binding

- Exchange和Exchange、Queue之间的连接关系
- Binding中可以包含RoutingKey或者参数

#### Queue
- 消息队列，实际存储消息数据
- Durability，是否持久化
- Auto delete，代表当最后一个监听被移除后，该Queue会自动被删除

#### Message

- 服务器和应用程序之间传递的数据

- 本质上就是一段数据，由Properties和Payload（body）组成

- 常用属性：delivery mode 、headers

- 其他属性：content_type、content_encoding、priority、correlation_id、reply_to、expiration、message_id、timestamp、type、user_id、app_id、cluster_id

#### VirtualHost

- 虚拟主机，用于进行逻辑隔离，最上层的消息路由

- 一个virtual host里面可以有若干个Exchange和Queue

- 同一个Virtual Host里面不能有相同名称的Exchange和Queue

  

### 消息如何保证100%的投递成功

#### 什么是生产端的可靠性投递？

- 保障消息的成功发出
- 保障MQ节点的成功接收
- 发送端收到MQ节点(Broker)确认应答
- 完善的消息进行补偿机制

#### BAT/TMD互联网大厂的解决方案

- 消息落库，对消息状态进行打标

  ![Message_InDB](https://github.com/chenyaowu/rabbitmq/blob/master/image/Message_InDB.jpg)

- 消息的延迟投递，做二次确认，回调检查

  ![Message2](https://github.com/chenyaowu/rabbitmq/blob/master/image/Message2.jpg)

#### 消费端-幂等性保证

- 唯一ID + 指纹码机制
  - 唯一ID + 指纹码机制，利用数据库主键去重
  - SELECT COUNT(1) FROM T_ORDER WHERE ID = 唯一ID + 指纹码
  - 好处： 实现简单
  - 坏处： 高并发下有数据库写入的性能瓶颈
  - 解决方案：跟进ID进行分库分表进行算法路由
- 利用Redis原子性特性实现
  - 使用Redis进行幂等，需要考虑的问题：
    1. 我们是否要进行数据落库，如果落库的话，关键解决的问题是数据库和缓存如何做到原子性？
    2. 如果不进行落库，那么都存储到缓存中，如何设置定时同步的策略？

#### Confirm确认消息

- 消息的确认，是指生产者投递消息后，如果Broker收到消息，则会给我们生产者一个应答。

- 生产者进行接收应答，用来确认这条消息是否正常的发送到Broker，这种方式也是消息的可靠性投递的核心保障。

  ![Confirm](https://github.com/chenyaowu/rabbitmq/blob/master/image/Confirm.jpg)

- 实现Confirm确认消息

  1. 在channel上开启确认模式channel.confirmSelect()
  2. 在channel上添加监听：addConfirmListener，监听成功和失败的返回结果，根据具体的消息对消息进行重新发送、或记录日志等后续处理。

#### Return消息机制

- Return Listener用于处理一些不可路由的消息
- 我们的消息生产者，通过制定一个Exchange和Routingkey，把消息送达到某一个队列中去，然后我们的消费者监听队列，进行消费处理操作。
- 但是在某些情况下，如果我们在发送消息的时候，当前的exchange不存在或者指定的路由key找不到队列，这个时候如果我们需要监听这种不可达的消息，就要使用Return Listerner。
- 在基础API中有一个关键的配置项：Mandatory,如果为true，则监听器会接收到路由不可达的消息，然后进行后续处理，如果为false，那么broker端自动删除该消息。

![Return](https://github.com/chenyaowu/rabbitmq/blob/master/image/Return.jpg)

#### 消费端自定义监听

- 我们一般就是在代码中编写while循环，进行consumer.nextDelivery方法进行获取下一条消息，然后进行消费处理。
- 但是我们使用自定义的Consumer更加方便，解耦性更加的强，也是在实际工作中最常用的使用方式。

#### 消费端限流

- 当RabbitMQ服务器有上万条未处理的消息，我们随便打开一个客户端，巨量的消息瞬间全部推送过来，单个客户端无法同时处理这么多数据。

- RabbitMQ提供了一种qos(服务质量保证)功能，即在非自动确认消息的前提下，如果一定数量的消息（通过基于consume或者channel设置Qos的值）未被确认前，不进行消费新的消息。

  ```c
  /**
  * prefetchSize 消费大小限制（0，不限制）
  * prefetchCount 会告诉RabbitMQ不要同时给一个消费者推送多余N个消息，即一旦有N个消息没有ack，则该	     consume将block掉，知道有消息ack
  * global 是否将上面设置应用与channel。false:限制在consumer级别
  **/
  void BasicQos(uint prefetchSize, ushort prefetchCount, bool global);
  
  ```
  
- prefetchSize和global这两项，rabbitmq没有实现，展示不研究prefetch_count在no_ack = false的情况下生效，即在自动应答的情况下，这两个值是不生效的。

#### 消费端ACK与重回队列

- 消费端的手工ACK和NACK
  - 消费端进行消费的时候，如果由于业务异常我们可以进行日志的记录，然后进行补偿
  - 如果由于服务器宕机等严重问题，那我们就需要手工进行ACK保障消费端消费成功。
  
- 消费端的重回队列
  - 消费端重回队列是为了对没有处理成功的消息，把消息重新会递给Broker!
  - 一般我们在实际应用中，都会关闭重回队列，也就是设置为Fasle

  

#### TTL队列/消息

- TTL是Time To Live的缩写，也就是生存时间。
- RabbitMQ支持消息的过期时间，在消息发送时可以进行指定
- RabbitMQ支持队列的过期时间，从消息入队列开始计算，只要超过了队列的超时时间配置，那么消息会自动的清除。

#### 死信队列

- 利用DLX,当消息在一个队列中变成死信队列之后，它能被重新publish到另一个Exchange，这个Exchange就是DLX
- 消息变成死信有以下情况
  1. 消息被拒绝，并且requeue = false;
  2. 消息TTL过期
  3. 队列达到最大长度
- DLX也是一个正常的Exchange，和一般的Exchange没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性。
- 当这个队列中有死信时，RabbitMQ就会自动的将这个消息重新发布到设置的Exchange上去，进而被路由到另一个队列。
- 可以监听这个队列中消息做相应的处理，这个特性可以弥补RabbitMQ3.0以前支持的immediate参数的功能。
- 死信队列设置：
  1. 设置死信队列的exchange和queue，然后进行绑定
  2. 进行正常声明交换机、队列、绑定，在队列上加上一个参数：arguments.put("x-dead-letter-exchange", "dlx.exchange");
  3. 这样消息在过期、requeue、队列在达到最大长度时，消息就可以直接路由到死信队列。

## RabbitMQ整合SpringAMQP

### RabbitAdmin

- RabbitAdmin类可以很好的操作RabbitMQ，在Spring中直接进行注入即可

  ```java
  @Bean
  	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
  		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
  		rabbitAdmin.setAutoStartup(true);
  		return rabbitAdmin;
  	}
  ```

- 注意autoStartup必须设置为true，否则Spring容器不会加载RabbitAdmin类

- RabbitAdmin类底层实现就是从Spring容器中获取Exchange、Binding、RoutingKey以及Queue的@Bean声明

- 然后使用RabbitTemplate的execute方法执行对应的声明、修改、删除等一系列RabbitMQ基础功能操作。

### SpringAMQP声明

- 在Rabbit基础API里面声明一个Exchange、声明一个绑定、一个队列

  ```ja
  channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);
  channel.queueDeclare(queueName, true, false, false, null);
  channel.queueBind(queueName, exchangeName, routingKey);
  ```

- 使用SpringAMQP去声明，就需要使用SpringAMQP的如下模式，即声明@Bean方式

  ```java
  @Bean
  public TopicExchange exchange001() {
      return new TopicExchange("topic001", true, false);
  }
  
  @Bean
  public Queue queue001() {
      return new Queue("queue001", true); //队列持久
  }
  
  @Bean
  public Binding binding001() {
      return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
  }
  ```

### 消息模板-RabbitTemplate

- 我们在于SringAMQP整合的时候进行发送消息的关键类
- 该类提供了丰富的发送消息的方式，包括可靠性投递消息方法、回调监听消息接口ConfirmCallback、返回值确认接口ReturnCallback等等。同样我们需要进行注入到Spring容器中，然后直接使用。
- 在与Spring整合时需要实例化，但是在与SpringBoot整合时，在配置文件里添加配置即可。

### 简单消息监听容器-SimpleMessageListenerContainer

- 这个类非常的强大，我们可以对他进行很多设置，对于消费者的配置项，这个类都可以满足
- 监听队列（多个队列）、自动启动、自动声明功能
- 设置事务特性、事务管理器、事务属性、事务容器（并发）、是否开启事务、回滚消息等
- 设置消费者数量、最小最大数量、批量消费
- 设置消息确认和自动确认模式、是否重回队列、异常捕获handler函数
- 设置消费者标签生成策略、是否独占模式、消费者属性等
- 设置具体的监听器、消息转换器等等
- 注意：可以进行动态设置，比如在运行中的应用可以动态的修改其消费者数量的大小、接收消息的模式等
- 很多基于RabbitMQ的自制化后端监控台在进行动态设置的时候，也是根据这一特性去实现的。

### 消息监听适配器-MessageListenerAdapter

- defaultListenerMethod默认监听方法名称：用于设置监听方法名称
- Delegate委托对象：实际真实的委托对象，用于处理消息
- queueOrTagToMethodName队列标识与方法名称组成的集合可以一一进行队列与方法名称的匹配
- 队列和方法名称绑定，即指定队列里的消息会被绑定的方法所接受处理

### 消息转换器-MessageConverter

- 我们在进行发送消息的时候，正常情况下消息体为二进制的数据方式进行传输，如果希望内部帮我们进行转换，或者指定自定义的转换器，就需要用到MessageConverter
- 自定义常用转换器：MessageConverter，一般来讲都需要实现这个接口，并重写toMessage方法（java对象转换成message）和fromMessage：Message对象转换成java对象
- Json转换器： Jackson2JsonMessageConverter，可以进行java对象的转换功能
- DefaultJackson2JavaTypeMapper映射器：可以进行java对象的映射关系
- 自定义二进制转换器：图片类型、PDF、PPT、流媒体

## RabbitMQ整合SpringBoot整合配置

### 生产端核心配置

```properties
spring.rabbitmq.publisher-confirms=true
spring.rabbitmq.publisher-returns=true
# 在发送消息的时候对template进行配置mandatory=true保证监听有效
spring.rabbitmq.template.mandatory=true 
#生产端还可以配置其他属性，比如发送重试，超时时间，次数，间隔等
```

### publisher-confirms

- 实现一个监听器用于监听Broker端给我们返回的确认请求：RabbitTemplate.ConfirmCallBack

### publisher-returns

- 保证消息对Broker端是可达的，如果出现路由键不可达的情况，则使用监听器对不可达的消息进行后续的处理，保证消息的路由成功：RabbitTemplate.ReturnCallback

### 消费端核心配置

```properties
#首先配置手工确认模式，用于ACK的手工处理，这样我们可以保证消息的可靠性送达，或者在消费端消费失败的时候可以做到重回队列，根据业务记录日志等处理
spring.cloud.stream.rabbit.bindings.input_channel.consumer.acknowledge-mode=MANUAL
#可以设置消费端的监听个数和最大个数，用于控制消费端的并发情况
spring.cloud.stream.bindings.input_channel.consumer.concurrency=1
spring.cloud.stream.rabbit.bindings.input_channel.consumer.max-concurrency=5
```

### @RabbitListener

- 消费端监听@RabbitListener注解，这个对于在实际工作中非常的好用

- @RabbitListener是一个组合注解，里面可以注解配置@QueueBingding、@Queue、@Exchange直接通过这个组合注解一次性搞定消费端交换机、队列、绑定、路由、并且配置监听功能等

- example

  ```java
   @RabbitListener(
          bindings = @QueueBinding(
              value = @Queue(value = "queue-1", durable = "true"),
              exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
              key = "springboot.*"
           )
      )
      @RabbitHandler
      public void onMessage(Message message, Channel channel) throws Exception {
          System.err.println("--------------------------------------");
          System.err.println("消费端Payload: " + message.getPayload());
          Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
          //手工ACK
          channel.basicAck(deliveryTag, false);
      }
  ```



## RabbitMQ整合SpringCloudStream整合

- SpringCloudStream整合架构核心图

  ![RabbitSpringCloud](https://github.com/chenyaowu/rabbitmq/blob/master/image/RabbitSpringCloud.jpg)

- Barista接口：是定义来作为后面类的参数，这一接口定义通道类型和名称，通道名称是作为配置用，通道类型则决定了app会使用这一通过进行发送消息还是从中接收消息。

- @Output：输出注解，用于定义发送消息接口

- @Input：输入注解，用于定义消息的消费者接口

- @StreamListener：用于定义监听方法的注解

- 只需要使用好上面3个注解，就可以使用好SpringCloudStream。在实现高性能消息的生产和消费的场景非常适合，但是使用SpringCloudStream框架有一个非常大的问题就是不能实现可靠性投递，也就是没办法保证消息的100%可靠性，会存在少量消息丢失问题。

  




# RabbitMQ

- RabbitMQ是一个开源的消息代理和队列服务器，用来通过普通协议在完全不同的应用之间共享数据。RabbitMQ是使用Erlang语言来编写的，并且RabbitMQ是基于AMQP协议的。

- 哪些公司在使用？滴滴、美团、头条、携程

- 优点

  1. 开源、性能优秀，稳定性保障
  2. 提供可靠性消息投递模式（confirm）、返回模式（return）
  3. 与SpringAMQP完美的整合，API丰富
  4. 集群模式丰富，表达式匹配，HA模式，镜像队列模型
  5. 保证数据不丢失的前提做到高可靠性、可用性

- 高性能原因：Erlang语言最初在于交换机领域的架构模式，这样使得RabbitMQ在Broker之间进行数据交互的性能是非常优秀的。Erlang的优点：Erlang有着和原生Socket一样的延迟。

- AMQP协议：具有现代特征的二进制协议。是一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。

- AMQP协议模型

  ![AMQP](https://github.com/chenyaowu/rabbitmq/blob/master/image/AMQP.jpg)

- AMQP核心概念

  - Server： 又称Broker，接收客户端的连接，实现AMQP实体服务
  - Connection：连接，应用程序与Broker的网络连接
  - Channel：网络信道，几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道。客户端可建立多个Channel，每个Channel代表一个会话任务。
  - Message：消息，服务器和应用程序之间传送的数据，有Properties和Body组成。Properties可以对消息进行修饰，比如消息的优先级、延迟等高级特性；Body则就是消息体的内容
  - Virtual host：虚拟主机，用于进行逻辑隔离，最上层的消息路由。一个Virtual Host里面可以有若干个Exchange和Queue，同一个Virtual Host里面不能有相同名称的Exchange或Queue
  - Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列。
  - Binding：Exchange和Queue之间的虚拟连接，binding中可以包含routing key
  - Routing Key：一个路由规则，虚拟机可用它来确定如何路由一个特定消息。
  - Queue：也称为Message Queue，消息队列，保存消息并将它们转发给消费者。

- RabbitMQ整体架构

  ![RabbitMQ](https://github.com/chenyaowu/rabbitmq/blob/master/image/RabbitMQ.jpg)

- RabbitMQ消息流转图

  ![RabbitMQ](https://github.com/chenyaowu/rabbitmq/blob/master/image/RabbitMQ2.jpg)

## 安装

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



## 命令行与管控台-基础操作

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


## Exchange

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
  
  
  
  
  
  
  
  
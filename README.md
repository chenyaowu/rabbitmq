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
# 项目简介
Spring AMQP学习。

# 技术选型
Spring Boot、Gradle、MySQL、Junit 5、Rest Assured、Docker

|功能|命令|备注|
| --- | --- | --- |
|生成IntelliJ工程|`./idea.sh`|自动打开IntelliJ|
|本地运行|`./run.sh`|自动启动MySQL，监听5005调试端口|
|本地构建|`./local-build.sh`|启动启动MySQL，运行所有类型的自动化测试|
|停止MySQL/RabbitMQ|`./gradlew composeDown`|将清空所有数据|
|手动启动MySQL/RabbitMQ|`./gradlew composeUp`|RabbitMQ访问：[http://localhost:15673/](http://localhost:15673/)|

# 领域对象
|领域对象|中文名|业务功能|
| --- | --- | --- |
|Order|订单|表示用于下的订单，包含多个产品及其数量|

# 测试策略
|测试类型|代码目录|测试内容|
| --- | --- | --- |
|单元测试|`src/test/java`|包含核心领域模型（包含领域对象和Factory类）的测试|
|组件测试|`src/componentTest/java`|用于测试一些核心的组件级对象，比如Repository|
|API测试|`src/apiTest/java`|模拟客户端调用API|
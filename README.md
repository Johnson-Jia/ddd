## 项目结构
```
./ddd-bootstrap  // 启动模块
    └── pom.xml
./ddd-bff  // Backend For Frontend,controller 层 
    └── pom.xml
./ddd-domain //领域模块
├── ddd-domain-north //北向网关 领域服务入口，对外提供rpc服务和应用接口 ohs开放主机服务
│   └── pom.xml
├── ddd-domain-core //领域服务层、应用层、核心业务逻辑
│   └── pom.xml
├── ddd-domain-model //聚合（聚合根）、实体、键值对
│   └── pom.xml
├── ddd-domain-south //南向网关 领域服务出口，仓储层接口定义 acl防腐层
│   └── pom.xml
└── pom.xml
./ddd-infrastructure //基础设施模块 基础服务、资源实现（数据库持久化实现）、异步消息服务
    └── pom.xml
./ddd-common //通用工具包
    └── pom.xml
./pom.xml
```

##概念参考文章
### 重要阅读 
  [DDD概念、设计详情](https://blog.csdn.net/m0_37583655/article/details/117565641)
  
  [公众号【梦语路】DDD板块，大佬持续更新](https://mp.weixin.qq.com/s/1P_9HT5lA4-rSSMLIANu2g)
  
### 参考阅读
  [DDD项目设计](https://blog.csdn.net/wenwang3000/article/details/127159380)
  
  [公众号【楼仔】](https://mp.weixin.qq.com/s/jU0awhez7QzN_nKrm4BNwg)

##反思
项目有点过度DDD了，只能借鉴，真实使用场景中不能过度的拘泥于形式。 
比如：实体、值对象、领域服务等等

####核心目的，做到即可：
    单一职责、高内聚、低耦合、隔离防腐，提升代码的整洁度、可读性、可重用性、高扩展性、业务逻辑不依赖于具体的技术实现，比如仓储层可以随便更换，只需实现南向网关的接口即可。

所以如果能达到目的，用不用DDD，用到什么程度都无所谓！

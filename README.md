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

# DDD Demo（领域驱动设计演示项目）

> Simple Demo For DDD / 这是一个 DDD 结构的演示项目。
>
> 以「用户中心」(注册 / 登录 / 角色菜单) 为业务载体，演示 DDD 战略分层（north/south 双向网关）、战术构建块（聚合根 / 实体 / 值对象 / 领域服务 / 领域事件 / 仓储）、充血模型与工厂 + 策略模式的落地方式。

> ⚠️ **定位说明**：本项目偏重架构演示，存在一定的「过度设计」与若干 `TODO` 占位逻辑，**适合借鉴思路，不建议直接用于生产**。

---

## 一、技术栈

| 分类 | 选型 | 版本 |
| --- | --- | --- |
| 基础框架 | Spring Boot | 3.5.14 |
| 微服务 | Spring Cloud / Spring Cloud Alibaba | 2025.0.2 / 2025.0.0.0 |
| RPC | Apache Dubbo | 3.3.5 |
| 注册 / 配置中心 | Nacos | （随 SCA 版本） |
| 持久层 | MyBatis-Plus | 3.5.12 |
| 数据库 / 连接池 | MySQL 8 + Druid | — |
| 对象映射 | MapStruct | 1.6.3 |
| 序列化 | Jackson（jackson-bom 统一管理） | 2.18.1 |
| 缓存 | Spring Cache + Redis | — |
| 其它 | Lombok / commons-collections4 / yauaa(UA 解析) | — |
| JDK | Java | 21 |

---

## 二、模块结构

```
ddd
├── ddd-bootstrap        # 启动模块：MainApplication、配置、建表 SQL
├── ddd-bff              # Backend For Frontend：Controller / Command / VO（展示层）
├── ddd-domain           # 领域模块（聚合工程）
│   ├── ddd-domain-north # 北向网关：应用服务接口（OHS 开放主机服务，对外 RPC/HTTP 契约）
│   ├── ddd-domain-core  # 领域服务层 + 应用层实现（核心业务逻辑）
│   ├── ddd-domain-model # 聚合根 / 实体 / 值对象 / 领域事件 / 枚举（纯领域模型）
│   └── ddd-domain-south # 南向网关：仓储接口、领域事件发布接口（ACL 防腐层）
├── ddd-infrastructure   # 基础设施：仓储实现 / PO / Mapper / Converter / 事件发布实现 / RPC 暴露适配
└── ddd-common           # 通用工具：DDD 标记接口、事件基类、异常体系、Result 封装
```

### 模块依赖关系图

> DDD 的核心是**依赖方向**：外层依赖内层，领域内核不依赖任何技术实现。

```
                          ┌─────────────────────────────────────────────┐
                          │              ddd-bootstrap (启动)            │
                          └───┬───────────────┬─────────────────────────┘
                              │               │
                  ┌───────────▼────┐   ┌──────▼──────────┐
                  │   ddd-bff      │   │ ddd-infrastructure│
                  │  (展示/Controller)│  │ (仓储实现/PO/Mapper)│
                  └───────┬────────┘   └──────────┬────────┘
                          │                       │ implements（依赖倒置）
                          │ dep                   │
                  ┌───────▼────────┐   ┌──────────▼─────────┐
                  │ ddd-domain-north│   │ ddd-domain-south   │
                  │ (应用服务接口)   │   │ (仓储/事件 接口)     │
                  └───────┬────────┘   └──────────┬─────────┘
                          │                       │
                          └────────┐   ┌──────────┘
                              ┌────▼───▼─────────────┐
                              │   ddd-domain-core    │  (应用服务实现 + 领域服务)
                              │   依赖 north/south/model │
                              └───────────┬──────────┘
                                          │
                                  ┌───────▼─────────┐
                                  │ ddd-domain-model │  (聚合根/实体/值对象)
                                  └───────┬─────────┘
                                          │
                                  ┌───────▼─────────┐
                                  │   ddd-common     │  (标记接口/事件基类/工具)
                                  └─────────────────┘
```

**调用链路**（以登录为例）：

```
HTTP 请求
  → ddd-bff        UserController
  → ddd-domain-north  UserApplicationService（接口）
  → ddd-domain-core   UserApplicationServiceImpl（编排 + 领域规则）
  → ddd-domain-south  LoginRepository（接口）
  → ddd-infrastructure LoginRepositoryImpl（实现）→ MyBatis-Plus → MySQL
```

对外 **Dubbo RPC 暴露**（供其他服务调用，由 infrastructure 适配层承担）：

```
外部服务 ─Dubbo─▶ ddd-infrastructure  UserRpcServiceProvider（@DubboService）
                      └─委托─▶ ddd-domain-core  UserApplicationServiceImpl（@Primary）
```

---

## 三、核心设计要点

- **双向网关（战略设计）**
  - `north`（OHS 开放主机服务）：对外暴露应用服务接口，可作为 Dubbo / HTTP 契约。
  - `south`（ACL 防腐层）：定义仓储、领域事件发布等**出站接口**，由 `infrastructure` 实现，依赖倒置，领域层可无感替换底层技术。
- **领域内核零技术依赖**：`ddd-domain-model` 为纯 POJO；`ddd-domain-core` 不依赖 Dubbo / Spring 事件。RPC 暴露（`UserRpcServiceProvider`）与事件订阅都收敛到 `ddd-infrastructure` 适配层，业务逻辑与技术实现彻底解耦。

- **充血模型**：行为下沉到实体，创建走静态工厂保证不变量。
  - `Login`：`register()` / `reconstitute()` 静态工厂、`createSecret()` 生成会话密钥、`checkPassword()` 校验。
  - `Menus`：`isMenus()/isButton()/enableMenus()/disableMenus()` 等菜单状态与类型判断。

- **值对象类型安全**：`UserId / RoleId / Phone / Address` 等以值对象承载（`sameValueAs` 比较），避免裸 `Long/String` 满天飞。

- **领域事件解耦**：`BaseDomainEvent` + `DomainEventPublisher`（接口在 south、实现在 infra），注册 / 登录成功后异步发布事件，监听器侧完成副作用（当前为日志）。

- **工厂 + 策略模式**：`UserAuthFactory` 依据 `AuthTypeEnum` 动态选择授权实现（`DefaultAuthImpl / WechatAuthImpl / DingDingAuthImpl`），新增渠道只需新增一个 `UserAuthService` 实现。

- **统一的异常与返回**：`BaseException` 体系 + `GlobalExceptionHandler` + `Result<T>` 封装，错误码 / 提示 / traceId 一致。

---

## 四、快速启动

### 前置环境

- JDK 21
- Maven 3.6.3+
- MySQL 8.x
- Nacos（配置中心 + 注册中心，默认 `bootstrap.properties` 指向内网地址，本地需自行部署并修改）

### 步骤

1. **初始化数据库**

   ```sql
   CREATE DATABASE ddd_test DEFAULT CHARACTER SET utf8mb4;
   ```

   导入建表与初始数据脚本：

   ```
   ddd-bootstrap/src/main/resources/db/ddd_test.sql
   ```

   含 4 张表：`t_uc_login`（登录信息）、`t_uc_user_info`（用户详情）、`t_uc_role`（角色）、`t_uc_role_menus`（菜单 / 功能）。

2. **修改配置**

   - `ddd-bootstrap/src/main/resources/application.properties`
     - 数据库地址 / 账号密码（默认 `172.21.0.67:3306`，`root/123456`）
   - `ddd-bootstrap/src/main/resources/bootstrap.properties`
     - Nacos 配置中心、注册中心地址（默认 `172.21.0.19:8849`），通过 `spring.config.import` 从 Nacos 拉取配置
   - `spring.config.import` 已带 `optional:` 前缀，**本地无 Nacos 时不会阻塞启动**，可直接用本地配置单机调试。

3. **构建**

   ```bash
   mvn clean install -DskipTests
   ```

4. **运行**

   ```bash
   # 方式一：直接运行启动类
   #   com.tbc.ddd.MainApplication（@EnableDubbo @EnableAsync @MapperScan）

   # 方式二：打包后运行
   java -jar ddd-bootstrap/target/ddd-bootstrap.jar
   ```

   默认端口 `8080`，context-path 为 `/ddd`（由 `server.servlet.context-path=/${spring.application.name}` 决定）。

---

## 五、接口示例

> 完整路径前缀：`http://localhost:8080/ddd`

| 接口 | 方法 | 入参（Command） | 说明 |
| --- | --- | --- | --- |
| `/loginByPhone` | POST | `LoginByPhoneCommand` | 手机号 + 密码登录 |
| `/loginByName` | POST | `LoginByNameCommand` | 登录名 + 密码登录 |
| `/userRegister` | POST | `UserRegisterCommand` | 注册（按授权类型走不同策略） |

注册示例：

```bash
curl -X POST http://localhost:8080/ddd/userRegister \
  -H 'Content-Type: application/json' \
  -d '{"phone":"18000000000","loginName":"demo","password":"123456","authType":"WECHAT","code":"xxx"}'
```

---

## 六、核心业务流程

### 注册流程

```
UserController.userRegister(UserRegisterCommand)
  └─ UserConverter → UserRegisterDTO
  └─ UserApplicationService.userRegister（编排）
       ├─ 查重：手机号 / 登录名是否已存在
       ├─ UserAuthFactory.createAuthService(authType)  # 选授权策略
       │    └─ WechatAuthImpl / DingDingAuthImpl / DefaultAuthImpl.getUserInfo(code)
       ├─ login.bindOpenId/bindUnionId、loginRepository.save → t_uc_login（密码经 Login.register 加密）
       └─ userInfoRepository.save → t_uc_user_info
       ├─ DomainEventPublisher.publishEvent(RegisterEvent)  # 异步事件
       └─ 组装 UserVO（login + role + 菜单树 + userInfo）返回
```

### 登录流程

```
loginByPhone / loginByName
  └─ loginRepository 查询 Login 聚合
  └─ login.checkPassword(password)  # 领域对象自校验
  └─ login.createSecret()           # 生成 session / secretKey
  └─ publishEvent(LoginEvent)
  └─ 组装 UserDTO（含角色 + 菜单权限树）返回
```

### 多渠道授权（工厂 + 策略）

```
AuthTypeEnum (UNKNOWN / WECHAT / DINDING)
   └─ UserAuthFactory  ──>  Map<UserAuthService>  ──>  命中 getAuthType() 的实现
```

### 菜单权限树

应用层按 `parentId` 递归构建菜单树（`Menus.setButtonCode()` 聚合下属按钮功能编码），结果组装进 `RoleVO` 返回前端用于权限控制。

---

## 七、设计取舍

> **项目有点过度 DDD 了，只能借鉴思路，真实使用场景中不必拘泥于形式。**
> 核心目的是：充血模型、单一职责、高内聚低耦合、隔离防腐，业务逻辑不依赖具体技术实现（如仓储层可随意更换，只需实现南向网关接口）。能达到目的即可，用不用 DDD、用到什么程度都无所谓。

---

## 八、参考阅读

### 重要阅读
- [DDD 概念、设计详情](https://blog.csdn.net/m0_37583655/article/details/117565641)
- [公众号【梦语路】DDD 板块，大佬持续更新](https://mp.weixin.qq.com/s/jNBtcnRJHRZ7zQa0q9IBDw)

### 参考阅读
- [DDD 项目设计](https://blog.csdn.net/wenwang3000/article/details/127159380)
- [公众号【楼仔】](https://mp.weixin.qq.com/s/jU0awhez7QzN_nKrm4BNwg)

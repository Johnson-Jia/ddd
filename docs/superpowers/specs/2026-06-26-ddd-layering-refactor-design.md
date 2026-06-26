# DDD 分层重构设计：领域内核去技术依赖 + Login 充血模型加固

- **日期**：2026-06-26
- **状态**：已批准（维护者确认）
- **范围**：P0 领域内核去技术依赖 + P1 Login 充血模型加固
- **验证基线**：`JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests` → `BUILD SUCCESS`（JDK 21 因旧 Lombok 不兼容，无法编译）

---

## 1. 背景与目标

`com.tbc.ddd` 是一个 Spring Boot 2.7.9 + MyBatis-Plus 的 DDD 演示项目，采用 north/south 双向网关分层。代码审查发现两类问题：

- **P0**：领域内核（`domain-core` / `domain-model`）泄漏了技术细节（Dubbo、Spring、MapStruct），违背「领域层不依赖技术实现」原则。
- **P1**：`Login` 充血模型的不变量保护被 `@Builder` 架空，密码处理存在脆弱写法。

**目标**：
1. 让领域内核不再依赖任何技术框架，回归纯领域。
2. 加固 `Login` 充血模型，消除 builder 绕过校验与 `setPassword` 自赋值补救。

---

## 2. 现状问题（精确到文件/行）

### 2.1 P0 领域内核技术泄漏
- `ddd-domain-core/pom.xml:40-43` 依赖 `dubbo-spring-boot-starter`；`UserApplicationServiceImpl.java:36` 标注 `@DubboService`。RPC 暴露属基础设施关注点。
- `ddd-domain-model/pom.xml:35-46` 依赖 `mapstruct` / `mapstruct-processor` / `spring-context`——经 grep 确认 **model 模块源码零使用**，纯 pom 冗余。
- `LoginEventListener.java:21-23` / `RegisterEventListener.java:21-23`（位于 domain-core）使用 Spring `@EventListener` / `@Async`。

### 2.2 P1 Login 充血模型隐患
- `Login.java:32-35` 同时使用 `@Builder` + `@Setter(PRIVATE)`；`Login.java:93-97` 手写 `public setPassword`（含 MD5 加密）。`@Builder` 构建会**绕过**加密/校验。
- `UserAssembler.java:77-84` 的 `toLogin` 经 mapstruct 走 builder，注册时 `password` 为明文。
- `UserDomainService.java:43` 用 `login.setPassword(login.getPassword())` 自赋值补救触发加密——脆弱，依赖「toLogin 未加密」隐含前提；一旦 `toLogin` 改走 setter 即双重加密、登录永久失败。
- **利好**：全项目无任何手写 `Login.builder()` 调用（grep 确认），builder 仅被两个 mapstruct 转换器隐式使用，P1 改动面收敛。

---

## 3. 设计决策（已与维护者确认）

1. `@DubboService` **下沉到 `ddd-infrastructure`**（不新建模块）。
2. 领域事件监听器**迁到 `ddd-infrastructure`**。
3. `Login` **采用静态工厂强制校验**（去掉 `@Builder`）。

---

## 4. 详细改动（3 新增 / 6 修改 / 3 删除）

### 4.1 P0-A — `@DubboService` 下沉到 infrastructure

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `ddd-domain-core/pom.xml` | 移除 `dubbo-spring-boot-starter` 依赖 |
| 修改 | `UserApplicationServiceImpl.java` | `@DubboService` → `@Service @Primary`（`org.springframework.stereotype.Service`），删除 `org.apache.dubbo...DubboService` import |
| 修改 | `ddd-infrastructure/pom.xml` | 新增 `ddd-domain-north` + `dubbo-spring-boot-starter` 依赖 |
| **新增** | `infrastructure/user/rpc/UserRpcServiceProvider.java` | `implements UserApplicationService` + `@DubboService`，注入 `UserApplicationService delegate`（=core 的 `@Primary` 实现），逐方法委托 |

**注入歧义处理**：容器内会有两个 `UserApplicationService` bean（core 实现 + provider 自身）。给 core 实现标 `@Primary`，provider 注入 `delegate` 时 Spring 选 primary 实现。bff 仍直连实现（同进程不走 RPC），RPC 仅对外暴露。

`UserRpcServiceProvider` 骨架：

```java
@DubboService
@RequiredArgsConstructor
public class UserRpcServiceProvider implements UserApplicationService {
    private final UserApplicationService delegate; // core 的 @Primary 实现

    @Override public UserDTO loginByPhone(String phone, String password) { return delegate.loginByPhone(phone, password); }
    @Override public UserDTO loginByName(String loginName, String password) { return delegate.loginByName(loginName, password); }
    @Override public UserDTO userRegister(UserRegisterDTO dto) { return delegate.userRegister(dto); }
}
```

> 说明：infrastructure **编译期只依赖 `ddd-domain-north`（接口）**，不依赖 `ddd-domain-core`；core 实现由 Spring 在 bootstrap 装配时注入，依赖方向保持干净。

### 4.2 P0-B — 领域模型去技术依赖

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `ddd-domain-model/pom.xml` | 移除 `spring-context`、`mapstruct`、`mapstruct-processor` |

### 4.3 P0-C — 事件监听器迁出 core

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 删除 | `ddd-domain-core/user/event/LoginEventListener.java` | — |
| 删除 | `ddd-domain-core/user/event/RegisterEventListener.java` | — |
| **新增** | `ddd-infrastructure/user/event/LoginEventListener.java` | 包名 `com.tbc.ddd.infrastructure.user.event`，逻辑不变 |
| **新增** | `ddd-infrastructure/user/event/RegisterEventListener.java` | 同上 |

> infrastructure 经 `ddd-domain-south` 传递依赖到 `domain-model`，可访问 `LoginEvent` / `RegisterEvent`；Spring 由 mybatis-plus 传递引入。

### 4.4 P1 — `Login` 静态工厂

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `Login.java` | 去 `@Builder`；新增私有构造 + `register(...)` / `reconstitute(...)` 静态工厂；`setOpenId/setUnionId` → `bindOpenId/bindUnionId`；**删除 `setPassword`**；保留 `checkPassword` / `createSecret` |
| 修改 | `UserAssembler.java` | `toLogin` 由 mapstruct builder 改为 `default` 方法，调用 `Login.register(...)` |
| 修改 | `LoginConverter.java` | `toLogin(LoginPO)` 由 mapstruct builder 改为 `default` 方法，调用 `Login.reconstitute(...)`（手写 `String→AuthTypeEnum`、`roleId` null 处理，复刻原 `defaultValue="-1"` 行为） |
| 修改 | `UserDomainService.java` | `setOpenId/setUnionId` → `bindOpenId/bindUnionId`；**删除自赋值 `login.setPassword(login.getPassword())`** |

`Login` 新结构要点：

```java
@Data
@Setter(AccessLevel.PRIVATE)
public class Login implements AggregateRoot {
    // 字段不变：userId, phone, loginName, password, authType, openId, unionId, roleId, secret, createTime

    private Login() {}

    /** 注册：明文密码加密后存入 */
    public static Login register(Phone phone, String loginName, String rawPassword, AuthTypeEnum authType) {
        VerificationUtil.isTrue(StringUtils.isBlank(rawPassword), new PasswordException("Password is not exist."));
        Login login = new Login();
        login.phone = phone;
        login.loginName = loginName;
        login.password = EncryptUtil.MD5(rawPassword); // 强制加密
        login.authType = authType;
        return login;
    }

    /** 重建：从持久化恢复，password 为密文，原样填入 */
    public static Login reconstitute(UserId userId, Phone phone, String loginName, String password,
            AuthTypeEnum authType, String openId, String unionId, RoleId roleId, Long createTime) { ... }

    public void bindOpenId(String openId) { /* 原 setOpenId 校验逻辑 */ }
    public void bindUnionId(String unionId) { /* 原 setUnionId 校验逻辑 */ }
    public void checkPassword(String rawPassword) { /* 不变 */ }
    public void createSecret() { /* 不变 */ }
}
```

`UserAssembler.toLogin`（default 方法）：

```java
default Login toLogin(UserRegisterDTO dto) {
    return Login.register(
        Phone.builder().phone(dto.getPhone()).build(),
        dto.getLoginName(),
        dto.getPassword(),
        dto.getAuthType());
}
```

`LoginConverter.toLogin(LoginPO)`（default 方法，复刻原映射行为）：

```java
default Login toLogin(LoginPO po) {
    if (po == null) return null;
    AuthTypeEnum authType = (po.getAuthType() == null || po.getAuthType().isEmpty()) ? null : AuthTypeEnum.valueOf(po.getAuthType());
    Integer rid = po.getRoleId();
    RoleId roleId = RoleId.builder().id(rid != null ? rid : -1).build(); // 复刻 defaultValue="-1"
    return Login.reconstitute(
        UserId.builder().id(po.getUserId()).build(),
        Phone.builder().phone(po.getPhone()).build(),
        po.getLoginName(), po.getPassword(), authType,
        po.getOpenId(), po.getUnionId(), roleId, po.getCreateTime());
}
```

`UserDomainService.userRegister` 改动：

```java
login.bindOpenId(authUser.getOpenId());
login.bindUnionId(authUser.getUnionId());
// 删除：login.setPassword(login.getPassword());  // 密码已在 register 工厂加密
login = loginRepository.save(login);
```

---

## 5. 验证方式

- 每完成一组改动执行：`JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests`，确认 `BUILD SUCCESS`。
- 全部完成后整体再编译一次确认。
- 项目无单测基础设施，本轮以「编译通过 + 行为不变」为完成标准。

## 6. 行为保证

- 注册密码仍为单次 MD5；登录 `checkPassword` 逻辑不变。
- 领域事件仍异步发布（仅类位置迁移）。
- Dubbo 仍以 `UserApplicationService` 接口对外暴露（仅暴露类从 core 迁到 infra）。
- `LoginPO ↔ Login` 字段映射行为不变（`roleId` 缺省 -1、`secret` 不恢复）。

## 7. 不在本次范围

P2 聚合边界（`Menus` 聚合根归属）、P3 服务职责错位（空 `RoleDomainService` / 跨聚合编排位置）、P4 DTO 归属、P5 BFF 分层穿透、P6 MD5 安全/测试缺失等，留待后续。

## 8. 风险与回滚

- **风险**：`@Primary` 注入若被误用（多个 `@Primary`）会致歧义——本次仅 core 实现标 `@Primary`，可控。
- **回滚**：改动以文件为单位、按 P0-A/B/C、P1 顺序提交，可逐项 revert。

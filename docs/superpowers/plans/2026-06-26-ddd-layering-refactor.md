# DDD 分层重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 让领域内核(`ddd-domain-core`/`ddd-domain-model`)不再依赖 Dubbo/Spring/MapStruct,并加固 `Login` 充血模型(静态工厂替代 @Builder,消除密码自赋值补救)。

**Architecture:** 4 个独立可编译的提交,按依赖顺序执行:① 依赖净化(model 去 → core 显式补)② Dubbo 下沉到 infrastructure ③ 事件监听器迁出 core ④ Login 静态工厂。每个提交后 `mvn compile` 通过。

**Tech Stack:** Spring Boot 2.7.9 / Dubbo 3.1.6 / MyBatis-Plus / MapStruct / Maven / JDK 1.8(编译目标)

**验证基线:** `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests` → `BUILD SUCCESS`(JDK 21 因旧 Lombok 不兼容,必须用 JDK 8 编译)。项目无单测基础设施,本轮以「编译通过 + 行为不变」为完成标准。

**当前分支:** `refactor/ddd-layering-cleanup`(已含 README 与 spec 提交)

---

## 文件结构总览

| 操作 | 文件 | 责任 |
| --- | --- | --- |
| 修改 | `ddd-domain/ddd-domain-model/pom.xml` | 去掉 spring-context/mapstruct/processor,回归纯 POJO |
| 修改 | `ddd-domain/ddd-domain-core/pom.xml` | 显式补 spring-context/mapstruct/processor;Task2 删 dubbo |
| 修改 | `ddd-infrastructure/pom.xml` | 加 ddd-domain-north + dubbo |
| 修改 | `UserApplicationServiceImpl.java` | `@DubboService` → `@Service @Primary` |
| 新增 | `infrastructure/user/rpc/UserRpcServiceProvider.java` | RPC 暴露适配,委托 core 实现 |
| 删除 | `ddd-domain-core/user/event/LoginEventListener.java` | — |
| 删除 | `ddd-domain-core/user/event/RegisterEventListener.java` | — |
| 新增 | `ddd-infrastructure/user/event/LoginEventListener.java` | 迁移自 core |
| 新增 | `ddd-infrastructure/user/event/RegisterEventListener.java` | 迁移自 core |
| 修改 | `Login.java` | 去 @Builder,加 register/reconstitute 静态工厂 |
| 修改 | `UserAssembler.java` | toLogin 改 default 方法 |
| 修改 | `LoginConverter.java` | toLogin(LoginPO) 改 default 方法 |
| 修改 | `UserDomainService.java` | bindOpenId/bindUnionId,删自赋值 |

---

## Task 1: 依赖净化(P0-B:model 去技术依赖 + core 显式补)

**Files:**
- Modify: `ddd-domain/ddd-domain-model/pom.xml`
- Modify: `ddd-domain/ddd-domain-core/pom.xml`

> 背景:`ddd-domain-model` 的 spring-context/mapstruct 是纯 pom 冗余(源码零使用),实际被 core 经传递依赖使用。本任务把它们从 model 删除、在 core 显式声明,使 model 回归纯 POJO。**此时 core 仍保留 dubbo**(Task 2 再删),保证本提交可编译。

- [ ] **Step 1: 从 model pom 删除 3 个依赖**

打开 `ddd-domain/ddd-domain-model/pom.xml`,删除以下 3 个 `<dependency>` 块(mapstruct、mapstruct-processor、spring-context):

```xml
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
```

删除后 model 的 `<dependencies>` 仅保留:`ddd-common`、`commons-collections4`、`lombok`。

- [ ] **Step 2: core pom 显式补 spring-context + mapstruct + processor**

打开 `ddd-domain/ddd-domain-core/pom.xml`,在 `<dependencies>` 内(dubbo 依赖块**之前**)新增:

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
        </dependency>
```

(version 由根 pom 的 dependencyManagement / spring-boot BOM 管理,无需显式版本号。)

- [ ] **Step 3: core pom 的 build 增加 annotationProcessorPaths**

在 `ddd-domain-core/pom.xml` 的 `maven-compiler-plugin` `<configuration>` 内追加 `<annotationProcessorPaths>`(保证 lombok 先于 mapstruct 执行),与 infra/bff 一致:

```xml
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>${javase_version}</source>
                <target>${javase_version}</target>
                <encoding>${project_build_sourceEncoding}</encoding>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
```

- [ ] **Step 4: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add ddd-domain/ddd-domain-model/pom.xml ddd-domain/ddd-domain-core/pom.xml
git commit -m "refactor(domain): model 去除 spring/mapstruct 冗余依赖，core 显式声明" -m "P0-B: 让 ddd-domain-model 回归纯 POJO；spring-context/mapstruct 迁移到真正使用的 ddd-domain-core。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 2: Dubbo 下沉到 infrastructure(P0-A)

**Files:**
- Modify: `ddd-domain/ddd-domain-core/pom.xml`
- Modify: `ddd-domain-core/.../user/application/UserApplicationServiceImpl.java`
- Modify: `ddd-infrastructure/pom.xml`
- Create: `ddd-infrastructure/.../user/rpc/UserRpcServiceProvider.java`

- [ ] **Step 1: core pom 删除 dubbo 依赖**

打开 `ddd-domain/ddd-domain-core/pom.xml`,删除:

```xml
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
```

- [ ] **Step 2: UserApplicationServiceImpl 注解替换**

打开 `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/application/UserApplicationServiceImpl.java`。

删除 dubbo import:
```java
import org.apache.dubbo.config.annotation.DubboService;
```
新增 spring import(与现有 import 合并):
```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
```

将类注解:
```java
@DubboService
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {
```
改为:
```java
@Service
@Primary
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {
```

- [ ] **Step 3: infra pom 增加 north + dubbo 依赖**

打开 `ddd-infrastructure/pom.xml`,在 `<dependencies>` 内(ddd-domain-south 依赖块之后)新增:

```xml
        <dependency>
            <groupId>com.tbc.ddd</groupId>
            <artifactId>ddd-domain-north</artifactId>
            <version>${revision}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
```

- [ ] **Step 4: 新建 UserRpcServiceProvider**

创建 `ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/rpc/UserRpcServiceProvider.java`:

```java
package com.tbc.ddd.infrastructure.user.rpc;

import org.apache.dubbo.config.annotation.DubboService;

import com.tbc.ddd.domain.north.user.application.UserApplicationService;
import com.tbc.ddd.domain.user.dto.UserDTO;
import com.tbc.ddd.domain.user.dto.UserRegisterDTO;

import lombok.RequiredArgsConstructor;

/**
 * 用户应用服务 RPC 暴露适配。
 * <p>
 * 领域核心层(core)不再依赖 Dubbo；RPC 暴露(OHS)下沉到基础设施层，
 * 通过委托持有 core 的 {@link UserApplicationService} 实现。
 *
 * @author Johnson.Jia
 */
@DubboService
@RequiredArgsConstructor
public class UserRpcServiceProvider implements UserApplicationService {

    private final UserApplicationService delegate;

    @Override
    public UserDTO loginByPhone(String phone, String password) {
        return delegate.loginByPhone(phone, password);
    }

    @Override
    public UserDTO loginByName(String loginName, String password) {
        return delegate.loginByName(loginName, password);
    }

    @Override
    public UserDTO userRegister(UserRegisterDTO userRegisterDTO) {
        return delegate.userRegister(userRegisterDTO);
    }
}
```

> 注入说明:容器内有 core 的 `@Primary` 实现与 provider 自身两个 `UserApplicationService` bean,Spring 注入 `delegate` 时选 `@Primary` 实现。

- [ ] **Step 5: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit**

```bash
git add ddd-domain/ddd-domain-core/pom.xml ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/application/UserApplicationServiceImpl.java ddd-infrastructure/pom.xml ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/rpc/UserRpcServiceProvider.java
git commit -m "refactor(domain): @DubboService 下沉到 infrastructure，core 去 Dubbo 依赖" -m "P0-A: 领域核心层不再依赖 Dubbo；RPC 暴露适配 UserRpcServiceProvider 委托 core 的 @Primary 实现。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 3: 领域事件监听器迁出 core(P0-C)

**Files:**
- Delete: `ddd-domain-core/.../user/event/LoginEventListener.java`
- Delete: `ddd-domain-core/.../user/event/RegisterEventListener.java`
- Create: `ddd-infrastructure/.../user/event/LoginEventListener.java`
- Create: `ddd-infrastructure/.../user/event/RegisterEventListener.java`

- [ ] **Step 1: 删除 core 中的两个监听器**

删除文件:
- `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/event/LoginEventListener.java`
- `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/event/RegisterEventListener.java`

```bash
git rm ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/event/LoginEventListener.java
git rm ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/event/RegisterEventListener.java
```

- [ ] **Step 2: 在 infrastructure 新建 LoginEventListener**

创建 `ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/event/LoginEventListener.java`:

```java
package com.tbc.ddd.infrastructure.user.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tbc.ddd.common.utils.Consts;
import com.tbc.ddd.domain.user.event.LoginEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功事件监听
 *
 * @author Johnson.Jia
 * @date 2023/3/18 17:36:48
 */
@Slf4j
@Component
public class LoginEventListener {

    @Async
    @EventListener(LoginEvent.class)
    public void loginEvent(LoginEvent event) {
        log.info("登录成功,event:{}", Consts.objectToString(event));
    }
}
```

- [ ] **Step 3: 在 infrastructure 新建 RegisterEventListener**

创建 `ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/event/RegisterEventListener.java`:

```java
package com.tbc.ddd.infrastructure.user.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tbc.ddd.common.utils.Consts;
import com.tbc.ddd.domain.user.event.RegisterEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 注册成功事件监听
 *
 * @author Johnson.Jia
 */
@Slf4j
@Component
public class RegisterEventListener {

    @Async
    @EventListener(RegisterEvent.class)
    public void loginEvent(RegisterEvent event) {
        log.info("注册成功,event:{}", Consts.objectToString(event));
    }
}
```

- [ ] **Step 4: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/event/
git commit -m "refactor(domain): 领域事件监听器迁出 core 至 infrastructure" -m "P0-C: Spring 事件订阅机制属基础设施关注点，从领域核心层移出。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 4: Login 静态工厂加固(P1)

**Files:**
- Modify: `ddd-domain-model/.../user/model/Login.java`
- Modify: `ddd-domain-core/.../user/assembler/UserAssembler.java`
- Modify: `ddd-infrastructure/.../user/converter/LoginConverter.java`
- Modify: `ddd-domain-core/.../user/service/UserDomainService.java`

> 这 4 个文件相互耦合,必须作为一个原子提交(Login 去 @Builder/setPassword 后,assembler/converter/domainservice 必须同步改才能编译)。

- [ ] **Step 1: 重构 Login.java**

用以下完整内容替换 `ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/model/Login.java`:

```java
package com.tbc.ddd.domain.user.model;

import java.util.Objects;
import java.util.UUID;

import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.exception.OpenIdException;
import com.tbc.ddd.domain.user.valueobject.Phone;
import com.tbc.ddd.domain.user.valueobject.UserId;
import org.apache.commons.lang3.StringUtils;

import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.common.utils.EncryptUtil;
import com.tbc.ddd.domain.role.valueobject.RoleId;
import com.tbc.ddd.domain.user.exception.PasswordException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * <p>
 * 用户登录信息 对象(聚合根)
 * </p>
 * <p>
 * 创建必须走 {@link #register} / {@link #reconstitute} 静态工厂,
 * 保证密码加密等不变量在构造时即成立,避免被构造器/builder 绕过。
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class Login implements AggregateRoot {

    /**
     * 用户id
     */
    private UserId userId;

    /**
     * 手机号码
     */
    private Phone phone;

    /**
     * 用户名 / 登录名 / 真实姓名
     */
    private String loginName;

    /**
     * 用户登录密码(已加密)
     */
    private String password;

    /**
     * 授权类型
     */
    private AuthTypeEnum authType;

    /**
     * 用户 微信 小程序唯一标识 open id
     */
    private String openId;

    /**
     * 用户在微信开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionId;

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 密钥
     */
    private Secret secret;

    /**
     * 创建时间
     */
    private Long createTime;

    private Login() {
    }

    /**
     * 注册工厂：明文密码经 MD5 加密后存入。
     *
     * @author Johnson.Jia
     * @param phone
     *            手机号
     * @param loginName
     *            登录名
     * @param rawPassword
     *            明文密码
     * @param authType
     *            授权类型
     * @return
     */
    public static Login register(Phone phone, String loginName, String rawPassword, AuthTypeEnum authType) {
        VerificationUtil.isTrue(StringUtils.isBlank(rawPassword), new PasswordException("Password is not exist."));
        Login login = new Login();
        login.phone = phone;
        login.loginName = loginName;
        login.password = EncryptUtil.MD5(rawPassword);
        login.authType = authType;
        return login;
    }

    /**
     * 重建工厂：从持久化恢复,password 为密文,原样填入不重新加密。
     *
     * @author Johnson.Jia
     * @param userId
     * @param phone
     * @param loginName
     * @param password
     *            密文密码
     * @param authType
     * @param openId
     * @param unionId
     * @param roleId
     * @param createTime
     * @return
     */
    public static Login reconstitute(UserId userId, Phone phone, String loginName, String password,
        AuthTypeEnum authType, String openId, String unionId, RoleId roleId, Long createTime) {
        Login login = new Login();
        login.userId = userId;
        login.phone = phone;
        login.loginName = loginName;
        login.password = password;
        login.authType = authType;
        login.openId = openId;
        login.unionId = unionId;
        login.roleId = roleId;
        login.createTime = createTime;
        return login;
    }

    /**
     * 绑定 openId(含非空校验)
     *
     * @author Johnson.Jia
     * @param openId
     */
    public void bindOpenId(String openId) {
        VerificationUtil.isTrue(StringUtils.isBlank(openId), new OpenIdException("OpenId is not exist, auth failed."));
        this.openId = openId;
    }

    /**
     * 绑定 unionId(含非空校验)
     *
     * @author Johnson.Jia
     * @param unionId
     */
    public void bindUnionId(String unionId) {
        VerificationUtil.isTrue(StringUtils.isBlank(unionId),
            new OpenIdException("UnionId is not exist, auth failed."));
        this.unionId = unionId;
    }

    /**
     * 生成会话密钥
     */
    public void createSecret() {
        this.secret =
            Secret.builder().sessionId(UUID.randomUUID().toString()).secretKey(UUID.randomUUID().toString()).build();
    }

    /**
     * 校验密码
     *
     * @author Johnson.Jia
     * @param rawPassword
     *            明文密码
     */
    public void checkPassword(String rawPassword) {
        VerificationUtil.isTrue(StringUtils.isBlank(rawPassword), new PasswordException("Password is not exist."));

        VerificationUtil.isFalse(Objects.equals(EncryptUtil.MD5(rawPassword), this.password),
            new PasswordException("Password Error."));
    }

}
```

- [ ] **Step 2: UserAssembler.toLogin 改 default 方法**

打开 `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/assembler/UserAssembler.java`。

将 `toLogin` 方法(含其上方的 `@Mapping` 注解)整体替换为 default 方法:

删除:
```java
    @Mapping(source = "phone", target = "phone.phone")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "openId", ignore = true)
    @Mapping(target = "unionId", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "secret", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Login toLogin(UserRegisterDTO userRegisterDTO);
```

替换为:
```java
    /**
     * dto 转 Login 聚合(注册场景：明文密码经工厂加密)
     *
     * @author Johnson.Jia
     * @param userRegisterDTO
     * @return
     */
    default Login toLogin(UserRegisterDTO userRegisterDTO) {
        return Login.register(Phone.builder().phone(userRegisterDTO.getPhone()).build(),
            userRegisterDTO.getLoginName(), userRegisterDTO.getPassword(), userRegisterDTO.getAuthType());
    }
```

并补充 import(若尚未存在):
```java
import com.tbc.ddd.domain.user.valueobject.Phone;
```
(UserAssembler 现有 import 已含 `Login`、`UserRegisterDTO`;`Phone` 需新增。)

- [ ] **Step 3: LoginConverter.toLogin(LoginPO) 改 default 方法**

打开 `ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/converter/LoginConverter.java`。

将 `toLogin(LoginPO)` 方法(含其上方 `@Mapping` 注解)替换为 default 方法:

删除:
```java
    @Mapping(source = "roleId", target = "roleId.id", defaultValue = "-1")
    @Mapping(source = "userId", target = "userId.id")
    @Mapping(source = "phone", target = "phone.phone")
    @Mapping(target = "secret", ignore = true)
    Login toLogin(LoginPO loginPO);
```

替换为:
```java
    /**
     * PO 转 Login 聚合(重建场景：密文密码原样恢复)
     *
     * @author Johnson.Jia
     * @param loginPO
     * @return
     */
    default Login toLogin(LoginPO loginPO) {
        if (loginPO == null) {
            return null;
        }
        AuthTypeEnum authType = (loginPO.getAuthType() == null || loginPO.getAuthType().isEmpty()) ? null
            : AuthTypeEnum.valueOf(loginPO.getAuthType());
        Integer rid = loginPO.getRoleId();
        RoleId roleId = RoleId.builder().id(rid != null ? rid : -1).build();
        return Login.reconstitute(UserId.builder().id(loginPO.getUserId()).build(),
            Phone.builder().phone(loginPO.getPhone()).build(), loginPO.getLoginName(), loginPO.getPassword(),
            authType, loginPO.getOpenId(), loginPO.getUnionId(), roleId, loginPO.getCreateTime());
    }
```

并补充 import:
```java
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.valueobject.Phone;
import com.tbc.ddd.domain.user.valueobject.UserId;
import com.tbc.ddd.domain.role.valueobject.RoleId;
```
(LoginConverter 现有 import 已含 `Login`、`LoginPO`、`Mapping`、`Mapper`、`List`、`Phone`;需新增 `AuthTypeEnum`、`UserId`、`RoleId`。注意现有已有 `Phone` import,勿重复。)

- [ ] **Step 4: UserDomainService 调整**

打开 `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/service/UserDomainService.java`。

将 `userRegister` 方法中的 3 行:

```java
        login.setOpenId(authUser.getOpenId());
        login.setUnionId(authUser.getUnionId());
        login.setPassword(login.getPassword());
```

替换为:

```java
        login.bindOpenId(authUser.getOpenId());
        login.bindUnionId(authUser.getUnionId());
```

(删除自赋值 `setPassword(login.getPassword())`——密码已在 `UserAssembler.toLogin` → `Login.register` 时加密。)

- [ ] **Step 5: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit**

```bash
git add ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/model/Login.java ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/assembler/UserAssembler.java ddd-infrastructure/src/main/java/com/tbc/ddd/infrastructure/user/converter/LoginConverter.java ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/service/UserDomainService.java
git commit -m "refactor(user): Login 改静态工厂，消除密码自赋值与 builder 绕过校验" -m "P1: Login.register/reconstitute 强制密码加密不变量；删除 setPassword 自赋值补救；assembler/converter 改 default 方法调用工厂。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## 完成标准

- 4 个提交全部 `BUILD SUCCESS`(JDK 8 编译)。
- 注册流程:`UserRegisterDTO → Login.register`(密码 MD5 加密)→ bindOpenId/bindUnionId → save。行为与重构前一致(单次 MD5)。
- 登录流程:`LoginRepository` 经 `LoginConverter.toLogin` → `Login.reconstitute`(密文)→ `checkPassword`。行为一致。
- Dubbo 仍以 `UserApplicationService` 接口对外暴露(暴露类从 core 迁至 infra 的 `UserRpcServiceProvider`)。
- 领域事件仍异步发布(监听器从 core 迁至 infra)。
- `ddd-domain-model` 无任何框架依赖(仅 common + collections + lombok);`ddd-domain-core` 无 Dubbo 依赖。

# DDD 结构重构实现计划（P2-P6）

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 完成 P2 聚合边界 / P3 服务职责 / P4 DTO 归属 / P5 VO 隔离 / P6 工厂缓存（排除密码 BCrypt 与 Spring Boot 升级）。

**Architecture:** 6 个独立可编译提交，按依赖顺序：P3 删空壳服务 → P4 DTO 迁 north → P6 工厂缓存 → P2 Role 净化 → P5 建 VO+converter → P5 controller 返回 VO。

**Tech Stack:** Spring Boot 2.7.9 / MyBatis-Plus / MapStruct / Maven / JDK 1.8

**验证基线：** `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests` → `BUILD SUCCESS`。项目无单测，以「编译通过 + 行为不变」为完成标准。

**当前分支：** `refactor/ddd-structure-cleanup`（已含 spec 提交 `7a425bd`）

---

## 文件结构总览

| 操作 | 文件 | 责任 |
| --- | --- | --- |
| 删除 | `domain/role/service/RoleDomainService.java` | 空壳 |
| 删除 | `domain/user/service/UserDomainService.java` | userLogin 空、userRegister 上移后空 |
| 修改 | `UserApplicationServiceImpl.java` | userRegister 编排上移、initUserDTO 建树 |
| 移动 | 7 个 DTO → `ddd-domain-north` | 应用契约 DTO |
| 修改 | 多处 import（core/bff） | DTO 包名变更 |
| 修改 | `UserAuthFactory.java` | 缓存 Map |
| 修改 | `Role.java` | 移除 list/建树 |
| 修改 | `UserAssembler.java` | toMenusDtoList |
| 新增 | 5 个 VO（bff） | 接口层 VO |
| 修改 | `UserConverter.java` | DTO→VO |
| 修改 | `UserController.java` / `RoleOrMenusController.java` | 返回 VO |
| 删除 | `UserLoginVO.java` | 被 UserVO 取代 |

---

## Task 1: P3 服务职责归位

**Files:**
- Modify: `ddd-domain-core/.../user/application/UserApplicationServiceImpl.java`
- Delete: `ddd-domain-core/.../role/service/RoleDomainService.java`
- Delete: `ddd-domain-core/.../user/service/UserDomainService.java`

> 先做 P3，这样 Task 2（DTO 迁移）就不必再改即将删除的 `UserDomainService` 的 import。

- [ ] **Step 1: 重写 UserApplicationServiceImpl（userRegister 编排上移 + 删空调用）**

用以下完整内容替换 `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/application/UserApplicationServiceImpl.java`：

```java
package com.tbc.ddd.domain.user.application;

import java.util.Objects;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.north.user.application.UserApplicationService;
import com.tbc.ddd.domain.role.dto.RoleDTO;
import com.tbc.ddd.domain.role.model.Role;
import com.tbc.ddd.domain.south.event.DomainEventPublisher;
import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.domain.south.role.repository.RoleRepository;
import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.assembler.UserAssembler;
import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.dto.LoginDTO;
import com.tbc.ddd.domain.user.dto.UserDTO;
import com.tbc.ddd.domain.user.dto.UserInfoDTO;
import com.tbc.ddd.domain.user.dto.UserRegisterDTO;
import com.tbc.ddd.domain.user.event.LoginEvent;
import com.tbc.ddd.domain.user.event.RegisterEvent;
import com.tbc.ddd.domain.user.exception.UserException;
import com.tbc.ddd.domain.user.factory.UserAuthFactory;
import com.tbc.ddd.domain.user.factory.auth.UserAuthService;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.UserInfo;
import com.tbc.ddd.domain.user.valueobject.Phone;

import lombok.RequiredArgsConstructor;

/**
 * 用户服务接口实现
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:01:31
 */
@Service
@Primary
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    final LoginRepository loginRepository;
    final RoleRepository roleRepository;
    final MenusRepository menusRepository;
    final UserInfoRepository userInfoRepository;

    final UserAuthFactory userAuthFactory;

    final UserAssembler userAssembler;

    final DomainEventPublisher domainEventPublisher;

    @Override
    public UserDTO loginByPhone(String phone, String password) {
        Login login = loginRepository.getByPhone(Phone.builder().phone(phone).build());
        return userLogin(login, password);
    }

    @Override
    public UserDTO loginByName(String loginName, String password) {
        Login login = loginRepository.getByLoginName(loginName);
        return userLogin(login, password);
    }

    @Override
    public UserDTO userRegister(UserRegisterDTO userRegisterDTO) {
        Login login = userAssembler.toLogin(userRegisterDTO);

        VerificationUtil.isTrue(Objects.nonNull(loginRepository.getByPhone(login.getPhone())),
            new UserException("User already exists."));
        VerificationUtil.isTrue(Objects.nonNull(loginRepository.getByLoginName(login.getLoginName())),
            new UserException("User already exists."));

        // 授权获取用户信息（原领域服务逻辑上移）
        UserAuthService authService = userAuthFactory.createAuthService(login.getAuthType());
        AuthUserDTO authUser = authService.getUserInfo(userRegisterDTO.getCode());
        login.bindOpenId(authUser.getOpenId());
        login.bindUnionId(authUser.getUnionId());
        login = loginRepository.save(login);

        userInfoRepository.save(UserInfo.builder().userId(login.getUserId()).gender(authUser.getGender())
            .nickName(authUser.getNickName()).avatarUrl(authUser.getAvatarUrl()).address(authUser.getAddress())
            .createTime(System.currentTimeMillis()).build());

        UserDTO userDTO = this.initUserDTO(login);
        // 发布注册成功事件
        domainEventPublisher.publishEvent(new RegisterEvent(userDTO));
        return userDTO;
    }

    /**
     * 用户登录
     */
    private UserDTO userLogin(Login login, String password) {
        VerificationUtil.isTrue(Objects.isNull(login), "用户不存在");
        login.checkPassword(password);
        login.createSecret();
        UserDTO user = this.initUserDTO(login);
        // 发布登录成功事件
        domainEventPublisher.publishEvent(new LoginEvent(login));
        return user;
    }

    /**
     * 初始化 userDTO
     */
    private UserDTO initUserDTO(Login login) {
        Role role = roleRepository.getById(login.getRoleId());
        if (role != null) {
            role.createMenusTree(menusRepository.getListByIds(role.getMenus()));
        }
        LoginDTO loginDTO = userAssembler.toLoginDto(login);
        RoleDTO roleDTO = userAssembler.toRoleDto(role);
        UserInfoDTO userInfoDTO = userAssembler.toUserInfoDto(userInfoRepository.getById(login.getUserId()));
        return UserDTO.builder().login(loginDTO).role(roleDTO).userInfo(userInfoDTO).build();
    }
}
```

> 注：`initUserDTO` 暂保留 `role.createMenusTree(...)`（Role 还未改造，Task 4 再改），保证本步编译通过。

- [ ] **Step 2: 删除 RoleDomainService**

```bash
git rm ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/role/service/RoleDomainService.java
```

- [ ] **Step 3: 删除 UserDomainService**

```bash
git rm ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/service/UserDomainService.java
```

- [ ] **Step 4: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "refactor(user): 服务职责归位，删空壳服务，userRegister 编排上移" -m "P3: 删除空壳 RoleDomainService/UserDomainService；跨聚合编排骨架(授权+save)上移到 UserApplicationServiceImpl。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 2: P4 DTO 迁移到 north

**Files:**
- Move(7): `ddd-domain-model/.../user/dto/{UserDTO,LoginDTO,UserInfoDTO,UserRegisterDTO,AuthUserDTO}.java` → `ddd-domain-north/.../user/dto/`
- Move(2): `ddd-domain-model/.../role/dto/{RoleDTO,MenusDTO}.java` → `ddd-domain-north/.../role/dto/`
- Modify(imports): core 的 `UserAssembler`、`UserApplicationServiceImpl`、`RegisterEvent`、`factory/auth/{UserAuthService,DefaultAuthImpl,WechatAuthImpl,DingDingAuthImpl}`
- Modify(imports): bff 的 `UserController`、`UserConverter`、`UserLoginVO`、`RoleOrMenusController`、`RoleOrMenusService`、`RoleOrMenusServiceImpl`、`RoleOrMenusConverter`

> 包名 `com.tbc.ddd.domain.{user,role}.dto` → `com.tbc.ddd.domain.north.{user,role}.dto`。

- [ ] **Step 1: 新建 7 个 DTO 到 north（改包名）**

在 `ddd-domain/ddd-domain-north/src/main/java/com/tbc/ddd/domain/north/user/dto/` 下创建 5 个文件，内容与原文件相同、仅 `package` 改为 `com.tbc.ddd.domain.north.user.dto`：`UserDTO.java`、`LoginDTO.java`、`UserInfoDTO.java`、`UserRegisterDTO.java`、`AuthUserDTO.java`。

在 `ddd-domain/ddd-domain-north/src/main/java/com/tbc/ddd/domain/north/role/dto/` 下创建 2 个文件，`package` 改为 `com.tbc.ddd.domain.north.role.dto`：`RoleDTO.java`、`MenusDTO.java`。

> 注意 DTO 间互引也要改：`UserDTO` import `com.tbc.ddd.domain.north.role.dto.RoleDTO` 与 `com.tbc.ddd.domain.north.user.dto.{LoginDTO,UserInfoDTO}`；`RoleDTO` import `com.tbc.ddd.domain.north.role.dto.MenusDTO`。

- [ ] **Step 2: 删除 model 中的旧 DTO**

```bash
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/dto/UserDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/dto/LoginDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/dto/UserInfoDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/dto/UserRegisterDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/user/dto/AuthUserDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/role/dto/RoleDTO.java
git rm ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/role/dto/MenusDTO.java
```

- [ ] **Step 3: 更新 core 的 import**

逐文件把 `com.tbc.ddd.domain.user.dto.*` → `com.tbc.ddd.domain.north.user.dto.*`、`com.tbc.ddd.domain.role.dto.*` → `com.tbc.ddd.domain.north.role.dto.*`：

- `UserAssembler.java`：`LoginDTO/UserInfoDTO/UserRegisterDTO` → north.user.dto；`RoleDTO/MenusDTO` → north.role.dto
- `UserApplicationServiceImpl.java`：`AuthUserDTO/LoginDTO/UserDTO/UserInfoDTO/UserRegisterDTO` → north.user.dto；`RoleDTO` → north.role.dto
- `RegisterEvent.java`：`UserDTO` → north.user.dto
- `factory/auth/UserAuthService.java`：`AuthUserDTO` → north.user.dto
- `factory/auth/DefaultAuthImpl.java`：`AuthUserDTO` → north.user.dto
- `factory/auth/WechatAuthImpl.java`：`AuthUserDTO` → north.user.dto
- `factory/auth/DingDingAuthImpl.java`：`AuthUserDTO` → north.user.dto

- [ ] **Step 4: 更新 bff 的 import**

- `UserController.java`：`UserDTO` → north.user.dto
- `UserConverter.java`：`UserRegisterDTO` → north.user.dto
- `UserLoginVO.java`：`RoleDTO` → north.role.dto
- `RoleOrMenusController.java`：`MenusDTO` → north.role.dto
- `RoleOrMenusService.java`：`MenusDTO` → north.role.dto
- `RoleOrMenusServiceImpl.java`：`MenusDTO` → north.role.dto
- `RoleOrMenusConverter.java`：`MenusDTO` → north.role.dto

- [ ] **Step 5: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit**

```bash
git add -A
git commit -m "refactor(domain): 应用层 DTO 从 model 迁移到 north" -m "P4: UserDTO/LoginDTO/UserInfoDTO/UserRegisterDTO/AuthUserDTO/RoleDTO/MenusDTO 迁至 ddd-domain-north；model 只留实体/值对象/事件/枚举。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 3: P6 工厂缓存

**Files:**
- Modify: `ddd-domain-core/.../user/factory/UserAuthFactory.java`

- [ ] **Step 1: 重写 UserAuthFactory（缓存 Map）**

用以下完整内容替换 `ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/factory/UserAuthFactory.java`：

```java
package com.tbc.ddd.domain.user.factory;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.tbc.ddd.common.exception.BaseException;
import com.tbc.ddd.common.tools.VerificationUtil;
import com.tbc.ddd.domain.user.enums.AuthTypeEnum;
import com.tbc.ddd.domain.user.factory.auth.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 用户授权服务工厂
 *
 * @author Johnson.Jia
 * @date 2023/3/21 18:45:23
 */
@Service
@RequiredArgsConstructor
public class UserAuthFactory {

    final ApplicationContext context;

    Map<AuthTypeEnum, UserAuthService> authServiceMap;

    @PostConstruct
    void init() {
        authServiceMap = new EnumMap<>(AuthTypeEnum.class);
        context.getBeansOfType(UserAuthService.class).values()
            .forEach(authService -> authServiceMap.put(authService.getAuthType(), authService));
    }

    /**
     * 按授权类型获取授权服务
     *
     * @author Johnson.Jia
     * @param authTypeEnum
     *            授权类型
     * @return 命中的授权服务；未命中返回 UNKNOWN 默认实现
     */
    public UserAuthService createAuthService(AuthTypeEnum authTypeEnum) {
        VerificationUtil.isTrue(authTypeEnum == null, new BaseException("AuthType is not exist."));
        return authServiceMap.getOrDefault(authTypeEnum, authServiceMap.get(AuthTypeEnum.UNKNOWN));
    }

}
```

> 说明：`@PostConstruct` 一次性收集所有 `UserAuthService` bean，按 `getAuthType()` 建 `EnumMap`；`createAuthService` 直接查表，去掉每次 `getBeansOfType` 反射。默认实现取 `UNKNOWN` 对应的 `DefaultAuthImpl`。

- [ ] **Step 2: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit**

```bash
git add ddd-domain/ddd-domain-core/src/main/java/com/tbc/ddd/domain/user/factory/UserAuthFactory.java
git commit -m "perf(user): UserAuthFactory 缓存授权服务，去除每次反射查找" -m "P6: @PostConstruct 一次性收集 bean 建 EnumMap，createAuthService 直接查表。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 4: P2 Role 聚合净化

**Files:**
- Modify: `ddd-domain-model/.../role/model/Role.java`
- Modify: `ddd-domain-core/.../user/application/UserApplicationServiceImpl.java`
- Modify: `ddd-domain-core/.../user/assembler/UserAssembler.java`

- [ ] **Step 1: 精简 Role（移除 list 与建树方法）**

用以下完整内容替换 `ddd-domain/ddd-domain-model/src/main/java/com/tbc/ddd/domain/role/model/Role.java`：

```java
package com.tbc.ddd.domain.role.model;

import java.time.LocalDateTime;
import java.util.List;

import com.tbc.ddd.common.ddd.AggregateRoot;
import com.tbc.ddd.domain.role.valueobject.MenusId;
import com.tbc.ddd.domain.role.valueobject.RoleId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * <p>
 * 用户角色表(聚合根)
 * </p>
 * <p>
 * 聚合根只承载自身持久状态(roleId/name/menus/createTime)；
 * 菜单树组装属展示逻辑，由应用层负责，不再挂在 Role 上。
 *
 * @author Johnson.Jia
 * @since 2023-03-15 15:42:55
 */
@Data
@Builder
@Setter(AccessLevel.PRIVATE)
public class Role implements AggregateRoot {

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 角色 名称
     */
    private String name;

    /**
     * 角色 拥有 菜单 ( 功能 )
     */
    private List<MenusId> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
```

- [ ] **Step 2: UserAssembler 新增 toMenusDtoList**

在 `UserAssembler.java` 接口体内（任意方法旁）新增方法声明：

```java
    /**
     * 菜单集合转 DTO 集合(供应用层组装菜单树)
     *
     * @author Johnson.Jia
     * @param menus
     * @return
     */
    List<MenusDTO> toMenusDtoList(List<Menus> menus);
```

（`Menus`、`MenusDTO` 已在该文件 import 中；mapstruct 自动基于 `toMenusDto` 生成集合映射，且能递归处理 `MenusDTO.list`。）

- [ ] **Step 3: UserApplicationServiceImpl 接管建树逻辑**

在 `UserApplicationServiceImpl.java` 中：

3a. 调整 `initUserDTO` 方法（把 `role.createMenusTree(...)` 替换为应用层建树 + 组装 RoleDTO.list）：

替换：
```java
    private UserDTO initUserDTO(Login login) {
        Role role = roleRepository.getById(login.getRoleId());
        if (role != null) {
            role.createMenusTree(menusRepository.getListByIds(role.getMenus()));
        }
        LoginDTO loginDTO = userAssembler.toLoginDto(login);
        RoleDTO roleDTO = userAssembler.toRoleDto(role);
        UserInfoDTO userInfoDTO = userAssembler.toUserInfoDto(userInfoRepository.getById(login.getUserId()));
        return UserDTO.builder().login(loginDTO).role(roleDTO).userInfo(userInfoDTO).build();
    }
```
为：
```java
    private UserDTO initUserDTO(Login login) {
        Role role = roleRepository.getById(login.getRoleId());
        RoleDTO roleDTO = userAssembler.toRoleDto(role);
        if (role != null) {
            List<Menus> menus = menusRepository.getListByIds(role.getMenus());
            roleDTO.setList(userAssembler.toMenusDtoList(buildMenusTree(menus)));
        }
        LoginDTO loginDTO = userAssembler.toLoginDto(login);
        UserInfoDTO userInfoDTO = userAssembler.toUserInfoDto(userInfoRepository.getById(login.getUserId()));
        return UserDTO.builder().login(loginDTO).role(roleDTO).userInfo(userInfoDTO).build();
    }

    /**
     * 构建菜单树(原 Role 领域逻辑迁移至应用层组装)
     */
    private List<Menus> buildMenusTree(List<Menus> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<Menus> tree = menusRecursion(
            list.stream().filter(Menus::isMenus).collect(Collectors.toList()),
            MenusId.builder().id(0).build());
        tree.forEach(menus -> menus.setButtonCode(list));
        return tree;
    }

    private List<Menus> menusRecursion(List<Menus> list, MenusId parentId) {
        List<Menus> result = new ArrayList<>();
        list.forEach(menus -> {
            if (menus.getParentId().equals(parentId) && menus.isEnable()) {
                menus.addMenusTree(menusRecursion(list, menus.getMenusId()));
                if (menus.isMenus()) {
                    result.add(menus);
                }
            }
        });
        return result;
    }
```

3b. 补充 import（在 `UserApplicationServiceImpl.java` 顶部 import 区）：
```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.tbc.ddd.domain.role.model.Menus;
import com.tbc.ddd.domain.role.valueobject.MenusId;
```
（`RoleDTO`、`MenusRepository` 等已 import。`Role` 已 import。）

- [ ] **Step 4: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "refactor(role): Role 聚合净化，建树逻辑上移应用层" -m "P2: Role 移除 list 字段与建树方法回归纯净聚合根；菜单树组装移至 UserApplicationServiceImpl，结果组装进 RoleDTO.list。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 5: P5 建 VO + converter

**Files:**
- Create: `bff/user/model/vo/{UserVO,LoginVO,UserInfoVO}.java`
- Create: `bff/role/model/vo/{RoleVO,MenusVO}.java`
- Modify: `bff/user/converter/UserConverter.java`

> 本步只新增 VO 与映射方法，不改 controller——保证编译通过且不影响现有接口；Task 6 再切 controller。

- [ ] **Step 1: 新建 5 个 VO（统一 `com.tbc.ddd.bff.vo` 包）**

为避免 user/role 子包互相引用，所有 VO 统一放 `com.tbc.ddd.bff.vo`。在 `ddd-bff/src/main/java/com/tbc/ddd/bff/vo/` 下创建 5 个 VO：

`MenusVO.java`：
```java
package com.tbc.ddd.bff.vo;

import com.tbc.ddd.domain.role.enums.MenusStatusEnum;
import com.tbc.ddd.domain.role.enums.MenusTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class MenusVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String url;
    private String interfaceUrl;
    private String icon;
    private String reIcon;
    private Integer parentId;
    private MenusTypeEnum type;
    private String code;
    private MenusStatusEnum status;
    private List<MenusVO> list;
}
```

`RoleVO.java`：
```java
package com.tbc.ddd.bff.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private List<MenusVO> list;
}
```

`LoginVO.java`：
```java
package com.tbc.ddd.bff.vo;

import com.tbc.ddd.common.bean.Secret;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String phone;
    private String loginName;
    private Long createTime;
    private Secret secret;
}
```

`UserInfoVO.java`：
```java
package com.tbc.ddd.bff.vo;

import com.tbc.ddd.domain.user.enums.GenderEnum;
import com.tbc.ddd.domain.user.valueobject.Address;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户详情 VO
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String nickName;
    private GenderEnum gender;
    private String avatarUrl;
    private String identityCard;
    private Address address;
}
```

`UserVO.java`：
```java
package com.tbc.ddd.bff.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息 VO(接口层出参)
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LoginVO login;
    private RoleVO role;
    private UserInfoVO userInfo;
}
```

- [ ] **Step 2: UserConverter 增加 DTO→VO 映射**

用以下完整内容替换 `ddd-bff/src/main/java/com/tbc/ddd/bff/user/converter/UserConverter.java`：

```java
package com.tbc.ddd.bff.user.converter;

import com.tbc.ddd.bff.user.command.UserRegisterCommand;
import com.tbc.ddd.bff.vo.LoginVO;
import com.tbc.ddd.bff.vo.MenusVO;
import com.tbc.ddd.bff.vo.RoleVO;
import com.tbc.ddd.bff.vo.UserInfoVO;
import com.tbc.ddd.bff.vo.UserVO;
import com.tbc.ddd.domain.north.role.dto.MenusDTO;
import com.tbc.ddd.domain.north.role.dto.RoleDTO;
import com.tbc.ddd.domain.north.user.dto.LoginDTO;
import com.tbc.ddd.domain.north.user.dto.UserDTO;
import com.tbc.ddd.domain.north.user.dto.UserInfoDTO;
import com.tbc.ddd.domain.north.user.dto.UserRegisterDTO;
import org.mapstruct.Mapper;

/**
 * bff 转换器：command→DTO、DTO→VO
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:46:22
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /** req 转 dto */
    UserRegisterDTO toUserRegisterDto(UserRegisterCommand userRegisterCommand);

    /** DTO 转 VO（mapstruct 自动嵌套，list/树递归） */
    UserVO toUserVO(UserDTO userDTO);

    LoginVO toLoginVO(LoginDTO loginDTO);

    RoleVO toRoleVO(RoleDTO roleDTO);

    UserInfoVO toUserInfoVO(UserInfoDTO userInfoDTO);

    MenusVO toMenusVO(MenusDTO menusDTO);

}
```

> mapstruct 会自动处理 `UserDTO.login→LoginVO`、`RoleDTO.list→List<MenusVO>`、`MenusDTO.list→List<MenusVO>`（递归）等嵌套映射。

- [ ] **Step 3: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat(bff): 新增接口层 VO 与 DTO→VO 映射" -m "P5a: 统一 bff.vo 包(UserVO/LoginVO/UserInfoVO/RoleVO/MenusVO)；UserConverter 增加 toUserVO 等映射。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 6: P5 controller 返回 VO

**Files:**
- Modify: `bff/user/controller/UserController.java`
- Modify: `bff/role/controller/RoleOrMenusController.java`
- Delete: `bff/user/model/vo/UserLoginVO.java`

- [ ] **Step 1: UserController 返回 UserVO**

用以下完整内容替换 `ddd-bff/src/main/java/com/tbc/ddd/bff/user/controller/UserController.java`：

```java
package com.tbc.ddd.bff.user.controller;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tbc.ddd.bff.user.command.LoginByNameCommand;
import com.tbc.ddd.bff.user.command.LoginByPhoneCommand;
import com.tbc.ddd.bff.user.command.UserRegisterCommand;
import com.tbc.ddd.bff.user.converter.UserConverter;
import com.tbc.ddd.bff.vo.UserVO;
import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.spring.BaseController;
import com.tbc.ddd.domain.north.user.application.UserApplicationService;

import lombok.RequiredArgsConstructor;

/**
 * 用户展示层
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:52:23
 */
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    final UserApplicationService userApplicationService;
    final UserConverter userConverter;

    @PostMapping("/loginByPhone")
    public Result<UserVO> loginByPhone(@RequestBody @Valid LoginByPhoneCommand login) {
        return Result.ok(userConverter.toUserVO(
            userApplicationService.loginByPhone(login.getPhone(), login.getPassword())));
    }

    @PostMapping("/loginByName")
    public Result<UserVO> loginByName(@RequestBody @Valid LoginByNameCommand login) {
        return Result.ok(
            userConverter.toUserVO(userApplicationService.loginByName(login.getLoginName(), login.getPassword())));
    }

    @PostMapping("/userRegister")
    public Result<UserVO> userRegister(@RequestBody @Validated UserRegisterCommand registerReq) {
        return Result.ok(userConverter
            .toUserVO(userApplicationService.userRegister(userConverter.toUserRegisterDto(registerReq))));
    }

}
```

- [ ] **Step 2: RoleOrMenusController 返回 MenusVO**

用以下完整内容替换 `ddd-bff/src/main/java/com/tbc/ddd/bff/role/controller/RoleOrMenusController.java`：

```java
package com.tbc.ddd.bff.role.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tbc.ddd.bff.role.model.query.MenusQuery;
import com.tbc.ddd.bff.role.query.RoleOrMenusService;
import com.tbc.ddd.bff.user.converter.UserConverter;
import com.tbc.ddd.bff.vo.MenusVO;
import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.spring.BaseController;
import com.tbc.ddd.domain.north.role.dto.MenusDTO;

import lombok.RequiredArgsConstructor;

/**
 * 角色 菜单
 *
 * @author Johnson.Jia
 * @date 2023/3/22 12:41:48
 */
@RestController
@RequiredArgsConstructor
public class RoleOrMenusController extends BaseController {

    final RoleOrMenusService roleOrMenusService;
    final UserConverter userConverter;

    @PostMapping("/getMenusList")
    public Result<IPage<MenusVO>> getMenusList(@RequestBody MenusQuery menusQuery) {
        IPage<MenusDTO> page = roleOrMenusService.queryListByPage(menusQuery);
        return Result.ok(page.convert(userConverter::toMenusVO));
    }

}
```

- [ ] **Step 3: 删除被取代的 UserLoginVO**

```bash
git rm ddd-bff/src/main/java/com/tbc/ddd/bff/user/model/vo/UserLoginVO.java
```

- [ ] **Step 4: 编译验证**

Run: `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests 2>&1 | grep -E "BUILD SUCCESS|BUILD FAILURE|ERROR"`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "refactor(bff): controller 返回 VO，移除 UserLoginVO" -m "P5b: UserController 返回 UserVO、RoleOrMenusController 返回 IPage<MenusVO>；删除被取代的 UserLoginVO。" -m "Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## 完成标准

- 6 个提交全部 `BUILD SUCCESS`（JDK 8 编译）。
- `ddd-domain-model` 不再含任何 DTO（只留实体/值对象/事件/枚举/领域异常）；DTO 全在 `ddd-domain-north`。
- `Role` 聚合根不含建树视图；菜单树由应用层组装。
- 不存在空壳 `RoleDomainService`/`UserDomainService`；跨聚合编排在应用服务。
- bff controller 对外只返回 VO，不暴露 north DTO。
- `UserAuthFactory.createAuthService` 不再每次反射。
- 行为不变：登录/注册返回字段一致（DTO→VO 字段对应）；菜单树/分页查询结果一致；多渠道授权选择一致。

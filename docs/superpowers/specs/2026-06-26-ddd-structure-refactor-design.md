# DDD 结构重构设计：聚合边界 / 服务职责 / DTO 归属 / VO 隔离 / 工厂缓存

- **日期**：2026-06-26
- **状态**：已批准（维护者确认）
- **范围**：P2 聚合边界 + P3 服务职责 + P4 DTO 归属 + P5 VO 隔离 + P6 工厂缓存
- **排除**：P6 中的「密码 MD5→BCrypt」「Spring Boot 2.7→3.x」两项（破坏性/大迁移，维护者确认跳过；密码保留 MD5、Spring Boot 保留 2.7）
- **验证基线**：`JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests` → `BUILD SUCCESS`
- **关联**：上一轮领域分层重构见 `2026-06-26-ddd-layering-refactor-design.md`

---

## 1. 背景与目标

承接上一轮（领域内核去技术依赖 + Login 加固），本轮处理 README 中剩余的 P2-P6 结构性待办，目标是让分层与职责更贴合 DDD：

- 聚合根只承载自身持久状态与领域行为，不混入读模型。
- 应用服务负责跨聚合编排骨架，领域服务承载纯领域规则，删除空壳。
- DTO 归属应用契约层（north），领域模型层（model）只留实体/值对象/事件/枚举。
- 接口层（bff）用自有 VO 对外，不把应用层 DTO 直接暴露给前端。
- 工厂去除每次反射查找。

## 2. 现状问题（精确到文件）

### 2.1 P2 聚合边界
- `Menus` 有独立仓储 `MenusRepository`，作为独立聚合根**合理**，保持不变。
- `Role` 实体内混入 `List<Menus> list`（`Role.java:54`）+ `createMenusTree`/`menusRecursion`（`:65-95`）：这是「给前端建菜单树」的展示逻辑，非 Role 持久状态，让 Role 聚合根承载了读模型。

### 2.2 P3 服务职责错位
- `RoleDomainService` 是空类（`RoleDomainService.java:15`）。
- `UserDomainService.userLogin()` 是空 TODO（`UserDomainService.java:33-35`）但被 `UserApplicationServiceImpl.userLogin` 调用。
- `UserDomainService.userRegister`（`:37-50`）做了跨聚合编排（授权取用户信息 + `loginRepository.save` + `userInfoRepository.save`）——这属应用服务职责，领域服务应只放纯领域规则。

### 2.3 P4 DTO 归属
- 7 个应用层 DTO 位于最核心的 `ddd-domain-model`：`user/dto/{UserDTO, LoginDTO, UserInfoDTO, UserRegisterDTO, AuthUserDTO}`、`role/dto/{RoleDTO, MenusDTO}`。DTO 属应用层传输对象，不该和实体/值对象同处领域模型层。

### 2.4 P5 分层穿透
- `UserController` 直接返回领域层 `UserDTO`（`UserController.java:35,41,46`）；`RoleOrMenusController` 直接返回 `MenusDTO`（`RoleOrMenusController.java:29`）。接口层应用自有 VO 隔离。
- `UserLoginVO`（`bff/user/model/vo`）已定义却字段不全且未使用。

### 2.5 P6 工厂缓存
- `UserAuthFactory.createAuthService` 每次调用都 `context.getBeansOfType` 反射遍历（`UserAuthFactory.java:48`）。

## 3. 设计决策（已与维护者确认）

1. P2：`Menus` 保持；`Role` 移除 `list` 与建树方法，建树逻辑上移到应用层组装进 `RoleDTO.list`。
2. P3：删除 `RoleDomainService`；删除 `UserDomainService.userLogin` 空方法；`userRegister` 编排上移到 `UserApplicationServiceImpl`，`UserDomainService` 变空则删除。
3. P4：7 个 DTO 迁到 `ddd-domain-north`，包名 `com.tbc.ddd.domain.north.{user,role}.dto`。
4. P5：**全面 VO 化**——bff 新建 `UserVO`(+子 `LoginVO/RoleVO/UserInfoVO`)、`MenusVO`，converter 做 DTO→VO，controller 返回 VO。
5. P6：`UserAuthFactory` `@PostConstruct` 缓存为 `Map<AuthTypeEnum, UserAuthService>`。

## 4. 详细改动

### 4.1 P2 — Role 聚合净化

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `domain/role/model/Role.java` | 移除 `List<Menus> list` 字段、`createMenusTree`、`menusRecursion`；保留 `roleId/name/menus(List<MenusId>)/createTime` |
| 修改 | `UserApplicationServiceImpl.java` | `initUserDTO` 内自行建树（从 Role 搬来的逻辑，作私有方法 `buildMenusTree`），结果转 `List<MenusDTO>` 组装进 `RoleDTO.list` |
| 修改 | `UserAssembler.java` | `toRoleDto` 不再映射 `list`；新增 `toMenusDtoList(List<Menus>)` |

建树逻辑（原 `Role.menusRecursion`）迁移到 `UserApplicationServiceImpl` 私有方法，仍调用 `Menus` 的 `isMenus/isEnable/addMenusTree/setButtonCode` 行为（这些保留在 Menus 实体）。

### 4.2 P3 — 服务职责归位

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 删除 | `domain/role/service/RoleDomainService.java` | 空类 |
| 删除 | `domain/user/service/UserDomainService.java` | userLogin 空、userRegister 上移后变空 |
| 修改 | `UserApplicationServiceImpl.java` | `userRegister` 编排上移：注入 `UserAuthFactory`，移除 `UserDomainService` 依赖；内联授权取信息 + bindOpenId/bindUnionId + save login + save userInfo |

上移后 `UserApplicationServiceImpl.userRegister` 关键流程：
```java
Login login = userAssembler.toLogin(dto);
// 查重（已有）
UserAuthService auth = userAuthFactory.createAuthService(login.getAuthType());
AuthUserDTO authUser = auth.getUserInfo(dto.getCode());
login.bindOpenId(authUser.getOpenId());
login.bindUnionId(authUser.getUnionId());
login = loginRepository.save(login);
userInfoRepository.save(UserInfo.builder()...authUser...build());
UserDTO userDTO = initUserDTO(login);
domainEventPublisher.publishEvent(new RegisterEvent(userDTO));
return userDTO;
```
（删除 `userDomainService.userLogin(login)` 空调用）

### 4.3 P4 — DTO 迁移到 north

移动 7 文件并改包名：
- `domain.user.dto.{UserDTO,LoginDTO,UserInfoDTO,UserRegisterDTO,AuthUserDTO}` → `com.tbc.ddd.domain.north.user.dto`
- `domain.role.dto.{RoleDTO,MenusDTO}` → `com.tbc.ddd.domain.north.role.dto`

更新 import 的引用方：
- **core**：`UserAssembler`、`UserApplicationServiceImpl`、`RegisterEvent`、`factory/auth/{UserAuthService,DefaultAuthImpl,WechatAuthImpl,DingDingAuthImpl}`
- **bff**：`UserController`、`UserConverter`、`UserLoginVO`、`RoleOrMenusController`、`RoleOrMenusService`、`RoleOrMenusServiceImpl`、`RoleOrMenusConverter`

> north 已依赖 model，DTO 引用的枚举（`GenderEnum/MenusTypeEnum/MenusStatusEnum`）与值对象（`Address`）仍可用。core/bff 已依赖 north，迁移后无新增模块依赖。

### 4.4 P5 — bff 全面 VO 化

新建 VO（`bff/{user,role}/model/vo/`）：
- `UserVO`（`login: LoginVO, role: RoleVO, userInfo: UserInfoVO`）
- `LoginVO`（`userId, phone, loginName, createTime, secret: Secret`）
- `UserInfoVO`（`userId, nickName, gender: GenderEnum, avatarUrl, identityCard, address: Address`）
- `RoleVO`（`id, name, list: List<MenusVO>`）
- `MenusVO`（`id, name, url, interfaceUrl, icon, reIcon, parentId, type: MenusTypeEnum, code, status: MenusStatusEnum, list: List<MenusVO>`）

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `bff/user/converter/UserConverter.java` | 增 `toUserVO(UserDTO)` 及子结构映射（mapstruct 自动嵌套 + `toLoginVO/toRoleVO/toUserInfoVO/toMenusVO`） |
| 新增 | `bff/role/converter` 或复用 | `RoleMenusPO→MenusVO` 或 `MenusDTO→MenusVO` 映射 |
| 修改 | `UserController.java` | 返回 `Result<UserVO>`，用 converter 转 |
| 修改 | `RoleOrMenusController.java` | 返回 `Result<IPage<MenusVO>>`；bff query 层（`RoleOrMenusService`）内部仍返回 `MenusDTO`，controller 层 `IPage.convert` 转 `MenusVO` |

> `UserLoginVO` 字段不全且将被 `UserVO` 取代，删除。

### 4.5 P6 — 工厂缓存

| 操作 | 文件 | 改动 |
| --- | --- | --- |
| 修改 | `UserAuthFactory.java` | `@PostConstruct` 一次性 `getBeansOfType`，按 `getAuthType()` 构建 `Map<AuthTypeEnum, UserAuthService>`；`createAuthService` 直接 `map.get`，缺失返回 `defaultAuthService` |

## 5. 验证方式

- 每组改动执行 `JAVA_HOME=/d/Java/jdk1.8 mvn -B compile -DskipTests`，确认 `BUILD SUCCESS`。
- 无单测基础设施，以「编译通过 + 行为不变」为完成标准。

## 6. 行为保证

- 登录/注册返回信息内容不变（仅外层类型由 DTO 变 VO，字段一致）。
- 菜单分页查询结果不变（仅类型 `MenusDTO`→`MenusVO`）。
- 注册流程：查重 → 授权 → bindOpenId/bindUnionId → save login → save userInfo → 发事件，顺序与逻辑不变。
- 菜单树构建结果不变（逻辑从 Role 搬到应用服务，行为等价）。
- 多渠道授权选择结果不变（工厂缓存后行为等价）。

## 7. 风险与回滚

- **P4 import 面广**：机械改动，靠编译验证兜底；遗漏 import 会直接编译失败，易发现。
- **P5 VO 字段遗漏**：VO 与 DTO 字段需一一对应，遗漏会丢字段；靠字段对照 + 编译。
- **P3 删除 UserDomainService**：确认无其他引用（仅 `UserApplicationServiceImpl`），编译验证。
- **回滚**：按 P2/P3/P4/P5/P6 分提交，可逐项 revert。

# 账号相关接口文档

> 适用控制器：`iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
>
> 相关实现：
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/BaseController.java`
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/GlobalExceptionHandlerController.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/component/RedisComponent.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/service/impl/UserInfoServiceImpl.java`

---

## 1. 逻辑检查结论

本次检查范围：验证码、注册、登录、自动登录、登出 5 个接口。

### 1.1 当前实现可用点

1. 验证码流程完整：生成、Redis 存储、校验、一次性消费（`AccountController.java:35-64`、`AccountController.java:66-84`）。
2. 注册流程完整：参数校验 + 邮箱昵称唯一性校验 + 用户初始化入库（`UserInfoServiceImpl.java:169-192`）。
3. 登录成功会刷新用户最后登录时间/IP，并签发 token 到 Redis 与 cookie（`UserInfoServiceImpl.java:203-210`、`BaseController.java:85-90`）。
4. 自动登录会在 token 剩余有效期小于 1 天时续期（`AccountController.java:93-96`）。
5. 登出会删除 Redis token 并清理 cookie（`BaseController.java:98-113`）。

### 1.2 需要前端明确知晓的行为

1. `register`、`login` 都不是 `@RequestBody`，必须用 query/form 参数提交，不能发 JSON body（`AccountController.java:50-54`、`AccountController.java:67-71`）。
2. 验证码校验失败后，验证码也会被删除（`finally`），前端必须重新拉验证码（`AccountController.java:61-63`、`AccountController.java:81-83`）。
3. `login` 服务端是直接比较数据库密码与入参字符串（`UserInfoServiceImpl.java:197`）；而注册入库时使用了 MD5（`UserInfoServiceImpl.java:183`）。因此当前实现下，前端登录参数 `password` 需要传 **MD5 值**（`AccountController.java:69` 注释也已说明）。
4. `autoLogin` 读取 token 来源是 **header: token**（`BaseController.java:94-95`），不是从 cookie 读取；前端调用该接口时需在请求头带上 token。

---

## 2. 通用约定

## 2.1 Base Path

控制器类上有 `@RequestMapping("account")`（`AccountController.java:26`），接口前缀统一为：

- `/account`

## 2.2 响应结构

统一返回 `ResponseVO`：

```json
{
  "status": "success | error",
  "code": 200,
  "info": "请求成功",
  "data": {}
}
```

- 成功：`BaseController#getSuccessResponseVO`（`BaseController.java:24-31`）
- 异常：`GlobalExceptionHandlerController`（`GlobalExceptionHandlerController.java:21-53`）

## 2.3 错误码

- `200`：请求成功
- `404`：请求地址不存在
- `600`：请求参数错误 / 业务异常（验证码错误、账号密码错误、邮箱已存在等）
- `601`：信息已经存在（数据库唯一键冲突）
- `500`：服务器错误

---

## 3. 获取验证码接口

### 3.1 接口信息

- **URL**：`/account/verifyCode`
- **方法**：后端未限制（`@RequestMapping`），建议前端使用 `GET`
- **请求参数**：无

### 3.2 成功响应示例

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": {
    "verifyCodeKey": "7c2dxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "verifyCodeBase64": "data:image/png;base64,iVBORw0KGgoAAA..."
  }
}
```

### 3.3 字段说明

| 字段 | 类型 | 说明 |
|---|---|---|
| data.verifyCodeKey | string | 验证码标识，后续 register/login 必传 |
| data.verifyCodeBase64 | string | 验证码图片（可直接用于 img src） |

### 3.4 后端行为

- 验证码答案保存到 Redis：`itube:verifyCode:{verifyCodeKey}`（`RedisComponent.java:16-24`、`Constants.java:10`）。
- 过期时间约 10 分钟（`RedisComponent.java:18`、`Constants.java:6`）。

---

## 4. 注册接口

### 4.1 接口信息

- **URL**：`/account/register`
- **方法**：后端未限制，前端必须用 `POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

### 4.2 参数说明

| 参数名 | 类型 | 必填 | 规则 | 说明 |
|---|---|---|---|---|
| email | string | 是 | `@Email` + `@Size(max=150)` | 注册邮箱 |
| nickname | string | 是 | `@Size(max=20)` | 昵称 |
| password | string | 是 | `Constants.REGEX_PASSWORD` | 8-18 位，至少字母+数字 |
| verifyCodeKey | string | 是 | `@NotEmpty` | 验证码 key |
| verifyCode | string | 是 | `@NotEmpty` | 用户输入验证码 |

### 4.3 成功响应

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

### 4.4 常见失败响应

- 验证码错误：`code=600, info=验证码错误`
- 邮箱已存在：`code=600, info=邮箱已存在`
- 昵称已存在：`code=600, info=昵称已存在`
- 参数校验失败：`code=600, info=请求参数错误`

### 4.5 核心流程

1. 按 `verifyCodeKey` 读取 Redis 验证码。
2. `equalsIgnoreCase` 比对用户输入。
3. 校验通过后执行注册。
4. `finally` 删除验证码（一次性消费）。

---

## 5. 登录接口

### 5.1 接口信息

- **URL**：`/account/login`
- **方法**：后端未限制，前端必须用 `POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

### 5.2 参数说明

| 参数名 | 类型 | 必填 | 规则 | 说明 |
|---|---|---|---|---|
| email | string | 是 | `@Email` + `@NotEmpty` | 登录邮箱 |
| password | string | 是 | `@NotEmpty` | 当前实现要求传 **MD5后的密码字符串** |
| verifyCodeKey | string | 是 | `@NotEmpty` | 验证码 key |
| verifyCode | string | 是 | `@NotEmpty` | 用户输入验证码 |

### 5.3 成功响应示例

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": {
    "userId": "1234567890",
    "nickname": "testUser",
    "avatar": null,
    "expireAt": 1770000000000,
    "token": "b7d9xxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "fansCount": 0,
    "currentCoinCount": 10,
    "focusCount": 0
  }
}
```

### 5.4 常见失败响应

- 验证码错误：`code=600, info=验证码错误`
- 账号或密码错误：`code=600, info=账号或密码错误`
- 账号已被禁用：`code=600, info=账号已被禁用`
- 参数校验失败：`code=600, info=请求参数错误`

### 5.5 后端行为

1. 校验验证码（失败也会删除验证码 key）。
2. 校验账号状态与密码。
3. 更新最后登录时间、登录 IP（`UserInfoServiceImpl.java:203-206`）。
4. 生成 token，写入 Redis（7 天）（`RedisComponent.java:31-36`）。
5. 通过 cookie 下发 token（`BaseController.java:85-90`）。

---

## 6. 自动登录接口

### 6.1 接口信息

- **URL**：`/account/autoLogin`
- **方法**：后端未限制，建议前端使用 `GET`
- **请求头**：`token: <登录返回token>`（必须）
- **请求参数**：无

### 6.2 成功响应

#### 6.2.1 token 有效

返回登录态对象（`TokenUserInfoDto`）。

#### 6.2.2 token 无效/缺失

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

### 6.3 后端行为

- 从请求头读取 `token` 并查 Redis（`BaseController.java:94-95`）。
- 若剩余有效期 < 1 天，则续期 7 天并重新写 cookie（`AccountController.java:93-96`）。

---

## 7. 登出接口

### 7.1 接口信息

- **URL**：`/account/logout`
- **方法**：后端未限制，建议前端使用 `POST`
- **请求参数**：无

### 7.2 成功响应

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

### 7.3 后端行为

- 从请求携带的 cookie 中查找 `token`（`BaseController.java:100-106`）。
- 删除 Redis token，并下发 Max-Age=0 的同名 cookie 清理浏览器端（`BaseController.java:106-109`）。

---

## 8. 前端联调建议流程

1. 调 `/account/verifyCode` 渲染验证码图。
2. 提交注册（或登录）时带上 `verifyCodeKey + verifyCode`。
3. 任意验证码错误后，立即重新获取验证码。
4. 登录成功后：
   - 保存 `data.token` 到前端状态（用于请求头）
   - 后续调 `/account/autoLogin` 时在 header 传 `token`
5. 退出登录时调用 `/account/logout`。

---

## 9. 联调验收清单

- [ ] `/account/verifyCode` 可返回 base64 图片与 key
- [ ] 注册成功返回 `code=200`
- [ ] 登录成功返回 `TokenUserInfoDto`，包含 token
- [ ] 错误验证码返回 `code=600`
- [ ] 同一验证码 key 只能使用一次
- [ ] `/account/autoLogin` 在 token 失效时返回 `data=null`
- [ ] `/account/logout` 后 token 在 Redis 不可用

---

## 10. 改进建议（追加）

1. **限制 HTTP 方法**：将 `@RequestMapping` 改为 `@GetMapping/@PostMapping`，避免 GET 误调用敏感接口（`AccountController.java:35,49,66,86,100`）。
2. **统一 token 读取来源**：当前 `login/logout` 走 cookie，而 `autoLogin` 从 header 取 token（`BaseController.java:94-95`）；建议统一策略（仅 cookie 或仅 header）。
3. **密码处理策略统一**：当前登录依赖前端传 MD5，建议改为后端统一加密比对（避免客户端强耦合加密细节）（`UserInfoServiceImpl.java:197`）。
4. **Cookie 安全属性**：建议设置 `HttpOnly`、`Secure`（HTTPS）、`SameSite`，降低 XSS/CSRF 风险（`BaseController.java:85-90`）。
5. **登出兼容 header token**：若未来前端只走 header token，当前 logout 可能删不到 Redis token；建议补充从 header 兜底读取 token（`BaseController.java:98-113`）。
6. **接口文档中补充环境前缀**：例如 `/api/account/*`（如果网关有统一前缀），减少联调歧义。

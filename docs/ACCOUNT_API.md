# 用户端账号接口文档（iTube-web）

> 控制器：`iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
>
> 相关实现：
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/BaseController.java`
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/GlobalExceptionHandlerController.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/component/RedisComponent.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/service/impl/UserInfoServiceImpl.java`

---

## 1. 本地联调地址

根据 `iTube-web/src/main/resources/application.yaml`：

- `server.port = 7071`
- 未配置 `context-path`

本地 base URL：

- **`http://localhost:7071`**

接口前缀：

- **`/account`**（`@RequestMapping("account")`）

完整示例：

- `http://localhost:7071/account/verifyCode`

---

## 2. 逻辑检查结论

检查范围：验证码、注册、登录、自动登录、登出。

### 2.1 当前实现可用点

1. 验证码流程完整：生成、Redis 存储、校验、一次性消费。
2. 注册流程完整：参数校验 + 邮箱昵称唯一性校验 + 初始化入库。
3. 登录成功会更新最后登录时间/IP，并签发 token 到 Redis + cookie。
4. 自动登录在 token 剩余有效期小于 1 天时续期。
5. 登出会删除 Redis token 并清理 cookie。

### 2.2 前端需注意

1. `register`、`login` 不是 `@RequestBody`，需使用 query/form 参数，不要发 JSON body。
2. 验证码失败后也会删除，必须重新拉取验证码。
3. 当前登录实现需前端传 **MD5 后密码字符串**（与注册入库策略一致）。
4. `autoLogin` 从 header `token` 读取，不是从 cookie 读取。

---

## 3. 通用约定

### 3.1 响应结构

```json
{
  "status": "success | error",
  "code": 200,
  "info": "请求成功",
  "data": {}
}
```

### 3.2 错误码

- `200`：请求成功
- `404`：请求地址不存在
- `600`：请求参数错误 / 业务异常
- `601`：信息已经存在
- `500`：服务器错误

---

## 4. 获取验证码

- **URL**：`/account/verifyCode`
- **方法**：建议 `GET`
- **请求参数**：无

成功响应示例：

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": {
    "verifyCodeKey": "uuid",
    "verifyCodeBase64": "data:image/png;base64,..."
  }
}
```

---

## 5. 注册

- **URL**：`/account/register`
- **方法**：`POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

参数：

| 参数名 | 必填 | 说明 |
|---|---|---|
| email | 是 | `@Email` + `@Size(max=150)` |
| nickname | 是 | `@Size(max=20)` |
| password | 是 | `Constants.REGEX_PASSWORD` |
| verifyCodeKey | 是 | 验证码 key |
| verifyCode | 是 | 验证码 |

常见失败：
- `验证码错误`
- `邮箱已存在`
- `昵称已存在`
- `请求参数错误`

---

## 6. 登录

- **URL**：`/account/login`
- **方法**：`POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

参数：

| 参数名 | 必填 | 说明 |
|---|---|---|
| email | 是 | `@Email` + `@NotEmpty` |
| password | 是 | 当前实现需传 MD5 后字符串 |
| verifyCodeKey | 是 | 验证码 key |
| verifyCode | 是 | 验证码 |

成功响应 `data` 为 `TokenUserInfoDto`，包含 token。

---

## 7. 自动登录

- **URL**：`/account/autoLogin`
- **方法**：建议 `GET`
- **请求头**：`token: <登录返回token>`（必须）

token 无效/缺失时返回 `data: null`。

---

## 8. 登出

- **URL**：`/account/logout`
- **方法**：建议 `POST`
- **请求参数**：无

后端会删除 Redis token 并清理 cookie。

---

## 9. 前端联调流程

1. 先调 `/account/verifyCode` 获取验证码图与 key。
2. 调注册或登录时携带 `verifyCodeKey + verifyCode`。
3. 验证码错误后，必须重新获取验证码。
4. 登录成功后保存 token，调用 `/account/autoLogin` 时放到 header。
5. 退出登录调 `/account/logout`。

---

## 10. 改进建议（用户端）

1. 限制 HTTP 方法（`@GetMapping/@PostMapping`）。
2. 统一 token 读取来源（cookie/header 二选一）。
3. 密码加密策略统一到后端，减少前端耦合。
4. cookie 增加 `HttpOnly`、`Secure`、`SameSite`。
5. logout 增加 header token 兜底。
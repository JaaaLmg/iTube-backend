# 管理端账号接口文档（iTube-admin）

> 控制器：`iTube-admin/src/main/java/com/ja/itubeadmin/controller/AccountController.java`
>
> 相关实现：
> - `iTube-admin/src/main/java/com/ja/itubeadmin/controller/AccountController.java`
> - `iTube-admin/src/main/java/com/ja/itubeadmin/controller/BaseController.java`
> - `iTube-admin/src/main/java/com/ja/itubeadmin/controller/GlobalExceptionHandlerController.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/component/RedisComponent.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/entity/config/AppConfig.java`

---

## 1. 本地联调地址

根据 `iTube-admin/src/main/resources/application.yaml`：

- `server.port = 7070`
- `server.servlet.context-path = /admin`

本地 base URL：

- **`http://localhost:7070/admin`**

接口前缀：

- **`/account`**（`@RequestMapping("account")`）

完整示例：

- `http://localhost:7070/admin/account/login`

---

## 2. 逻辑检查结论

管理端已实现 `verifyCode`、`login`、`logout`，当前流程可用：

1. 验证码生成与 Redis 存储正常。
2. 登录校验验证码 + 配置账号密码，成功后写 admin token 到 Redis 并下发 cookie。
3. 登出删除 Redis 中 admin token，并清理 `adminToken` cookie。

### 前端需注意

1. `login` 不是 `@RequestBody`，需 query/form 参数提交。
2. 验证码失败后也会删除 key，需重新获取。
3. 当前实现要求 `password` 传 **MD5 后字符串**。

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

## 4. 获取验证码（管理端）

- **URL**：`/account/verifyCode`
- **方法**：建议 `GET`
- **请求参数**：无

成功响应：

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

## 5. 登录（管理端）

- **URL**：`/account/login`
- **方法**：`POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

参数：

| 参数名 | 必填 | 说明 |
|---|---|---|
| account | 是 | 管理账号（`@NotEmpty`） |
| password | 是 | 当前实现需传 MD5 后字符串 |
| verifyCodeKey | 是 | 验证码 key |
| verifyCode | 是 | 验证码 |

成功响应示例：

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": "admin"
}
```

登录后状态：

- cookie：`adminToken=<uuid>`（有效期 1 天）
- Redis：`itube:token:admin:{token}`，value=account

---

## 6. 登出（管理端）

- **URL**：`/account/logout`
- **方法**：建议 `POST`
- **请求参数**：无

成功响应：

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

后端行为：

1. 从 cookie 读取 `adminToken`
2. 删除 Redis token
3. 下发 Max-Age=0 的 `adminToken` cookie

---

## 7. 前端联调流程（管理端）

1. 先调 `/account/verifyCode` 获取验证码图和 key。
2. 登录时提交 `account/password/verifyCodeKey/verifyCode`。
3. 验证码错误后重新获取验证码。
4. 登录成功后确认浏览器保存 `adminToken` cookie。
5. 调 `/account/logout` 完成退出。

---

## 8. 联调验收清单（管理端）

- [ ] `/account/verifyCode` 返回 key + base64
- [ ] `/account/login` 成功后写入 `adminToken` cookie
- [ ] `/account/login` 验证码错误返回 `code=600`
- [ ] 同一验证码 key 仅可用一次
- [ ] `/account/logout` 后 Redis admin token 被删除

---

## 9. 改进建议（管理端）

1. 限制 HTTP 方法（`@GetMapping/@PostMapping`）。
2. 明确 `admin.password` 配置是明文还是 MD5，避免双重 MD5 问题。
3. cookie 增加 `HttpOnly`、`Secure`、`SameSite`。
4. `BaseController#getTokenUserInfoDto` 当前读取 web token 名称与 web token Redis 链路，和 admin token 体系不一致，建议尽快修正。
5. 补充管理端自动登录/会话校验接口，便于页面刷新恢复登录态。
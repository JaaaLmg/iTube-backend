# 账号相关接口文档

> 适用控制器：`iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
>
> 相关实现：
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/AccountController.java`
> - `iTube-web/src/main/java/com/ja/itubeweb/controller/GlobalExceptionHandlerController.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/component/RedisComponent.java`
> - `iTube-common/src/main/java/com/ja/itubecommon/service/impl/UserInfoServiceImpl.java`

---

## 1. 通用约定

## 1.1 Base Path

接口前缀为：

- `/account`

## 1.2 响应体统一结构

统一返回 `ResponseVO`（`ResponseVO.java:2-6`）：

```json
{
  "status": "success | error",
  "code": 200,
  "info": "请求成功",
  "data": {}
}
```

成功返回由 `BaseController#getSuccessResponseVO` 组装（`BaseController.java:9-15`）：
- `status = success`
- `code = 200`
- `info = 请求成功`

异常返回由全局异常处理器组装（`GlobalExceptionHandlerController.java:21-53`）。

## 1.3 错误码说明

来自 `ResponseCodeEnum`（`ResponseCodeEnum.java:2-7`）和全局异常处理：

- `200`：请求成功
- `404`：请求地址不存在
- `600`：请求参数错误 / 业务异常（例如验证码错误、邮箱已存在、昵称已存在）
- `601`：信息已经存在（数据库唯一键冲突）
- `500`：服务器错误，请联系管理员

> 说明：当抛出 `BusinessException("xxx")` 时，code 通常为 `600`，info 为具体业务文案（`GlobalExceptionHandlerController.java:31-35`）。

---

## 2. 获取验证码接口

## 2.1 接口信息

- **URL**：`/account/verifyCode`
- **方法**：当前后端未限制（`@RequestMapping`），建议前端用 `GET`
- **Content-Type**：无特殊要求

## 2.2 请求参数

无。

## 2.3 成功响应示例

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

## 2.4 返回字段说明

| 字段 | 类型 | 必有 | 说明 |
|---|---|---|---|
| status | string | 是 | success / error |
| code | number | 是 | 业务状态码 |
| info | string | 是 | 状态说明 |
| data.verifyCodeKey | string | 是 | 验证码 key，注册时必须回传 |
| data.verifyCodeBase64 | string | 是 | 验证码图片 base64，可直接用于 img src |

## 2.5 后端行为说明

- 验证码由 `ArithmeticCaptcha(100, 42)` 生成（`AccountController.java:34`）。
- 正确答案写入 Redis，key 由后端生成 UUID 并返回（`RedisComponent.java:15-18`）。
- 过期时间约 10 分钟（`Constants.REDIS_KEY_EXPIRE_ONE_MIN * 10`，见 `RedisComponent.java:17`、`Constants.java:6`）。

---

## 3. 注册接口

## 3.1 接口信息

- **URL**：`/account/register`
- **方法**：当前后端未限制（`@RequestMapping`），前端必须使用 `POST`
- **参数位置**：query/form 参数（非 JSON body）
- **支持的 Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

## 3.1.1 `application/x-www-form-urlencoded` 与 `multipart/form-data` 的区别

两者在当前接口中都能被 Spring 正常绑定到方法参数（`email`、`nickname`、`password`、`verifyCodeKey`、`verifyCode`），核心区别如下：

| 维度 | x-www-form-urlencoded | multipart/form-data |
|---|---|---|
| 典型场景 | 纯文本表单提交 | 文件上传 + 文本字段 |
| 请求体编码 | `k1=v1&k2=v2` URL 编码 | 按 boundary 分段传输 |
| 开销 | 更小 | 更大（有边界与分段头） |
| 后端解析复杂度 | 更低 | 更高 |
| 本接口适配性 | 最适合 | 可用但非必要 |

**结论（本项目注册接口）**：

- 该接口只有文本字段，没有文件字段，优先使用 **`application/x-www-form-urlencoded`**。
- 若你前端当前统一封装为 `multipart/form-data`，也可以继续使用，不会影响功能。
- 不建议把密码放在 URL query string；即使后端能接收 query，也应通过 POST body（form 或 multipart）提交。


| 参数名 | 类型 | 必填 | 校验规则 | 说明 |
|---|---|---|---|---|
| email | string | 是 | `@Email` + `@Size(max=150)` | 注册邮箱 |
| nickname | string | 是 | `@Size(max=20)` | 昵称 |
| password | string | 是 | 正则：`^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$` | 8-18 位，至少包含字母+数字，仅允许字母数字和 `~!@#$%^&*_` |
| verifyCodeKey | string | 是 | `@NotEmpty` | 验证码 key（来自 verifyCode 接口） |
| verifyCode | string | 是 | `@NotEmpty` | 用户输入验证码 |

## 3.3 成功响应示例

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

## 3.4 失败响应示例

### 1）验证码错误

```json
{
  "status": "error",
  "code": 600,
  "info": "验证码错误",
  "data": null
}
```

### 2）邮箱已存在

```json
{
  "status": "error",
  "code": 600,
  "info": "邮箱已存在",
  "data": null
}
```

### 3）昵称已存在

```json
{
  "status": "error",
  "code": 600,
  "info": "昵称已存在",
  "data": null
}
```

### 4）参数校验失败（如 email 格式不正确）

```json
{
  "status": "error",
  "code": 600,
  "info": "请求参数错误",
  "data": null
}
```

## 3.5 后端处理流程

1. 根据 `verifyCodeKey` 从 Redis 取验证码。
2. 与用户输入 `verifyCode` 做忽略大小写比较（`AccountController.java:53`）。
3. 不通过则抛 `BusinessException("验证码错误")`（`AccountController.java:54`）。
4. 通过后进入注册逻辑：
   - 校验邮箱是否存在（`UserInfoServiceImpl.java:164-167`）
   - 校验昵称是否存在（`UserInfoServiceImpl.java:168-171`）
   - 生成 userId、密码 MD5、设置默认值并入库（`UserInfoServiceImpl.java:172-185`）
5. `finally` 中删除验证码（`AccountController.java:58-60`），保证一次性。

---

## 4. 前端对接流程

1. 页面初始化或点击“换一张”时调用 `/account/verifyCode`。
2. 将 `verifyCodeBase64` 设置到 `<img src="...">`。
3. 保存 `verifyCodeKey` 到页面状态。
4. 用户填写表单后，POST `/account/register`，提交五个参数。
5. 根据 `code/info` 提示用户：
   - 成功：进入登录页或自动登录流程
   - 失败：展示 `info`
6. **无论注册失败原因是什么，建议立即重新调用验证码接口**（因为验证码已被后端删除）。

### 需要前端特别注意的点

1. `register` 当前是 `@RequestMapping("register")`（`AccountController.java:46`），未限定 HTTP 方法。
   - 实际上会接受 GET/POST 等。
   - 前端务必使用 **POST**，避免密码出现在 URL。
2. `register` 参数不是 `@RequestBody`，而是方法参数（`AccountController.java:47-51`）。
   - 前端应使用 **form 参数**提交（`application/x-www-form-urlencoded` 推荐，`multipart/form-data` 也可）。
   - 不要直接发 JSON body。
3. 验证码失败后也会删除 key（`finally` 逻辑）。
   - 前端遇到“验证码错误”时，应重新拉取验证码再提交。

---

## 5. 联调验收清单

- [ ] `/account/verifyCode` 可返回 base64 图片与 key
- [ ] 注册成功返回 `code=200`
- [ ] 错误验证码返回 `code=600` 且 `info=验证码错误`
- [ ] 同一验证码 key 第二次提交失败（一次性消费）
- [ ] 重复邮箱/昵称返回明确文案
- [ ] 参数格式错误返回 `code=600`

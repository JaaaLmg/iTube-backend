# 管理端视频分类接口文档（ADMIN_CATEGORY_API）

> 控制器：`iTube-admin/src/main/java/com/ja/itubeadmin/controller/CategoryController.java`

---

## 1. 本地联调地址

根据 `iTube-admin/src/main/resources/application.yaml`：

- `server.port = 7070`
- `server.servlet.context-path = /admin`

本地 Base URL：

- **`http://localhost:7070/admin`**

分类接口前缀：

- **`/category`**

完整示例：

- `http://localhost:7070/admin/category/loadCategory`

---

## 2. 通用响应结构

统一返回 `ResponseVO`：

```json
{
  "status": "success | error",
  "code": 200,
  "info": "请求成功",
  "data": {}
}
```

常见 code：

- `200`：成功
- `600`：参数错误/业务异常
- `601`：信息已存在（如唯一键冲突）
- `500`：服务器错误
- `404`：路径不存在

---

## 3. 接口总览

1. 查询分类树：`/category/loadCategory`
2. 新增/修改分类：`/category/saveCategory`
3. 删除分类（递归）：`/category/deleteCategory`
4. 修改排序：`/category/changeSort`

> 当前后端使用 `@RequestMapping`，未限制 HTTP 方法。前端建议：
> - 查询：GET
> - 写操作：POST

---

## 4. 查询分类树

- **URL**：`/category/loadCategory`
- **建议方法**：`GET`
- **参数**：`CategoryInfoQuery`（可选）
- **后端固定行为**：按 `sort asc` 排序后返回树形结构

### 返回 data 结构

`data` 是数组，每一项为：

```json
{
  "categoryId": 1,
  "categoryCode": "music",
  "categoryName": "音乐",
  "pCategoryId": 0,
  "icon": "...",
  "background": "...",
  "sort": 1,
  "children": []
}
```

---

## 5. 新增或修改分类

- **URL**：`/category/saveCategory`
- **建议方法**：`POST`
- **参数位置**：query/form（非 JSON body）
- **Content-Type**：
  - `application/x-www-form-urlencoded`（推荐）
  - `multipart/form-data`（可用）

### 请求参数

| 参数名 | 必填 | 类型 | 说明 |
|---|---|---|---|
| categoryId | 否 | Integer | 为空=新增；非空=修改 |
| pCategoryId | 是 | Integer | 父分类 ID（顶级通常传 0） |
| categoryCode | 是 | String | 分类编码（业务上应唯一） |
| categoryName | 是 | String | 分类名称 |
| icon | 否 | String | 图标 |
| background | 否 | String | 背景图 |

### 成功响应

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

### 常见失败

- `categoryCode` 重复（如服务层抛业务异常）
- 参数缺失（`pCategoryId/categoryCode/categoryName`）
- 修改时 `categoryId` 不存在（取决于服务层实现）

---

## 6. 删除分类（递归删除）

- **URL**：`/category/deleteCategory`
- **建议方法**：`POST`（或 DELETE）
- **参数位置**：query/form

### 请求参数

| 参数名 | 必填 | 类型 | 说明 |
|---|---|---|---|
| categoryId | 是 | Integer | 要删除的分类 ID |

### 业务语义

- 该接口为**递归删除**：会删除当前分类及其全部子分类。
- 前端必须做二次确认提示。

### 成功响应

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

---

## 7. 修改排序

- **URL**：`/category/changeSort`
- **建议方法**：`POST`
- **参数位置**：query/form

### 请求参数

| 参数名 | 必填 | 类型 | 说明 |
|---|---|---|---|
| pCategoryId | 是 | Integer | 同级分类的父 ID |
| categoryIds | 是 | String | 逗号分隔分类 ID，表示目标顺序，如 `5,2,8` |

### 规则说明

- `categoryIds` 的顺序就是最终排序顺序，后端从 1 开始递增赋值 sort。
- `categoryIds` 中应只包含同一 `pCategoryId` 下的分类 ID。

### 成功响应

```json
{
  "status": "success",
  "code": 200,
  "info": "请求成功",
  "data": null
}
```

---

## 8. 前端联调建议流程

1. 页面加载先调 `/category/loadCategory` 渲染树。
2. 新增/编辑弹窗提交到 `/category/saveCategory`。
3. 删除前弹窗确认“将递归删除子分类”。
4. 拖拽排序后，按顺序拼接 `categoryIds` 调 `/category/changeSort`。
5. 每次写操作成功后刷新分类树。

---

## 9. 改进建议（后端）

1. **限制 HTTP 方法**：避免写操作被 GET 触发。
2. **将写接口改为 JSON body**：可读性更高，扩展性更好。
3. **`changeSort` 增加参数健壮性校验**：过滤空值、非法数字、跨父级 ID。
4. **删除接口增加保护**：例如根节点不可删、存在资源引用时禁止删。
5. **统一返回更明确的业务错误文案**：便于前端精确提示。
6. **建议拆分 save 接口为 create/update**：减少歧义。

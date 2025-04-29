# 自动答题
这个是调用DeepSeek的API来完成的自动答题的接口，直接部署或在服务器上部署都可以
# 使用方法
运行后接口如下
### 请求示例

```http
GET /getData?title=牛顿第一定律&type=物理&options=A%20物体静止%20B%20匀速运动%20C%20加速运动%20D%20没有受力 HTTP/1.1
Host: localhost:8080
```

- **Method**：GET  
- **URL**：`http://localhost:8080/getData`  
- **Query Parameters**：
  - `title`：牛顿第一定律（必填）
  - `type`：物理（可选）
  - `options`：A 物体静止 B 匀速运动 C 加速运动 D 没有受力（可选）

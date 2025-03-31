# MyGem 项目文档

## 项目简介
MyGem 是一个基于 Spring Boot 3.x 开发的现代化 Web 应用系统，采用 JWT 进行身份认证，使用 MySQL 数据库存储数据。系统提供了用户管理、认证授权等核心功能。

## 技术栈
- 后端：Spring Boot 3.x
- 安全框架：Spring Security + JWT
- 数据库：MySQL
- ORM：MyBatis-Plus
- 前端：Vue 3 + Element Plus（推荐）

## 环境要求
- JDK 17+
- MySQL 8.0+
- Node.js 16+（前端开发）

## 接口文档

### 基础信息
- 基础URL：`http://localhost:8080/api`
- 所有需要认证的接口都需要在请求头中携带 token：
  ```
  Authorization: Bearer <your_token>
  ```

### 认证相关接口

#### 1. 用户注册
- 请求路径：`/auth/register`
- 请求方法：POST
- 请求参数：
  ```json
  {
    "username": "string",     // 用户名
    "password": "string",     // 密码
    "email": "string"        // 邮箱（可选）
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "userId": "number",
      "username": "string",
      "email": "string",
      "role": "string",
      "creditScore": "number",
      "balance": "number"
    }
  }
  ```

#### 2. 用户登录
- 请求路径：`/auth/login`
- 请求方法：POST
- 请求参数：
  ```json
  {
    "username": "string",     // 用户名
    "password": "string"      // 密码
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "string",      // JWT token
      "user": {
        "userId": "number",
        "username": "string",
        "email": "string",
        "role": "string",
        "creditScore": "number",
        "balance": "number"
      }
    }
  }
  ```

#### 3. 获取当前用户信息
- 请求路径：`/auth/me`
- 请求方法：GET
- 请求头：需要携带 token
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": "number",
      "username": "string",
      "email": "string",
      "role": "string",
      "creditScore": "number",
      "balance": "number"
    }
  }
  ```

### 错误响应格式
所有接口在发生错误时会返回统一的错误格式：
```json
{
  "code": "number",      // HTTP 状态码
  "message": "string",   // 错误信息
  "data": null
}
```

### 常见错误码
- 200：成功
- 400：请求参数错误
- 401：未认证或认证失败
- 403：权限不足
- 404：资源不存在
- 500：服务器内部错误

## 用户管理接口

### 1. 获取用户列表（管理员）
- 请求路径：`/admin/users`
- 请求方法：GET
- 请求头：需要携带 token
- 请求参数：
  ```
  page: number      // 页码，从1开始
  size: number      // 每页数量
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "total": "number",      // 总记录数
      "pages": "number",      // 总页数
      "current": "number",    // 当前页码
      "size": "number",       // 每页数量
      "records": [            // 用户列表
        {
          "userId": "number",
          "username": "string",
          "email": "string",
          "role": "string",
          "creditScore": "number",
          "balance": "number",
          "createTime": "string",
          "updateTime": "string"
        }
      ]
    }
  }
  ```

### 2. 获取用户详情
- 请求路径：`/users/{userId}`
- 请求方法：GET
- 请求头：需要携带 token
- 路径参数：
  - userId: number  // 用户ID
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": "number",
      "username": "string",
      "email": "string",
      "role": "string",
      "creditScore": "number",
      "balance": "number",
      "createTime": "string",
      "updateTime": "string"
    }
  }
  ```

### 3. 更新用户信息
- 请求路径：`/users/{userId}`
- 请求方法：PUT
- 请求头：需要携带 token
- 路径参数：
  - userId: number  // 用户ID
- 请求参数：
  ```json
  {
    "email": "string",        // 邮箱
    "phone": "string",        // 手机号（可选）
    "avatar": "string"        // 头像URL（可选）
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "userId": "number",
      "username": "string",
      "email": "string",
      "phone": "string",
      "avatar": "string",
      "role": "string",
      "creditScore": "number",
      "balance": "number",
      "updateTime": "string"
    }
  }
  ```

### 4. 修改密码
- 请求路径：`/users/password`
- 请求方法：PUT
- 请求头：需要携带 token
- 请求参数：
  ```json
  {
    "oldPassword": "string",  // 旧密码
    "newPassword": "string"   // 新密码
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "密码修改成功",
    "data": null
  }
  ```

### 5. 更新用户信用分（管理员）
- 请求路径：`/admin/users/{userId}/credit-score`
- 请求方法：PUT
- 请求头：需要携带 token
- 路径参数：
  - userId: number  // 用户ID
- 请求参数：
  ```json
  {
    "score": "number"  // 信用分变动值（正数增加，负数减少）
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "信用分更新成功",
    "data": {
      "userId": "number",
      "username": "string",
      "creditScore": "number"
    }
  }
  ```

### 6. 更新用户余额（管理员）
- 请求路径：`/admin/users/{userId}/balance`
- 请求方法：PUT
- 请求头：需要携带 token
- 路径参数：
  - userId: number  // 用户ID
- 请求参数：
  ```json
  {
    "amount": "number"  // 余额变动值（正数增加，负数减少）
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "余额更新成功",
    "data": {
      "userId": "number",
      "username": "string",
      "balance": "number"
    }
  }
  ```

## 业务功能接口

### 1. 获取用户交易记录
- 请求路径：`/transactions`
- 请求方法：GET
- 请求头：需要携带 token
- 请求参数：
  ```
  page: number      // 页码，从1开始
  size: number      // 每页数量
  type: string      // 交易类型（可选）：INCOME/EXPENSE
  startTime: string // 开始时间（可选）：yyyy-MM-dd HH:mm:ss
  endTime: string   // 结束时间（可选）：yyyy-MM-dd HH:mm:ss
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "total": "number",      // 总记录数
      "pages": "number",      // 总页数
      "current": "number",    // 当前页码
      "size": "number",       // 每页数量
      "records": [            // 交易记录列表
        {
          "transactionId": "number",
          "userId": "number",
          "type": "string",   // INCOME/EXPENSE
          "amount": "number",
          "description": "string",
          "createTime": "string"
        }
      ]
    }
  }
  ```

### 2. 获取用户信用记录
- 请求路径：`/credit-records`
- 请求方法：GET
- 请求头：需要携带 token
- 请求参数：
  ```
  page: number      // 页码，从1开始
  size: number      // 每页数量
  type: string      // 变动类型（可选）：INCREASE/DECREASE
  startTime: string // 开始时间（可选）：yyyy-MM-dd HH:mm:ss
  endTime: string   // 结束时间（可选）：yyyy-MM-dd HH:mm:ss
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "total": "number",      // 总记录数
      "pages": "number",      // 总页数
      "current": "number",    // 当前页码
      "size": "number",       // 每页数量
      "records": [            // 信用记录列表
        {
          "recordId": "number",
          "userId": "number",
          "type": "string",   // INCREASE/DECREASE
          "score": "number",
          "reason": "string",
          "createTime": "string"
        }
      ]
    }
  }
  ```

### 3. 获取用户统计信息
- 请求路径：`/statistics`
- 请求方法：GET
- 请求头：需要携带 token
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalIncome": "number",    // 总收入
      "totalExpense": "number",   // 总支出
      "currentBalance": "number", // 当前余额
      "creditScore": "number",    // 当前信用分
      "monthlyStats": [           // 月度统计
        {
          "month": "string",      // 月份：yyyy-MM
          "income": "number",     // 收入
          "expense": "number"     // 支出
        }
      ]
    }
  }
  ```

### 4. 获取系统公告
- 请求路径：`/announcements`
- 请求方法：GET
- 请求头：需要携带 token
- 请求参数：
  ```
  page: number      // 页码，从1开始
  size: number      // 每页数量
  type: string      // 公告类型（可选）：SYSTEM/NOTICE/WARNING
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "total": "number",      // 总记录数
      "pages": "number",      // 总页数
      "current": "number",    // 当前页码
      "size": "number",       // 每页数量
      "records": [            // 公告列表
        {
          "announcementId": "number",
          "title": "string",
          "content": "string",
          "type": "string",   // SYSTEM/NOTICE/WARNING
          "createTime": "string",
          "expireTime": "string"
        }
      ]
    }
  }
  ```

### 5. 获取用户消息
- 请求路径：`/messages`
- 请求方法：GET
- 请求头：需要携带 token
- 请求参数：
  ```
  page: number      // 页码，从1开始
  size: number      // 每页数量
  read: boolean     // 是否已读（可选）
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "total": "number",      // 总记录数
      "pages": "number",      // 总页数
      "current": "number",    // 当前页码
      "size": "number",       // 每页数量
      "records": [            // 消息列表
        {
          "messageId": "number",
          "title": "string",
          "content": "string",
          "type": "string",   // SYSTEM/TRANSACTION/CREDIT
          "read": "boolean",
          "createTime": "string"
        }
      ]
    }
  }
  ```

### 6. 标记消息为已读
- 请求路径：`/messages/{messageId}/read`
- 请求方法：PUT
- 请求头：需要携带 token
- 路径参数：
  - messageId: number  // 消息ID
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "标记成功",
    "data": null
  }
  ```

## 前端开发建议
1. 页面布局建议：
   - 使用 Element Plus 的 Layout 组件进行整体布局
   - 顶部导航栏显示用户信息和主要功能入口
   - 左侧菜单栏用于功能导航
   - 主内容区域使用卡片式布局

2. 状态管理建议：
   - 使用 Pinia 进行状态管理
   - 将用户信息、认证状态等全局状态存储在 store 中
   - 实现持久化存储，避免刷新页面后状态丢失

3. 路由配置建议：
   - 实现路由懒加载
   - 配置路由守卫，处理权限控制
   - 使用路由元信息配置页面标题和权限要求

4. 组件开发建议：
   - 将通用组件抽离为独立组件
   - 使用 TypeScript 定义接口和类型
   - 实现组件的响应式设计

5. 样式开发建议：
   - 使用 SCSS 预处理器
   - 定义全局变量和混入
   - 实现主题切换功能

## 系统配置和部署

### 1. 环境配置
#### 1.1 开发环境配置
在 `application-dev.yml` 中配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mygem?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
  security:
    jwt:
      secret: your-secret-key
      expiration: 3600000  # 1小时

logging:
  level:
    root: INFO
    com.coco.mygem: DEBUG
```

#### 1.2 生产环境配置
在 `application-prod.yml` 中配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://your-production-db:3306/mygem?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration: 3600000

logging:
  level:
    root: WARN
    com.coco.mygem: INFO
```

### 2. 数据库配置
#### 2.1 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE mygem DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE mygem;

-- 创建用户表
CREATE TABLE user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    credit_score INT NOT NULL DEFAULT 100,
    balance BIGINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

-- 创建交易记录表
CREATE TABLE transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount BIGINT NOT NULL,
    description VARCHAR(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 创建信用记录表
CREATE TABLE credit_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    score INT NOT NULL,
    reason VARCHAR(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 创建系统公告表
CREATE TABLE announcement (
    announcement_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expire_time DATETIME
);

-- 创建用户消息表
CREATE TABLE message (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    read_status TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
```

### 3. 部署说明
#### 3.1 后端部署
1. 打包应用：
```bash
mvn clean package -DskipTests
```

2. 运行应用：
```bash
java -jar target/mygem-1.0.0.jar --spring.profiles.active=prod
```

#### 3.2 前端部署
1. 安装依赖：
```bash
cd frontend
npm install
```

2. 构建生产版本：
```bash
npm run build
```

3. 部署构建文件：
将 `dist` 目录下的文件部署到 Web 服务器（如 Nginx）

#### 3.3 Nginx 配置示例
```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 4. 监控和维护
#### 4.1 日志管理
- 系统日志：`logs/system.log`
- 错误日志：`logs/error.log`
- 业务日志：`logs/business.log`

#### 4.2 性能监控
- 使用 Spring Boot Actuator 监控应用状态
- 使用 Prometheus + Grafana 监控系统性能

#### 4.3 备份策略
- 数据库每日凌晨自动备份
- 日志文件按天归档
- 配置文件定期备份

### 5. 安全建议
1. 生产环境必须使用 HTTPS
2. 定期更新依赖包版本
3. 使用强密码策略
4. 实现登录失败次数限制
5. 敏感信息使用环境变量配置

## 更新日志
### v1.0.0 (2024-03-31)
- 初始版本发布
- 实现用户认证和授权
- 实现用户管理功能
- 实现交易和信用记录功能
- 实现系统公告和消息功能

-- 创建数据库
CREATE DATABASE IF NOT EXISTS mygem DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mygem;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '角色：ROLE_USER/ROLE_ADMIN',
    credit_score INT NOT NULL DEFAULT 100 COMMENT '信用分',
    balance BIGINT NOT NULL DEFAULT 0 COMMENT '余额（单位：分）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交易ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type VARCHAR(20) NOT NULL COMMENT '交易类型：INCOME-收入，EXPENSE-支出',
    amount BIGINT NOT NULL COMMENT '交易金额（单位：分）',
    category VARCHAR(50) COMMENT '交易类别',
    description VARCHAR(255) COMMENT '交易描述',
    status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '交易状态：SUCCESS-成功，FAILED-失败，PENDING-待处理',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 信用记录表
CREATE TABLE IF NOT EXISTS credit_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type VARCHAR(20) NOT NULL COMMENT '变动类型：INCREASE-增加，DECREASE-减少',
    score INT NOT NULL COMMENT '变动分数',
    reason VARCHAR(255) NOT NULL COMMENT '变动原因',
    operator_id BIGINT COMMENT '操作人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (operator_id) REFERENCES user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='信用记录表';

-- 系统公告表
CREATE TABLE IF NOT EXISTS announcement (
    announcement_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    type VARCHAR(20) NOT NULL COMMENT '公告类型：SYSTEM-系统公告，NOTICE-普通通知，WARNING-警告',
    priority INT NOT NULL DEFAULT 0 COMMENT '优先级：0-普通，1-重要，2-紧急',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-未发布，1-已发布，2-已过期',
    start_time DATETIME COMMENT '生效时间',
    expire_time DATETIME COMMENT '过期时间',
    create_by BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (create_by) REFERENCES user(user_id),
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 用户消息表
CREATE TABLE IF NOT EXISTS message (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL COMMENT '消息标题',
    content TEXT NOT NULL COMMENT '消息内容',
    type VARCHAR(20) NOT NULL COMMENT '消息类型：SYSTEM-系统消息，TRANSACTION-交易消息，CREDIT-信用消息',
    priority INT NOT NULL DEFAULT 0 COMMENT '优先级：0-普通，1-重要，2-紧急',
    read_status TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    read_time DATETIME COMMENT '阅读时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_read_status (read_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_desc VARCHAR(255) NOT NULL COMMENT '操作描述',
    operation_method VARCHAR(255) COMMENT '操作方法',
    operation_params TEXT COMMENT '操作参数',
    operation_result TEXT COMMENT '操作结果',
    operation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(255) COMMENT '用户代理',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_operation_time (operation_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    config_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT NOT NULL COMMENT '配置值',
    config_desc VARCHAR(255) COMMENT '配置描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 角色表
CREATE TABLE roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL
);

-- 帖子表
CREATE TABLE posts (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    tech_tags VARCHAR(255),
    budget DECIMAL(20,2),
    target_amount DECIMAL(20,2),
    raised_amount DECIMAL(20,2) DEFAULT 0.00,
    progress INT DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    github_repo VARCHAR(255),
    github_branch VARCHAR(50),
    create_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 投资表
CREATE TABLE investments (
    investment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time BIGINT NOT NULL,
    confirm_time BIGINT,
    return_rate DECIMAL(5,2),
    description TEXT,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 审核历史表
CREATE TABLE review_history (
    review_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    review_comment TEXT,
    review_time BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id)
);

-- 用户行为记录表
CREATE TABLE user_behaviors (
    behavior_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    behavior_type VARCHAR(20) NOT NULL,
    create_time BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

-- 用户兴趣标签表
CREATE TABLE user_interests (
    interest_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    weight INT DEFAULT 1,
    create_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 审核规则表
CREATE TABLE review_rules (
    rule_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(50) NOT NULL,
    rule_value TEXT NOT NULL,
    description TEXT,
    create_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL
); 
-- 用户表
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    github_username VARCHAR(50),
    github_id BIGINT,
    github_avatar VARCHAR(255),
    github_bio TEXT,
    skill_tags VARCHAR(255),
    credit_score INT DEFAULT 100,
    balance DECIMAL(20,2) DEFAULT 0.00,
    create_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

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
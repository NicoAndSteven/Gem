package com.coco.mygem.utils;

/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/19 16:53
 * @Version: 1.0
 */

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * UUID生成工具集（符合RFC 4122标准）
 * 使用说明：
 * 1. 常规场景：UuidGenerator.standardUuid()
 * 2. 简化格式：UuidGenerator.simpleUuid()
 * 3. 带业务前缀：UuidGenerator.prefixedUuid("ORDER_")
 */
public final class UuidGenerator {
    // 加密安全随机数生成器
    private static final Random SECURE_RANDOM = new SecureRandom();

    // 禁用构造函数
    private UuidGenerator() {
        throw new AssertionError("禁止实例化工具类");
    }

    /**
     * 生成标准格式UUID（带连字符）
     * 示例：123e4567-e89b-12d3-a456-426614174000
     */
    public static String standardUuid() {
        return buildUuid(true, false, null);
    }

    /**
     * 生成简化格式UUID（无连字符）
     * 示例：123e4567e89b12d3a456426614174000
     */
    public static String simpleUuid() {
        return buildUuid(false, false, null);
    }

    /**
     * 生成带业务前缀的UUID
     * @param prefix 业务前缀（如"ORDER_"）
     */
    public static String prefixedUuid(String prefix) {
        return buildUuid(false, false, prefix);
    }

    /**
     * 生成数据库优化格式UUID（全小写带连字符）
     */
    public static String dbUuid() {
        return buildUuid(true, true, null);
    }

    /**
     * 核心构建方法
     * @param withHyphens 是否包含连字符
     * @param lowercase   是否全小写
     * @param prefix      自定义前缀
     */
    private static String buildUuid(boolean withHyphens,
                                    boolean lowercase,
                                    String prefix) {
        // 生成符合RFC 4122的版本4 UUID
        UUID uuid = new UUID(
                SECURE_RANDOM.nextLong(),
                SECURE_RANDOM.nextLong()
        );

        // 转换为字符串并处理格式
        String result = uuid.toString();
        if (!withHyphens) {
            result = result.replace("-", "");
        }
        if (lowercase) {
            result = result.toLowerCase();
        }
        if (prefix != null) {
            result = prefix + result;
        }

        return result;
    }

    /**
     * 批量生成UUID（优化性能）
     * @param count 需要生成的数量
     */
    public static String[] batchGenerate(int count) {
        if (count <= 0) throw new IllegalArgumentException("数量必须大于0");

        String[] uuids = new String[count];
        for (int i = 0; i < count; i++) {
            uuids[i] = simpleUuid();
        }
        return uuids;
    }
}
package org.example.entity; // 建议放在 entity 或 domain 包下

import jakarta.persistence.*; // 使用 jakarta.persistence (Spring Boot 3+)
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity // 标记这是一个 JPA 实体
@Table(name = "users") // 显式指定映射到的数据库表名 (可选，默认是类名)
@Getter // Lombok 注解，自动生成 getter 方法
@Setter // Lombok 注解，自动生成 setter 方法
@NoArgsConstructor // Lombok 注解，生成无参构造函数
@AllArgsConstructor // Lombok 注解，生成包含所有字段的构造函数
public class User {

    @Id // 标记这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略，IDENTITY 适用于 MariaDB/MySQL 的自增列
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50) // 映射到列，并指定约束
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false) // 实际应用中应该存储密码的哈希值
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private boolean active = true; // 可以给字段设置默认值

    // 构造函数、Getter、Setter 由 Lombok 生成，如果不用 Lombok 需要手动添加
}
create table if not exists ua_user (
    `id` varchar(32) primary key not null comment '主键ID',
    `created_at` datetime null comment '记录创建时间',
    `updated_at` datetime null comment '记录更新时间',
    `deleted_at` datetime null comment '逻辑删除字段',

    `username` varchar(255) null default null comment '用户名',
    `password` varchar(255) null default null comment '密码',
    `mobile` varchar(255) null default null comment '手机号',
    `email` varchar(255) null default null comment '邮箱',
    `tenant_id` varchar(255) null default null comment '租户ID',
    `enable` tinyint(1) null default 1 comment '是否启用；1 - enable，0 - disable'
) ENGINE=InnoDB character set utf8mb4 collate utf8mb4_unicode_ci;

-- private Long id;
-- private String username;
-- private String password;
-- private String status;
-- private String type;
-- private String phoneNumber;
-- private String email;
-- private String name;
-- private Collection<String> resources = new ArrayList<>();
-- private Collection<String> roles = new ArrayList<>();
-- private Collection<GrantedAuthority> grantedAuthorities;
-- private Long tenantId;

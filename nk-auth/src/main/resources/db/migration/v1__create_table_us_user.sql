create table if not exists nk_uc.ua_user (
    id varchar(32) primary key not null,
    created_at timestamp null default current_timestamp::timestamp,
    updated_at timestamp null default current_timestamp::timestamp,
    deleted_at timestamp null,

    username varchar(255) null default null,
    password varchar(255) null default null,
    mobile varchar(255) null default null,
    email varchar(255) null default null,
    tenant_id varchar(255) null default null,
    enable smallint null default 1
);

COMMENT ON TABLE nk_uc.ua_user IS '用户信息表';
COMMENT ON COLUMN nk_uc.ua_user.id IS '主键';
COMMENT ON COLUMN nk_uc.ua_user.created_at IS '记录创建时间';
COMMENT ON COLUMN nk_uc.ua_user.updated_at IS '记录更新时间';
COMMENT ON COLUMN nk_uc.ua_user.deleted_at IS '逻辑删除字段';

COMMENT ON COLUMN nk_uc.ua_user.username IS '用户名';
COMMENT ON COLUMN nk_uc.ua_user.password IS '密码';
COMMENT ON COLUMN nk_uc.ua_user.mobile IS '手机号';
COMMENT ON COLUMN nk_uc.ua_user.email IS '邮箱';
COMMENT ON COLUMN nk_uc.ua_user.tenant_id IS '租户ID';
COMMENT ON COLUMN nk_uc.ua_user.enable IS '是否启用；1 - enable，0 - disable';

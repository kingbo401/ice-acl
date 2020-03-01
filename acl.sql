SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `app`
-- ----------------------------
CREATE TABLE `app` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '应用key，唯一',
  `app_secret` varchar(256) NOT NULL COMMENT '应用秘钥',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(256) NULL COMMENT '应用描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appkey` (`app_key`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用表';

-- ----------------------------
--  Table structure for `menu`
-- ----------------------------
CREATE TABLE `menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '应用appkey',
  `menu_key` varchar(64) NOT NULL COMMENT '菜单key',
  `target` varchar(64) NULL COMMENT 'url跳转方式',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `subgroup` varchar(64) NOT NULL COMMENT '分组',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单父id，一级菜单父id为0',
  `url` varchar(256) NOT NULL COMMENT '菜单url',
  `icon` varchar(256) DEFAULT NULL COMMENT '菜单图标url',
  `show_type` tinyint(50) NOT NULL DEFAULT '0' COMMENT '菜单显示类型 1 普通页面 2 tab页 3 hidden',
  `idx` int(11) NOT NULL DEFAULT '0' COMMENT '排序，从小到大',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appkey_menukey_subgroup` (`app_key`,`menu_key`,`subgroup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';
-- ----------------------------
--  Table structure for `menu_permission_ref`
-- ----------------------------
CREATE TABLE `menu_permission_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 ',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_permission` (`menu_id`,`permission_id`),
  KEY `idx_permission` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限关联表';
-- ----------------------------
--  Table structure for `permission`
-- ----------------------------
CREATE TABLE `permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '应用appkey',
  `permission_key` varchar(255) NOT NULL COMMENT '权限建，同一个应用内要求唯一',
  `subgroup` varchar(64) NOT NULL COMMENT '分组',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_permission` (`app_key`,`permission_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限实体表';

-- ----------------------------
--  Table structure for `permission_group`
-- ----------------------------
CREATE TABLE `permission_group` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT 'Acl appkey',
  `tenant` varchar(64) NOT NULL COMMENT '租户id，表示此权限组属于哪个租户',
  `pid` bigint(11) NOT NULL DEFAULT '0' COMMENT '权限组父id',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(128) DEFAULT NULL COMMENT '权限组描述',
  `idx` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `subgroup` varchar(64) NOT NULL COMMENT '分组',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_appkey_tenant` (`app_key`,`tenant`),
  KEY `idx_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `permission_group_ref`
-- ----------------------------
CREATE TABLE `permission_group_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '权限组id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `status` tinyint(1) NOT NULL COMMENT '0 删除 1正常  2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_permission` (`group_id`,`permission_id`),
  KEY `idx_permission` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `role`
-- ----------------------------
CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '应用appkey',
  `role_key` varchar(64) NOT NULL COMMENT '角色ID',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(255) NOT NULL COMMENT '角色描述',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `subgroup` varchar(64) NOT NULL COMMENT '分组',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_appkey_rolekey` (`app_key`,`role_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
--  Table structure for `role_permission_ref`
-- ----------------------------
CREATE TABLE `role_permission_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常  2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey` (`role_id`,`permission_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

-- ----------------------------
--  Table structure for `role_menu_ref`
-- ----------------------------
CREATE TABLE `role_menu_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `status` tinyint(4) DEFAULT '1',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_roleid_menuid` (`role_id`,`menu_id`) USING BTREE,
  KEY `idx_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

-- ----------------------------
--  Table structure for `role_permission_group_ref`
-- ----------------------------
CREATE TABLE `role_permission_group_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `group_id` bigint(20) NOT NULL COMMENT '权限组id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-删除;1-正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_group` (`role_id`,`group_id`),
  KEY `idx_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限组关联表';

-- ----------------------------
--  Table structure for `user_permission_group_ref`
-- ----------------------------
CREATE TABLE `user_permission_group_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `group_id` bigint(20) NOT NULL COMMENT '权限组id',
  `tenant` varchar(64) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userid_group_tenant` (`user_id`,`group_id`,`tenant`),
  KEY `idx_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限组关联表';

-- ----------------------------
--  Table structure for `user_permission_ref`
-- ----------------------------
CREATE TABLE `user_permission_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常  2封禁',
  `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_permission_tenant` (`user_id`,`permission_id`,`tenant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限关联表';

-- ----------------------------
--  Table structure for `user_role_grant_log`
-- ----------------------------
CREATE TABLE `user_role_grant_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT '应用appkey',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `admin_user_id` varchar(64) NOT NULL COMMENT '授权者用户id',
  `granted_user_id` varchar(64) NOT NULL COMMENT '被授权者用户id',
  `role_ids_pre` varchar(1024) DEFAULT NULL COMMENT '授权前的角色id，用逗号分隔',
  `role_ids_after` varchar(1024) DEFAULT NULL COMMENT '授权后的角色id，用逗号分隔',
  `role_info_pre` varchar(2048) DEFAULT NULL COMMENT '授权前的角色描述信息',
  `role_info_after` varchar(2048) DEFAULT NULL COMMENT '授权后的角色描述信息',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `grant_type` int(11) NOT NULL COMMENT '0删除 1新增 2修改',
  PRIMARY KEY (`id`),
  KEY `idx_appkey_tenant` (`app_key`,`tenant`),
  KEY `idx_admin_user_id` (`admin_user_id`),
  KEY `idx_granted_user_id` (`granted_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='给用户授予角色记录表';

-- ----------------------------
--  Table structure for `user_role_ref`
-- ----------------------------
CREATE TABLE `user_role_ref` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `status` tinyint(1) DEFAULT NULL COMMENT '0 删除 1正常 2封禁',
  `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role_tenant` (`user_id`,`role_id`,`tenant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

-- ----------------------------
--  Table structure for `data_grant_record`
-- ----------------------------
CREATE TABLE `data_grant_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(128) NOT NULL COMMENT '授权应用key',
  `model_id` bigint(20) NOT NULL COMMENT '数据模型id',
  `grant_target_id` varchar(128) NOT NULL COMMENT '授权目标id',
  `grant_target_type` tinyint(50) NOT NULL COMMENT '授权目标类型 1 用户 2角色 3权限组',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `operation_codes` varchar(1024) NOT NULL COMMENT '操作列表,json',
  `property_rules` varchar(15000) NOT NULL COMMENT '属性规则,json',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常',
  `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_grant` (`model_id`,`tenant`,`grant_target_id`,`grant_target_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据权限授权表';

-- ----------------------------
--  Table structure for `data_model`
-- ----------------------------
CREATE TABLE `data_model` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT 'app表里的appkey',
  `code` varchar(128) NOT NULL COMMENT '数据模型码,全局唯一，用于验权',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(512) NULL COMMENT '数据模型描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据模型';

-- ----------------------------
--  Table structure for `data_operation`
-- ----------------------------
CREATE TABLE `data_operation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model_id` bigint(20) NOT NULL COMMENT '数据模型id',
  `code` varchar(128) NOT NULL COMMENT '操作编码，用于验权',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(512) DEFAULT NULL COMMENT '说明',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_model_code` (`model_id`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据操作表';

-- ----------------------------
--  Table structure for `data_property`
-- ----------------------------
CREATE TABLE `data_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model_id` bigint(20) NOT NULL COMMENT '数据模型id',
  `code` varchar(128) NOT NULL DEFAULT '' COMMENT '编码，用于验权',
  `data_type` varchar(64) NOT NULL DEFAULT '' COMMENT '数据类型',
  `config` varchar(2048) NOT NULL DEFAULT '' COMMENT '字段配置',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `name_en` varchar(64) NULL COMMENT '英文名',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 删除 1正常 2封禁',
  `default_access_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 不能访问 1可访问',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_modelid_code` (`model_id`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据属性';

-- ----------------------------
--  Table structure for `data_property_access`
-- ----------------------------
CREATE TABLE `data_property_access` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_key` varchar(64) NOT NULL COMMENT 'app唯一建',
  `model_id` bigint(20) NOT NULL COMMENT '模型id',
  `property_id` bigint(20) NOT NULL COMMENT '属性id',
  `grant_target_id` varchar(128) NOT NULL COMMENT '被禁止访问的目标id',
  `grant_target_type` tinyint(1) NOT NULL COMMENT '被禁止访问的目标类型  1 用户 2角色 3权限组',
  `tenant` varchar(64) NOT NULL COMMENT '租户id',
  `access_type` int(11) NOT NULL DEFAULT '1' COMMENT '0 不能访问 1可访问',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0删除 1正常',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_idx` (`app_key`,`model_id`,`property_id`,`grant_target_type`,`tenant`,`grant_target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模型字段访问控制表';


SET FOREIGN_KEY_CHECKS = 1;

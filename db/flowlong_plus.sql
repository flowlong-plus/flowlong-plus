SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flw_ext_instance
-- ----------------------------
DROP TABLE IF EXISTS `flw_ext_instance`;
CREATE TABLE `flw_ext_instance` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `process_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `model_content` text COMMENT '流程模型定义JSON内容',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `fk_ext_instance_id` FOREIGN KEY (`id`) REFERENCES `flw_his_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='扩展流程实例表';

-- ----------------------------
-- Records of flw_ext_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_his_instance
-- ----------------------------
DROP TABLE IF EXISTS `flw_his_instance`;
CREATE TABLE `flw_his_instance` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `create_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) NOT NULL COMMENT '创建人名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `process_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `parent_instance_id` bigint(20) DEFAULT NULL COMMENT '父流程实例ID',
  `priority` tinyint(1) DEFAULT NULL COMMENT '优先级',
  `instance_no` varchar(50) DEFAULT NULL COMMENT '流程实例编号',
  `business_key` varchar(100) DEFAULT NULL COMMENT '业务KEY',
  `variable` text COMMENT '变量json',
  `current_node` varchar(100) NOT NULL COMMENT '当前所在节点',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '期望完成时间',
  `last_update_by` varchar(50) DEFAULT NULL COMMENT '上次更新人',
  `last_update_time` timestamp NULL DEFAULT NULL COMMENT '上次更新时间',
  `instance_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0，审批中 1，审批通过 2，审批拒绝 3，撤销审批 4，超时结束 5，强制终止',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `duration` bigint(20) DEFAULT NULL COMMENT '处理耗时',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_his_instance_process_id` (`process_id`) USING BTREE,
  CONSTRAINT `fk_his_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `flw_process` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='历史流程实例表';

-- ----------------------------
-- Records of flw_his_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_his_task
-- ----------------------------
DROP TABLE IF EXISTS `flw_his_task`;
CREATE TABLE `flw_his_task` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `create_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) NOT NULL COMMENT '创建人名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `instance_id` bigint(20) NOT NULL COMMENT '流程实例ID',
  `parent_task_id` bigint(20) DEFAULT NULL COMMENT '父任务ID',
  `call_process_id` bigint(20) DEFAULT NULL COMMENT '调用外部流程定义ID',
  `call_instance_id` bigint(20) DEFAULT NULL COMMENT '调用外部流程实例ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `display_name` varchar(200) NOT NULL COMMENT '任务显示名称',
  `task_type` tinyint(1) NOT NULL COMMENT '任务类型',
  `perform_type` tinyint(1) DEFAULT NULL COMMENT '参与类型',
  `action_url` varchar(200) DEFAULT NULL COMMENT '任务处理的url',
  `variable` json DEFAULT NULL COMMENT '变量json',
  `assignor_id` varchar(100) DEFAULT NULL COMMENT '委托人ID',
  `assignor` varchar(255) DEFAULT NULL COMMENT '委托人',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '任务期望完成时间',
  `remind_time` timestamp NULL DEFAULT NULL COMMENT '提醒时间',
  `remind_repeat` tinyint(1) NOT NULL DEFAULT '0' COMMENT '提醒次数',
  `viewed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已阅 0，否 1，是',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '任务完成时间',
  `task_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '任务状态 0，活动 1，跳转 2，完成 3，拒绝 4，撤销审批  5，超时 6，终止 7，驳回终止',
  `duration` bigint(20) DEFAULT NULL COMMENT '处理耗时',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_his_task_instance_id` (`instance_id`) USING BTREE,
  KEY `idx_his_task_parent_task_id` (`parent_task_id`) USING BTREE,
  CONSTRAINT `fk_his_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `flw_his_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='历史任务表';

-- ----------------------------
-- Records of flw_his_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_his_task_actor
-- ----------------------------
DROP TABLE IF EXISTS `flw_his_task_actor`;
CREATE TABLE `flw_his_task_actor` (
  `id` bigint(20) NOT NULL COMMENT '主键 ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `instance_id` bigint(20) NOT NULL COMMENT '流程实例ID',
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `actor_id` varchar(100) NOT NULL COMMENT '参与者ID',
  `actor_name` varchar(100) NOT NULL COMMENT '参与者名称',
  `actor_type` int(11) NOT NULL COMMENT '参与者类型 0，用户 1，角色 2，部门',
  `weight` int(11) DEFAULT NULL COMMENT '权重，票签任务时，该值为不同处理人员的分量比例，代理任务时，该值为 1 时为代理人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_his_task_actor_task_id` (`task_id`) USING BTREE,
  CONSTRAINT `fk_his_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `flw_his_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='历史任务参与者表';

-- ----------------------------
-- Records of flw_his_task_actor
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_instance
-- ----------------------------
DROP TABLE IF EXISTS `flw_instance`;
CREATE TABLE `flw_instance` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `create_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) NOT NULL COMMENT '创建人名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `process_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `parent_instance_id` bigint(20) DEFAULT NULL COMMENT '父流程实例ID',
  `priority` tinyint(1) DEFAULT NULL COMMENT '优先级',
  `instance_no` varchar(50) DEFAULT NULL COMMENT '流程实例编号',
  `business_key` varchar(100) DEFAULT NULL COMMENT '业务KEY',
  `variable` text COMMENT '变量json',
  `current_node` varchar(100) NOT NULL COMMENT '当前所在节点',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '期望完成时间',
  `last_update_by` varchar(50) DEFAULT NULL COMMENT '上次更新人',
  `last_update_time` timestamp NULL DEFAULT NULL COMMENT '上次更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_instance_process_id` (`process_id`) USING BTREE,
  CONSTRAINT `fk_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `flw_process` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程实例表';

-- ----------------------------
-- Records of flw_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_process
-- ----------------------------
DROP TABLE IF EXISTS `flw_process`;
CREATE TABLE `flw_process` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `create_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) NOT NULL COMMENT '创建人名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `process_key` varchar(100) NOT NULL COMMENT '流程定义 key 唯一标识',
  `process_name` varchar(100) NOT NULL COMMENT '流程定义名称',
  `process_icon` varchar(255) DEFAULT NULL COMMENT '流程图标地址',
  `process_type` varchar(100) DEFAULT NULL COMMENT '流程类型',
  `process_version` int(11) NOT NULL DEFAULT '1' COMMENT '流程版本，默认 1',
  `instance_url` varchar(200) DEFAULT NULL COMMENT '实例地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注说明',
  `use_scope` tinyint(1) NOT NULL DEFAULT '0' COMMENT '使用范围 0，全员 1，指定人员（业务关联） 2，均不可提交',
  `process_state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '流程状态 0，不可用 1，可用',
  `model_content` text COMMENT '流程模型定义JSON内容',
  `sort` tinyint(1) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_process_name` (`process_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程定义表';

-- ----------------------------
-- Records of flw_process
-- ----------------------------
BEGIN;
INSERT INTO `flw_process` (`id`, `tenant_id`, `create_id`, `create_by`, `create_time`, `process_key`, `process_name`, `process_icon`, `process_type`, `process_version`, `instance_url`, `remark`, `use_scope`, `process_state`, `model_content`, `sort`) VALUES (1800216215284391937, '1', '10003', 'admin', '2024-06-10 13:19:27', 'YtYSSC', '未命名流程', NULL, NULL, 1, '', NULL, 0, 1, '{\"name\":\"未命名流程\",\"key\":\"YtYSSC\",\"instanceUrl\":\"\",\"nodeConfig\":{\"nodeName\":\"发起人\",\"callProcessKey\":null,\"actionUrl\":null,\"type\":0,\"setType\":null,\"nodeAssigneeList\":[],\"examineLevel\":null,\"directorLevel\":null,\"selectMode\":null,\"term\":null,\"termMode\":null,\"examineMode\":null,\"directorMode\":null,\"passWeight\":null,\"conditionNodes\":null,\"remind\":null,\"allowSelection\":null,\"allowTransfer\":null,\"allowAppendNode\":null,\"allowRollback\":null,\"approveSelf\":null,\"extendConfig\":null,\"childNode\":{\"nodeName\":\"结束\",\"callProcessKey\":null,\"actionUrl\":null,\"type\":-1,\"setType\":null,\"nodeAssigneeList\":[],\"examineLevel\":null,\"directorLevel\":null,\"selectMode\":null,\"term\":null,\"termMode\":null,\"examineMode\":null,\"directorMode\":null,\"passWeight\":null,\"conditionNodes\":null,\"remind\":null,\"allowSelection\":null,\"allowTransfer\":null,\"allowAppendNode\":null,\"allowRollback\":null,\"approveSelf\":null,\"extendConfig\":null,\"childNode\":null,\"parentNode\":null},\"parentNode\":null}}', 0);
COMMIT;

-- ----------------------------
-- Table structure for flw_task
-- ----------------------------
DROP TABLE IF EXISTS `flw_task`;
CREATE TABLE `flw_task` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `create_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) NOT NULL COMMENT '创建人名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `instance_id` bigint(20) NOT NULL COMMENT '流程实例ID',
  `parent_task_id` bigint(20) DEFAULT NULL COMMENT '父任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `display_name` varchar(200) NOT NULL COMMENT '任务显示名称',
  `task_type` tinyint(1) NOT NULL COMMENT '任务类型',
  `perform_type` tinyint(1) DEFAULT NULL COMMENT '参与类型',
  `action_url` varchar(200) DEFAULT NULL COMMENT '任务处理的url',
  `variable` json DEFAULT NULL COMMENT '变量json',
  `assignor_id` varchar(100) DEFAULT NULL COMMENT '委托人ID',
  `assignor` varchar(255) DEFAULT NULL COMMENT '委托人',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '任务期望完成时间',
  `remind_time` timestamp NULL DEFAULT NULL COMMENT '提醒时间',
  `remind_repeat` tinyint(1) NOT NULL DEFAULT '0' COMMENT '提醒次数',
  `viewed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已阅 0，否 1，是',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_task_instance_id` (`instance_id`) USING BTREE,
  CONSTRAINT `fk_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `flw_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务表';

-- ----------------------------
-- Records of flw_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for flw_task_actor
-- ----------------------------
DROP TABLE IF EXISTS `flw_task_actor`;
CREATE TABLE `flw_task_actor` (
  `id` bigint(20) NOT NULL COMMENT '主键 ID',
  `tenant_id` varchar(50) DEFAULT NULL COMMENT '租户ID',
  `instance_id` bigint(20) NOT NULL COMMENT '流程实例ID',
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `actor_id` varchar(100) NOT NULL COMMENT '参与者ID',
  `actor_name` varchar(100) NOT NULL COMMENT '参与者名称',
  `actor_type` int(11) NOT NULL COMMENT '参与者类型 0，用户 1，角色 2，部门',
  `weight` int(11) DEFAULT NULL COMMENT '权重，票签任务时，该值为不同处理人员的分量比例，代理任务时，该值为 1 时为代理人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_task_actor_task_id` (`task_id`) USING BTREE,
  CONSTRAINT `fk_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `flw_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务参与者表';

-- ----------------------------
-- Records of flw_task_actor
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` varchar(32) NOT NULL,
  `dept_name` varchar(64) NOT NULL COMMENT '部门名称',
  `level` int(11) NOT NULL COMMENT '部门层级数',
  `parent` varchar(32) DEFAULT NULL COMMENT '上级部门id',
  `deputy_leaders` text,
  `main_leader` varchar(32) DEFAULT NULL COMMENT '部门主管理员',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `is_enable` tinyint(1) DEFAULT '0' COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`dept_id`) USING BTREE,
  KEY `FK9E19DA6CFE1E12FB11` (`parent`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`dept_id`, `dept_name`, `level`, `parent`, `deputy_leaders`, `main_leader`, `order_num`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('12', '总部', 1, '0', NULL, NULL, 3, 1, '2024-01-20 11:18:29', '2024-05-23 13:52:20', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_file_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_detail`;
CREATE TABLE `sys_file_detail` (
  `id` varchar(32) NOT NULL COMMENT '文件id',
  `url` varchar(512) NOT NULL COMMENT '文件访问地址',
  `size` bigint(20) DEFAULT NULL COMMENT '文件大小，单位字节',
  `filename` varchar(256) DEFAULT NULL COMMENT '文件名称',
  `original_filename` varchar(256) DEFAULT NULL COMMENT '原始文件名',
  `base_path` varchar(256) DEFAULT NULL COMMENT '基础存储路径',
  `path` varchar(256) DEFAULT NULL COMMENT '存储路径',
  `ext` varchar(32) DEFAULT NULL COMMENT '文件扩展名',
  `content_type` varchar(128) DEFAULT NULL COMMENT 'MIME类型',
  `platform` varchar(32) DEFAULT NULL COMMENT '存储平台',
  `th_url` varchar(512) DEFAULT NULL COMMENT '缩略图访问路径',
  `th_filename` varchar(256) DEFAULT NULL COMMENT '缩略图名称',
  `th_size` bigint(20) DEFAULT NULL COMMENT '缩略图大小，单位字节',
  `th_content_type` varchar(128) DEFAULT NULL COMMENT '缩略图MIME类型',
  `object_id` varchar(32) DEFAULT NULL COMMENT '文件所属对象id',
  `object_type` varchar(32) DEFAULT NULL COMMENT '文件所属对象类型，例如用户头像，评价图片',
  `metadata` text COMMENT '文件元数据',
  `user_metadata` text COMMENT '文件用户元数据',
  `th_metadata` text COMMENT '缩略图元数据',
  `th_user_metadata` text COMMENT '缩略图用户元数据',
  `attr` text COMMENT '附加属性',
  `file_acl` varchar(32) DEFAULT NULL COMMENT '文件ACL',
  `th_file_acl` varchar(32) DEFAULT NULL COMMENT '缩略图文件ACL',
  `hash_info` text COMMENT '哈希信息',
  `upload_id` varchar(128) DEFAULT NULL COMMENT '上传ID，仅在手动分片上传时使用',
  `upload_status` int(11) DEFAULT NULL COMMENT '上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文件记录表';

-- ----------------------------
-- Records of sys_file_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_file_part_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_part_detail`;
CREATE TABLE `sys_file_part_detail` (
  `id` varchar(32) NOT NULL COMMENT '分片id',
  `platform` varchar(32) DEFAULT NULL COMMENT '存储平台',
  `upload_id` varchar(128) DEFAULT NULL COMMENT '上传ID，仅在手动分片上传时使用',
  `e_tag` varchar(255) DEFAULT NULL COMMENT '分片 ETag',
  `part_number` int(11) DEFAULT NULL COMMENT '分片号。每一个上传的分片都有一个分片号，一般情况下取值范围是1~10000',
  `part_size` bigint(20) DEFAULT NULL COMMENT '文件大小，单位字节',
  `hash_info` text COMMENT '哈希信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='文件分片信息表，仅在手动分片上传时使用';

-- ----------------------------
-- Records of sys_file_part_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `menu_name` varchar(255) DEFAULT '' COMMENT '菜单名称',
  `permission_code` varchar(255) DEFAULT '' COMMENT '权限码',
  `permission_name` varchar(255) DEFAULT '' COMMENT '本权限的中文释义',
  `menu_type` char(1) DEFAULT NULL COMMENT '0=目录;1=菜单;2=按钮',
  `open_type` varchar(16) DEFAULT NULL COMMENT 'self_=当前窗口;blank_=新窗口',
  `level` int(11) DEFAULT NULL COMMENT '层级',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级id',
  `route_path` varchar(255) DEFAULT NULL COMMENT '路由地址',
  `icon` varchar(200) DEFAULT NULL COMMENT '菜单图标',
  `hide_in_menu` tinyint(1) NOT NULL DEFAULT '0' COMMENT '菜单显示状态',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '菜单状态:0=正常;1=停用',
  `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
  `keep_alive` tinyint(1) DEFAULT '0' COMMENT '是否缓存',
  `is_frame` int(11) DEFAULT '1' COMMENT '是否为外链',
  `component` varchar(250) DEFAULT NULL COMMENT '组件路径',
  `active_menu` varchar(250) DEFAULT NULL COMMENT '选中路由名称',
  `fixed_index_in_tab` int(3) DEFAULT '0' COMMENT '是否固定多页签',
  `route_name` varchar(250) DEFAULT NULL COMMENT '路由名称',
  `href` varchar(1000) DEFAULT NULL COMMENT '外部链接地址',
  `title` varchar(32) DEFAULT NULL,
  `i18n_key` varchar(64) DEFAULT NULL,
  `local_icon` varchar(64) DEFAULT NULL,
  `multi_tab` tinyint(1) DEFAULT NULL,
  `constant` tinyint(1) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='权限菜单';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('958', '关于', '', '', '1', NULL, 1, '0', '/about', 'fluent:book-information-24-regular', 0, '1', 10, 0, 0, 'layout.base$view.about', NULL, NULL, 'about', NULL, '关于', '', NULL, NULL, NULL, '2024-02-26 12:42:27', '10003', '2024-02-26 14:44:57', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('959', '首页', '', '', '1', NULL, 1, '0', '/home', 'mdi:monitor-dashboard', 0, '1', 1, 0, 0, 'layout.base$view.home', NULL, NULL, 'home', NULL, '首页', NULL, NULL, NULL, NULL, '2024-02-26 13:14:49', '10003', '2024-02-26 13:14:49', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('960', '系统管理', '', '', '0', NULL, 1, '0', '/manage', 'carbon:cloud-service-management', 0, '1', 9, 0, 0, 'layout.base', NULL, NULL, 'manage', NULL, '系统管理', NULL, NULL, NULL, NULL, '2024-02-26 13:17:51', '10003', '2024-02-26 13:17:51', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('961', '用户管理', '', '', '1', NULL, 2, '960', '/manage/user', 'ic:round-manage-accounts', 0, '1', 9, 0, 0, 'view.manage_user', NULL, NULL, 'manage_user', NULL, '用户管理', NULL, NULL, NULL, NULL, '2024-02-26 13:21:03', '10003', '2024-02-26 13:21:03', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('963', '菜单管理', '', '', '1', NULL, 2, '960', '/manage/menu', 'material-symbols:route', 0, '1', 3, 0, 0, 'view.manage_menu', NULL, NULL, 'manage_menu', NULL, '菜单管理', NULL, NULL, NULL, NULL, '2024-02-26 13:26:51', '10003', '2024-02-26 13:26:51', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('969', '角色管理', '', '', '1', NULL, 2, '960', '/manage/role', 'carbon:user-role', 0, '1', 0, 0, 0, 'view.manage_role', NULL, NULL, 'manage_role', NULL, '角色管理', 'route.manage_role', '', NULL, NULL, '2024-02-26 22:43:49', '10003', '2024-02-26 22:43:49', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('970', '部门管理', '', '', '1', NULL, 2, '960', '/manage/dept', 'carbon:load-balancer-application', 0, '1', 3, 0, 0, 'view.manage_dept', NULL, NULL, 'manage_dept', NULL, '部门管理', NULL, '', NULL, NULL, '2024-02-27 10:39:45', '10003', '2024-02-27 10:39:45', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('971', '岗位管理', '', '', '1', NULL, 2, '960', '/manage/post', 'carbon:product', 0, '1', 5, 0, 0, 'view.manage_post', NULL, 3, 'manage_post', NULL, '岗位管理', NULL, '', NULL, NULL, '2024-02-27 11:55:02', '10003', '2024-02-27 11:55:02', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('972', '租户管理', '', '', '1', NULL, 2, '960', '/manage/tenant', 'carbon:building-insights-1', 0, '1', 4, 0, 0, 'view.manage_tenant', NULL, NULL, 'manage_tenant', NULL, '租户管理', NULL, '', NULL, NULL, '2024-02-27 14:57:10', '10003', '2024-02-27 14:57:10', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('976', '流程设计', '', '', '1', NULL, 1, '0', '/workflow/designer', 'bi:feather', 0, '1', 0, 0, 0, 'layout.base$view.workflow_designer', NULL, NULL, 'workflow-designer', NULL, '流程设计', NULL, '', NULL, NULL, '2024-05-22 11:34:53', '10003', '2024-05-22 11:34:53', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('977', '创建流程', '', '', '1', NULL, 1, '0', '/workflow/designer/create', '', 1, '1', 0, 0, 0, 'layout.base$view.workflow_designer_create', NULL, NULL, 'workflow-designer-create', NULL, '创建流程', NULL, '', NULL, NULL, '2024-05-22 14:08:25', '10003', '2024-05-22 14:08:25', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('978', '工作台', '', '', '0', NULL, 1, '0', '/workflow/workspace', 'material-symbols-light:cards-sharp', 0, '1', 0, 0, 0, 'layout.base', NULL, NULL, 'workflow-workspace', NULL, '工作台', NULL, '', NULL, NULL, '2024-05-22 16:45:31', '10003', '2024-05-22 16:45:31', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('979', '发起申请', '', '', '1', NULL, 2, '978', '/workflow/workspace/approval', 'ic:baseline-edit', 0, '1', 10, 0, 0, 'view.workflow_workspace_approval', NULL, NULL, 'workflow_workspace_approval', NULL, '发起申请', NULL, '', NULL, NULL, '2024-05-22 16:45:31', '10003', '2024-05-22 16:45:31', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('980', '我的申请', '', '', '1', NULL, 2, '978', '/workflow/workspace/apply', 'mdi:card-text', 0, '1', 20, 0, 0, 'view.workflow_workspace_apply', NULL, NULL, 'workflow_workspace_apply', NULL, '我的申请', NULL, '', NULL, NULL, '2024-05-22 16:45:31', '10003', '2024-05-22 16:45:31', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('981', '我收到的', '', '', '1', NULL, 2, '978', '/workflow/workspace/about', 'mdi:content-copy', 0, '1', 30, 0, 0, 'view.workflow_workspace_about', NULL, NULL, 'workflow_workspace_about', NULL, '我收到的', NULL, '', NULL, NULL, '2024-05-22 16:45:31', '10003', '2024-05-22 16:45:31', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('982', '待审批', '', '', '1', NULL, 2, '978', '/workflow/workspace/todo', 'ic:round-approval', 0, '1', 40, 0, 0, 'view.workflow_workspace_todo', NULL, NULL, 'workflow_workspace_todo', NULL, '待审批', NULL, '', NULL, NULL, '2024-05-22 17:48:54', '10003', '2024-05-22 17:48:54', '10003', '1');
INSERT INTO `sys_permission` (`id`, `menu_name`, `permission_code`, `permission_name`, `menu_type`, `open_type`, `level`, `parent_id`, `route_path`, `icon`, `hide_in_menu`, `status`, `order_num`, `keep_alive`, `is_frame`, `component`, `active_menu`, `fixed_index_in_tab`, `route_name`, `href`, `title`, `i18n_key`, `local_icon`, `multi_tab`, `constant`, `create_time`, `create_by`, `update_time`, `update_by`, `tenant_id`) VALUES ('983', '已审批', '', '', '1', NULL, 2, '978', '/workflow/workspace/done', 'ic:baseline-done-all', 0, '1', 50, 0, 0, 'view.workflow_workspace_done', NULL, NULL, 'workflow_workspace_done', NULL, '已审批', NULL, '', NULL, NULL, '2024-05-22 17:50:00', '10003', '2024-05-22 17:50:00', '10003', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` varchar(32) NOT NULL,
  `post_name` varchar(64) NOT NULL COMMENT '岗位名称',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `status` char(1) DEFAULT '\0' COMMENT '状态',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='岗位/职务';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` (`post_id`, `post_name`, `post_code`, `status`, `order_num`, `tenant_id`, `create_time`, `update_time`) VALUES ('4', '研发', 'M001', '1', 3, '1', '2022-07-27 14:05:16', '2024-05-23 13:53:13');
INSERT INTO `sys_post` (`post_id`, `post_name`, `post_code`, `status`, `order_num`, `tenant_id`, `create_time`, `update_time`) VALUES ('7', '测试', 'M002', '1', 6, '1', '2022-11-16 17:28:35', '2024-05-23 13:53:21');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名',
  `role_code` varchar(100) NOT NULL DEFAULT '' COMMENT '角色编码(Key值)',
  `remark` varchar(128) DEFAULT NULL COMMENT '说明',
  `status` tinyint(1) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `delete_flag` tinyint(1) DEFAULT '1' COMMENT '逻辑删除',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `status`, `create_time`, `update_time`, `delete_flag`, `tenant_id`) VALUES ('1', '超级管理员', 'super-admin', '拥有全部权限', 1, '2017-11-22 16:24:34', '2022-07-26 17:24:11', 0, '1');
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `status`, `create_time`, `update_time`, `delete_flag`, `tenant_id`) VALUES ('10', '管理员', 'admin', '管理员', 1, '2022-11-18 11:45:02', '2024-05-23 11:52:04', 0, '1');
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `status`, `create_time`, `update_time`, `delete_flag`, `tenant_id`) VALUES ('12', '工作流', 'workflow', '工作流', 1, '2024-05-23 13:54:29', '2024-05-23 13:54:37', 0, '1');
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `status`, `create_time`, `update_time`, `delete_flag`, `tenant_id`) VALUES ('9', '预览员', 'guest', '预览权限', 1, '2022-05-31 22:48:20', '2024-02-27 22:05:49', 0, '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限菜单关联';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1253', '11', '958', '2024-02-27 11:46:15', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1254', '11', '959', '2024-02-27 11:46:15', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1255', '11', '963', '2024-02-27 11:46:15', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1256', '11', '960', '2024-02-27 11:46:15', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1257', '9', '959', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1258', '9', '969', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1259', '9', '963', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1260', '9', '958', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1261', '9', '960', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1262', '9', '961', '2024-02-27 22:05:49', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1263', '10', '102', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1264', '10', '103', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1265', '10', '104', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1266', '10', '105', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1267', '10', '302', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1268', '10', '303', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1269', '10', '304', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1270', '10', '305', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1271', '10', '401', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1272', '10', '501', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1273', '10', '502', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1274', '10', '503', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1275', '10', '504', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1276', '10', '505', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1277', '10', '601', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1278', '10', '602', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1279', '10', '603', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1280', '10', '604', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1281', '10', '605', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1282', '10', '701', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1283', '10', '702', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1284', '10', '703', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1285', '10', '704', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1286', '10', '705', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1287', '10', '802', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1288', '10', '803', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1289', '10', '804', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1290', '10', '805', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1291', '10', '902', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1292', '10', '903', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1293', '10', '904', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1294', '10', '905', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1295', '10', '906', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1296', '10', '907', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1297', '10', '909', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1298', '10', '910', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1299', '10', '911', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1300', '10', '912', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1301', '10', '914', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1302', '10', '915', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1303', '10', '916', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1304', '10', '917', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1305', '10', '919', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1306', '10', '920', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1307', '10', '921', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1308', '10', '922', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1309', '10', '938', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1310', '10', '2', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1311', '10', '4', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1312', '10', '926', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1313', '10', '5', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1314', '10', '946', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1315', '10', '948', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1316', '10', '949', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1317', '10', '955', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1318', '10', '956', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1319', '10', '957', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1320', '10', '958', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1321', '10', '959', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1322', '10', '960', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1323', '10', '961', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1324', '10', '969', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1325', '10', '963', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1326', '10', '970', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1327', '10', '972', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1328', '10', '971', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1329', '10', '978', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1330', '10', '979', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1331', '10', '980', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1332', '10', '981', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1333', '10', '982', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1334', '10', '983', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1335', '10', '977', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1336', '10', '976', '2024-05-23 11:52:04', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1347', '12', '976', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1348', '12', '977', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1349', '12', '978', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1350', '12', '979', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1351', '12', '980', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1352', '12', '981', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1353', '12', '982', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1354', '12', '983', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1355', '12', '959', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1356', '12', '958', '2024-05-23 13:54:37', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('161', '1', '721', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('162', '1', '722', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('163', '1', '723', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('164', '1', '727', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('165', '1', '104', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('166', '1', '105', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('167', '1', '728', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('168', '1', '729', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('169', '1', '730', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('170', '1', '731', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('171', '1', '732', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('172', '1', '733', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('173', '1', '734', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('174', '1', '735', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('175', '1', '736', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('176', '1', '737', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('177', '1', '738', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('178', '1', '739', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('179', '1', '744', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('180', '1', '745', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('181', '1', '740', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('182', '1', '746', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('183', '1', '747', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('184', '1', '748', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('185', '1', '749', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('186', '1', '750', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('187', '1', '751', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('188', '1', '752', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('189', '1', '753', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('190', '1', '754', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('191', '1', '741', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('192', '1', '768', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('193', '1', '769', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('194', '1', '770', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('195', '1', '771', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('196', '1', '755', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('197', '1', '756', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('198', '1', '757', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('199', '1', '758', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('20', '1', '1', '2024-01-20 11:18:29', '2022-05-26 11:18:19', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('200', '1', '759', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('201', '1', '760', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('202', '1', '761', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('203', '1', '762', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('204', '1', '763', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('205', '1', '764', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('206', '1', '765', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('207', '1', '766', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('208', '1', '767', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('209', '1', '742', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('21', '1', '2', '2024-01-20 11:18:29', '2022-05-26 11:18:39', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('210', '1', '774', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('211', '1', '775', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('212', '1', '743', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('213', '1', '772', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('214', '1', '773', '2022-06-14 09:15:01', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('22', '1', '3', '2022-05-26 11:18:49', '2022-05-26 11:18:49', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('23', '1', '4', '2022-05-26 11:18:58', '2022-05-26 11:19:01', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('24', '1', '5', '2022-05-26 11:19:10', '2022-05-26 11:19:10', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('25', '1', '6', '2022-05-26 11:19:20', '2022-05-26 11:21:27', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('26', '1', '101', '2022-05-26 11:19:39', '2022-05-26 11:19:39', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('27', '1', '102', '2022-05-26 11:19:47', '2022-05-26 11:19:47', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('274', '1', '8', '2022-06-25 15:27:22', '2022-06-25 15:27:20', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('275', '1', '801', '2022-06-25 15:27:34', '2022-06-25 15:27:32', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('276', '1', '802', '2022-06-25 15:27:43', '2022-06-25 15:27:42', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('277', '1', '803', '2022-06-25 15:27:52', '2022-06-25 15:27:55', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('278', '1', '804', '2022-06-25 15:28:08', '2022-06-25 15:28:07', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('279', '1', '805', '2022-06-25 15:28:22', '2022-06-25 15:28:21', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('28', '1', '103', '2022-05-26 11:19:57', '2022-05-26 11:19:56', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('29', '1', '301', '2022-05-26 11:20:12', '2022-05-26 11:20:11', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('30', '1', '302', '2022-05-26 11:20:26', '2022-05-26 11:20:26', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('304', '1', '9', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('305', '1', '901', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('306', '1', '902', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('307', '1', '903', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('308', '1', '904', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('309', '1', '905', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('31', '1', '303', '2022-05-26 11:20:34', '2022-05-26 11:20:34', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('310', '1', '779', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('311', '1', '780', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('312', '1', '781', '2022-07-11 09:12:47', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('316', '1', '781', '2022-07-11 15:40:57', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('32', '1', '304', '2022-05-26 11:20:42', '2022-05-26 11:20:42', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('321', '1', '923', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('322', '1', '924', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('323', '1', '10', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('324', '1', '908', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('325', '1', '909', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('326', '1', '910', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('327', '1', '911', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('328', '1', '912', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('329', '1', '11', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('33', '1', '305', '2022-05-26 11:20:51', '2022-05-26 11:20:51', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('330', '1', '913', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('331', '1', '914', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('332', '1', '915', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('333', '1', '916', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('334', '1', '917', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('335', '1', '12', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('336', '1', '918', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('337', '1', '919', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('338', '1', '920', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('339', '1', '921', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('34', '1', '501', '2022-05-26 11:21:08', '2022-05-26 11:21:08', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('340', '1', '922', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('341', '1', '776', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('342', '1', '777', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('343', '1', '778', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('344', '1', '781', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('345', '1', '906', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('346', '1', '907', '2022-07-25 21:16:07', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('347', '1', '925', '2022-07-25 21:51:00', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('348', '1', '927', '2022-07-26 17:09:14', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('349', '1', '926', '2022-07-26 17:09:14', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('35', '1', '502', '2022-05-26 11:21:14', '2022-05-26 11:21:49', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('350', '1', '928', '2022-07-26 17:09:14', NULL, '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('36', '1', '503', '2022-05-26 11:22:01', '2022-05-26 11:22:00', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('37', '1', '504', '2022-05-26 11:22:08', '2022-05-26 11:22:08', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('38', '1', '505', '2022-05-26 11:22:16', '2022-05-26 11:22:16', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('39', '1', '601', '2022-05-26 11:22:44', '2022-05-26 11:22:44', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('40', '1', '602', '2022-05-26 11:22:52', '2022-05-26 11:22:52', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('41', '1', '603', '2022-05-26 11:23:01', '2022-05-26 11:23:01', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('42', '1', '604', '2022-05-26 11:23:10', '2022-05-26 11:23:09', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('43', '1', '605', '2022-05-26 11:23:24', '2022-05-26 11:23:24', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('44', '1', '701', '2022-05-26 11:23:33', '2022-05-26 11:23:33', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('45', '1', '702', '2022-05-26 11:23:42', '2022-05-26 11:23:42', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('47', '1', '703', '2022-05-26 11:24:03', '2022-05-26 11:24:03', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('48', '1', '704', '2022-05-26 11:24:13', '2022-05-26 11:24:12', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('49', '1', '705', '2022-05-26 11:24:22', '2022-05-26 11:24:22', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('50', '1', '7', '2022-05-26 15:16:20', '2022-05-26 15:16:18', '1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('51', '1', '401', '2022-05-26 15:16:40', '2022-05-26 15:16:25', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_storage_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_storage_config`;
CREATE TABLE `sys_storage_config` (
  `id` varchar(32) NOT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `platform` varchar(64) DEFAULT NULL COMMENT '平台',
  `config` json DEFAULT NULL COMMENT '配置',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='对象存储配置';

-- ----------------------------
-- Records of sys_storage_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_storage_config` (`id`, `name`, `platform`, `config`, `enable`) VALUES ('1', '七牛云', 'qiniu-kodo', '{\"domain\": \"\", \"basePath\": \"file/\", \"accessKey\": \"\", \"secretKey\": \"\", \"bucketName\": \"flowlong-oss\"}', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` varchar(32) NOT NULL,
  `tenant_code` varchar(64) NOT NULL COMMENT '租户编码',
  `tenant_name` varchar(64) NOT NULL COMMENT '租户名称',
  `begin_date` datetime NOT NULL COMMENT '开始时间',
  `end_date` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '租户状态',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='租户信息';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` (`id`, `tenant_code`, `tenant_name`, `begin_date`, `end_date`, `status`, `create_time`, `update_time`, `delete_flag`, `tenant_id`) VALUES ('1', 'workflow', '工作流测试租户', '2022-10-01 14:54:31', '2030-10-25 14:54:38', 1, '2022-11-05 21:45:06', '2024-06-02 23:51:37', 0, '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL,
  `account` varchar(32) DEFAULT NULL COMMENT '登录账号',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(128) DEFAULT NULL COMMENT '昵称',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `remark` varchar(128) DEFAULT NULL COMMENT '个人介绍',
  `email` varchar(128) NOT NULL DEFAULT '' COMMENT '邮箱',
  `delete_flag` tinyint(1) DEFAULT '1' COMMENT '逻辑删除',
  `dept_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `post_id` varchar(32) DEFAULT NULL COMMENT '岗位id',
  `leader_id` varchar(32) DEFAULT NULL COMMENT '直属上级id',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '是否启用:0=禁用;1=启用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户信息';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10003', 'admin', '48e9e0d3e6ac939b60995a3600acb27b', 'admin', 1, '', '超级管理员', '', 0, '12', NULL, NULL, 1, '2024-01-20 11:18:29', '2024-05-30 13:59:28', '1');
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10033', 'test', '48e9e0d3e6ac939b60995a3600acb27b', '测试', 1, '', '', '', 0, '12', '4', NULL, 1, '2024-01-20 11:18:29', '2024-05-24 14:20:35', '1');
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10036', 'zhangsan', '48e9e0d3e6ac939b60995a3600acb27b', '张三', 2, '', '', '', 0, '12', '4', NULL, 1, '2024-01-20 11:18:29', '2024-05-24 10:45:18', '1');
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10037', 'lisi', '48e9e0d3e6ac939b60995a3600acb27b', '李四', 1, '', '', '', 0, '12', '7', NULL, 1, '2024-01-20 11:18:29', '2024-02-21 22:22:56', '1');
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10038', 'xiongda', 'ee31ad693de49fd7e16d434b2a69a852', '熊大', 1, '', NULL, '', 0, '12', '4', NULL, 1, '2024-05-23 13:55:18', '2024-05-24 10:10:27', '1');
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `sex`, `mobile`, `remark`, `email`, `delete_flag`, `dept_id`, `post_id`, `leader_id`, `is_enable`, `create_time`, `update_time`, `tenant_id`) VALUES ('10039', 'xionger', 'ee31ad693de49fd7e16d434b2a69a852', '熊二', 1, '', NULL, '', 0, '12', '4', NULL, 1, '2024-05-23 13:55:48', NULL, '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `role_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('1', '10003', '1', '2022-06-24 11:43:07', '2022-06-24 11:43:05', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('28', '10031', '1', '2023-08-20 22:28:41', '2023-08-20 22:28:41', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('4', '10003', '9', '2022-06-28 11:10:26', '2022-06-28 11:10:29', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('43', '10037', '10', '2024-02-21 22:22:56', '2024-02-21 22:22:56', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('53', '10038', '10', '2024-02-26 22:40:14', '2024-02-26 22:40:14', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('61', '10038', '12', '2024-05-23 13:55:18', '2024-05-23 13:55:18', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('62', '10039', '12', '2024-05-23 13:55:48', '2024-05-23 13:55:48', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('63', '10036', '10', '2024-05-24 10:45:18', '2024-05-24 10:45:18', '1');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `tenant_id`) VALUES ('64', '10033', '10', '2024-05-24 13:57:48', '2024-05-24 13:57:48', '1');
COMMIT;

-- ----------------------------
-- Table structure for wflow_ext_instance
-- ----------------------------
DROP TABLE IF EXISTS `wflow_ext_instance`;
CREATE TABLE `wflow_ext_instance` (
  `id` varchar(32) NOT NULL COMMENT 'flowlong实例ID',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户ID',
  `template_id` varchar(32) DEFAULT NULL COMMENT '流程模版id',
  `process_id` varchar(32) DEFAULT NULL COMMENT 'flowlong流程id',
  `process_version` int(11) DEFAULT NULL COMMENT 'flowlong流程版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of wflow_ext_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wflow_instance_action_record
-- ----------------------------
DROP TABLE IF EXISTS `wflow_instance_action_record`;
CREATE TABLE `wflow_instance_action_record` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `instance_id` varchar(32) NOT NULL COMMENT '流程实例id',
  `comment` varchar(255) DEFAULT NULL COMMENT '评论',
  `attachments` json DEFAULT NULL COMMENT '附件',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `robot` tinyint(1) DEFAULT NULL COMMENT '抄送或者自动审批的话为true',
  `auditor_id` varchar(32) DEFAULT NULL COMMENT '审批办理人id',
  `action_type` tinyint(2) DEFAULT NULL COMMENT '动作类型',
  `user_ids` json DEFAULT NULL COMMENT '用户的ids,例如抄送的人可以放进来',
  `assignee_id` varchar(32) DEFAULT NULL COMMENT '分配人ID，例如加签给谁，减签给谁，转交给谁ID',
  `node_id` varchar(32) DEFAULT NULL COMMENT 'flowlong中的nodeName,例如加签、减签、转交给谁，将当前节点记录一下',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程动作记录';

-- ----------------------------
-- Records of wflow_instance_action_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wflow_process_groups
-- ----------------------------
DROP TABLE IF EXISTS `wflow_process_groups`;
CREATE TABLE `wflow_process_groups` (
  `group_id` varchar(20) NOT NULL COMMENT 'id',
  `group_name` varchar(50) DEFAULT NULL COMMENT '组名',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序号',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程分组';

-- ----------------------------
-- Records of wflow_process_groups
-- ----------------------------
BEGIN;
INSERT INTO `wflow_process_groups` (`group_id`, `group_name`, `sort_num`, `created`, `updated`, `tenant_id`) VALUES ('1793813591874510849', '测试分组', 1, '2024-05-24 09:17:42', '2024-05-24 09:17:42', '1');
COMMIT;

-- ----------------------------
-- Table structure for wflow_process_templates
-- ----------------------------
DROP TABLE IF EXISTS `wflow_process_templates`;
CREATE TABLE `wflow_process_templates` (
  `template_id` varchar(50) NOT NULL COMMENT '审批摸板ID',
  `template_key` varchar(32) DEFAULT NULL COMMENT '模版标识',
  `template_version` int(11) DEFAULT '1' COMMENT '版本，默认1',
  `template_name` varchar(50) DEFAULT NULL COMMENT '模版名称',
  `settings` longtext COMMENT '基础设置',
  `form_items` longtext COMMENT '模版表单',
  `process` longtext COMMENT '流程定义',
  `logo` text COMMENT '图标url',
  `notify` text COMMENT 'notify',
  `who_commit` longtext COMMENT '谁能提交',
  `who_edit` longtext COMMENT '谁能编辑',
  `who_export` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '谁能导出数据',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `group_id` bigint(20) DEFAULT NULL COMMENT '由wflow_template_group记录',
  `status` tinyint(3) DEFAULT NULL COMMENT '0:停用;1:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT 'workflow_id',
  `workflow_version` int(11) DEFAULT NULL COMMENT 'workflow流程版本，默认 1',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程定义模版';

-- ----------------------------
-- Records of wflow_process_templates
-- ----------------------------
BEGIN;
INSERT INTO `wflow_process_templates` (`template_id`, `template_key`, `template_version`, `template_name`, `settings`, `form_items`, `process`, `logo`, `notify`, `who_commit`, `who_edit`, `who_export`, `remark`, `group_id`, `status`, `create_time`, `update_time`, `workflow_id`, `workflow_version`, `tenant_id`) VALUES ('1800216215317946370', 'YtYSSC', 1, '未命名流程', '{\"sign\":false}', '[{\"title\":\"单行文本输入\",\"name\":\"TextInput\",\"icon\":\"x-icon-edit\",\"value\":\"\",\"valueType\":\"String\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6718239958223\"}]', '{\"id\":\"node_start\",\"parentId\":null,\"type\":0,\"nodeName\":\"发起人\",\"nodeAssigneeList\":[],\"childNode\":{\"id\":\"node_end\",\"parentId\":\"node_root\",\"type\":-1,\"nodeName\":\"结束\",\"nodeAssigneeList\":[]}}', NULL, NULL, NULL, NULL, NULL, '', 1793813591874510849, NULL, '2024-06-10 13:19:27', '2024-06-10 13:19:27', 1800216215284391937, 1, '1');
COMMIT;

-- ----------------------------
-- Table structure for wflow_template_group
-- ----------------------------
DROP TABLE IF EXISTS `wflow_template_group`;
CREATE TABLE `wflow_template_group` (
  `id` varchar(20) NOT NULL COMMENT 'id',
  `template_id` varchar(20) DEFAULT NULL COMMENT 'templateId',
  `group_id` varchar(20) DEFAULT NULL COMMENT 'groupId',
  `sort_num` int(11) DEFAULT NULL COMMENT 'sortNum',
  `create_time` datetime DEFAULT NULL COMMENT 'created',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程与分组关联记录';

-- ----------------------------
-- Records of wflow_template_group
-- ----------------------------
BEGIN;
INSERT INTO `wflow_template_group` (`id`, `template_id`, `group_id`, `sort_num`, `create_time`, `tenant_id`) VALUES ('1800216215355695105', '1800216215317946370', '1793813591874510849', NULL, NULL, '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

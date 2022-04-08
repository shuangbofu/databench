DROP DATABASE IF EXISTS `databench`;

CREATE DATABASE `databench`;

use `databench`;

DROP TABLE IF EXISTS `biz`;
CREATE TABLE `biz`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `name`         varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',
    `description`  varchar(256)        NOT NULL DEFAULT '' COMMENT '描述',
    `workspace_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '工作空间ID',
    `cfg`          text COMMENT '配置',
    `layout_cfg`   text COMMENT '布局配置',
    `dag_graph`    text COMMENT 'dag图配置',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '业务流程表';

DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `name`         varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',
    `workspace_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '工作空间ID',
    `biz_id`       bigint(20)          NOT NULL DEFAULT '0' COMMENT '业务流程ID',
    `folder_id`    bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件夹ID',
    `belong`       varchar(16)         NOT NULL DEFAULT '' COMMENT '所属模块',
    `file_type`    varchar(16)         NOT NULL DEFAULT '' COMMENT '文件类型',
    `category`     varchar(16)         NOT NULL DEFAULT '' COMMENT '所属目录',
    `version`      int(10)             NOT NULL DEFAULT '0' COMMENT '版本',
    `delete_flag`  tinyint(1)          NOT NULL DEFAULT '0' COMMENT '删除字段',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '文件表';

DROP TABLE IF EXISTS `file_commit`;
CREATE TABLE `file_commit`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `file_id`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件ID',
    `version`      int(10)             NOT NULL DEFAULT '0' COMMENT '版本',
    `commit_type`  varchar(16)         NOT NULL DEFAULT '' COMMENT '提交类型,add, modify, discard',
    `published`    tinyint(1)          NOT NULL DEFAULT '0' COMMENT '是否发布',
    `comment`      varchar(128)        NOT NULL DEFAULT '' COMMENT '评论',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '文件提交表';

DROP TABLE IF EXISTS `file_version`;
CREATE TABLE `file_version`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `file_id`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件ID',
    `version`      int(10)             NOT NULL DEFAULT '0' COMMENT '版本',
    `content`      text COMMENT '文件内容',
    `cfg`          text COMMENT '文件配置',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '文件内容版本表';

DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `name`         varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',
    `workspace_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '工作空间ID',
    `biz_id`       bigint(20)          NOT NULL DEFAULT '0' COMMENT '业务流程ID',
    `belong`       varchar(16)         NOT NULL DEFAULT '' COMMENT '所属模块, development, query, api, resource',
    `category`     varchar(16)         NOT NULL DEFAULT 'undefined' COMMENT '所属目录',
    `parent_id`    bigint(20)          NOT NULL DEFAULT '0' COMMENT '父文件夹ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '文件夹表';

DROP TABLE IF EXISTS `node`;
CREATE TABLE `node`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `file_id`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件ID',
    `name`         varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',
    `dev_version`  int(10)             NOT NULL DEFAULT '0' COMMENT '开发版本',
    `prod_version` int(10)             NOT NULL DEFAULT '0' COMMENT '生产版本',
    `node_type`    varchar(16)         NOT NULL DEFAULT '' COMMENT '节点类型',
    `owner`        varchar(16)         NOT NULL DEFAULT '' COMMENT '所属人',
    `workspace_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '工作空间ID',
    `biz_id`       bigint(20)          NOT NULL DEFAULT '0' COMMENT '业务流程ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '节点表';

DROP TABLE IF EXISTS `node_dep`;
CREATE TABLE `node_dep`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `child_id`     bigint(20)          NOT NULL DEFAULT '0' COMMENT '子结点ID',
    `parent_id`    bigint(20)          NOT NULL DEFAULT '0' COMMENT '父节点ID',
    `prod`         tinyint(1)          NOT NULL DEFAULT '0' COMMENT '是否生产',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '节点依赖表';

DROP TABLE IF EXISTS `output`;
CREATE TABLE `output`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `name`         varchar(128)        NOT NULL DEFAULT '' COMMENT '名称',
    `table_name`   varchar(128)        NOT NULL DEFAULT '' COMMENT '表名',
    `source`       varchar(8)          NOT NULL DEFAULT '' COMMENT '输出源',
    `file_id`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件ID',
    `ref_file_ids` varchar(128)        NOT NULL DEFAULT '' COMMENT '引用当前输出的下游文件ID集合',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '节点输出表';

DROP TABLE IF EXISTS `workspace`;
CREATE TABLE `workspace`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `name`         varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',
    `space_cfg`    text COMMENT '空间配置',
    `description`  varchar(32)         NOT NULL DEFAULT '' COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '工作空间表';

DROP TABLE IF EXISTS `job_history`;
CREATE TABLE `job_history`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `gmt_create`   bigint(13)          NOT NULL DEFAULT '0' COMMENT '创建时间',
    `gmt_modified` bigint(13)          NOT NULL DEFAULT '0' COMMENT '修改时间',
    `create_by`    varchar(64)         NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_by`  varchar(64)         NOT NULL DEFAULT '' COMMENT '修改人',
    `workspace_id` bigint(20)          NOT NULL DEFAULT '0' COMMENT '工作空间ID',
    `biz_id`       bigint(20)          NOT NULL DEFAULT '0' COMMENT '业务流程ID',
    `tenant`       varchar(64)         NOT NULL DEFAULT '' COMMENT '租户',
    `deleted`      tinyint(1)          NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `job_id`       varchar(64)         NOT NULL DEFAULT '' COMMENT '作业',
    `done`         int(10)             NOT NULL DEFAULT '0' COMMENT '是否完成',
    `status`       varchar(16)         NOT NULL DEFAULT '' COMMENT '状态',
    `file_id`      bigint(20)          NOT NULL DEFAULT '0' COMMENT '文件ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '临时作业历史表';

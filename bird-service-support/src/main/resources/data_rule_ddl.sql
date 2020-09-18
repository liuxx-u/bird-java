CREATE TABLE `bird_data_rule` (
  `id` varchar(64) NOT NULL,
  `appName` varchar(80) DEFAULT NULL COMMENT '系统名称',
  `name` varchar(45) DEFAULT NULL COMMENT '规则名称',
  `className` varchar(80) DEFAULT NULL COMMENT 'DO类名',
  `fieldName` varchar(45) DEFAULT NULL COMMENT '字段名称',
  `tableName` varchar(45) DEFAULT NULL COMMENT '对应的表名',
  `tableNote` varchar(80) DEFAULT NULL COMMENT '表名注释',
  `dbFieldName` varchar(45) DEFAULT NULL COMMENT '对于数据库字段名',
  `sourceStrategy` varchar(16) DEFAULT NULL COMMENT '数据源策略',
  `sourceProvider` varchar(160) DEFAULT NULL COMMENT '数据源提供者类名',
  `sourceUrl` varchar(160) DEFAULT NULL COMMENT '数据源获取地址',
  `delFlag` tinyint(4) unsigned DEFAULT 0,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


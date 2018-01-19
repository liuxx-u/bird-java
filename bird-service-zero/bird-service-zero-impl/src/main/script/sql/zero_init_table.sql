CREATE SCHEMA IF NOT EXISTS `bird-zero`;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_organization` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '岗位名称',
  `parentId` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级Id',
  `parentIds` VARCHAR(45) NULL COMMENT '父级路径',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `orderNo` SMALLINT UNSIGNED NULL DEFAULT 0 COMMENT '排序号',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '删除标识',
  `createTime` DATETIME NULL COMMENT '创建时间',
  `modifiedTime` DATETIME NULL COMMENT '修改时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(32) NOT NULL COMMENT '用户名',
  `nickName` VARCHAR(32) NULL COMMENT '昵称',
  `password` VARCHAR(128) NULL COMMENT '密码',
  `phoneNo` VARCHAR(45) NULL COMMENT '联系电话',
  `locked` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否锁定',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `lastLoginTime` DATETIME NULL COMMENT '最后登录时间',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_role` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '角色名(岗位)',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `organizationId` INT UNSIGNED NOT NULL COMMENT '所属组织机构',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `role_organizationId_idx` (`organizationId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_user_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userId` BIGINT UNSIGNED NOT NULL COMMENT '用户id',
  `roleId` INT UNSIGNED NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  INDEX `userId_idx` (`userId` ASC),
  INDEX `roleId_idx` (`roleId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_permission` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(80) NOT NULL COMMENT 'key',
  `name` VARCHAR(45) NULL COMMENT '权限名称',
  `parentId` INT UNSIGNED NULL COMMENT '父级id',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_role_permission` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleId` INT UNSIGNED NOT NULL COMMENT '角色Id',
  `permissionName` VARCHAR(128) NOT NULL COMMENT '权限名',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `roleId_idx` (`roleId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_menu` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '菜单名称',
  `url` VARCHAR(128) NULL COMMENT '关联地址',
  `icon` VARCHAR(45) NULL COMMENT '图标',
  `parentId` INT UNSIGNED NOT NULL DEFAULT 0,
  `orderNo` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序号',
  `permissionName` VARCHAR(128) NULL COMMENT '所需权限名',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `visitType` TINYINT UNSIGNED NULL COMMENT '1.游客访问;2.登录访问;3.权限访问',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_user_login_attempt` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL COMMENT '用户名',
  `clientIp` VARCHAR(45) NULL COMMENT '登录客户端IP',
  `clientName` VARCHAR(80) NULL COMMENT '登录客户端名称',
  `browserInfo` VARCHAR(256) NULL COMMENT '浏览器信息',
  `result` TINYINT UNSIGNED NOT NULL COMMENT '1.成功;2.用户名不存在;3.密码错误;4.用户被锁定',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_site` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '站点名称',
  `key` VARCHAR(45) NOT NULL,
  `host` VARCHAR(45) NOT NULL COMMENT '主机地址',
  `indexUrl` VARCHAR(128) NULL COMMENT '主页地址',
  `loginNotifyUrl` VARCHAR(128) NULL COMMENT '登录成功通知地址',
  `remark` VARCHAR(128) NULL COMMENT '备注',
  `disabled` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否禁用',
  `permissionName` VARCHAR(128) NULL COMMENT '所需权限名',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_user_site` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `userId` BIGINT UNSIGNED NOT NULL COMMENT '用户id',
  `siteId` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_site_userId_idx` (`userId` ASC),
  INDEX `user_site_siteId_idx` (`siteId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_module` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `siteId` INT UNSIGNED NULL COMMENT '所属站点',
  `name` VARCHAR(45) NULL COMMENT '模块名称',
  `parentId` INT UNSIGNED NULL COMMENT '父模块id',
  `description` VARCHAR(256) NULL COMMENT '模块描述',
  `orderNo` INT UNSIGNED NULL DEFAULT 0 COMMENT '排序号',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0,
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `zero_module_siteId_idx` (`siteId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_dicType` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '名称',
  `key` VARCHAR(64) NOT NULL COMMENT 'key',
  `parentId` INT UNSIGNED NULL DEFAULT 0 COMMENT '父级id',
  `placeholder` VARCHAR(64) NULL COMMENT '选择框默认文字',
  `remark` VARCHAR(45) NULL COMMENT '备注',
  `defaultCode` VARCHAR(128) NULL COMMENT '默认业务码',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_dicItem` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '名称',
  `code` VARCHAR(128) NOT NULL COMMENT '业务码',
  `dicTypeId` INT UNSIGNED NOT NULL COMMENT '所属字典id',
  `orderNo` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序号',
  `disable` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否禁用',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `zero_dicItem_dicTypeId_idx` (`dicTypeId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_form` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '表单名称',
  `key` VARCHAR(45) NOT NULL COMMENT '表单key',
  `description` VARCHAR(256) NULL COMMENT '描述',
  `enabled` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否启用',
  `withTab` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否分组渲染',
  `saveUrl` VARCHAR(128) NULL COMMENT '数据提交地址',
  `tabType` VARCHAR(45) NULL DEFAULT 'line' COMMENT 'tab的类型',
  `tabPosition` VARCHAR(45) NULL DEFAULT 'top' COMMENT 'tab所在位置',
  `defaultGroupName` VARCHAR(45) NULL COMMENT '默认的分组名称',
  `lineCapacity` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '每行容量',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0,
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_form_field` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '字段名',
  `key` VARCHAR(45) NULL COMMENT 'key',
  `defaultValue` VARCHAR(128) NULL COMMENT '默认值',
  `tips` VARCHAR(128) NULL COMMENT '提示',
  `groupName` VARCHAR(45) NULL COMMENT '分组名',
  `validateRegular` VARCHAR(80) NULL COMMENT '验证的正则表达式',
  `isRequired` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否必填',
  `orderNo` SMALLINT UNSIGNED NULL DEFAULT 0 COMMENT '排序号',
  `fieldType` VARCHAR(45) NULL COMMENT '字段类型',
  `optionsKey` VARCHAR(45) NULL COMMENT '类型为下拉时，对应的字典key',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0,
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  `formId` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  INDEX `zero_form_field_formId_idx` (`formId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_form_value` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userId` BIGINT UNSIGNED NULL COMMENT '用户id',
  `formId` BIGINT UNSIGNED NULL COMMENT '表单id',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0,
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `zero_form_value_formId_idx` (`formId` ASC),
  INDEX `zero_form_value_userId_idx` (`userId` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-zero`.`zero_form_field_value` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `formValueId` BIGINT UNSIGNED NULL COMMENT '表单id',
  `fieldId` BIGINT UNSIGNED NULL COMMENT '字段id',
  `key` VARCHAR(45) NULL COMMENT '字段key',
  `value` TEXT NULL COMMENT '值',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0,
  `createTime` DATETIME NULL,
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `zero_form_field_value_formValueId_idx` (`formValueId` ASC),
  INDEX `zero_form_field_value_fieldId_idx` (`fieldId` ASC))
ENGINE = InnoDB;










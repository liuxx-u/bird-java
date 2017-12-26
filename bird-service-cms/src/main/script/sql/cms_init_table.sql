CREATE SCHEMA IF NOT EXISTS `bird-cms`;

CREATE TABLE IF NOT EXISTS `bird-cms`.`cms_classify` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '分类名称',
  `parentId` BIGINT UNSIGNED NULL COMMENT '父级id',
  `parentIds` VARCHAR(64) NULL COMMENT '父级id集合',
  `orderNo` SMALLINT UNSIGNED NULL COMMENT '排序号',
  `logo` VARCHAR(128) NULL COMMENT 'LOGO',
  `delFlag` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL COMMENT '创建时间',
  `modifiedTime` DATETIME NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-cms`.`cms_attribute` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '属性名',
  `key` VARCHAR(45) NOT NULL COMMENT '属性Key',
  `defaultValue` VARCHAR(128) NULL,
  `tips` VARCHAR(128) NULL COMMENT '提示',
  `groupName` VARCHAR(45) NULL COMMENT '分组名',
  `validateRegular` VARCHAR(80) NULL COMMENT '验证的正则表达式',
  `isRequired` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否必填项',
  `orderNo` SMALLINT UNSIGNED NULL COMMENT '排序号',
  `fieldType` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '1:普通文本;2:数字;3:单选;4:多选;5:日期;6:文本域;7:布尔值;8:图片;9:富文本;',
  `classifyId` BIGINT UNSIGNED NOT NULL COMMENT '所属分类',
  `optionsKey` VARCHAR(45) NULL COMMENT '单/多选模式下对应的字典key',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL COMMENT '创建时间',
  `modifiedTime` DATETIME NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  INDEX `attribute_classifyId_idx` (`classifyId` ASC),
  CONSTRAINT `attribute_classifyId`
    FOREIGN KEY (`classifyId`)
    REFERENCES `bird-cms`.`cms_classify` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-cms`.`cms_content` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL COMMENT '标题',
  `classifyId` BIGINT UNSIGNED NOT NULL COMMENT '所属分类id',
  `brief` VARCHAR(128) NULL COMMENT '简介',
  `content` TEXT NULL COMMENT '内容',
  `link` VARCHAR(128) NULL COMMENT '链接地址',
  `cover` VARCHAR(128) NULL COMMENT '封面',
  `orderNo` VARCHAR(45) NULL DEFAULT 0 COMMENT '排序号',
  `browserNo` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '浏览数',
  `praiseNo` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL COMMENT '创建时间',
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `content_classifyId_idx` (`classifyId` ASC),
  CONSTRAINT `content_classifyId`
    FOREIGN KEY (`classifyId`)
    REFERENCES `bird-cms`.`cms_classify` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-cms`.`cms_content_attribute` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `contentId` BIGINT UNSIGNED NOT NULL COMMENT '新闻id',
  `value` TEXT NULL COMMENT '属性值',
  `key` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `content_attribute_contentId_idx` (`contentId` ASC),
  CONSTRAINT `content_attribute_contentId`
    FOREIGN KEY (`contentId`)
    REFERENCES `bird-cms`.`cms_content` (`classifyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bird-cms`.`cms_content_praise` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userId` BIGINT UNSIGNED NOT NULL COMMENT '用户id',
  `contentId` BIGINT UNSIGNED NOT NULL COMMENT '新闻id',
  `delFlag` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `createTime` DATETIME NULL COMMENT '创建时间',
  `modifiedTime` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `content_praise_contentId_idx` (`contentId` ASC),
  CONSTRAINT `content_praise_contentId`
    FOREIGN KEY (`contentId`)
    REFERENCES `bird-cms`.`cms_content` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


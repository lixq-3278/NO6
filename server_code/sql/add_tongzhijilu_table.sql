-- 新增通知记录表
DROP TABLE IF EXISTS `tongzhijilu`;
CREATE TABLE `tongzhijilu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tongzhiid` bigint(20) DEFAULT NULL COMMENT '通知ID',
  `tongzhibianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知编号',
  `zhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `shouji` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `tongzhineirong` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知内容',
  `zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `chongshicishu` int(11) DEFAULT 0 COMMENT '重试次数',
  `zuijinfasongshijian` datetime DEFAULT NULL COMMENT '最近发送时间',
  `cuowuxinxi` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知记录表';

-- 修改就诊通知表，添加状态字段
ALTER TABLE `jiuzhentongzhi` ADD COLUMN `zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知状态' AFTER `tongzhibeizhu`;

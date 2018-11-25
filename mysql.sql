/*==============================================================*/
/* Table: t_dict_type                                           */
/*==============================================================*/
create table if NOT EXISTS t_dict_type
(
   id                   bigint(20) not null auto_increment comment 'id',
   create_by            bigint(20) comment '创建人id',
   create_date          datetime comment '创建时间',
   update_by            bigint(20) comment '更新人id',
   update_date          datetime comment '更新时间',
   type_code            national varchar(255) not null comment '字典编码',
   type_name            national varchar(255) not null comment '字典编码名称',
   type_group_id        bigint(20) not null comment '字典组id',
   type_group_code      national varchar(255) not null comment '字典组编码',
   order_num            int(4) comment '排序',
   primary key (id)
);

alter table t_dict_type comment '字典';
alter table t_dict_type AUTO_INCREMENT = 100000000;

/*==============================================================*/
/* Table: t_dict_type_group                                     */
/*==============================================================*/
create table if NOT EXISTS t_dict_type_group
(
   id                   bigint(20) not null auto_increment comment 'id',
   create_by            bigint(20) comment '创建人id',
   create_date          datetime comment '创建时间',
   update_by            bigint(20) comment '更新人id',
   update_date          datetime comment '更新时间',
   type_group_code      national varchar(255) not null comment '字典组编码',
   type_group_name      national varchar(255) not null comment '字典组名称',
   primary key (id)
);

alter table t_dict_type_group comment '字典组';

-- ----------------------------
-- Records of t_dict_type
-- ----------------------------
INSERT INTO `t_dict_type` VALUES (100000001, NULL, NULL, NULL, NULL, 'mobile', '手机端', 1, '', 1);
INSERT INTO `t_dict_type` VALUES (100000002, NULL, NULL, NULL, NULL, 'box', '智能音箱', 1, '', 2);
INSERT INTO `t_dict_type` VALUES (100000003, NULL, NULL, NULL, NULL, '0', '不可用', 2, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000004, NULL, NULL, NULL, NULL, '1', '可用', 2, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000005, NULL, NULL, NULL, NULL, 'ROLE_USER', '普通用户', 3, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000006, NULL, NULL, NULL, NULL, 'ROLE_ADMIN', '管理员', 3, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000007, NULL, NULL, NULL, NULL, 'ROLE_SU', '超管', 3, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000008, NULL, NULL, NULL, NULL, '1', '男', 4, 'sex', NULL);
INSERT INTO `t_dict_type` VALUES (100000009, NULL, NULL, NULL, NULL, '0', '女', 4, 'sex', NULL);
INSERT INTO `t_dict_type` VALUES (100000010, NULL, '2018-11-19 10:23:45', NULL, NULL, '0', '离线', 5, '', 1);
INSERT INTO `t_dict_type` VALUES (100000011, NULL, '2018-11-19 11:27:11', NULL, NULL, '1', '在线', 5, '', 2);
INSERT INTO `t_dict_type` VALUES (100000012, NULL, NULL, NULL, NULL, '0', '关闭', 6, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000013, NULL, NULL, NULL, NULL, '1', '启用', 6, '', NULL);
INSERT INTO `t_dict_type` VALUES (100000014, NULL, NULL, NULL, NULL, '1', '一级菜单', 7, '', 1);
INSERT INTO `t_dict_type` VALUES (100000015, NULL, NULL, NULL, NULL, '2', '二级菜单', 7, '', 2);
INSERT INTO `t_dict_type` VALUES (95684685694238720, NULL, '2018-11-18 23:09:45', NULL, NULL, 'test_01', '测试子字典1', 95684503049076736, '', 1);
INSERT INTO `t_dict_type` VALUES (95854276256989184, NULL, '2018-11-19 10:23:38', NULL, NULL, '2', '勿扰', 5, '', 3);
INSERT INTO `t_dict_type` VALUES (96958562013544448, NULL, '2018-11-22 11:34:26', NULL, NULL, '1', '待付款', 96950073392365568, 'bill_status', 1);
INSERT INTO `t_dict_type` VALUES (96959193222742016, NULL, '2018-11-22 11:34:11', NULL, NULL, '2', '已付款，待发货', 96950073392365568, 'bill_status', 2);
INSERT INTO `t_dict_type` VALUES (96961451628953600, NULL, '2018-11-22 11:43:09', NULL, NULL, '3', '已发货，待收货', 96950073392365568, 'bill_status', 1);
INSERT INTO `t_dict_type` VALUES (96962768984342528, NULL, '2018-11-22 11:48:23', NULL, NULL, '4', '交易成功', 96950073392365568, 'bill_status', 4);
INSERT INTO `t_dict_type` VALUES (96963159130112000, NULL, '2018-11-22 11:49:56', NULL, NULL, '5', '交易关闭', 96950073392365568, 'bill_status', 5);
INSERT INTO `t_dict_type` VALUES (96963562651516928, NULL, '2018-11-22 11:51:33', NULL, NULL, '6', '退款中', 96950073392365568, 'bill_status', 6);
INSERT INTO `t_dict_type` VALUES (97357187684237312, NULL, '2018-11-23 13:55:40', NULL, NULL, '1', '充值', 97356870024429568, 'bill_type', 1);
INSERT INTO `t_dict_type` VALUES (96964537789448192, NULL, '2018-11-22 11:55:25', NULL, NULL, '43', '发热', 95684503049076736, 'test', 1);
INSERT INTO `t_dict_type` VALUES (96965184324632576, NULL, '2018-11-22 11:57:59', NULL, NULL, '3', '非人防', 95684503049076736, 'test', 1);
INSERT INTO `t_dict_type` VALUES (96965451875090432, NULL, '2018-11-22 11:59:03', NULL, NULL, '44', '烦烦烦', 95684503049076736, 'test', 1);
INSERT INTO `t_dict_type` VALUES (97354533956485120, NULL, '2018-11-23 13:45:07', NULL, NULL, '3', '3', 95684503049076736, 'test', 3);
INSERT INTO `t_dict_type` VALUES (97366040169676800, NULL, '2018-11-23 14:30:51', NULL, NULL, 'alipay', '支付宝', 97365554750291968, 'payment_type', 1);
INSERT INTO `t_dict_type` VALUES (97366110180999168, NULL, '2018-11-23 14:31:07', NULL, NULL, 'wxpay', '微信', 97365554750291968, 'payment_type', 2);
INSERT INTO `t_dict_type` VALUES (97536125287006208, NULL, '2018-11-24 01:46:42', NULL, NULL, '1', '产品消息', 97535843673047040, 'msg_type', 1);
INSERT INTO `t_dict_type` VALUES (97536221990879232, NULL, '2018-11-24 01:47:05', NULL, NULL, '2', '安全消息', 97535843673047040, 'msg_type', 2);
INSERT INTO `t_dict_type` VALUES (97536285903683584, NULL, '2018-11-24 01:47:21', NULL, NULL, '3', '服务消息', 97535843673047040, 'msg_type', 3);
INSERT INTO `t_dict_type` VALUES (97536343411785728, NULL, '2018-11-24 01:47:34', NULL, NULL, '4', '活动消息', 97535843673047040, 'msg_type', 4);
INSERT INTO `t_dict_type` VALUES (97536415625117696, NULL, '2018-11-24 01:47:51', NULL, NULL, '5', '历史消息', 97535843673047040, 'msg_type', 5);
INSERT INTO `t_dict_type` VALUES (97536494998126592, NULL, '2018-11-24 01:48:10', NULL, NULL, '6', '故障消息', 97535843673047040, 'msg_type', 6);

-- ----------------------------
-- Records of t_dict_type_group
-- ----------------------------
INSERT INTO `t_dict_type_group` VALUES (1, NULL, NULL, NULL, NULL, 'login_type', '登录方式');
INSERT INTO `t_dict_type_group` VALUES (2, NULL, NULL, NULL, NULL, 'status', '状态');
INSERT INTO `t_dict_type_group` VALUES (3, NULL, NULL, NULL, NULL, 'role_type', '角色种类');
INSERT INTO `t_dict_type_group` VALUES (4, NULL, NULL, NULL, NULL, 'sex_type', '性别');
INSERT INTO `t_dict_type_group` VALUES (5, NULL, NULL, NULL, NULL, 'login_status', '登录状态');
INSERT INTO `t_dict_type_group` VALUES (6, NULL, NULL, NULL, NULL, 'alarm_clock_status', '闹钟状态');
INSERT INTO `t_dict_type_group` VALUES (7, NULL, NULL, NULL, NULL, 'menu_type', '菜单类型');
INSERT INTO `t_dict_type_group` VALUES (96950073392365568, NULL, '2018-11-22 10:58:08', NULL, NULL, 'bill_status', '订单状态');
INSERT INTO `t_dict_type_group` VALUES (97356870024429568, NULL, '2018-11-23 13:54:24', NULL, NULL, 'bill_type', '账单类型');
INSERT INTO `t_dict_type_group` VALUES (97365554750291968, NULL, '2018-11-23 14:28:55', NULL, NULL, 'payment_type', '支付方式');
INSERT INTO `t_dict_type_group` VALUES (97535843673047040, NULL, '2018-11-24 01:45:35', NULL, NULL, 'msg_type', '消息类型');
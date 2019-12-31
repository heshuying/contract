
-- ----------------------------
-- Table structure for month_chain_group_order
-- ----------------------------
DROP TABLE IF EXISTS `month_chain_group_order`;
CREATE TABLE `month_chain_group_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `year` int(50) DEFAULT NULL COMMENT '年',
  `month` int(50) DEFAULT NULL COMMENT '月',
  `product_line_code` varchar(20) DEFAULT NULL COMMENT '产品组编码',
  `product_line_name` varchar(200) DEFAULT NULL COMMENT '产品组名称',
  `brand_cdde` varchar(20) DEFAULT '' COMMENT '品牌编码',
  `brand_name` varchar(200) DEFAULT NULL COMMENT '品牌名称',
  `xw_code` varchar(50) DEFAULT NULL COMMENT '用户小微编码',
  `xw_name` varchar(200) DEFAULT NULL COMMENT '用户小微名称',
  `series_code` varchar(50) DEFAULT NULL COMMENT '系列编码',
  `series_name` varchar(200) DEFAULT NULL COMMENT '系列名称',
  `product_code` varchar(50) DEFAULT NULL COMMENT '型号编码',
  `product_name` varchar(200) DEFAULT NULL COMMENT '型号名称',
  `trade_code` varchar(50) DEFAULT NULL COMMENT '中心编码',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '中心名称',
  `wg_code` varchar(50) DEFAULT NULL COMMENT '网格小微编码',
  `wg_name` varchar(200) DEFAULT NULL COMMENT '网格小微名称',
  `order_num` int(11) DEFAULT NULL COMMENT '抢单数量',
  `order_amt` varchar(255) DEFAULT NULL COMMENT '抢单数量金额（万元）',
  `submit_emp` int(11) DEFAULT NULL,
  `submit_time` datetime DEFAULT NULL,
  `add1` varchar(255) DEFAULT NULL COMMENT '达成目标需协同问题-产品竞争力',
  `add2` varchar(255) DEFAULT NULL COMMENT '达成目标需协同问题-价格',
  `add3` varchar(255) DEFAULT NULL COMMENT '达成目标需协同问题-供货',
  `add4` varchar(255) DEFAULT NULL COMMENT '达成目标需协同问题-质量',
  `add5` varchar(255) DEFAULT NULL COMMENT '达成目标需协同问题-服务',
  `add6` varchar(255) DEFAULT '' COMMENT '达成目标需协同问题-营销',
  `add7` varchar(255) DEFAULT '' COMMENT '达成目标需协同问题-其他',
  `add8` varchar(255) DEFAULT '' COMMENT '启用备用字段，A:线上渠道；O：传统渠道；T:自有渠道',
  `add9` varchar(255) DEFAULT '' COMMENT '备用字段，暂不送数',
  `add10` varchar(255) DEFAULT '' COMMENT '备用字段，暂不送数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for sys_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_code`;
CREATE TABLE `sys_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(255) DEFAULT '' COMMENT '代码种类',
  `key` varchar(128) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `type_desc` varchar(255) DEFAULT NULL COMMENT '代码种类描述',
  `value_desc` varchar(255) DEFAULT NULL COMMENT '代码描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_employee_ehr
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee_ehr`;
CREATE TABLE `sys_employee_ehr` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `empSn` varchar(10) DEFAULT '' COMMENT '员工工号',
  `empName` varchar(80) DEFAULT '' COMMENT '员工姓名',
  `posCode` varchar(10) DEFAULT '' COMMENT '岗位编码',
  `posName` varchar(80) DEFAULT '' COMMENT '岗位名称',
  `deptCode` varchar(10) DEFAULT '' COMMENT '员工所在部门编码',
  `deptName` varchar(80) DEFAULT '' COMMENT '员工所在部门名称',
  `nodeCode` varchar(10) DEFAULT '',
  `nodeName` varchar(80) DEFAULT '' COMMENT '小微节点名称',
  `mobileNo` varchar(80) DEFAULT '' COMMENT '电话号码',
  `notesmail` varchar(255) DEFAULT '' COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_employee_z
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee_z`;
CREATE TABLE `sys_employee_z` (
  `ID` int(255) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `empSN` varchar(255) DEFAULT '' COMMENT '工号',
  `empName` varchar(255) DEFAULT '' COMMENT '姓名',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_net
-- ----------------------------
DROP TABLE IF EXISTS `sys_net`;
CREATE TABLE `sys_net` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `center_code` varchar(255) DEFAULT '' COMMENT '中心编码',
  `center_name` varchar(255) DEFAULT '' COMMENT '中心名称',
  `jyt_code` varchar(255) DEFAULT '' COMMENT '经营体编码',
  `jyt_name` varchar(255) DEFAULT '' COMMENT '经营体名称',
  `empSN` varchar(255) DEFAULT '' COMMENT '工号',
  `empName` varchar(255) DEFAULT '' COMMENT '姓名',
  `xw_code` varchar(255) DEFAULT '' COMMENT '网格所属市场小微编码',
  `xw_name` varchar(255) DEFAULT NULL COMMENT '网格所属市场小微名称',
  `net_gdp` varchar(255) DEFAULT '' COMMENT '网格GDP',
  `net_population_count` varchar(255) DEFAULT '' COMMENT '网格人群规模',
  `product_line_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ind_1` (`xw_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_node_ehr
-- ----------------------------
DROP TABLE IF EXISTS `sys_node_ehr`;
CREATE TABLE `sys_node_ehr` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xwCode` varchar(10) DEFAULT '' COMMENT '小微编码',
  `xwName` varchar(80) DEFAULT '' COMMENT '小微名称',
  `nodeCode` varchar(10) DEFAULT '' COMMENT '节点编码',
  `nodeName` varchar(80) DEFAULT '' COMMENT '节点名称',
  `nodeManagerCode` varchar(10) DEFAULT '' COMMENT '节点直线工号',
  `nodeManagerName` varchar(80) DEFAULT '' COMMENT '节点直线姓名',
  `nodeManagerPos` varchar(10) DEFAULT '' COMMENT '节点直线岗位编码',
  `data_type` varchar(10) DEFAULT '' COMMENT '数据类型（01：ODS自动更新数据；02：自定义数据）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `organization_code` varchar(100) DEFAULT NULL COMMENT '机构编码',
  `organization_name` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `organization_type` varchar(100) DEFAULT NULL COMMENT '机构类型',
  `organization_type_name` varchar(255) DEFAULT NULL COMMENT '机构类型名称',
  `organization_classify` varchar(255) DEFAULT NULL COMMENT '小微分类编码',
  `organization_classify_name` varchar(255) DEFAULT NULL COMMENT '小微分类名称',
  `organization_area` varchar(255) DEFAULT NULL COMMENT '机构区域',
  `organization_area_code` varchar(255) DEFAULT NULL COMMENT '机构区域编码',
  `parent_id` int(11) DEFAULT '0' COMMENT '上级id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_process_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_process_conf`;
CREATE TABLE `sys_process_conf` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pt_code` varchar(255) DEFAULT '' COMMENT '平台编码',
  `grab_target_solution_conf` varchar(255) DEFAULT '' COMMENT '抢单目标解决方案配置(“01：开放抢入目标“；“02：指定到型号到小微类型的目标”)',
  `grab_value_solution_conf` varchar(255) DEFAULT NULL COMMENT '“01：抢单量不做控制“；“02：抢单量不能低于举单量”',
  `model_series_solution_conf` varchar(255) DEFAULT '' COMMENT '举单型号选择方案配置（01：人群系列方案；02：型号组合方案）',
  `net_solution_conf` varchar(255) DEFAULT '' COMMENT '网格配置方案（01：有网格；02：无网格）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` varchar(30) NOT NULL  COMMENT '主键',
  `name` varchar(50) NOT NULL,
  `parent_id` varchar(30) DEFAULT NULL,
  `type` int(1) NOT NULL DEFAULT '0',
  `url` varchar(100) DEFAULT NULL,
  `permission` varchar(50) DEFAULT NULL,
  `color` varchar(10) DEFAULT NULL,
  `icon` varchar(30) DEFAULT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  `verification` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user_role_ehr
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_ehr`;
CREATE TABLE `sys_user_role_ehr` (
  `id` int(30) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `empSn` varchar(10) NOT NULL,
  `role_code` varchar(11) NOT NULL,
  `role_name` varchar(30) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`,`empSn`,`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_xiaowei_center
-- ----------------------------
DROP TABLE IF EXISTS `sys_xiaowei_center`;
CREATE TABLE `sys_xiaowei_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xw_type` varchar(255) DEFAULT NULL,
  `pt_code` varchar(255) DEFAULT NULL COMMENT '行业编码',
  `xw_code` varchar(255) DEFAULT NULL COMMENT '小微编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_xiaowei_ehr
-- ----------------------------
DROP TABLE IF EXISTS `sys_xiaowei_ehr`;
CREATE TABLE `sys_xiaowei_ehr` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xwCode` varchar(10) DEFAULT '' COMMENT '小微编码',
  `xwName` varchar(80) DEFAULT '' COMMENT '小微名称',
  `xwMasterCode` varchar(10) DEFAULT '' COMMENT '小微主工号',
  `xwMasterName` varchar(80) DEFAULT '' COMMENT '小微主姓名',
  `ptCode` varchar(10) DEFAULT '' COMMENT '平台编码',
  `ptName` varchar(80) DEFAULT '' COMMENT '平台名称',
  `buCode` varchar(10) DEFAULT '' COMMENT '领域编码',
  `buName` varchar(80) DEFAULT '' COMMENT '领域名称',
  `xwGroupType` varchar(10) DEFAULT NULL COMMENT '链群类型编码  例:1 2 1,2   1：创单  2：体验',
  `xwGroupName` varchar(80) DEFAULT NULL COMMENT '链群类型名称 例:创单 体验 创单,体验',
  `xwStyle` varchar(255) DEFAULT NULL COMMENT '小微行业',
  `xwStyleCode` varchar(255) DEFAULT NULL COMMENT '小微行业编码',
  `xwType` varchar(255) DEFAULT NULL COMMENT '小微类型(1用户2设计3制造)',
  `xwTypeCode` varchar(255) DEFAULT NULL COMMENT '小微类型(1用户2设计3制造)编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_xiaowei_type_ehr
-- ----------------------------
DROP TABLE IF EXISTS `sys_xiaowei_type_ehr`;
CREATE TABLE `sys_xiaowei_type_ehr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_key` int(11) DEFAULT NULL COMMENT '小微类型key',
  `type_value` varchar(255) DEFAULT NULL COMMENT '小微类型value',
  `Status` tinyint(1) DEFAULT NULL,
  `Remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_actual_added_value
-- ----------------------------
DROP TABLE IF EXISTS `z_actual_added_value`;
CREATE TABLE `z_actual_added_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contracts_id` int(11) DEFAULT NULL COMMENT '合约id',
  `model_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL COMMENT '日期',
  `model_code` varchar(255) DEFAULT '' COMMENT '型号编码',
  `model` varchar(255) DEFAULT NULL COMMENT '型号名称',
  `added_value` decimal(10,2) DEFAULT NULL COMMENT '增值空间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_chain_result
-- ----------------------------
DROP TABLE IF EXISTS `z_chain_result`;
CREATE TABLE `z_chain_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `upload_type` varchar(255) DEFAULT NULL COMMENT '上传数据类型',
  `upload_id` int(11) DEFAULT NULL COMMENT '上传数据在自己主表的id',
  `result_code` varchar(255) DEFAULT NULL,
  `result_msg` varchar(255) DEFAULT NULL,
  `err_code` varchar(255) DEFAULT NULL,
  `err_msg` varchar(255) DEFAULT NULL,
  `upload_status` tinyint(1) DEFAULT NULL COMMENT '上链状态',
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_demand_info
-- ----------------------------
DROP TABLE IF EXISTS `z_demand_info`;
CREATE TABLE `z_demand_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demand_code` varchar(255) NOT NULL COMMENT '需求编码',
  `model_id` varchar(255) DEFAULT NULL COMMENT '产品型号ID',
  `model_desc` varchar(256) DEFAULT NULL COMMENT '型号描述',
  `sale_qty` int(15) DEFAULT NULL COMMENT '销量',
  `profit_rate` decimal(12,4) DEFAULT NULL COMMENT '利润率',
  `model_cost` decimal(20,2) DEFAULT NULL COMMENT '成本',
  `pa_date` date DEFAULT NULL COMMENT '开发准时',
  `gross_profit_rate` decimal(12,4) DEFAULT NULL COMMENT '毛利率',
  `market_input` decimal(22,6) DEFAULT NULL COMMENT '营销投入',
  `make_cost` decimal(22,6) DEFAULT NULL COMMENT '单台制造费',
  `make_ratio` decimal(22,2) DEFAULT NULL COMMENT '制造系数',
  `cpbm` varchar(255) DEFAULT NULL COMMENT '成品编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_difference_cause
-- ----------------------------
DROP TABLE IF EXISTS `z_difference_cause`;
CREATE TABLE `z_difference_cause` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(100) DEFAULT NULL COMMENT '时间',
  `industry_trade` varchar(100) DEFAULT NULL COMMENT '工贸',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `plan` varchar(200) DEFAULT NULL COMMENT '计划',
  `actual` varchar(200) DEFAULT NULL COMMENT '实际',
  `type` varchar(10) DEFAULT NULL COMMENT '类型，1口碑服务；2交货进度；3成本竞争力',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_diff_reason
-- ----------------------------
DROP TABLE IF EXISTS `z_diff_reason`;
CREATE TABLE `z_diff_reason` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(255) DEFAULT NULL COMMENT '用户编码',
  `month` int(255) DEFAULT NULL COMMENT '月',
  `year` int(255) DEFAULT NULL COMMENT '年',
  `type` varchar(255) DEFAULT NULL COMMENT '类型：1.收入 2.销量',
  `diff_reason` varchar(255) DEFAULT NULL COMMENT '差因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_dynamic_optimization
-- ----------------------------
DROP TABLE IF EXISTS `z_dynamic_optimization`;
CREATE TABLE `z_dynamic_optimization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `better_type` varchar(255) DEFAULT NULL COMMENT '优化类型:0:组织优化',
  `contracts_id` int(11) DEFAULT NULL,
  `grab_id` int(11) DEFAULT NULL,
  `xwStyleCode` varchar(255) DEFAULT NULL COMMENT '链群名称',
  `xwCode` varchar(11) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL COMMENT '状态：0:可抢单1：不可抢单',
  `update_code` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '抢单表ID',
  `xwMasterCode` varchar(255) DEFAULT NULL COMMENT '小微主编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='动态优化表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `z_contracts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT 0 COMMENT '关联id',
  `contract_name` varchar(200) DEFAULT NULL COMMENT '合约名称',
  `contract_type` varchar(10) DEFAULT NULL COMMENT '10链群主合约、20商圈合约、30创客合约',
  `status` varchar(10) DEFAULT NULL COMMENT '状态：0抢单中，1 已生效,4:已过期',
  `share_space` decimal(10,4) DEFAULT NULL COMMENT '分享空间',
  `share_percent` varchar(80) DEFAULT NULL COMMENT '分享比例',
  `chain_code` varchar(80) DEFAULT NULL COMMENT '链群编码',
  `region_code` varchar(80) DEFAULT NULL COMMENT '地区编码',
  `join_time` datetime DEFAULT NULL COMMENT '抢入截止时间',
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `xiaowei_code` varchar(80) DEFAULT NULL COMMENT 'xw编码',
  `create_code` varchar(80) DEFAULT NULL COMMENT '创建人编码',
  `create_name` varchar(80) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `org_code` varchar(50) DEFAULT '',
  `org_name` varchar(50) DEFAULT '',
  org_type varchar(50) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `z_contracts_factor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contract_id` int DEFAULT NULL COMMENT '合约ID',
  `factor_code` varchar(80) DEFAULT NULL COMMENT '编码',
  `factor_name` varchar(80) DEFAULT NULL COMMENT '名称',
  `factor_value` varchar(80) DEFAULT NULL COMMENT '值',
  `factor_type` varchar(80) DEFAULT NULL COMMENT '类型',
  `factor_unit` varchar(80) DEFAULT NULL COMMENT '单位',
  `factor_directon` char(1) DEFAULT '1' COMMENT '1-正向；0-负向',
  `region_code` varchar(80) DEFAULT NULL COMMENT '地区编码',
  `region_name` varchar(80) DEFAULT NULL COMMENT '地区名称',
  `mesh_code` varchar(80) DEFAULT NULL COMMENT '网格编码',
  `mesh_name` varchar(80) DEFAULT NULL COMMENT '网格名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_hr_chain_info
-- ----------------------------
DROP TABLE IF EXISTS `z_hr_chain_info`;
CREATE TABLE `z_hr_chain_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chain_code` varchar(255) DEFAULT NULL COMMENT '链群编码',
  `chain_name` varchar(255) DEFAULT NULL COMMENT '链群名称',
  `ty_master_xw_code` varchar(255) DEFAULT '' COMMENT '体验链群小微编码',
  `ty_master_xw_name` varchar(255) DEFAULT '' COMMENT '体验链群小微名称',
  `ty_master_empsn` varchar(255) DEFAULT '' COMMENT '体验链群主工号',
  `ty_master_empname` varchar(255) DEFAULT '' COMMENT '体验链群主姓名',
  `ty_region_code` varchar(255) DEFAULT '' COMMENT '体验链群所属区域编码',
  `cd_master_xw_code` varchar(255) DEFAULT '' COMMENT '创单链群小微编码',
  `cd_master_xw_name` varchar(255) DEFAULT '' COMMENT '创单链群小微名称',
  `cd_master_empsn` varchar(255) DEFAULT '' COMMENT '创单链群主工号',
  `cd_master_empname` varchar(255) DEFAULT '' COMMENT '创单链群主姓名',
  `contracts_id` int(11) DEFAULT NULL COMMENT '合约id',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `ty_chain_percents` varchar(255) DEFAULT '' COMMENT '体验链群实际分享比例',
  `cd_chain_percents` varchar(255) DEFAULT '' COMMENT '创单链群实际分享比例',
  `market_percents` varchar(255) DEFAULT '' COMMENT '市场整体分享比例（体验链群分享比例中）',
  `net_percents` varchar(255) DEFAULT '' COMMENT '市场整体分享比例（体验链群分享比例中）',
  `plan_setps` varchar(255) DEFAULT '' COMMENT '预计分享台阶比例',
  `share_setps` varchar(255) DEFAULT '' COMMENT '实际分享台阶比例',
  `chain_pt_code` varchar(50) NOT NULL DEFAULT '' COMMENT '链群平台code',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_lq_info_edw
-- ----------------------------
DROP TABLE IF EXISTS `z_lq_info_edw`;
CREATE TABLE `z_lq_info_edw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contracts_id` int(11) DEFAULT NULL,
  `chain_code` varchar(255) DEFAULT NULL,
  `contracts_name` varchar(255) DEFAULT NULL,
  `ty_region_code` varchar(255) DEFAULT NULL,
  `create_code` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `grab_order_code` varchar(50) DEFAULT NULL,
  `grab_order_name` varchar(50) DEFAULT NULL,
  `xw_code` varchar(100) DEFAULT NULL,
  `xw_name` varchar(100) DEFAULT NULL,
  `model_code` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `series` varchar(100) DEFAULT NULL,
  `title_code` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `bottom_target` decimal(10,2) DEFAULT NULL,
  `create_target` varchar(255) DEFAULT NULL,
  `grab_target` decimal(20,2) DEFAULT NULL,
  `hashcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_mesh
-- ----------------------------
DROP TABLE IF EXISTS `z_mesh`;
CREATE TABLE `z_mesh` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model` varchar(255) DEFAULT '' COMMENT '型号',
  `mesh_xw` varchar(255) DEFAULT '' COMMENT '网格名称',
  `target_quantity` varchar(255) DEFAULT NULL COMMENT '销量',
  `date` varchar(255) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_mesh_radio
-- ----------------------------
DROP TABLE IF EXISTS `z_mesh_radio`;
CREATE TABLE `z_mesh_radio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢单主键',
  `model` varchar(255) DEFAULT NULL COMMENT '型号',
  `target_quantity` varchar(255) DEFAULT NULL COMMENT '销量',
  `mesh_radio` varchar(255) DEFAULT NULL COMMENT '网格分享比例',
  `mesh_xw` varchar(255) DEFAULT NULL COMMENT '网格小微',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_mesh_target
-- ----------------------------
DROP TABLE IF EXISTS `z_mesh_target`;
CREATE TABLE `z_mesh_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `year` varchar(10) DEFAULT NULL COMMENT '年',
  `month` varchar(10) DEFAULT NULL COMMENT '月',
  `product` varchar(50) DEFAULT NULL COMMENT '产品组',
  `brand` varchar(20) DEFAULT NULL COMMENT '品牌',
  `xw_code` varchar(100) DEFAULT NULL COMMENT '小微姓名',
  `series` varchar(50) DEFAULT NULL COMMENT '系列',
  `maori_segment` varchar(100) DEFAULT NULL COMMENT '毛利段',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `model_code` varchar(255) DEFAULT NULL COMMENT '型号编码',
  `mesh_xw` varchar(100) DEFAULT NULL COMMENT '网格小微',
  `same_quantity` varchar(100) DEFAULT NULL COMMENT '同期数量',
  `same_amount` varchar(100) DEFAULT NULL COMMENT '同期金额',
  `target_quantity` varchar(100) DEFAULT NULL COMMENT '抢单目标数量',
  `target_amount` varchar(100) DEFAULT NULL COMMENT '抢单目标金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_mesh_target_copy
-- ----------------------------
DROP TABLE IF EXISTS `z_mesh_target_copy`;
CREATE TABLE `z_mesh_target_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `year` varchar(10) DEFAULT NULL COMMENT '年',
  `month` varchar(10) DEFAULT NULL COMMENT '月',
  `product` varchar(50) DEFAULT NULL COMMENT '产品组',
  `brand` varchar(20) DEFAULT NULL COMMENT '品牌',
  `xw_code` varchar(100) DEFAULT NULL COMMENT '小微姓名',
  `series` varchar(50) DEFAULT NULL COMMENT '系列',
  `maori_segment` varchar(100) DEFAULT NULL COMMENT '毛利段',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `model` varchar(100) DEFAULT NULL COMMENT '型号',
  `mesh_xw` varchar(100) DEFAULT NULL COMMENT '网格小微',
  `same_quantity` varchar(100) DEFAULT NULL COMMENT '同期数量',
  `same_amount` varchar(100) DEFAULT NULL COMMENT '同期金额',
  `target_quantity` varchar(100) DEFAULT NULL COMMENT '抢单目标数量',
  `target_amount` varchar(100) DEFAULT NULL COMMENT '抢单目标金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_multiple_coefficient
-- ----------------------------
DROP TABLE IF EXISTS `z_multiple_coefficient`;
CREATE TABLE `z_multiple_coefficient` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `period_code` date DEFAULT NULL COMMENT '合约时间',
  `grab_id` int(11) DEFAULT NULL COMMENT '抢单主表id',
  `initial_coefficient` decimal(10,2) DEFAULT NULL COMMENT '初算系数',
  `final_coefficient` decimal(10,2) DEFAULT NULL COMMENT '终算系数',
  `balance_coefficient` decimal(10,2) DEFAULT NULL COMMENT '结余系数',
  `xcoefficient` decimal(10,2) DEFAULT NULL COMMENT '横轴系数',
  `ycoefficient` decimal(10,2) DEFAULT NULL COMMENT '纵轴系数',
  `share_quota` decimal(10,2) DEFAULT NULL,
  `create_date` date DEFAULT NULL COMMENT '计算时间',
  `series` varchar(255) DEFAULT NULL COMMENT '系列',
  `model_code` varchar(255) DEFAULT NULL COMMENT '型号编码',
  `model` varchar(255) DEFAULT NULL COMMENT '型号名称',
  `xw_name` varchar(255) DEFAULT NULL COMMENT '小微名称',
  `xw_code` varchar(255) DEFAULT NULL COMMENT '小微code',
  `bottom_sales` varchar(255) DEFAULT NULL COMMENT '底线销量',
  `jud_sales` varchar(255) DEFAULT NULL COMMENT '举单销量',
  `qiangd_sales` varchar(255) DEFAULT NULL COMMENT '抢单销量',
  `shij_sales` varchar(255) DEFAULT NULL COMMENT '实际销量',
  `shij_share_added` varchar(255) DEFAULT NULL COMMENT '实际分享',
  `share_steps` varchar(255) DEFAULT NULL COMMENT '链群分享台阶',
  `share_proportion` varchar(255) DEFAULT NULL COMMENT '链群类型分享比例(%',
  `xw_share_proportion` varchar(255) DEFAULT NULL COMMENT '小微链群分享比例(%)',
  `xw_share_coefficient` varchar(255) DEFAULT NULL COMMENT '小微分享加速系数',
  `series_share_quota` varchar(255) DEFAULT NULL COMMENT '系列分享额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_multiple_coefficient_new
-- ----------------------------
DROP TABLE IF EXISTS `z_multiple_coefficient_new`;
CREATE TABLE `z_multiple_coefficient_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `period_code` varchar(255) DEFAULT '' COMMENT '合约年月',
  `lq_code` varchar(255) DEFAULT '' COMMENT '链群编码',
  `lq_name` varchar(255) DEFAULT '' COMMENT '链群名称',
  `contract_id` varchar(255) DEFAULT '' COMMENT '合约ID',
  `grab_id` int(11) DEFAULT NULL COMMENT '抢单主表id',
  `model_code` varchar(255) DEFAULT '' COMMENT '型号编码',
  `model_name` varchar(255) DEFAULT '' COMMENT '型号名称',
  `series` varchar(255) DEFAULT '' COMMENT '型号系列',
  `xw_code` varchar(255) DEFAULT '' COMMENT '小微编码',
  `xw_name` varchar(255) DEFAULT '' COMMENT '小微名称',
  `chain_expected_percents` varchar(255) DEFAULT '' COMMENT '链群预期分享比例',
  `chain_actual_percents` varchar(255) DEFAULT '' COMMENT '链群实际分享比例',
  `expected_share_added` varchar(255) DEFAULT '' COMMENT '抢单增值空间',
  `actual_share_added` varchar(255) DEFAULT '' COMMENT '实际增值空间',
  `expected_steps` varchar(255) DEFAULT '' COMMENT '链群预期分享台阶',
  `actual_steps` varchar(255) DEFAULT '' COMMENT '链群实际分享台阶',
  `share_ratio` varchar(255) DEFAULT '' COMMENT '小微分享比例',
  `expected_share_quota` decimal(10,2) DEFAULT NULL COMMENT '预期分享额',
  `xw_socre` varchar(10) DEFAULT '' COMMENT '小微五星评价',
  `share_quota` decimal(10,2) DEFAULT NULL COMMENT '实际分享额',
  `create_date` date DEFAULT NULL COMMENT '计算时间',
  `chain_pt_code` varchar(20) DEFAULT '' COMMENT '链群平台编码',
  `org_code` varchar(255) DEFAULT NULL,
  `org_name` varchar(255) DEFAULT NULL,
  `grab_order_code` varchar(50) DEFAULT NULL COMMENT '抢单人编码',
  `grab_order_name` varchar(50) DEFAULT '' COMMENT '抢单人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_net_bottom
-- ----------------------------
DROP TABLE IF EXISTS `z_net_bottom`;
CREATE TABLE `z_net_bottom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pmai_area_code` varchar(50) DEFAULT '' COMMENT '网格经营体编码',
  `pmai_area_name` varchar(50) DEFAULT '' COMMENT '网格经营体名称',
  `model_code` varchar(50) DEFAULT '' COMMENT '型号编码',
  `model_name` varchar(50) DEFAULT '' COMMENT '型号名称',
  `target_code` varchar(50) DEFAULT '' COMMENT '目标编码',
  `target_name` varchar(50) DEFAULT '' COMMENT '目标名称',
  `target_bottom_count` varchar(50) DEFAULT '' COMMENT '目标底线量',
  `target_create_count` varchar(50) DEFAULT '' COMMENT '目标举单量',
  `period_code` varchar(50) DEFAULT '' COMMENT '作用年月',
  `pt_code` varchar(20) DEFAULT '' COMMENT '平台编码',
  `create_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_order
-- ----------------------------
DROP TABLE IF EXISTS `z_order`;
CREATE TABLE `z_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `order_name` varchar(4000) DEFAULT NULL COMMENT '单名称',
  `order_target` varchar(4000) DEFAULT NULL COMMENT '单目标',
  `order_flag` varchar(255) DEFAULT NULL COMMENT '单标识，1默认  2自建',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_product_acturl
-- ----------------------------
DROP TABLE IF EXISTS `z_product_acturl`;
CREATE TABLE `z_product_acturl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_code` varchar(255) DEFAULT NULL COMMENT '日期',
  `pl_code` varchar(255) DEFAULT NULL COMMENT '产业编码',
  `pl_name` varchar(255) DEFAULT NULL COMMENT '产业名称',
  `region_code` varchar(255) DEFAULT NULL COMMENT '区域编码',
  `region_name` varchar(255) DEFAULT NULL COMMENT '区域名称',
  `mat_code` varchar(255) DEFAULT NULL COMMENT '型号编码',
  `mat_name` varchar(255) DEFAULT NULL COMMENT '型号名称',
  `amt` decimal(10,2) DEFAULT NULL COMMENT '收入',
  `qty` decimal(10,2) DEFAULT NULL COMMENT '销量',
  `create_date` date DEFAULT NULL COMMENT '创建时间',
  `lirun` decimal(10,2) DEFAULT NULL COMMENT '利润',
  `bomcb` decimal(10,2) DEFAULT NULL COMMENT 'bom成本',
  `dtcb` decimal(10,2) DEFAULT NULL COMMENT '单台成本',
  `xssr` decimal(10,2) DEFAULT NULL COMMENT '销售收入',
  `fee` decimal(10,2) DEFAULT NULL COMMENT '费用',
  `pa_date_actual` varchar(255) DEFAULT NULL COMMENT '上市时间',
  `is_zx` varchar(255) DEFAULT NULL COMMENT '是否在销',
  `ibprice` decimal(10,2) DEFAULT NULL COMMENT '供价',
  `lq_name` varchar(255) DEFAULT NULL COMMENT '链群',
  `qty_DX` decimal(10,2) DEFAULT NULL COMMENT '当月底线销量',
  `qty_TQ` decimal(10,2) DEFAULT NULL COMMENT '同期销量',
  `PRODUCT_XL_CODE` varchar(255) DEFAULT NULL COMMENT '产品系列编码',
  `RQ` varchar(255) DEFAULT NULL COMMENT '人群编码',
  `IS_HOT` varchar(255) DEFAULT NULL COMMENT '是否是爆款',
  `mlv` decimal(10,2) DEFAULT NULL COMMENT '毛利率',
  `blv` decimal(10,2) DEFAULT NULL COMMENT '不良率',
  `jhv` decimal(10,2) DEFAULT NULL COMMENT '按期交货率',
  `sy_lirun` decimal(10,4) DEFAULT NULL COMMENT '损益表-利润额',
  `sy_lirunlv` decimal(10,4) DEFAULT NULL COMMENT '损益表-利润率',
  `sy_jsr` decimal(10,4) DEFAULT NULL COMMENT '损益表-净销售收入',
  `sy_zzfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-制造费率',
  `sy_wlfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-物流费率',
  `sy_bxfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-保修费率',
  `sy_yffv` decimal(10,4) DEFAULT NULL COMMENT '损益表-研发费率',
  `sy_scfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-市场费率',
  `sy_glfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-管理费率',
  `sy_jjfv` decimal(10,4) DEFAULT NULL COMMENT '损益表-间接费率',
  `cha_songzhuang` decimal(10,2) DEFAULT NULL COMMENT '送装零延误差',
  `lv_songzhuang` decimal(10,2) DEFAULT NULL COMMENT '送装零延误率',
  `cha_zhiliang` decimal(10,2) DEFAULT NULL COMMENT '质量零残次差',
  `lv_zhiliang` decimal(10,2) DEFAULT NULL COMMENT '质量零残次率',
  `kszdl` decimal(10,2) DEFAULT NULL COMMENT '客诉订单量',
  `cpzdl` decimal(10,2) DEFAULT NULL COMMENT '差评订单量',
  `cha_kesu` decimal(10,2) DEFAULT NULL COMMENT '运营零客诉差',
  `lv_kesu` decimal(10,2) DEFAULT NULL COMMENT '运营零客诉率',
  `cha_chaping` decimal(10,2) DEFAULT NULL COMMENT '客服零差评差',
  `lv_chaping` decimal(10,2) DEFAULT NULL COMMENT '客服零差评率',
  `shichang` decimal(10,2) DEFAULT NULL COMMENT '管送全链路时长',
  `IT_FEE` varchar(50) DEFAULT '' COMMENT '房车-IT费用',
  `FINANCE_FEE` varchar(50) DEFAULT '' COMMENT '房车-财务费用',
  `FEE_COST_RATE` varchar(50) DEFAULT '' COMMENT '房车-资金成本率',
  `TAX_FEE` varchar(50) DEFAULT '' COMMENT '房车-税费',
  `BU_FEE` varchar(50) DEFAULT '' COMMENT '房车-BU费及支持节点分摊费',
  `LOSS_ADJUSTMENTS` varchar(50) DEFAULT '' COMMENT '房车-损失调整项',
  `PROD_NET_COST` varchar(50) DEFAULT '' COMMENT '房车-产品未含税成本',
  `EMPLOYEE_COST` varchar(50) DEFAULT '' COMMENT '房车-人工成本',
  `ORGANIZE_FEE` varchar(50) DEFAULT '' COMMENT '房车-组织运营费',
  `created_date` date DEFAULT NULL COMMENT '创建_日期',
  `load_batch` varchar(30) DEFAULT '' COMMENT '加载批次',
  `data_source` varchar(30) DEFAULT '' COMMENT '数据来源',
  `xiaow_code` varchar(25) DEFAULT '' COMMENT '小微编码',
  `center_guizi_income` decimal(10,4) DEFAULT NULL COMMENT '中心柜收入',
  `sum_guizi_income` decimal(10,4) DEFAULT NULL COMMENT '柜收入（总）',
  `d_guizi_zero` decimal(10,4) DEFAULT NULL COMMENT 'D类柜清零',
  `daily_flow_count` decimal(10,4) DEFAULT NULL COMMENT '日流量',
  `ad_income` decimal(10,4) DEFAULT NULL COMMENT '广告收入',
  `profit_guizi_count` decimal(10,4) DEFAULT NULL COMMENT '盈利柜数量',
  `sum_profit` decimal(10,4) DEFAULT NULL COMMENT '利润额（总）',
  `zero_delays` decimal(10,4) DEFAULT NULL COMMENT '送用户零延误',
  `quality_zero_defect` decimal(10,4) DEFAULT NULL COMMENT '质量零残次 ',
  `zero_customer_complaint` decimal(10,4) DEFAULT NULL COMMENT '零客诉',
  `zero_negative_comment` decimal(10,4) DEFAULT NULL COMMENT '零差评',
  `seven_store_link_time` decimal(10,4) DEFAULT NULL COMMENT '七仓出发线路全链路时长',
  PRIMARY KEY (`id`),
  KEY `idx_ma` (`mat_name`(7)),
  KEY `idx_p` (`period_code`,`region_code`) USING BTREE,
  KEY `idx_mc` (`mat_code`(9)),
  KEY `idx_ac` (`period_code`,`region_code`,`mat_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_product_acturl_net
-- ----------------------------
DROP TABLE IF EXISTS `z_product_acturl_net`;
CREATE TABLE `z_product_acturl_net` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `amt` varchar(50) DEFAULT '' COMMENT '销售收入',
  `amt_tax` varchar(50) DEFAULT '' COMMENT '销售收入（含税）',
  `qty` varchar(50) DEFAULT '' COMMENT '实际销量',
  `net_code` varchar(50) DEFAULT '' COMMENT '网格编码',
  `net_name` varchar(50) DEFAULT '' COMMENT '网格名称',
  `net_xw_code` varchar(50) DEFAULT '' COMMENT '网格小微/经营体编码',
  `net_xw_name` varchar(50) DEFAULT '' COMMENT '网格小微/经营体名称',
  `net_master` varchar(50) DEFAULT '' COMMENT '网格主工号',
  `region_code` varchar(50) DEFAULT '' COMMENT '工贸编码',
  `region_name` varchar(50) DEFAULT '' COMMENT '工贸名称',
  `period_code` varchar(50) DEFAULT '' COMMENT '日历年/月',
  `model_code` varchar(50) DEFAULT '' COMMENT '型号编码',
  `model_name` varchar(50) DEFAULT '' COMMENT '型号名称',
  `sales_org` varchar(50) DEFAULT '' COMMENT '销售组织编码',
  `distr_chan` varchar(50) DEFAULT '' COMMENT '分销渠道编码',
  `zsd_hfje` varchar(50) DEFAULT '' COMMENT '后返金额（含税）',
  `zsd_flje` varchar(50) DEFAULT '' COMMENT '返利金额（含税）',
  `zsdg_xwdm` varchar(50) DEFAULT '' COMMENT '用户小微编码',
  `zmdm_pl` varchar(50) DEFAULT '' COMMENT 'PL编码',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`ID`),
  KEY `idx_pc` (`period_code`(6)) USING BTREE,
  KEY `idx_mn` (`model_name`(7)) USING BTREE,
  KEY `idx_mc` (`model_code`(7)) USING BTREE,
  KEY `idx_rc` (`region_code`(7)) USING BTREE,
  KEY `idx_rn` (`region_name`(7)) USING BTREE,
  KEY `idx_jyt` (`net_xw_code`,`period_code`,`model_code`) USING BTREE,
  KEY `ind_act` (`period_code`,`model_code`,`region_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_product_detail
-- ----------------------------
DROP TABLE IF EXISTS `z_product_detail`;
CREATE TABLE `z_product_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `target_code` varchar(255) DEFAULT '' COMMENT '目标编码',
  `target_name` varchar(255) DEFAULT '' COMMENT '目标名称',
  `target_des` varchar(255) DEFAULT '' COMMENT '目标描述',
  `target_unit` varchar(255) DEFAULT '' COMMENT '目标单位',
  `target_to` varchar(255) DEFAULT '' COMMENT '目标方向（1：整型；0：负向）',
  `target_pt_code` varchar(255) DEFAULT '' COMMENT '平台编码',
  `target_xw_category_code` varchar(255) DEFAULT '' COMMENT '目标承接小微类型Code',
  `target_xw_category_name` varchar(255) DEFAULT '' COMMENT '小微名称',
  `targe_year` varchar(255) DEFAULT '' COMMENT '目标底线年度',
  `target_month` varchar(255) DEFAULT '' COMMENT '目标底线月度',
  `create_date` date DEFAULT NULL COMMENT '创建时间',
  `target_model` varchar(255) DEFAULT '' COMMENT '目标底线型号',
  `target_bottom_line` varchar(255) DEFAULT '' COMMENT '目标底线',
  `target_jd_line` varchar(255) DEFAULT '' COMMENT '目标举单线',
  `target_logic` varchar(255) DEFAULT '' COMMENT '目标完成度核算逻辑',
  `target_weight` varchar(255) DEFAULT '' COMMENT '权重',
  `target_percent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_product_info
-- ----------------------------
DROP TABLE IF EXISTS `z_product_info`;
CREATE TABLE `z_product_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `product_series` varchar(32) DEFAULT NULL COMMENT ' 产品系列',
  `product_model` varchar(32) DEFAULT NULL COMMENT '产品信号',
  `product_model_code` varchar(32) DEFAULT NULL COMMENT '型号编码',
  `product_stru` varchar(10) DEFAULT '' COMMENT '产品结构（01：低端；02：中端；03：高端）',
  `pt_code` varchar(20) DEFAULT '' COMMENT '产品对应平台编码',
  `company` varchar(255) DEFAULT NULL,
  `is_net_device` varchar(10) DEFAULT '' COMMENT '是否是网器产品（01：是；）',
  `is_scene` varchar(10) DEFAULT '' COMMENT '是否是生态场景产品（01：是；）',
  `product_status` varchar(10) DEFAULT '' COMMENT '型号状态（01：在销；02：下市）',
  `period_code` varchar(20) DEFAULT '' COMMENT '年月',
  xw_code varchar(50) not null default '' comment '小微编码',
  xw_name varchar(50) not null default '' comment '小微名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_pt_series
-- ----------------------------
DROP TABLE IF EXISTS `z_pt_series`;
CREATE TABLE `z_pt_series` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pt_code` varchar(255) DEFAULT NULL COMMENT '平台编码',
  `product_series` varchar(32) DEFAULT NULL COMMENT '系列编号',
  `product_model_code` varchar(255) DEFAULT NULL COMMENT '型号编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan`;
CREATE TABLE `z_reserve_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `order_type` varchar(20) DEFAULT NULL COMMENT '单属性',
  `title` varchar(4000) DEFAULT NULL COMMENT '标题',
  `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `remind_type` int(11) DEFAULT NULL COMMENT '提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天',
  `remind_time` int(11) DEFAULT NULL COMMENT '提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月',
  `is_important` int(11) DEFAULT NULL COMMENT '是否重要：0/1 - 是/否',
  `executer` varchar(400) DEFAULT NULL COMMENT '执行人（多选时，逗号分隔）',
  `teamworker` varchar(400) DEFAULT NULL COMMENT '抄送人（多选时，号分割）',
  `create_user_code` varchar(100) DEFAULT NULL COMMENT '创建人编码',
  `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建人名字',
  `create_user_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_code` varchar(200) DEFAULT NULL COMMENT '任务编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8


-- ----------------------------
-- Table structure for z_reserve_plan_detail
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_detail`;
CREATE TABLE `z_reserve_plan_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '关联预案表id',
  `content` varchar(4000) DEFAULT NULL COMMENT '内容描述/方案',
  `cost` varchar(255) DEFAULT NULL COMMENT '成本',
  `term` varchar(100) DEFAULT NULL COMMENT '期限',
  `liable` varchar(20) DEFAULT NULL COMMENT '负责人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_detail_his
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_detail_his`;
CREATE TABLE `z_reserve_plan_detail_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '关联预案表id',
  `content` varchar(4000) DEFAULT NULL COMMENT '内容描述/方案',
  `cost` varchar(255) DEFAULT NULL COMMENT '成本',
  `term` varchar(100) DEFAULT NULL COMMENT '期限',
  `liable` varchar(20) DEFAULT NULL COMMENT '负责人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_his
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_his`;
CREATE TABLE `z_reserve_plan_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `order_type` varchar(20) DEFAULT NULL COMMENT '单属性',
  `title` varchar(4000) DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_second
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_second`;
CREATE TABLE `z_reserve_plan_second` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `order_type` varchar(20) DEFAULT NULL COMMENT '单属性',
  `title` varchar(255) DEFAULT NULL COMMENT '竞争力目标',
  `title_code` varchar(255) DEFAULT NULL,
  `bottom_target` decimal(10,2) DEFAULT NULL COMMENT '底线目标',
  `grab_target` decimal(20,2) DEFAULT NULL COMMENT '抢单目标',
  `create_target` varchar(255) DEFAULT NULL COMMENT '举单目标',
  `orderFlag` varchar(255) DEFAULT NULL,
  `model_code` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL COMMENT '型号',
  `target_unit` varchar(255) DEFAULT NULL COMMENT '目标单位',
  `target_to` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_second_detail
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_second_detail`;
CREATE TABLE `z_reserve_plan_second_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `stage` int(255) DEFAULT NULL COMMENT '路径',
  `stage_date` date DEFAULT NULL COMMENT '路径时间',
  `stage_target` decimal(10,2) DEFAULT NULL COMMENT '路径目标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_second_detail_his
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_second_detail_his`;
CREATE TABLE `z_reserve_plan_second_detail_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `stage` int(255) DEFAULT NULL COMMENT '路径',
  `stage_date` date DEFAULT NULL COMMENT '路径时间',
  `stage_target` decimal(10,2) DEFAULT NULL COMMENT '路径目标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_reserve_plan_second_his
-- ----------------------------
DROP TABLE IF EXISTS `z_reserve_plan_second_his`;
CREATE TABLE `z_reserve_plan_second_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT NULL COMMENT '抢入主表关联id',
  `order_type` varchar(20) DEFAULT NULL COMMENT '单属性',
  `title` varchar(255) DEFAULT NULL COMMENT '竞争力目标',
  `title_code` varchar(255) DEFAULT NULL,
  `bottom_target` int(255) DEFAULT NULL COMMENT '底线目标',
  `grab_target` decimal(20,2) DEFAULT NULL COMMENT '抢单目标',
  `create_target` varchar(255) DEFAULT NULL COMMENT '举单目标',
  `orderFlag` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL COMMENT '型号',
  `target_unit` varchar(255) DEFAULT NULL COMMENT '目标单位',
  `target_to` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_risk_free
-- ----------------------------
DROP TABLE IF EXISTS `z_risk_free`;
CREATE TABLE `z_risk_free` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `period_code` varchar(20) DEFAULT NULL COMMENT '年月',
  `lq_code` varchar(20) DEFAULT NULL COMMENT '链群编码',
  `contracts_id` varchar(20) DEFAULT NULL COMMENT '合约编码',
  `model_code` varchar(20) DEFAULT NULL COMMENT '型号编码',
  `model_name` varchar(20) DEFAULT '' COMMENT '型号名称',
  `xw_code` varchar(20) DEFAULT NULL COMMENT '市场小微编码',
  `xw_name` varchar(20) DEFAULT NULL COMMENT '市场小微名称',
  `lq_type_code` varchar(20) DEFAULT NULL COMMENT '链群类型编码（1：创单链群；2：体验链群）',
  `lq_type_name` varchar(20) DEFAULT NULL COMMENT '链群类型名称',
  `total_share` varchar(20) DEFAULT NULL COMMENT '总分享额',
  `risk_fee` varchar(20) DEFAULT NULL COMMENT '风险金（正数：风险金增加；负数：风险金兑现）',
  `share_quota` varchar(20) DEFAULT NULL COMMENT '实际分享额',
  `pre_risk_fee` varchar(20) DEFAULT NULL COMMENT '历史累计风险金',
  `total_risk_fee` varchar(20) DEFAULT NULL COMMENT '剩余风险金',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_score_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `z_score_evaluate`;
CREATE TABLE `z_score_evaluate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contracts_id` int(11) DEFAULT NULL COMMENT '合约主id',
  `grab_id` int(11) DEFAULT NULL COMMENT '抢单主表id',
  `score` varchar(255) DEFAULT NULL COMMENT '评分',
  `evaluate` varchar(255) DEFAULT NULL COMMENT '评价',
  `create_user` varchar(255) DEFAULT NULL COMMENT '评价人',
  `create_date` datetime DEFAULT NULL COMMENT '评价时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_share_percent
-- ----------------------------
DROP TABLE IF EXISTS `z_share_percent`;
CREATE TABLE `z_share_percent` (
  `ID` int(255) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_code` varchar(255) DEFAULT '' COMMENT '型号编码',
  `model_name` varchar(255) DEFAULT '' COMMENT '型号名称',
  `xw_code` varchar(255) DEFAULT '' COMMENT '小微编码',
  `xw_name` varchar(255) DEFAULT '' COMMENT '小微名称',
  `lq_code` varchar(255) DEFAULT '' COMMENT '链群编码',
  `lq_name` varchar(255) DEFAULT '' COMMENT '链群名称',
  `percent` varchar(255) DEFAULT '' COMMENT '分享比例',
  `period_code` varchar(255) DEFAULT '' COMMENT '年月',
  `company` varchar(255) DEFAULT '' COMMENT '公司-用于区分导数区分',
  `date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据时间',
  `region_code` varchar(255) DEFAULT NULL COMMENT '地区编码',
  `org_type` varchar(10) DEFAULT '' COMMENT '组织类型（1：节点；2：小微；3：创客；4：网格）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for z_share_percent_emp
-- ----------------------------
DROP TABLE IF EXISTS `z_share_percent_emp`;
CREATE TABLE `z_share_percent_emp` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_code` varchar(20) DEFAULT '' COMMENT '节点编码',
  `node_name` varchar(20) DEFAULT '' COMMENT '节点名称',
  `empsn` varchar(20) DEFAULT '' COMMENT '员工号',
  `empname` varchar(20) DEFAULT '' COMMENT '员工姓名',
  `percents` varchar(10) DEFAULT '' COMMENT '分享比例',
  `contracts_id` varchar(20) DEFAULT '' COMMENT '合约编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_target_basic
-- ----------------------------
DROP TABLE IF EXISTS `z_target_basic`;
CREATE TABLE `z_target_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `target_code` varchar(255) DEFAULT '' COMMENT '目标编码',
  `target_name` varchar(255) DEFAULT '' COMMENT '目标名称',
  `target_des` varchar(255) DEFAULT '' COMMENT '目标描述',
  `target_unit` varchar(255) DEFAULT '' COMMENT '目标单位',
  `target_to` varchar(255) DEFAULT '' COMMENT '目标方向（1：正向；0：负向）',
  `judan_flag` varchar(10) DEFAULT '' COMMENT '举单标识（0或空：非举单目标；1：举单目标）',
  `target_pt_code` varchar(255) DEFAULT '' COMMENT '平台编码',
  `target_diff_type` varchar(255) DEFAULT '' COMMENT '目标所属预实差类型(01：按照抢单目标平均合约所属月度的自然天数，并结合实际达成数据进行计算；02：始终按照抢单目标进行计算)',
  `target_dim` varchar(255) DEFAULT '' COMMENT '目标维度范围:01|02|03',
  `target_process_type` varchar(255) DEFAULT '' COMMENT '目标流程类型01:爆款02“新需求',
  `target_xw_category_code` varchar(255) DEFAULT '' COMMENT '目标承接小微类型Code',
  `target_xw_category_name` varchar(255) DEFAULT '' COMMENT '小微名称',
  `target_DSLogic_code` varchar(255) DEFAULT '' COMMENT '目标实际数据源',
  `targe_year` varchar(255) DEFAULT '' COMMENT '目标底线年度',
  `target_month` varchar(255) DEFAULT '' COMMENT '目标底线月度',
  `target_model` varchar(255) DEFAULT '' COMMENT '目标底线型号',
  `target_model_code` varchar(255) DEFAULT NULL COMMENT '目标底线型号',
  `target_bottom_line` varchar(255) DEFAULT NULL COMMENT '目标底线',
  `target_jd_line` varchar(255) DEFAULT NULL COMMENT '目标举单线',
  `target_logic` varchar(255) DEFAULT '' COMMENT '目标完成度核算逻辑',
  `target_weight` varchar(255) DEFAULT '' COMMENT '权重',
  `target_region_code` varchar(255) DEFAULT '' COMMENT '区域编码',
  `target_region_name` varchar(255) DEFAULT '' COMMENT '区域名称',
  `role_code` varchar(255) DEFAULT NULL COMMENT '目标对应的角色编码，小微/节点/人',
  `role_name` varchar(255) DEFAULT NULL COMMENT '目标对应的角色名称，小微/节点/人',
  `period_code` varchar(255) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_target_for_edw
-- ----------------------------
DROP TABLE IF EXISTS `z_target_for_edw`;
CREATE TABLE `z_target_for_edw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` varchar(255) DEFAULT NULL,
  `month` varchar(255) DEFAULT NULL,
  `model_code` varchar(255) DEFAULT NULL,
  `model_name` varchar(255) DEFAULT NULL,
  `target_count` varchar(255) DEFAULT NULL,
  `target_income` varchar(255) DEFAULT NULL,
  `target_profit` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `target_judan` varchar(255) DEFAULT NULL,
  `target_judan_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_trend_better
-- ----------------------------
DROP TABLE IF EXISTS `z_trend_better`;
CREATE TABLE `z_trend_better` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chain_name` varchar(255) DEFAULT NULL COMMENT '链群名称',
  `contract_id` int(11) DEFAULT NULL,
  `pd_id` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `grab_order_code` varchar(255) DEFAULT NULL COMMENT '抢单表ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='动态优化表';

-- ----------------------------
-- Table structure for z_user_info
-- ----------------------------
DROP TABLE IF EXISTS `z_user_info`;
CREATE TABLE `z_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '小微',
  `xiaowei_code` varchar(255) DEFAULT NULL COMMENT '小微编码',
  `xiaowei_name` varchar(255) DEFAULT NULL COMMENT '小微名称',
  `people_code` varchar(255) DEFAULT NULL COMMENT '人员编码',
  `people_name` varchar(255) DEFAULT NULL COMMENT '人员姓名',
  `classify` varchar(255) DEFAULT NULL COMMENT '分类',
  `xiaowei_type` varchar(255) DEFAULT NULL COMMENT '小微类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_user_order_info
-- ----------------------------
DROP TABLE IF EXISTS `z_user_order_info`;
CREATE TABLE `z_user_order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `xiaowei_name` varchar(255) DEFAULT NULL COMMENT '小微名称',
  `xiaowie_code` varchar(255) DEFAULT NULL COMMENT '小微编码',
  `people_code` varchar(255) DEFAULT NULL COMMENT '人员编码',
  `people_name` varchar(255) DEFAULT NULL COMMENT '人员名称',
  `order_name` varchar(255) DEFAULT NULL COMMENT '单名称',
  `order_target` varchar(255) DEFAULT NULL COMMENT '单目标',
  `month` varchar(255) DEFAULT NULL COMMENT '月份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_xiaowei_info
-- ----------------------------
DROP TABLE IF EXISTS `z_xiaowei_info`;
CREATE TABLE `z_xiaowei_info` (
  `id` int(11) DEFAULT NULL,
  `xiaowei_code` varchar(255) DEFAULT NULL,
  `xiaowei_code_desc` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_xiaowei_region_basic
-- ----------------------------
DROP TABLE IF EXISTS `z_xiaowei_region_basic`;
CREATE TABLE `z_xiaowei_region_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xiaowei_code` varchar(255) DEFAULT NULL COMMENT '小微编码',
  `xiaowei_name` varchar(255) DEFAULT NULL COMMENT '小微名称',
  `region_code` varchar(255) DEFAULT NULL COMMENT '区域编码',
  `region_name` varchar(255) DEFAULT NULL COMMENT '区域名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_xw_category
-- ----------------------------
DROP TABLE IF EXISTS `z_xw_category`;
CREATE TABLE `z_xw_category` (
  `xw_code` varchar(255) DEFAULT NULL COMMENT '小微编码',
  `xw_name` varchar(255) DEFAULT NULL,
  `target_category_code` varchar(255) DEFAULT NULL COMMENT '目标类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_xw_score
-- ----------------------------
DROP TABLE IF EXISTS `z_xw_score`;
CREATE TABLE `z_xw_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contract_id` int(11) DEFAULT NULL COMMENT '合约id',
  `grab_id` int(11) DEFAULT NULL COMMENT '抢单主表id',
  `model_id` int(11) DEFAULT NULL COMMENT '型号id(z_gambling_contracts_product_model表主键)',
  `xw_name` varchar(255) DEFAULT NULL COMMENT '抢入小微name',
  `xw_code` varchar(255) DEFAULT NULL COMMENT '小微code',
  `socre` decimal(10,2) DEFAULT NULL COMMENT '评分',
  `grab_count` varchar(10) DEFAULT '' COMMENT '抢单成功数量',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for z_xw_score_his
-- ----------------------------
DROP TABLE IF EXISTS `z_xw_score_his`;
CREATE TABLE `z_xw_score_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contract_id` int(11) DEFAULT NULL COMMENT '合约id',
  `grab_id` int(11) DEFAULT NULL COMMENT '抢单主表id',
  `model_id` int(11) DEFAULT NULL COMMENT '型号id(z_gambling_contracts_product_model表主键)',
  `xw_name` varchar(255) DEFAULT NULL COMMENT '抢入小微name',
  `xw_code` varchar(255) DEFAULT NULL COMMENT '小微code',
  `socre` decimal(10,2) DEFAULT NULL COMMENT '评分',
  `grab_count` varchar(10) DEFAULT '' COMMENT '抢单成功数量',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for x_people_share_ratio
-- ----------------------------
DROP TABLE IF EXISTS `z_people_share_ratio`;
CREATE TABLE `z_people_share_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contracts_id` int(11) DEFAULT NULL COMMENT '合约编码',
  `org_code` varchar(100) DEFAULT NULL COMMENT '组织编码',
  `org_name` varchar(100) DEFAULT NULL COMMENT '组织名称',
  `xw_code` varchar(100) DEFAULT NULL COMMENT '小微编码',
  `xw_name` varchar(100) DEFAULT NULL COMMENT '小微名称',
  `emp_sn` varchar(100) DEFAULT NULL COMMENT '员工号',
  `emp_name` varchar(100) DEFAULT NULL COMMENT '员工姓名',
  `percents` varchar(100) DEFAULT NULL COMMENT '分享比例',
  PRIMARY KEY (`id`),
  KEY `idx_con` (`contracts_id`),
  KEY `idx_xw` (`xw_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_emp_chain`;
CREATE TABLE `sys_emp_chain` (
 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `empSn` varchar(20) DEFAULT ''  COMMENT '员工工号',
  `chain_code` varchar(80) DEFAULT '' COMMENT '员工上链id',
  PRIMARY KEY (`id`),
  KEY `idx_empSn` (`empSn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for ext_lq
-- ----------------------------
DROP TABLE IF EXISTS `ext_lq`;
CREATE TABLE `ext_lq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lq_pt_code` varchar(20) DEFAULT NULL COMMENT '链群平台编码',
  `lq_pt_name` varchar(20) DEFAULT NULL COMMENT '链群平台名称',
  `lq_code` varchar(20) DEFAULT NULL COMMENT '链群编码',
  `lq_name` varchar(50) DEFAULT NULL COMMENT '链群名称',
  `h_share_quota` varchar(20) DEFAULT NULL COMMENT '高增值分享额',
  `h_share_speed` varchar(20) DEFAULT NULL COMMENT '高增值分享额倍速',
  `h_share_ratio` varchar(20) DEFAULT NULL COMMENT '高增值分享比例',
  `h_share_add_value` varchar(20) DEFAULT NULL COMMENT '高增值增值空间',
  `h_amt` varchar(20) DEFAULT NULL COMMENT '高增值收入',
  `h_amt_up` varchar(20) DEFAULT NULL COMMENT '高增值收入增长率',
  `h_lrv` varchar(20) DEFAULT NULL COMMENT '高增值利润率',
  `g_amt` varchar(20) DEFAULT NULL COMMENT '抢单收入额',
  `g_amt_up` varchar(20) DEFAULT NULL COMMENT '抢单收入增长率',
  `g_lrv` varchar(20) DEFAULT NULL COMMENT '抢单利润率',
  `g_share_add_value` varchar(20) DEFAULT NULL COMMENT '抢单分享增值空间',
  `g_share_ratio` varchar(20) DEFAULT NULL COMMENT '抢单分享比例',
  `g_share_quota` varchar(20) DEFAULT NULL COMMENT '抢单分享额',
  `g_share_speed` varchar(20) DEFAULT NULL COMMENT '抢单分享额倍速',
  `bottom_amt` varchar(20) DEFAULT NULL COMMENT '底线收入',
  `bottom_add_value` varchar(20) DEFAULT NULL COMMENT '底线收入增幅',
  `bottom_lrv` varchar(20) DEFAULT NULL COMMENT '底线收入利润率',
  `actual_amt` varchar(20) DEFAULT NULL COMMENT '实际收入额',
  `actual_amt_up` varchar(20) DEFAULT NULL COMMENT '实际收入增长率',
  `actual_lrv` varchar(20) DEFAULT NULL COMMENT '实际利润率',
  `actual_share_quota` varchar(20) DEFAULT NULL COMMENT '实际分享额',
  `actual_share_speed` varchar(20) DEFAULT NULL COMMENT '实际分享额倍速',
  `date` varchar(50) DEFAULT NULL COMMENT '推送数据日期',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ind_date` (`date`),
  KEY `ind_lqcode` (`lq_code`)
) ENGINE=InnoDB AUTO_INCREMENT=311112 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for ext_market
-- ----------------------------
DROP TABLE IF EXISTS `ext_market`;
CREATE TABLE `ext_market` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lq_pt_code` varchar(20) DEFAULT NULL COMMENT '链群平台编码',
  `lq_pt_name` varchar(20) DEFAULT NULL COMMENT '链群平台名称',
  `lq_code` varchar(20) DEFAULT NULL COMMENT '链群编码',
  `lq_name` varchar(50) DEFAULT NULL COMMENT '链群名称',
  `region_code` varchar(20) DEFAULT NULL COMMENT '市场编码',
  `region_name` varchar(50) DEFAULT NULL COMMENT '市场名称',
  `h_amt` varchar(20) DEFAULT NULL COMMENT '高增值收入',
  `h_amt_up` varchar(20) DEFAULT NULL COMMENT '高增值收入增长率',
  `h_share_quota` varchar(20) DEFAULT NULL COMMENT '高增值分享额',
  `g_amt` varchar(20) DEFAULT NULL COMMENT '抢单收入额',
  `g_amt_up` varchar(20) DEFAULT NULL COMMENT '抢单收入增长率',
  `g_share_quota` varchar(20) DEFAULT NULL COMMENT '抢单分享额',
  `bottom_amt` varchar(20) DEFAULT NULL COMMENT '分享底线收入',
  `bottom_amt_up` varchar(20) DEFAULT NULL COMMENT '分享底线增长率',
  `bottom_share_quota` varchar(20) DEFAULT NULL COMMENT '增值分享底线增值分享',
  `actual_amt` varchar(20) DEFAULT NULL COMMENT '实际收入额',
  `actual_amt_up` varchar(20) DEFAULT NULL COMMENT '实际收入增长率',
  `actual_lrv` varchar(20) DEFAULT NULL COMMENT '实际利润率',
  `actual_share_quota` varchar(20) DEFAULT NULL COMMENT '实际分享额',
  `actual_share_speed` varchar(20) DEFAULT NULL COMMENT '实际分享额倍速',
  `date` varchar(50) DEFAULT NULL COMMENT '推送数据日期',
  `xw_type` varchar(20) DEFAULT NULL COMMENT '0-汇总;1-链群',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ind_date` (`date`),
  KEY `ind_lqcode` (`lq_code`),
  KEY `idx` (`region_code`),
  KEY `idx_tt` (`xw_type`,`lq_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8569799 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ext_daily_data
-- ----------------------------
DROP TABLE IF EXISTS `ext_daily_data`;
CREATE TABLE `ext_daily_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_sn` varchar(20) DEFAULT NULL,
  `emp_name` varchar(20) DEFAULT NULL,
  `xw_code` varchar(20) DEFAULT NULL,
  `xw_name` varchar(50) DEFAULT NULL,
  `lq_pt_code` varchar(20) DEFAULT NULL,
  `lq_pt_name` varchar(50) DEFAULT NULL,
  `lq_code` varchar(20) DEFAULT NULL,
  `lq_name` varchar(20) DEFAULT NULL,
  `region_code` varchar(20) DEFAULT NULL,
  `region_name` varchar(20) DEFAULT NULL,
  `h_amt` varchar(20) DEFAULT NULL,
  `h_amt_up` varchar(20) DEFAULT NULL,
  `h_lrv` varchar(20) DEFAULT NULL,
  `h_share_quota` varchar(20) DEFAULT NULL,
  `h_share_speed` varchar(20) DEFAULT NULL,
  `g_amt` varchar(20) DEFAULT NULL,
  `g_amt_up` varchar(20) DEFAULT NULL,
  `g_lrv` varchar(20) DEFAULT NULL,
  `g_share_quota` varchar(20) DEFAULT NULL,
  `g_share_speed` varchar(20) DEFAULT NULL,
  `actual_amt` varchar(20) DEFAULT NULL,
  `actual_amt_up` varchar(20) DEFAULT NULL,
  `actual_lrv` varchar(50) DEFAULT NULL,
  `actual_share_quota` varchar(20) DEFAULT NULL,
  `actual_share_speed` varchar(20) DEFAULT NULL,
  `diff_share` varchar(20) DEFAULT NULL,
  `diff_income` varchar(20) DEFAULT NULL,
  `diff_lrv` varchar(20) DEFAULT NULL,
  `date` varchar(20) DEFAULT NULL,
  `contracts_id` varchar(20) DEFAULT NULL,
  `contracts_name` varchar(50) DEFAULT NULL,
  `xw_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_date` (`date`),
  KEY `ind_lqcode` (`lq_code`)
) ENGINE=InnoDB AUTO_INCREMENT=217880 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ext_daily_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_xw_region`;
CREATE TABLE `sys_xw_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `xw_code` varchar(20) DEFAULT NULL,
  `xw_name` varchar(50) DEFAULT NULL,
  `region_code` varchar(20) DEFAULT NULL,
  `region_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ind_xw_code` (`xw_code`),
  KEY `ind_region_code` (`region_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_xw_master
-- ----------------------------
DROP TABLE IF EXISTS `sys_xw_master`;
CREATE TABLE `sys_xw_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `xw_code` varchar(20) DEFAULT NULL COMMENT '小微编码',
  `xw_name` varchar(50) DEFAULT NULL COMMENT '小微名称',
  `pt_code` varchar(20) DEFAULT NULL COMMENT '平台编码',
  `pt_name` varchar(20) DEFAULT NULL COMMENT '平台名称',
  `master_code` varchar(20) DEFAULT NULL COMMENT '链群主编码',
  `master_name` varchar(20) DEFAULT NULL COMMENT '链群主名称',
  PRIMARY KEY (`id`),
  KEY `ind_master_code` (`master_code`),
  KEY `ind_xw_code` (`xw_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '小微和链群主关系表';

DROP TABLE IF EXISTS `t_ods_minbu`;
CREATE TABLE `t_ods_minbu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xwCode` varchar(100) DEFAULT NULL COMMENT '小微编码',
  `xwName` varchar(100) DEFAULT NULL COMMENT '小微名称',
  `xwMasterCode` varchar(100) DEFAULT NULL COMMENT '小微主工号',
  `xwMasterName` varchar(100) DEFAULT NULL COMMENT '小微主姓名',
  `littleXwCode` varchar(100) DEFAULT NULL COMMENT '最小作战单元编码（小小微编码）',
  `littleXwName` varchar(100) DEFAULT NULL COMMENT '最小作战单元名称（小小微名称）',
  `littleXwMasterCode` varchar(100) DEFAULT NULL COMMENT '最小作战单元主编码（小小微主编码）',
  `littleXwMasterName` varchar(100) DEFAULT NULL COMMENT '最小作战单元主姓名（小小微主姓名）',
  `ptCode` varchar(100) DEFAULT NULL COMMENT '所属平台编码',
  `ptName` varchar(100) DEFAULT NULL COMMENT '所属平台名称',
  `xwType3Code` varchar(100) DEFAULT NULL COMMENT '小微类型3编码',
  `xwType3` varchar(100) DEFAULT NULL COMMENT '小微类型3（研发、服务、制造...）',
  `xwType5Code` varchar(100) DEFAULT NULL COMMENT '小微类型5编码',
  `xwType5` varchar(100) DEFAULT NULL COMMENT '小微类型5（创单、体验）',
  `userXwCode` varchar(100) DEFAULT NULL COMMENT '对应用户小微编码',
  `userXwName` varchar(100) DEFAULT NULL COMMENT '对应用户小微名称',
	`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
	KEY `ind_xwCode` (`xwCode`),
	KEY `ind_littleXwCode` (`littleXwCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_ods_minbu_emp`;
CREATE TABLE `t_ods_minbu_emp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `littleXwCode` varchar(100) DEFAULT NULL COMMENT '最小作战单元编码（小小微编码）',
  `littleXwName` varchar(100) DEFAULT NULL COMMENT '最小作战单元名称（小小微名称）',
  `littleEmpsn` varchar(100) DEFAULT NULL COMMENT '所属最小作战单元工号',
  `littleEmpname` varchar(100) DEFAULT NULL COMMENT '所属最小作战单元姓名',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
	KEY `ind_littleXwCode` (`littleXwCode`),
	KEY `ind_littleEmpsn` (`littleEmpsn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


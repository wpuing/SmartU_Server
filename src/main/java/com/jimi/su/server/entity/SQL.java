package com.jimi.su.server.entity;

public class SQL {

	public final static String SELECT_FILE_BY_BRANCH_ID = "select * from file where BranchID = ?";

	public final static String SELECT_COMMAND_BY_BRANCH_ID = "select * from command where BranchID = ?";

	public final static String SELECT_BRANCH_BY_BRANCH_NAME = "select * from branch where BranchName = ?";

	public final static String SELECT_PCB_BY_PCB_VERSION = "select * from pcb where PcbVersion = ?";

	public final static String SELECT_HW_BY_HW_VERSION = "select * from hardware where HwVersion = ?";

	public final static String SELECT_GSENSOR_BY_GSENSOR_VERSION = "select * from gsensor where GsensorVersion = ?";

	public final static String SELECT_PLATFORM_BY_PLATFORM_VERSION = "select * from platform where PfVersion = ?";

	public final static String SELECT_FT_BY_FT_NAME = "select * from foreigntrade where FtName = ?";

	public final static String SELECT_DOMESTIC_BY_DOMESTIC_NAME = "select * from foreigntrade where FtVersion = ?";

	public final static String SELECT_FILE_BY_NAME = "select * from file where fileName = ?";

	public final static String SELECT_VERSION_BY_FILE_ID = "select * from version where FileId = ?";

	public static final String SELECT_COMMAND_DETAIL_PART1 = "SELECT a.*, branch.iD AS Branch_ID, branch.branchName AS Branch_BranchName FROM ( SELECT command.iD AS Command_ID, command.projectID AS Command_ProjectID, command.commandName AS Command_CommandName, command.command AS Command_Command, command.recvTitle AS Command_RecvTitle, command.titleIndex AS Command_TitleIndex, command.limitLength AS Command_LimitLength, command.startRange AS Command_StartRange, command.endRange AS Command_EndRange, command.readORWrite AS Command_ReadORWrite, command.reWrite AS Command_ReWrite, command.branchID AS Command_BranchID, project.iD AS Project_ID, project.projectName AS Project_ProjectName FROM command LEFT JOIN project ON command.ProjectID = project.ID ";

	public static final String SELECT_COMMAND_DETAIL_PART2 = " ) a LEFT JOIN branch ON a.Command_BranchID = branch.ID";

	public final static String SELECT_BRANCHTYPE_BY_PROJECT_ID = "select * from branchtype where ProjectID = ?";

	public final static String SELECT_COMMAND_BY_PROJECT_ID = "select * from command where ProjectID = ?";

	public final static String SELECT_PROJECT_BY_NAME = "select * from project where ProjectName = ?";

	public final static String SELECT_USER_BY_NAME = "select * from user where name = ?";

	public final static String SELECT_ALL_USERTYPE = "select distinct * from type";

	public final static String FIND_PERMISSION_BY_USER_ID = "select type.permission as permission from user inner join type on user.typeId = type.id where user.id = ?";

	public final static String SELECT_TYPE_BY_NAME = "select * from type where typeName = ?";

	public final static String SELECT_BIND_BY_BRANCH_ID = "select * from operationversionbind where BranchID = ?";

	public final static String SELECT_PCB_FUZZYLY = "select * from pcb where PcbVersion like ? and ID != 0";

	public final static String SELECT_HARDWARE_FUZZYLY = "select * from hardware where HwVersion like ? and ID != 0";

	public final static String SELECT_GSENSOR_FUZZYLY = "select * from gsensor where GsensorVersion like ? and ID != 0";

	public final static String SELECT_PLATFORM_FUZZYLY = "select * from platform where PfVersion like ? and ID != 0";

	public final static String SELECT_FT_FUZZYLY = "select * from foreigntrade where FtName like ? and ID != 0";

	public final static String SELECT_DOMESTIC_FUZZYLY = "select * from domestic where DomesticName like ? and ID != 0";

	public final static String SELECT_BRANCHTYPE_BY_PROJECTDI_AND_TYPE = "select branchtype.ID, branchtype.Type, branchtype.Name, branchtype.ProjectID from branchtype inner join project on branchtype.ProjectID = project.ID where branchtype.ProjectID = ? and branchtype.type = ? ORDER BY Position ASC,branchtype.ID ASC";

	public final static String SELECT_BRANCHTYPE_BY_TYPE_AND_NAME_PROJECTID = "select * from branchtype where Type = ? and Name = ? and ProjectID = ?";

	public final static String SELECT_RULE_BY_ATRR_AND_FROM_AND_TO = "select * from rule where rule.attribute = ? and rule.`from` = ? and rule.`to` = ?";

	public final static String SELECT_OPERATIONVERSIONBIND_BY_BRANCH_ID = "select * from operationversionbind where BranchID = ?";

	public final static String SELECT_BRANCH_BY_BRANCH_TYPE_ID = "select * from branch where BranchTypeID = ?";

	public final static String SELECT_ORDER_BY_NAME = "select * from orders where orderName = ?";

	public final static String SELECT_BRANCHNAME_SCALABLENUM_BY_USERNAME_ORDERNUM = "SELECT branch.BranchName as branchName,orders.scalableNum from branch  JOIN orders JOIN `user` on branch.ID = orders.branch and `user`.id = orders.`user` where `user`.`name` = ? and orders.orderName = ? and `user`.`enable` = 1";

	public final static String SELECT_ORDER_BY_USERNAME_ORDERNUM = "SELECT orders.* from branch  JOIN orders JOIN `user` on branch.ID = orders.branch and `user`.id = orders.`user` where `user`.`name` = ? and orders.orderName = ? and `user`.`enable` = 1";

	public final static String SELECT_ORDER_DETAIL = "SELECT orders.id,orders.orderName,branch.ID as branchId,branch.BranchName as branchName,`user`.id as userId,`user`.`name` as userName,orders.scalableNum,orders.upgradedNum FROM branch INNER JOIN orders INNER JOIN `user` ON branch.ID = orders.branch AND `user`.id = orders.`user` AND `user`.`enable` = 1";

	public final static String UPDATE_BRANCHTYPE_POSITION = "UPDATE branchtype b JOIN branchtype bb ON (b.ID = ? AND bb.ID = ?) OR (b.ID = ? AND bb.ID = ?) SET b.Position = bb.Position , bb.Position = b.Position";

	public final static String SELECT_BRANCH_BY_BRANCH_NAMES = "select * from branch where BranchName IN ";

	public final static String SELECT_ORDERS_BY_BRANCH_ID = "select * from orders where branch = ?";
	
	public final static String SELECT_USER_BY_NAME_PASSWORD = "SELECT id,name,password FROM user WHERE user.name = ? AND user.password = ?";

	public final static String SELECT_MAIL_BY_NAME = "SELECT * FROM mail WHERE EMailAddress = ?";
	
	public final static String SELECT_GROUP_BY_NAME = "SELECT * FROM organization o WHERE o.Group = ?";
	
	public final static String SELECT_UPGRADE_BY_ID = "SELECT ID,UpgradeLog,Status FROM device WHERE ID = ?";

	public final static String SELECT_VAGUE_MAIL_BY_NAME = "SELECT ID,EMailAddress,UserID FROM mail WHERE EMailAddress LIKE ";
	
	public final static String SELECT_MAIL_ALL = "SELECT ID,EMailAddress,UserID FROM mail";
	
	public final static String SELECT_VAGUE_ORGAN_BY_NAME = "SELECT o.ID,o.Group,o.UserID FROM organization o WHERE o.Group LIKE ";
	
	public final static String SELECT_ORGAN_ALL = "SELECT ID,Group,UserID FROM organization";

}

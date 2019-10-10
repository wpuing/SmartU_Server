package com.jimi.su.server.service;

import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.model.Device;
import com.jimi.su.server.util.ResultUtil;

/**
 * 设备管理业务层
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月15日
 */
public class DeviceService {

	
	public ResultUtil selectUpgrade(Integer deviceId) {
		Device device = Device.dao.findFirst(SQL.SELECT_UPGRADE_BY_ID,deviceId);
		return ResultUtil.succeed(device);
	}

}

package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.DeviceService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 设备管理控制器
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月15日
 */
public class DeviceController extends Controller {
	
	@Inject
	private DeviceService deviceService;
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 根据id查询设备升级日志及情况
	 * @param deviceId
	 */
	public void selectUpgradeById(Integer deviceId) {
		if (deviceId<=0) {
			throw new ParameterException("无效的设备id");
		}
		ResultUtil resultUtil = deviceService.selectUpgrade(deviceId);
		renderJson(resultUtil);
	}

}

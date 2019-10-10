package com.jimi.su.server.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 显示用户操作记录业务层
 * @author 几米物联自动化部-韦姚忠
 * @date   2019年7月18日
 */
public class ActionLogService extends SelectService {
	
//	private SelectService selectService = Enhancer.enhance(SelectService.class);

//	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
//		System.out.println("filter--->"+filter+",currentPage--->"+currentPage+",pageSize--->"+pageSize);
//		
//		Page<Record> pageRecord = selectService.select("action_log", currentPage, pageSize, "time", null, null, null);
//		Page<Record> page = Db.paginate(currentPage, pageSize, "SELECT *", "FROM action_log ORDER BY time");
//		return ResultUtil.succeed(page);
//		
//	} 
//	
	
	public ResultUtil select(Integer currentPage, Integer pageSize) {
		Page<Record> page = Db.paginate(currentPage, pageSize, "SELECT *", "FROM action_log ORDER BY time");
		return ResultUtil.succeed(page);
	}
}

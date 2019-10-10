package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.service.RuleService;
import com.jimi.su.server.util.ResultUtil;

/**
 * *.添加注释
 * 角色管理控制层
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月15日
 */
public class RuleController extends Controller {

	@Inject
	private RuleService ruleService;


	@Log("添加角色信息：{attribute}，{from}，{to}")
	public void add(String attribute, Integer from, Integer to) {
		if (attribute == null || from == null || to == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ruleService.add(attribute, from, to);
		renderJson(result);
	}


	@Log("要删除的角色的id是：{id}")
	public void delete(Integer id) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ruleService.delete(id);
		renderJson(result);
	}


	@Log("更新角色信息：{id}，{attribute}，{from}，{to}")
	public void update(Integer id, String attribute, Integer from, Integer to) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ruleService.update(id, attribute, from, to);
		renderJson(result);
	}


	public void select(String attribute, Integer currentPage, Integer pageSize) {
		if (attribute == null || currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new OperationException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = ruleService.select(attribute, currentPage, pageSize);
		renderJson(result);
	}
}

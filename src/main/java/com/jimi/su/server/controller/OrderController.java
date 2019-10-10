package com.jimi.su.server.controller;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.annotation.Open;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.OrderService;
import com.jimi.su.server.util.ResultUtil;

/**订单管理控制层
 * @author   HCJ
 * @date     2019年5月21日 下午2:15:12
 */
public class OrderController extends Controller {

	@Inject
	private OrderService orderService;

	/**
	 * maxOrderNameLength : 最大订单号长度
	 */
	private static final int maxOrderNameLength = 64;


	/**@author HCJ
	 * 添加记录
	 * @param orderName 订单号
	 * @param userId 用户id
	 * @param branchId 分支id
	 * @param scalableNum 可升级数量
	 * @date 2019年5月21日 下午2:15:30
	 */
	@Log("添加订单，订单号：{orderName}，用户id：{userId}，分支id：{branchId}，可升级数量：{scalableNum}")
	public void add(String orderName, Integer userId, Integer branchId, Integer scalableNum) {
		if (StringUtils.isBlank(orderName) || userId == null || branchId == null || scalableNum == null) {
			throw new ParameterException("参数不能为空");
		}
		if (orderName.length() > maxOrderNameLength) {
			throw new ParameterException("订单号过长");
		}
		ResultUtil result = orderService.add(orderName, userId, branchId, scalableNum);
		renderJson(result);
	}


	/**@author HCJ
	 * 删除记录
	 * @param id 订单id
	 * @date 2019年5月21日 下午2:16:05
	 */
	@Log("要删除的订单id：{id}")
	public void delete(Integer id) {
		if (id == null) {
			throw new ParameterException("参数不能为空");
		}
		ResultUtil result = orderService.delete(id);
		renderJson(result);
	}


	/**@author HCJ
	 * 查询记录
	 * @param filter 查询条件
	 * @param currentPage 当前页数
	 * @param pageSize 每页大小
	 * @date 2019年5月21日 下午2:16:25
	 */
	public void select(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new ParameterException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = orderService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	/**@author HCJ
	 * 更新记录
	 * @param id 订单id
	 * @param orderName 订单号
	 * @param userId 用户ID
	 * @param branchId 分支ID
	 * @date 2019年5月21日 下午5:01:55
	 */
	@Log("更新订单信息，订单id：{id}，订单号：{orderName}，用户id：{userId}，分支id：{branchId}")
	public void update(Integer id, String orderName, Integer userId, Integer branchId) {
		if (id == null) {
			throw new ParameterException("id不能为空");
		}
		ResultUtil result = orderService.update(id, orderName, userId, branchId);
		renderJson(result);
	}


	/**@author HCJ
	 * 获取分支名称和可升级数量
	 * @param userName 用户名称
	 * @param orderName 订单号
	 * @date 2019年5月21日 下午2:17:37
	 */
	@Open
	public void getBranchNameAndNum(String userName, String orderName) {
		if (StringUtils.isAnyBlank(userName, orderName)) {
			throw new ParameterException("参数不能为空");
		}
		ResultUtil result = orderService.getBranchNameAndNum(userName, orderName);
		renderJson(result);

	}


	/**@author HCJ
	 * 更新可升级数量和已升级数量
	 * @param userName 用户名
	 * @param orderName 订单号
	 * @param number 需要进行更新的具体数量
	 * @date 2019年5月21日 下午2:18:25
	 */
	@Open
	@Log("更新可升级数量和已升级数量,用户名：{userName}，订单号：{orderName}，需要进行更新的具体数量：{number}")
	public void updateNum(String userName, String orderName, Integer number) {
		if (StringUtils.isAnyBlank(userName, orderName) || number == null) {
			throw new ParameterException("参数不能为空");
		}
		ResultUtil result = orderService.updateNum(userName, orderName, number);
		renderJson(result);
	}
}

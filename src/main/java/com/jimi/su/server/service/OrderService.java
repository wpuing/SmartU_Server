package com.jimi.su.server.service;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.model.Branch;
import com.jimi.su.server.model.Orders;
import com.jimi.su.server.model.User;
import com.jimi.su.server.model.vo.OrderVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;

public class OrderService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	public ResultUtil add(String orderName, Integer userId, Integer branchId, Integer scalableNum) {
		if (Orders.dao.findFirst(SQL.SELECT_ORDER_BY_NAME, orderName) != null) {
			throw new OperationException("添加失败，订单号已存在");
		}
		if (User.dao.findById(userId) == null) {
			throw new OperationException("添加失败，不存在此用户");
		}
		if (Branch.dao.findById(branchId) == null) {
			throw new OperationException("添加失败，不存在此分支");
		}
		Orders order = new Orders();
		order.setOrderName(orderName.trim()).setUser(userId).setBranch(branchId).setScalableNum(scalableNum).setUpgradedNum(0).setCreateTime(new Date()).save();
		return ResultUtil.succeed();
	}


	public ResultUtil delete(Integer orderId) {
		Orders order = Orders.dao.findById(orderId);
		if (order == null) {
			throw new OperationException("删除失败，订单号不存在");
		}
		order.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		Page<Record> pageRecord = selectService.selectByBaseSql(SQL.SELECT_ORDER_DETAIL, "", currentPage, pageSize, null, "id", filter);
		List<OrderVO> orderVOs = OrderVO.fillList(pageRecord.getList());
		PageUtil<OrderVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, orderVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil update(Integer id, String orderName, Integer userId, Integer branchId) {
		Orders order = Orders.dao.findById(id);
		if (order == null) {
			throw new OperationException("更新失败，不存在此订单");
		}
		if (userId != null) {
			if (User.dao.findById(userId) == null) {
				throw new OperationException("更新失败，不存在此用户");
			}
			order.setUser(userId);
		}
		if (branchId != null) {
			if (Branch.dao.findById(branchId) == null) {
				throw new OperationException("更新失败，不存在此分支");
			}
			order.setBranch(branchId);
		}
		if (orderName != null) {
			order.setOrderName(orderName.trim());
		}
		order.update();
		return ResultUtil.succeed();
	}


	public ResultUtil getBranchNameAndNum(String userName, String orderName) {
		Record record = Db.findFirst(SQL.SELECT_BRANCHNAME_SCALABLENUM_BY_USERNAME_ORDERNUM, userName, orderName);
		if (record == null) {
			throw new OperationException("获取分支名和可升级数量失败，请检查用户名、订单号及用户权限");
		}
		return ResultUtil.succeed(record);
	}


	public ResultUtil updateNum(String userName, String orderName, Integer number) {
		Orders order = Orders.dao.findFirst(SQL.SELECT_ORDER_BY_USERNAME_ORDERNUM, userName, orderName);
		if (order == null) {
			throw new OperationException("获取订单失败，请检查用户名、订单号及用户权限");
		}
		Integer scalableNum = order.getScalableNum();
		if (scalableNum == 0) {
			throw new OperationException("没有可升级的数量");
		}
		if (scalableNum < number) {
			throw new OperationException("参数错误，请检查可升级数量是否足够");
		}
		order.setScalableNum(scalableNum - number).setUpgradedNum(order.getUpgradedNum() + number).update();
		return ResultUtil.succeed();
	}

}

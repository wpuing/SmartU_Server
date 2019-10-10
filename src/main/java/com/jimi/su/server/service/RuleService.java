package com.jimi.su.server.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.model.Rule;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.util.ResultUtil;


public class RuleService {


	public ResultUtil add(String attribute, Integer from, Integer to) {

		Rule rule = Rule.dao.findFirst(SQL.SELECT_RULE_BY_ATRR_AND_FROM_AND_TO, attribute, from, to);
		if (rule != null) {
			throw new OperationException("添加的更新规则已存在，请直接引用无需添加");
		}
		rule = new Rule();
		rule.setAttribute(attribute.trim()).setFrom(from).setTo(to).save();
		return ResultUtil.succeed();
	}


	public ResultUtil delete(Integer id) {
		Rule rule = Rule.dao.findById(id);
		if (rule == null) {
			throw new OperationException("升级规则不存在");
		}
		Boolean flag = rule.delete();
		if (flag) {
			return ResultUtil.succeed();
		} else {
			return ResultUtil.failed();
		}
	}


	public ResultUtil update(Integer id, String attribute, Integer from, Integer to) {
		Rule rule = Rule.dao.findById(id);
		if (rule == null) {
			throw new OperationException("升级规则不存在");
		}
		Rule tempRule = Rule.dao.findFirst(SQL.SELECT_RULE_BY_ATRR_AND_FROM_AND_TO, attribute, from, to);
		if (tempRule != null) {
			throw new OperationException("修改后的更新规则已存在，请直接引用无需更新");
		}
		if (attribute != null) {
			rule.setAttribute(attribute.trim());
		}
		
		if (from != null) {
			rule.setFrom(from);
		}
		if (to != null) {
			rule.setTo(to);
		}
		rule.update();
		return ResultUtil.succeed();
	}


	public ResultUtil select(String attribute, Integer currentPage, Integer pageSize) {
		SqlPara sqlPara = new SqlPara();
		switch (attribute) {
		case "ProjectID":
			sqlPara.setSql("SELECT rule.id, rule.attribute, rule.`from`, p1.ProjectName AS fromName, rule.`to`, p2.ProjectName AS toName from rule inner join project AS p1 on p1.ID = rule.`from` INNER JOIN project AS p2 ON p2.ID = rule.`to` WHERE attribute = 'ProjectID' AND rule.`from` <> rule.`to`");
			break;
		case "PcbID":
			sqlPara.setSql("SELECT rule.id, rule.attribute, rule.`from`, p1.PcbVersion AS fromName, rule.`to`, p2.PcbVersion AS toName FROM rule INNER JOIN pcb AS p1 ON p1.ID = rule.`from` INNER JOIN pcb AS p2 ON p2.ID = rule.`to` WHERE attribute = 'PcbID' AND rule.`from` <> rule.`to`");
			break;
		case "HwID":
			sqlPara.setSql("SELECT rule.id, rule.attribute, rule.`from`, p1.HwVersion AS fromName, rule.`to`, p2.HwVersion AS toName FROM rule INNER JOIN hardware AS p1 ON p1.ID = rule.`from` INNER JOIN hardware AS p2 ON p2.ID = rule.`to` WHERE attribute = 'HwID' AND rule.`from` <> rule.`to`");
			break;
		case "GsensorID":
			sqlPara.setSql("SELECT rule.id, rule.attribute, rule.`from`, p1.GsensorVersion AS fromName, rule.`to`, p2.GsensorVersion AS toName FROM rule INNER JOIN gsensor AS p1 ON p1.ID = rule.`from` INNER JOIN gsensor AS p2 ON p2.ID = rule.`to` WHERE attribute = 'GsensorID' AND rule.`from` <> rule.`to`");
			break;
		case "PfID":
			sqlPara.setSql("SELECT rule.id, rule.attribute, rule.`from`, p1.PfVersion AS fromName, rule.`to`, p2.PfVersion AS toName FROM rule INNER JOIN platform AS p1 ON p1.ID = rule.`from` INNER JOIN platform AS p2 ON p2.ID = rule.`to` WHERE attribute = 'PfID' AND rule.`from` <> rule.`to`");
			break;
		default:
			break;
		}
		Page<Record> page = Db.paginate(currentPage, pageSize, sqlPara);
		PageUtil<Record> pageUtil = new PageUtil<Record> ();
		pageUtil.fill(page, page.getList());
		return ResultUtil.succeed(pageUtil);
	}
}

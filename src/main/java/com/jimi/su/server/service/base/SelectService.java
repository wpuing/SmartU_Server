package com.jimi.su.server.service.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jimi.su.server.exception.ParameterException;

import cc.darhao.dautils.api.StringUtil;


/**
 * 通用查询业务层
 * <br>
 * <b>2018年5月23日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class SelectService {

	private final static String[] OPERATORS = {"#like#", "#=#", "#<#", "#>#", "#>=#", "#<=#", "#!=#"};


	/*
	 * public static void main(String[] args) { PropKit.use("config.properties");
	 * DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"),
	 * PropKit.get("password"), PropKit.get("driver")); ActiveRecordPlugin arp = new
	 * ActiveRecordPlugin(dp); arp.setDialect(new MysqlDialect()); //
	 * 用什么数据库，就设置什么Dialect arp.setShowSql(true); _MappingKit.mapping(arp);
	 * dp.start(); arp.start(); SelectService selectService = new SelectService();
	 * String result = selectService.select("bead", null, null, null, null, null,
	 * null).getList().toString(); System.out.println(result); result =
	 * selectService.select(new String[] {"head", "face", "bead"}, new String[]
	 * {"head.id=face.head_id", "face.id = bead.face_id"}, 1, 30, null, null, null,
	 * new String[] {"head.id", "face.id"}).getList().toString();
	 * System.out.println(result); }
	 */

	/**
	 * 分页查询，支持筛选和排序
	 * @param tables 提供可读的表名数组，若需要左连接，将需要左连接的表放在table的第一个位置
	 * @param refers 外键数组，单表可为null
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页的条目数
	 * @param ascBy 按指定字段升序，不可和descBy同时使用
	 * @param descBy 按指定字段降序，不可和ascBy同时使用
	 * @param filter 按字段筛选，支持<, >, >,=, <=, !=, =，多个字段请用&隔开
	 * @param discard 表中不要的字段,最好将关联的外键去掉，只保留一个
	 * @param isLeftJoin 是否需要左连接，将需要左连接的表放在table的第一个位置
	 * @return Page对象
	 */
	public Page<Record> select(String[] tables, String[] refers, Integer pageNo, Integer pageSize, String ascBy, String descBy, String filter, String[] discard, Boolean isLeftJoin) {
		StringBuffer sql = new StringBuffer();
		List<String> questionValues = new ArrayList<>();
		createFrom(tables, refers, sql, isLeftJoin);
		createWhere(filter, questionValues, sql);
		createOrderBy(ascBy, descBy, sql);
		return paginateAndFillWhereValues(tables, pageNo, pageSize, sql, questionValues, discard);
	}


	/**
	 * 分页查询，支持筛选和排序
	 * @param baseSql1 基础sql语句1
	 * @param baseSql2 基础sql语句2，where 部分将根据filter 插入到BaseSql1和baseSql2之间
	 * @param pageNo
	 * @param pageSize
	 * @param ascBy
	 * @param descBy
	 * @param filter
	 * @return
	 */
	public Page<Record> selectByBaseSql(String baseSql1, String baseSql2, Integer pageNo, Integer pageSize, String ascBy, String descBy, String filter) {
		StringBuffer sql = new StringBuffer();
		List<String> questionValues = new ArrayList<>();
		sql.append(baseSql1);
		createWhere(filter, questionValues, sql);
		sql.append(" " + baseSql2);
		createOrderBy(ascBy, descBy, sql);
		return paginateAndFillWhereValues(pageNo, pageSize, sql, questionValues);
	}
	/**
	 * 
	 * @param suffixSql 从select 到 where之前的sql语句
	 * @param pageNo
	 * @param pageSize
	 * @param ascBy
	 * @param descBy
	 * @param filter
	 * @return
	 */
	/*
	 * public Page<Record> select(String suffixSql, Integer pageNo, Integer
	 * pageSize, String ascBy, String descBy, String filter) { StringBuffer sql =
	 * new StringBuffer(); List<String> questionValues = new ArrayList<>();
	 * sql.append(suffixSql); createWhere(filter, questionValues, sql);
	 * createOrderBy(ascBy, descBy, sql); }
	 */


	/**
	 * 分页查询，支持筛选和排序
	 * @param tables 提供可读的表名数组
	 * @param refers 外键数组，单表可为null
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页的条目数
	 * @param ascBy 按指定字段升序，不可和descBy同时使用
	 * @param descBy 按指定字段降序，不可和ascBy同时使用
	 * @param filter 按字段筛选，支持<, >, >,=, <=, !=, =，多个字段请用&隔开
	 * @param discard 表中不要的字段,最好将关联的外键去掉，只保留一个
	 * @return Page对象
	 */
	public Page<Record> select(String[] tables, String[] refers, Integer pageNo, Integer pageSize, String ascBy, String descBy, String filter, String[] discard) {
		return select(tables, refers, pageNo, pageSize, ascBy, descBy, filter, discard, false);
	}


	/**
	 * 分页查询，支持筛选和排序
	 * @param table 提供可读的表名
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页的条目数
	 * @param ascBy 按指定字段升序，不可和descBy同时使用
	 * @param descBy 按指定字段降序，不可和ascBy同时使用
	 * @param filter 按字段筛选，支持<, >, >,=, <=, !=, =，多个字段请用&隔开
	 * @return Page对象
	 */
	public Page<Record> select(String table, Integer pageNo, Integer pageSize, String ascBy, String descBy, String filter, String[] discard) {
		return select(new String[] {table}, null, pageNo, pageSize, ascBy, descBy, filter, discard);
	}


	private void createFrom(String[] tables, String[] refers, StringBuffer sql, Boolean isLeftJoin) {
		// 表名非空判断
		if (tables == null) {
			throw new ParameterException("table name must be provided");
		}
		// 创建FROM
		sql.append(" FROM ");
		// 表是否在可读范围内
		String[] readabledTables = PropKit.use("config.properties").get("readableTables").split(",");
		int pass = 0;
		for (String table : tables) {
			for (String readabledTable : readabledTables) {
				if (readabledTable.equals(table)) {
					// 判断是否需要左连接
					pass++;
					if (pass == 1 && isLeftJoin) {
						sql.append(table + " LEFT JOIN (");
					} else {
						sql.append(table + " JOIN ");
					}
					break;
				}
			}
		}
		if (pass != tables.length) {
			throw new ParameterException("some tables are not readabled");
		}
		sql.delete(sql.lastIndexOf("JOIN"), sql.length());
		// 判断是否需要做连接
		if (isLeftJoin) {
			sql.append(" ) ");
		}
		// 创建ON
		if (refers != null) {
			sql.append(" ON ");
			for (String refer : refers) {
				sql.append(refer);
				sql.append(" AND ");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
	}


	private void createWhere(String filter, List<String> questionValues, StringBuffer sql) {
		// 判断filter存在与否
		if (filter != null) {
			sql.append(" WHERE ");
			String[] whereUnits = filter.split("#&#");

			int index = 0;
			for (String whereUnit : whereUnits) {
				// 分割键值与运算符
				int operatorStartIndex = -1;
				StringBuffer operator = new StringBuffer();
				int operationLength = 0;
				int i = 0;
				for (; i < OPERATORS.length; i++) {
					operatorStartIndex = whereUnit.indexOf(OPERATORS[i]);
					if (operatorStartIndex != -1) {
						operationLength = OPERATORS[i].length();
						operator.append(OPERATORS[i].substring(1, operationLength - 1));
						break;
					}
				}
				String key = whereUnit.substring(0, operatorStartIndex);
				String value = whereUnit.substring(operatorStartIndex + operationLength, whereUnit.length());
				sql.append(key + " " + operator.toString() + " ? AND ");
				if (OPERATORS[i].equals("#like#")) {
					questionValues.add("%" + value + "%");
				} else {
					questionValues.add(value);
				}
				if (index == whereUnits.length - 1) {
					sql.delete(sql.lastIndexOf("AND"), sql.length());
				}
				index++;
			}
			System.out.println(sql + "  " + questionValues);
		}
	}


	private void createOrderBy(String ascBy, String descBy, StringBuffer sql) {
		if (ascBy != null && descBy != null) {
			throw new ParameterException("ascBy and descBy can not be provided at the same time");
		} else if (ascBy != null) {
			sql.append(" ORDER BY " + ascBy + " ASC ");
		} else if (descBy != null) {
			sql.append(" ORDER BY " + descBy + " DESC ");
		}
	}


	private Page<Record> paginateAndFillWhereValues(String[] tables, Integer pageNo, Integer pageSize, StringBuffer sql, List<String> questionValues, String[] discard) {
		if ((pageNo != null && pageSize == null) || (pageNo == null && pageSize != null)) {
			throw new ParameterException("pageNo and pageSize must be provided at the same time");
		}
		String resultSet = createResultSet(tables, discard);
		if (pageNo == null && pageSize == null) {
			return Db.paginate(1, PropKit.use("config.properties").getInt("defaultPageSize"), resultSet, sql.toString(), questionValues.toArray());
		} else {
			return Db.paginate(pageNo, pageSize, resultSet, sql.toString(), questionValues.toArray());
		}
	}


	private Page<Record> paginateAndFillWhereValues(Integer pageNo, Integer pageSize, StringBuffer sql, List<String> questionValues) {
		if ((pageNo != null && pageSize == null) || (pageNo == null && pageSize != null)) {
			throw new ParameterException("pageNo and pageSize must be provided at the same time");
		}
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(sql.toString());
		for (String param : questionValues) {
			sqlPara.addPara(param);
		}
		if (pageNo == null && pageSize == null) {
			return Db.paginate(1, PropKit.use("config.properties").getInt("defaultPageSize"), sqlPara);
		} else {
			return Db.paginate(pageNo, pageSize, sqlPara);
		}
	}


	private String createResultSet(String[] tables, String[] discards) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		PropKit.use("config.properties");
		String baseModelPackage = PropKit.get("baseModelPackage");
		for (String table : tables) {
			try {
				String beanClassName = StrKit.firstCharToUpperCase(StringUtil.trimUnderLineAndToUpCase(table));
				Class<?> beanClass = Class.forName(baseModelPackage + ".Base" + beanClassName);
				Method[] methods = beanClass.getMethods();
				for (Method method : methods) {
					String methodName = method.getName();
					if (methodName.startsWith("set") == false || methodName.length() <= 3) {
						continue;
					}
					Class<?>[] types = method.getParameterTypes();
					if (types.length != 1) {
						continue;
					}
					String attrName = methodName.substring(3);
					String colName = StrKit.firstCharToLowerCase(attrName);
					String name = table + "." + colName;
					if (discards != null && discards.length > 0) {
						for (String discard : discards) {
							if (name.equals(discard)) {
								name = "";
							}
						}
					}
					if (!name.equals("")) {
						sql.append(name + " AS " + beanClassName + "_" + attrName);
						sql.append(",");
					}

				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		sql.delete(sql.lastIndexOf(","), sql.length());
		return sql.toString();
	}

}

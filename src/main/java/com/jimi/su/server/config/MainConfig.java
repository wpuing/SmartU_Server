package com.jimi.su.server.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jimi.su.server.controller.ActionLogController;
import com.jimi.su.server.controller.BranchController;
import com.jimi.su.server.controller.CommandController;
import com.jimi.su.server.controller.DeviceController;
import com.jimi.su.server.controller.FileController;
import com.jimi.su.server.controller.MailController;
import com.jimi.su.server.controller.OperationController;
import com.jimi.su.server.controller.OrderController;
import com.jimi.su.server.controller.ProjectController;
import com.jimi.su.server.controller.RuleController;
import com.jimi.su.server.controller.UserController;
import com.jimi.su.server.interceptor.ActionLogInterceptor;
import com.jimi.su.server.interceptor.CORSInterceptor;
import com.jimi.su.server.interceptor.ErrorLogInterceptor;
import com.jimi.su.server.model._MappingKit;
import com.jimi.su.server.util.TokenBox;


public class MainConfig extends JFinalConfig {

	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		// 读取数据库配置文件
		PropKit.use("config.properties");
		// 设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		// 设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath("upload/temp/");
		// 设置上传最大限制尺寸
		me.setMaxPostSize(Integer.MAX_VALUE);
		// 设置默认下载文件路径 renderFile使用
		me.setBaseDownloadPath("download");
		// 开启依赖注入
		me.setInjectDependency(true);
		// 设置json工厂
		me.setJsonFactory(MixedJsonFactory.me());
	}


	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/user", UserController.class);
		me.add("/command", CommandController.class);
		me.add("/project", ProjectController.class);
		me.add("/file", FileController.class);
		me.add("/operation", OperationController.class);
		me.add("/branch", BranchController.class);
		me.add("rule", RuleController.class);
		me.add("/order", OrderController.class);
		me.add("/mail", MailController.class);
		me.add("/device",DeviceController.class);
		me.add("/log",ActionLogController.class);
		
	}


	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ORM
	 * 缓存等插件
	 * 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 配置数据库连接池插件
		DruidPlugin dbPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		// orm映射 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		// dbPlugin.setDriverClass("com.mysql.cj.jdbc.Driver");
		dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
		/********在此添加数据库 表-Model 映射*********/
		// 如果使用了JFinal Model 生成器 生成了BaseModel 把下面注释解开即可
		_MappingKit.mapping(arp);

		// 添加到插件列表中
		me.add(dbPlugin);
		me.add(arp);
	}


	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {

		me.addGlobalActionInterceptor(new CORSInterceptor());
//		me.addGlobalActionInterceptor(new FileDeleteInterceptor());
		me.addGlobalActionInterceptor(new ErrorLogInterceptor());
//		me.addGlobalActionInterceptor(new AccessInterceptor());
		me.addGlobalActionInterceptor(new SessionInViewInterceptor());
		me.addGlobalActionInterceptor(new ActionLogInterceptor());
		
	}


	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers me) {

	}


	/**
	 * JFinal启动后调用
	 */
	@Override
	public void afterJFinalStart() {
		try {
			TokenBox.start(PropKit.getInt("sessionTimeout"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SU Server is running");
	}


	/**
	 * JFinal Stop之前调用 
	 */
	@Override
	public void beforeJFinalStop() {
		TokenBox.stop();
	}


	/**
	 * 配置模板引擎 
	 */
	@Override
	public void configEngine(Engine me) {

	}


	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
		
	}

}
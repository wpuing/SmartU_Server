package com.jimi.su.server.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("action_log", "id", ActionLog.class);
		arp.addMapping("branch", "ID", Branch.class);
		arp.addMapping("branchtype", "ID", Branchtype.class);
		arp.addMapping("command", "ID", Command.class);
		arp.addMapping("def_command", "ID", DefCommand.class);
		arp.addMapping("device", "ID", Device.class);
		arp.addMapping("domestic", "ID", Domestic.class);
		arp.addMapping("file", "ID", File.class);
		arp.addMapping("foreigntrade", "ID", Foreigntrade.class);
		arp.addMapping("gsensor", "ID", Gsensor.class);
		arp.addMapping("hardware", "ID", Hardware.class);
		arp.addMapping("mail", "ID", Mail.class);
		arp.addMapping("operation", "ID", Operation.class);
		arp.addMapping("operationversionbind", "ID", Operationversionbind.class);
		arp.addMapping("orders", "id", Orders.class);
		arp.addMapping("organization", "ID", Organization.class);
		arp.addMapping("pcb", "ID", Pcb.class);
		arp.addMapping("platform", "ID", Platform.class);
		arp.addMapping("project", "ID", Project.class);
		arp.addMapping("rule", "id", Rule.class);
		arp.addMapping("type", "id", Type.class);
		arp.addMapping("user", "id", User.class);
		arp.addMapping("version", "ID", Version.class);
	}
}

package com.anjiu.qlbs;

import java.util.Collection;

import com.anjiu.qlbs.base.ScpInfo;

import ch.ethz.ssh2.Connection;

public class ScpMain {
	
	public static void main(String[] args) {
		//加载数据
		ScpConstant.loadFile();
		ScpConstant.loadUpload();
		//控制台窗口命令
		/*
		 * 1.服务器选择
		 * 1.1.内网139操作
		 * 1.2.阿里云测试服操作
		 * 1.3.正式王者服操作
		 * 1.4.正式烈焰服操作
		 * 2.操作选择
		 * 2.1关服
		 * 2.2上传
		 * 2.3更新
		 * 2.4重启
		 * 2.5上传更新重启
		 * 2.6下载日志文件
		 * 2.7查看异常less filename | grep 'keywaord'
		 */
		//服务器选择
		String serverName = "";
		//操作选择
		String command = "";
		
		Collection<ScpInfo> scpLists = ScpConstant.scpInfoMap.get("wzyd");
		for (ScpInfo scpInfo : scpLists) {
			Connection conn = ScpAssist.getConn(scpInfo);
			ScpAssist.runCommand(conn, command);
		}
	}

	
}

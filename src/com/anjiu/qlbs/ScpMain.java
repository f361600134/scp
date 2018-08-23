package com.anjiu.qlbs;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

import com.anjiu.qlbs.base.ScpInfo;
import com.anjiu.qlbs.base.ScpOperate;

public class ScpMain {
	
	public static void main(String[] args) {
		//加载数据
		ScpConstant.loadFile();
		ScpConstant.loadUpload();
		openConsole();
	}
	
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
	static boolean first = true;
	static boolean second = true;
	static String tempServerName = "";
	public static void openConsole(){
		//控制台窗口命令
		Scanner input = new Scanner(System.in);
        String putword = null;// 记录输入的字符串
        do{
        	//第一步骤
        	if (first) {
        		Map<Integer, String> map= ScpConstant.getShows();
            	for (Integer key : map.keySet()) {
            		System.out.println(key+". "+map.get(key)+"操作");
        		}
            	putword = input.next(); // 等待输入值
                String val = map.get(Integer.parseInt(putword));
                if (val == null) {
                	 System.out.println("没有这个选项");
                	 continue;
    			}
                //打印所持有的服务器
                Collection<ScpInfo> list = ScpConstant.scpInfoMap.get(val);
                for (ScpInfo scpInfo : list) {
					System.out.println("scpInfo:"+scpInfo);
				}
                tempServerName = val;
                first = false;
			}
        	//第二步骤
            if (second) {
            	for (ScpOperate scpOperate : ScpOperate.values()) {
                 	System.out.println(scpOperate.getType()+". "+scpOperate.getName()+"操作");
     			}
            	second = false;
			}
            
            //进入第二步骤可返回
            putword = input.next(); // 等待输入值
            if (putword.equals("0")) {
            	first = true;
            	second = true;
            	System.out.println("返回上一层");
            	continue;
			}
            
            //最终操作
         	ScpOperate scpOperate = ScpOperate.getScpType(Integer.parseInt(putword));
         	if (scpOperate == null) {
         		System.out.println("没有这个选项");
         		continue;
 			}
         	Collection<ScpInfo> list = ScpConstant.scpInfoMap.get(tempServerName);
         	for (ScpInfo scpInfo : list) {
         		scpOperate.exeCommand(scpInfo);
			}
        }while(!putword.equals("-1"));   // 如果输入的值不是-1就继续输入
        System.out.println("你输入了\"-1\"，程序已经退出！");
        input.close(); // 关闭资源
	}

	
}

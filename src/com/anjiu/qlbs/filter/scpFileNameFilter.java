package com.anjiu.qlbs.filter;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang.StringUtils;

public class scpFileNameFilter implements FilenameFilter {

	private final String filename;
	private final String suffix;

	public scpFileNameFilter(String filename, String suffix) {
		this.filename = filename;
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File dir, String name) {
		boolean boola = StringUtils.isBlank(filename);
		boolean boolb = StringUtils.isBlank(suffix);
		// 如果前缀空,后缀空
		if (boola && boolb)
			return false;

		// 前缀不为空, 后缀空
		if (!boola && boolb)
			return name.contains(filename);

		// 前缀为空, 后缀不空
		if (boola && !boolb) {
			return name.endsWith(suffix);
		}

		// 前缀不空, 后缀不空
		if (!boola && !boolb) {
			return name.contains(filename) && name.endsWith("suffix");
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(aaa());
	}

	static boolean aaa() {
		boolean a = false;
		boolean b = false;

		if (a || b)
			return true;
		return false;
	}

}

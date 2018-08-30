package com.anjiu.qlbs.filter;

import java.io.File;
import java.util.Comparator;

public class scpFileSortFilter implements Comparator<File> {
	@Override
	public int compare(File o1, File o2) {
		return -Long.compare(o1.lastModified(), o2.lastModified());
	}
}

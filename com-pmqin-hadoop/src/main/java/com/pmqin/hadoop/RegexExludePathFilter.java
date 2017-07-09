package com.pmqin.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class RegexExludePathFilter implements PathFilter {
	private final String regex;

	public RegexExludePathFilter(String regex) {
		this.regex = regex;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// 该接口只需实现其中的一个方法即可，即accpet方法，方法返回true时表示被过滤掉
	public boolean accept(Path path) {
		return !path.toString().matches(regex);
	}

}

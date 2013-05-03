package com.clashroom.shared;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

public class Formatter {
	public static String format(final String format, final Object... args) {
		final RegExp regex = RegExp.compile("%[a-z]");
		final SplitResult split = regex.split(format);
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length() - 1; ++pos) {
			msg.append(split.get(pos));
			Object obj = args[pos];
			msg.append(obj == null ? "null" : obj.toString());
		}
		msg.append(split.get(split.length() - 1));
		return msg.toString();
	}
	
	public static String appendList(String list, String item) {
		if (list.length() > 0) list += ", ";
		list += item;
		return list;
	}
}

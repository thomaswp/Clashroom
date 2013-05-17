package com.clashroom.shared;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

/**
 * A class for doing text formatting.
 */
public class Formatter {
	/**
	 * A stand-in for the {@link String#format(String, Object...)} method, because
	 * GWT does not support it on the client side. This method uses "dumb" replacement,
	 * in that it does not pay attention to the letter following %'s, and does not obey directives
	 * such as %.02d. 
	 * @param format The format string, such as ["His name is %s and he has %d apples"]
	 * @param args The parameters, such as ["Bob", 12]
	 * @return The formatted String
	 */
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
	
	/**
	 * A not-so-useful utility method for appending items onto a String
	 * list. Returns the given list, plus the item tacked on to the end.
	 * If the list is not empy, it adds a comma and space between the two.
	 * 
	 * @param list The list to append to
	 * @param item The item to append
	 * @return The list with the item appended
	 */
	public static String appendList(String list, String item) {
		if (list.length() > 0) list += ", ";
		list += item;
		return list;
	}
}

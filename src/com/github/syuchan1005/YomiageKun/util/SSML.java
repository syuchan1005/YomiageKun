package com.github.syuchan1005.YomiageKun.util;

import java.util.Arrays;

/**
 * Created by syuchan on 2016/09/05.
 */
public class SSML {
	private static SSML ssml = new SSML();

	private SSML() {}

	public static String convert(String text) {
		String ssmls = "";
		if (text.contains("(") && text.contains(")")) {
			int num = text.indexOf("(");
			String substring = text.substring(num + 1);
			int endIndex = substring.indexOf(")");
			String[] split = new String[] {text.substring(0, num), substring.substring(0, endIndex), substring.substring(endIndex + 1)};
			for (Speech.Speaker speaker : Speech.Speaker.values()) {
				if (split[1].equalsIgnoreCase(speaker.name())) {
					ssmls += split[0];
					ssmls += "<voice name=\"" + speaker.getName() + "\">";
					ssmls += "<prosody rate=\"1.27\">";
					ssmls += split[2];
					ssmls += "</prosody>";
					ssmls += "</voice>";
					return convert(ssmls);
				}
			}
			return convert(String.join("", split));
		}
		return text;
	}
}

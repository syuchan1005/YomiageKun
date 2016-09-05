package com.github.syuchan1005.YomiageKun.util;

/**
 * Created by syuchan on 2016/09/05.
 */
public class SSML {
	private static SSML ssml = new SSML();

	private SSML() {}

	public static String convert(String text) {
		String ssmls = "";
		if (text.indexOf("(") != -1 && text.indexOf(")") != -1) {
			int num = text.indexOf("(");
			String substring = text.substring(num + 1);
			int endIndex = substring.indexOf(")");
			String[] split = new String[] {text.substring(0, num).toUpperCase(), substring.substring(0, endIndex), substring.substring(endIndex + 1)};
			for (Speech.Speaker speaker : Speech.Speaker.values()) {
				if (split[0].endsWith(speaker.name())) {
					ssmls += text.substring(0, split[0].length() - speaker.name().length());
					ssmls += "<voice name=\"" + speaker.getName() + "\">";
					ssmls += text.substring(num + 1, num + split[1].length() + 1);
					ssmls += "</voice>";
					ssmls += split[2];
					return convert(ssmls);
				}
			}
			return convert(split[0] + split[1] + split[2]);
		}
		return text;
	}
}

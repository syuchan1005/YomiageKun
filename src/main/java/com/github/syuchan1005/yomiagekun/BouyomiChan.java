package com.github.syuchan1005.yomiagekun;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by syuchan on 2017/01/20.
 */
public class BouyomiChan {
	private static final String NUMBER = "(-?[0-9]*[.]?[0-9]+)";

	public static String toSSML(String text) {
		return minimize(EtoSSML(JtoE(text)));
	}

	private static String JtoE(String text) {
		return text
				.replaceAll("やまびこ\\)", "(Reverb 2.5 0.7 40)")
				.replaceAll("エコー\\)", "(Reverb 0.5 0.1 40)")
				.replaceAll("残響\\(" + NUMBER + "\\s" + NUMBER + "\\s" + NUMBER + "\\)", "(Reverb $1 $2 $3)")
				.replaceAll("速度\\(" + NUMBER + "\\)", "(Speed $1)")
				.replaceAll("音程\\(" + NUMBER + "\\)", "(Tone $1)")
				.replaceAll("音量\\(" + NUMBER + "\\)", "(Volume $1)");
	}

	private static String EtoSSML(String text) {
		String ssml = text.replaceAll("\\(Reverb\\s" + NUMBER + "\\s" + NUMBER + "\\s" + NUMBER + "\\)", ""); // Reverb not Support!
		ssml = replace(ssml, "(?=\\(Speed\\s(-?[0-9]*[.]?[0-9]+)\\))",
				"\\(Speed\\s(-?[0-9]*[.]?[0-9]+)\\)(.+)", "<prosody rate=\"$1\">$2</prosody>");
		ssml = replace(ssml, "(?=\\(Tone\\s(-?[0-9]*[.]?[0-9]+)\\))",
				"\\(Tone\\s(-?[0-9]*[.]?[0-9]+)\\)(.+)", "<prosody pitch=\"$1\">$2</prosody>");
		ssml = replace(ssml, "(?=\\(Volume\\s(-?[0-9]*[.]?[0-9]+)\\))",
				"\\(Volume\\s(-?[0-9]*[.]?[0-9]+)\\)(.+)", "<prosody volume=\"$1\">$2</prosody>");
		return ssml;
	}

	private static String replace(String text, String splitRegex, String replaceRegex, String replace) {
		return Arrays.stream(text.split(splitRegex))
				.map(s -> s.replaceAll(replaceRegex, replace))
				.collect(Collectors.joining());
	}

	private static String minimize(String ssml) {
		List<Component> components = Arrays.stream(ssml.split("(?=<(\".*?\"|'.*?'|[^'\"])*?>)"))
				.map(Component::new).filter(c -> !c.tag.startsWith("/")).collect(Collectors.toList());
		for (int i = 1; i < components.size(); i++) {
			Component component = components.get(i - 1);
			Component component1 = components.get(i);
			if (component.isJoin(component1)) {
				component.join(component1);
				components.remove(i);
				i--;
			}
		}
		return toString(components);
	}

	private static String toString(List<Component> components) {
		for (int i = components.size() - 1; i >= 1; i--) {
			components.get(i - 1).innerText += components.get(i).toString();
			components.remove(i);
		}
		return components.get(0).toString();
	}

	private static class Component implements Cloneable {
		private static Pattern pattern = Pattern.compile("<([a-z/]+)\\s*([^/]*?)>(.*)");
		private String tag = "";
		private Map<String, String> option = new HashMap<>();
		private String innerText = "";

		private Component(String text) {
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				tag = matcher.group(1);
				innerText = matcher.group(3);
				for (String s : matcher.group(2).split("\\s")) {
					String[] split = s.split("=");
					if (split.length != 2) continue;
					option.put(split[0], split[1]);
				}
			} else {
				innerText = text;
			}
		}

		@Override
		public String toString() {
			if (tag.isEmpty()) {
				return innerText;
			}
			String str = '<' + tag;
			for (Map.Entry<String, String> entry : option.entrySet()) {
				str += " " + entry.getKey() + "=" + entry.getValue();
			}
			str += '>' + innerText + "</" + tag + '>';
			return str;
		}

		public boolean isJoin(Component component) {
			return this.tag.equals(component.tag) && (this.innerText.length() == 0 || component.innerText.length() == 0);
		}

		public void join(Component component) {
			this.option.putAll(component.option);
			if (component.innerText.isEmpty()) this.innerText = component.innerText;
		}
	}
}
